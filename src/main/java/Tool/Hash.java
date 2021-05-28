package Tool;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Hash {

    public static byte[] md5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            BigInteger bi = new BigInteger(1, md.digest());
            return DataTypeChange.stringToBytes(bi.toString(16));
        } catch (Exception e) {
            return null;
        }
    }
}

