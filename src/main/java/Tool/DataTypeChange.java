package Tool;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * byte数组与其他类型的相互转化类
 * 均为静态方法，通过类名调用即可
 */
public class DataTypeChange {

    /**
     * byte数组转int
     * @param bytes 数组
     * @return 转换结果
     */
    public static int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * int转byte数组
     * @param value 要转换的int值
     * @return 转换结果
     */
    public static byte[] intToBytes(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        return buffer.array();
    }

    /**
     * long转byte数组
     * @param value 要转换的long值
     * @return 转换结果
     */
    public static byte[] longToBytes(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(value);
        return buffer.array();
    }

    /**
     * byte数组转long
     * @param bytes byte数组
     * @return 转换结果
     */
    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer= ByteBuffer.allocate(8);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    /**
     * string转byte数组
     * 若转换则返回null
     * @param str 待转换string
     * @return 转换结果
     */
    public static byte[] stringToBytes(String str)
    {
        try {
            return str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Exception: String change into byteArray Fail!(String:"+str+")");
            return null;
        }
    }

    /**
     * byte数组转换string
     * 若转换失败则返回null
     * @param bytes byte数组
     * @return 转换结果
     */
    public static String bytesToString(byte[] bytes)
    {
        try {
            return new String(bytes,"utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Exception: ByteArray change into string Fail!");
            return null;
        }
    }

    public static byte IntToUnsignedByte(int value)
    {
        if(value>256||value<0)
        {
            System.out.println(value+"is too big to become unsignedByte!");
            return 0;
        }
        return (byte)value;
    }

    public static int UnsignedByteToInt(byte value)
    {
        return (int)value&0xFF;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
