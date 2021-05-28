package Tool;

public class AddByte {
    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }
    public static byte[] subBytes(byte[] data1,int a,int length) {
        byte[] data3 = new byte[length];
        System.arraycopy(data1, a, data3,0, length);

        return data3;
    }
}
