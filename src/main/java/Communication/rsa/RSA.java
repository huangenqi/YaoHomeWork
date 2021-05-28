package Communication.rsa;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private final static int numLength = 1024;//素数的位数
    private final static int accuracy = 100;//素数的准确率为1-(2^(-accuracy))

    private BigInteger n;
    private BigInteger d;
    private BigInteger e;

    public RSA(){
        generateKey();
    }


    /**
     * 获取两个超大随机素数p,q
     * @return 素数p,q
     */
    public static BigInteger[] getRandomPQ() {
        BigInteger p = BigInteger.probablePrime(numLength, new Random());
        while (!p.isProbablePrime(accuracy)) {
            p = BigInteger.probablePrime(numLength, new Random());
        }
        BigInteger q = BigInteger.probablePrime(numLength, new Random());
        while (!q.isProbablePrime(accuracy)) {
            q = BigInteger.probablePrime(numLength, new Random());
        }
        System.out.println("p:"+p.toByteArray().length);
        System.out.println("q:"+q.toByteArray().length);
        return new BigInteger[]{p, q};
    }

    /**
     * 生成公钥(n,e)，私钥(n,d)
     * @return (n,e,d)
     */
    private BigInteger[] generateKey(){
        BigInteger[] randomPQ=getRandomPQ();
        BigInteger p=randomPQ[0];
        BigInteger q=randomPQ[1];
        BigInteger n=p.multiply(q);//获取质数p,q的乘积n
        //计算n的欧拉函数的值
        BigInteger phiN=(p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e=new BigInteger("65537");//设置随机整数e为65537
        //使用拓展欧几里得方法，求解e对于phiN的模反元素d
        BigInteger[] tmp=extGCD(e,phiN);
        BigInteger d=tmp[1];
        //使d为正数
        if(d.signum()==-1){
            d=d.add(phiN);
        }

        this.n=n;
        this.d=d;
        this.e=e;

        System.out.println("n:"+n.toByteArray().length);
        System.out.println("d:"+d.toByteArray().length);
        System.out.println("e:"+e.toByteArray().length);
        return new BigInteger[]{n,e,d};
    }

    /**
     * 扩展欧几里得方法,计算 ax + by = 1中的x与y的整数解（a与b互质）
     * @param a
     * @param b
     * @return
     */
    private static BigInteger[] extGCD(BigInteger a, BigInteger b) {
        if (b.signum() == 0) {
            return new BigInteger[]{a, new BigInteger("1"), new BigInteger("0")};
        } else {
            BigInteger[] bigIntegers = extGCD(b, a.mod(b));
            BigInteger y = bigIntegers[1].subtract(a.divide(b).multiply(bigIntegers[2]));
            return new BigInteger[]{bigIntegers[0], bigIntegers[2], y};
        }
    }

    /**
     * 实现快速幂模运算
     * @param base
     * @param exp
     * @param mod
     * @return
     */
    private static BigInteger expMode(BigInteger base, BigInteger exp, BigInteger mod) {
        BigInteger res = BigInteger.ONE;
        //拷贝一份防止修改原引用
        BigInteger tempBase = new BigInteger(base.toString());

        for (int i = 0; i < exp.bitLength(); i++) {
            if (exp.testBit(i)) {//判断对应二进制位是否为1
                res = (res.multiply(tempBase)).mod(mod);
            }
            tempBase = tempBase.multiply(tempBase).mod(mod);
        }
        return res;
    }

    /**
     * RSA加密,密文c=m^e mod n
     * @return 密文c
     */
    public String RSAEncrypt(String m){
        //BigInteger[] keys=generateKey();
        BigInteger mTmp=new BigInteger(m.getBytes());
        BigInteger c=expMode(mTmp,this.e ,this.n);//密文c=m^e mod n
        System.out.println(c.toByteArray().length);
        return c.toString();
    }


    /**
     * RSA解密,明文m=c^d mod n
     * @return
     */
    public String RSADecrypt(String c){
        BigInteger mBigNum = expMode(new BigInteger(c), this.d, this.n);
        byte[] mbytes = new byte[mBigNum.bitLength() / 8 + 1];
        //将大数类型转化为字节数组
        for(int i=0;i<mbytes.length;i++){
            for (int j = 0; j < 8; j++) {
                if (mBigNum.testBit(j + i * 8)) {
                    mbytes[mbytes.length - 1 - i] |= 1 << j;
                }
            }
        }
        return new String(mbytes);
    }

    /**
     * 获得公钥
     * @return 公钥(n,e)，类型为大数类型
     */
    public BigInteger[] getPK(){
        return new BigInteger[]{this.n,this.e};
    }

    /**
     * 获得私钥
     * @return 私钥(n,d)，类型为大数类型
     */
    public BigInteger[] getSK(){
        return new BigInteger[]{this.n,this.d};
    }

    /**
     * 使用私钥(n,d)进行加密创建数字签名
     * @param plaintext byte数组类型的证书内容
     * @param n 私钥的 n
     * @param d 私钥的 d
     * @return 生成的数字签名
     */
    public static byte[] createSignature(byte[] plaintext,byte[] n,byte[] d){
        BigInteger mBigNum = expMode(new BigInteger(plaintext), new BigInteger(d), new BigInteger(n));
        return mBigNum.toByteArray();
    }

    /**
     * 使用公钥(n,e)对数字签名进行解密，验证数字签名
     * @param ciphertext byte数组类型的数字签名内容
     * @param n 公钥的 n
     * @param e 公钥的 d
     * @return 验证结果
     */
    public static boolean verification(byte [] ciphertext,byte[] n,byte[] e){
        BigInteger c=expMode(new BigInteger(ciphertext),new BigInteger(e) ,new BigInteger(n));

        return true;
    }

    public static void main(String[] args){
        String m="Hello world赵海超";
        RSA test=new RSA();
        String c=test.RSAEncrypt(m);
        System.out.println("密文："+c);

        String ms=test.RSADecrypt(c);
        System.out.println("原文："+ms);
    }
}
