package com.ruoyi.common.utils;

import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * 基于jSerialComm的Modbus RTU数据采集工具类（纯若依+MyBatis版）
 */
@Component
public class SerialModbusUtils {
    private static final Logger log = LoggerFactory.getLogger(SerialModbusUtils.class);

    // 从application.yml读取配置
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
    @Value("${serial-modbus.slave-id}")
    private int slaveId;
    @Value("${serial-modbus.timeout}")
    private int timeout;
    @Value("${serial-modbus.register.start-address}")
    private int startAddress;
    @Value("${serial-modbus.register.quantity}")
    private int quantity;
    @Value("${serial-modbus.register.type}")
    private String registerType;

    /**
     * 打开并配置串口（修复jSerialComm无exists()方法问题）
     */
    private SerialPort openSerialPort() {
        // 1. 校验串口是否存在（枚举所有可用串口）
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

        // 2. 初始化串口
        SerialPort serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);
        serialPort.setNumDataBits(dataBits);
        serialPort.setNumStopBits(stopBits);

        // 3. 配置校验位
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
                String errorMsg = "不支持的校验位：" + parity;
                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
        }
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, timeout, timeout);

        // 4. 打开串口
        if (!serialPort.openPort()) {
            String errorMsg = "串口" + portName + "打开失败！";
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        log.info("串口{}打开成功，参数：波特率{}，校验位{}", portName, baudRate, parity);
        return serialPort;
    }

    /**
     * 组装Modbus RTU请求帧（03读保持寄存器/04读输入寄存器）
     */
    private byte[] assembleModbusRTUFrame() {
        // 功能码：03=holding，04=input
        byte functionCode = "holding".equalsIgnoreCase(registerType) ? (byte) 0x03 : (byte) 0x04;

        // 组装核心帧（从站ID+功能码+起始地址+寄存器数量）
        ByteBuffer buffer = ByteBuffer.allocate(6);
        buffer.order(ByteOrder.BIG_ENDIAN); // Modbus大端序
        buffer.put((byte) slaveId);
        buffer.put(functionCode);
        buffer.putShort((short) startAddress);
        buffer.putShort((short) quantity);
        byte[] frameWithoutCRC = buffer.array();

        // 计算CRC16并拼接完整帧
        String crcHex = ConvertHexStrAndStrUtils.calculateCRC16(frameWithoutCRC);
        byte[] crc = ConvertHexStrAndStrUtils.hexStrToBytes(crcHex);
        if (crc == null || crc.length != 2) {
            throw new RuntimeException("CRC16计算异常：" + crcHex);
        }

        byte[] fullFrame = new byte[8];
        System.arraycopy(frameWithoutCRC, 0, fullFrame, 0, 6);
        System.arraycopy(crc, 0, fullFrame, 6, 2);
        log.info("Modbus RTU请求帧：{}", ConvertHexStrAndStrUtils.bytesToHexStr(fullFrame));
        return fullFrame;
    }

    /**
     * 解析Modbus RTU响应帧（提取寄存器值）
     */
    private int[] parseModbusRTUResponse(byte[] response) {
        // 校验响应长度
        if (response == null || response.length < 5) {
            throw new RuntimeException("响应帧长度异常：" + (response == null ? 0 : response.length));
        }

        // 校验CRC16（核心，防止数据错误）
        if (!ConvertHexStrAndStrUtils.checkCRC16(response)) {
            String errorMsg = "CRC16校验失败：" + ConvertHexStrAndStrUtils.bytesToHexStr(response);
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        // 校验功能码（异常响应判断）
        byte functionCode = response[1];
        if ((functionCode & 0x80) != 0) {
            throw new RuntimeException("Modbus Slave异常响应：功能码=" + Integer.toHexString(functionCode));
        }

        // 解析寄存器值（验证数据长度）
        int byteCount = response[2] & 0xFF;
        if (byteCount != quantity * 2) {
            throw new RuntimeException("数据长度异常：预期" + (quantity*2) + "字节，实际" + byteCount + "字节");
        }

        int[] registerValues = new int[quantity];
        for (int i = 0; i < quantity; i++) {
            int high = response[3 + i * 2] & 0xFF;
            int low = response[4 + i * 2] & 0xFF;
            registerValues[i] = (high << 8) | low;
            log.info("寄存器地址{}，值：{}", startAddress + i, registerValues[i]);
        }
        return registerValues;
    }

    /**
     * 对外暴露：读取Modbus Slave数据（核心方法）
     */
    public int[] readModbusSlaveData() {
        SerialPort serialPort = null;
        try {
            // 1. 打开串口
            serialPort = openSerialPort();

            // 2. 组装并发送请求帧
            byte[] requestFrame = assembleModbusRTUFrame();
            int sendLen = serialPort.writeBytes(requestFrame, requestFrame.length);
            if (sendLen != requestFrame.length) {
                throw new RuntimeException("请求帧发送失败：预期" + requestFrame.length + "字节，实际" + sendLen + "字节");
            }

            // 3. 读取响应帧（等待Slave响应）
            Thread.sleep(100); // 可根据波特率调整
            int availableBytes = serialPort.bytesAvailable();
            if (availableBytes == 0) {
                throw new RuntimeException("未读取到Modbus Slave响应");
            }
            byte[] responseFrame = new byte[availableBytes];
            serialPort.readBytes(responseFrame, availableBytes);
            log.info("Modbus RTU响应帧：{}", ConvertHexStrAndStrUtils.bytesToHexStr(responseFrame));

            // 4. 解析响应
            return parseModbusRTUResponse(responseFrame);

        } catch (InterruptedException e) {
            log.error("串口通信等待异常", e);
            throw new RuntimeException("串口通信等待异常", e);
        } finally {
            // 关闭串口（必须释放，否则端口占用）
            if (serialPort != null && serialPort.isOpen()) {
                serialPort.closePort();
                log.info("串口{}已关闭", portName);
            }
        }
    }

    // Getter（定时任务需要获取配置参数）
    public int getStartAddress() {
        return startAddress;
    }

    public int getSlaveId() {
        return slaveId;
    }
}