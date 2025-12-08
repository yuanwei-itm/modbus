package com.ruoyi.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

/**
 * 工业通信数据转换工具类（十六进制/字节/CRC16校验）
 */
public class ConvertHexStrAndStrUtils {
    private static final Logger log = LoggerFactory.getLogger(ConvertHexStrAndStrUtils.class);

    // 十六进制字符集
    private static final char[] HEXES = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    /**
     * 字节数组转十六进制字符串（大写）
     */
    public static String bytesToHexStr(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(HEXES[(b >> 4) & 0x0F]);
            hex.append(HEXES[b & 0x0F]);
        }
        return hex.toString().toUpperCase();
    }

    /**
     * 十六进制字符串转字节数组
     */
    public static byte[] hexStrToBytes(String hex) {
        if (hex == null || hex.isEmpty()) {
            return null;
        }
        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length / 2];
        try {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
            }
        } catch (NumberFormatException e) {
            log.error("十六进制转字节数组失败：{}", hex, e);
            return null;
        }
        return bytes;
    }

    /**
     * Modbus RTU标准CRC16校验（返回4位十六进制字符串）
     */
    public static String calculateCRC16(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        int crc = 0xFFFF;
        for (byte b : data) {
            crc ^= b & 0xFF;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x0001) == 1) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return String.format("%04X", ((crc & 0xFF) << 8) | ((crc >> 8) & 0xFF));
    }

    /**
     * 验证Modbus数据CRC16是否合法
     */
    public static Boolean checkCRC16(byte[] data) {
        if (data == null || data.length < 2) {
            return false;
        }
        byte[] slicedCrc = Arrays.copyOfRange(data, data.length - 2, data.length);
        byte[] slidedData = Arrays.copyOfRange(data, 0, data.length - 2);
        String crc = calculateCRC16(slidedData);
        return crc != null && crc.equals(bytesToHexStr(slicedCrc));
    }
}