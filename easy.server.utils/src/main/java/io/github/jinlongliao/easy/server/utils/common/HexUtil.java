package io.github.jinlongliao.easy.server.utils.common;


/**
 * Hex Util
 *
 * @author liaojinlong
 * @since 2021-12-27 14:35
 */
public class HexUtil {
    private static final char[] TABLE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final int T = 2;

    public static String byte2Hex(byte[] byteArr) {
        StringBuilder hex = new StringBuilder(byteArr.length * T);
        for (int index = 0; index < byteArr.length; index++) {
            hex.append(TABLE[(byteArr[index] & 0xf0) >> 4]);
            hex.append(TABLE[(byteArr[index] & 0x0f)]);
        }
        return hex.toString();
    }

    public static byte[] hex2Byte(String hex) {
        char[] array = hex.toCharArray();
        byte[] bytes = new byte[array.length / T];
        int arrayIndex = 0;
        for (int i = 0; i < bytes.length; i++) {
            byte high = tableIndex(array[arrayIndex++]);
            byte low = tableIndex(array[arrayIndex++]);
            bytes[i] = (byte) (high << 4 | low);
        }

        return bytes;
    }


    static byte tableIndex(char c) {
        byte _byte;
        switch (c) {
            case '0':
                _byte = 0;
                break;
            case '1':
                _byte = 1;
                break;
            case '2':
                _byte = 2;
                break;
            case '3':
                _byte = 3;
                break;
            case '4':
                _byte = 4;
                break;
            case '5':
                _byte = 5;
                break;
            case '6':
                _byte = 6;
                break;
            case '7':
                _byte = 7;
                break;
            case '8':
                _byte = 8;
                break;
            case '9':
                _byte = 9;
                break;
            case 'a':
            case 'A':
                _byte = 10;
                break;
            case 'b':
            case 'B':
                _byte = 11;
                break;
            case 'c':
            case 'C':
                _byte = 12;
                break;
            case 'd':
            case 'D':
                _byte = 13;
                break;
            case 'e':
            case 'E':
                _byte = 14;
                break;
            case 'f':
            case 'F':
                _byte = 15;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + c);
        }
        ;
        return _byte;
    }

    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for (int index = 0; i < l; ++i) {
            out[index++] = TABLE[(0xf0 & data[i]) >>> 4];
            out[index++] = TABLE[0xf & data[i]];
        }

        return out;
    }
}
