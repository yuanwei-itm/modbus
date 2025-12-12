package com.ruoyi.common.utils;

import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@Component
public class SerialModbusUtils {
    private static final Logger log = LoggerFactory.getLogger(SerialModbusUtils.class);

    // 从配置文件读固定参数
    @Value("${serial-modbus.port-name}")
    private String portName;
    @Value("${serial-modbus.baud-rate}")
    private int baudRate;
    @Value("${serial-modbus.data-bits}")
    private int dataBits;
    @Value("${serial-modbus.stop-bits}")
    private int stopBits;
    @Value("${serial-modbus.parity}")
    private String parity;
    @Value("${serial-modbus.timeout}")
    private int timeout;
    @Value("${serial-modbus.register.type}")
    private String registerType;  // 保持寄存器/输入寄存器（全局配置）

    // 1. 打开串口
    private SerialPort openSerialPort() {
        // 校验串口是否存在
        boolean portExists = false;
        SerialPort[] allPorts = SerialPort.getCommPorts();
        for (SerialPort port : allPorts) {
            if (port.getSystemPortName().equals(portName)) {
                portExists = true;
                break;
            }
        }
        if (!portExists) {
            String errorMsg = "串口" + portName + "不存在！可用串口：" + Arrays.toString(allPorts);
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        // 初始化并配置串口
        SerialPort serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);
        serialPort.setNumDataBits(dataBits);
        serialPort.setNumStopBits(stopBits);

        // 配置校验位
        switch (parity.toUpperCase()) {
            case "NONE":
                serialPort.setParity(SerialPort.NO_PARITY);
                break;
            case "ODD":
                serialPort.setParity(SerialPort.ODD_PARITY);
                break;
            case "EVEN":
                serialPort.setParity(SerialPort.EVEN_PARITY);
                break;
            default:
                throw new IllegalArgumentException("不支持的校验位：" + parity);
        }
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, timeout, timeout);

        // 打开串口
        if (!serialPort.openPort()) {
            throw new RuntimeException("串口" + portName + "打开失败！");
        }
        log.info("串口{}打开成功（波特率{}，校验位{}）", portName, baudRate, parity);
        return serialPort;
    }

    // 2. 组装Modbus RTU请求帧（动态接收slaveId、startAddress、quantity）
    private byte[] assembleModbusRTUFrame(int slaveId, int startAddress, int quantity) {
        // 功能码（03=保持寄存器，04=输入寄存器）
        byte functionCode = "holding".equalsIgnoreCase(registerType) ? (byte) 0x03 : (byte) 0x04;

        // 组装帧（从站ID+功能码+起始地址+数量）
        ByteBuffer buffer = ByteBuffer.allocate(6);
        buffer.order(ByteOrder.BIG_ENDIAN); // Modbus大端序
        buffer.put((byte) slaveId);         // 动态传入的从站ID
        buffer.put(functionCode);
        buffer.putShort((short) startAddress); // 动态传入的起始地址
        buffer.putShort((short) quantity);     // 动态传入的数量
        byte[] frameWithoutCRC = buffer.array();

        // 计算CRC16
        String crcHex = ConvertHexStrAndStrUtils.calculateCRC16(frameWithoutCRC);
        byte[] crc = ConvertHexStrAndStrUtils.hexStrToBytes(crcHex);
        if (crc == null || crc.length != 2) {
            throw new RuntimeException("CRC16计算失败：" + crcHex);
        }

        // 拼接完整帧（6字节帧+2字节CRC）
        byte[] fullFrame = new byte[8];
        System.arraycopy(frameWithoutCRC, 0, fullFrame, 0, 6);
        System.arraycopy(crc, 0, fullFrame, 6, 2);
        log.info("请求帧：{}", ConvertHexStrAndStrUtils.bytesToHexStr(fullFrame));
        return fullFrame;
    }

    // 3. 解析响应帧
    private int[] parseModbusRTUResponse(byte[] response, int quantity, int startAddress) {
        if (response == null || response.length < 5) {
            throw new RuntimeException("响应帧长度异常（" + (response == null ? 0 : response.length) + "字节）");
        }

        // 校验CRC
        if (!ConvertHexStrAndStrUtils.checkCRC16(response)) {
            throw new RuntimeException("CRC校验失败：" + ConvertHexStrAndStrUtils.bytesToHexStr(response));
        }

        // 校验功能码（是否异常响应）
        byte functionCode = response[1];
        if ((functionCode & 0x80) != 0) {
            throw new RuntimeException("设备异常响应（功能码：" + Integer.toHexString(functionCode) + "）");
        }

        // 解析寄存器值
        int byteCount = response[2] & 0xFF;
        if (byteCount != quantity * 2) { // 每个寄存器2字节
            throw new RuntimeException("数据长度异常（预期" + quantity*2 + "字节，实际" + byteCount + "字节）");
        }

        int[] registerValues = new int[quantity];
        for (int i = 0; i < quantity; i++) {
            int high = response[3 + i*2] & 0xFF; // 高8位
            int low = response[4 + i*2] & 0xFF;  // 低8位
            registerValues[i] = (high << 8) | low; // 拼接16位值
            log.info("寄存器地址{}，值：{}", startAddress + i, registerValues[i]); // 使用动态传入的startAddress
        }
        return registerValues;
    }

    // 4. 对外暴露：读取指定设备的寄存器（核心方法，动态参数）
    public int[] readModbusSlaveData(int slaveId, int startAddress, int quantity) {
        SerialPort serialPort = null;
        try {
            // 打开串口
            serialPort = openSerialPort();

            // 组装并发送请求
            byte[] requestFrame = assembleModbusRTUFrame(slaveId, startAddress, quantity);
            int sendLen = serialPort.writeBytes(requestFrame, requestFrame.length);
            if (sendLen != requestFrame.length) {
                throw new RuntimeException("请求发送失败（预期" + requestFrame.length + "，实际" + sendLen + "）");
            }

            // 读取响应（等待设备回复）
            Thread.sleep(100); // 短暂延迟，确保响应到达
            int availableBytes = serialPort.bytesAvailable();
            if (availableBytes == 0) {
                throw new RuntimeException("未收到设备响应（Slave" + slaveId + "）");
            }
            byte[] responseFrame = new byte[availableBytes];
            serialPort.readBytes(responseFrame, availableBytes);
            log.info("响应帧（Slave{}）：{}", slaveId, ConvertHexStrAndStrUtils.bytesToHexStr(responseFrame));

            // 解析响应（传入动态startAddress）
            return parseModbusRTUResponse(responseFrame, quantity, startAddress);

        } catch (InterruptedException e) {
            log.error("串口等待异常", e);
            throw new RuntimeException("串口等待异常", e);
        } finally {
            // 关闭串口（释放资源）
            if (serialPort != null && serialPort.isOpen()) {
                serialPort.closePort();
                log.info("串口{}已关闭", portName);
            }
        }
    }
}