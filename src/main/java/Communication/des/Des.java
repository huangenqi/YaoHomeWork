package Communication.des;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Des {
    //PC-1
    private static int[] PC1={57,49,41,33,25,17,9,
            1,58,50,42,34,26,18,
            10,2,59,51,43,35,27,
            19,11,3,60,52,44,36,
            63,55,47,39,31,23,15,
            7,62,54,46,38,30,22,
            14,6,61,53,45,37,29,
            21,13,5,28,20,12,4};
    //PC-2
    private static int[] PC2={14,17,11,24,1,5,3,28,
            15,6,21,10,23,19,12,4,
            26,8,16,7,27,20,13,2,
            41,52,31,37,47,55,30,40,
            51,45,33,48,44,49,39,56,
            34,53,46,42,50,36,29,32};
    //Schedule of Left Shifts
    private static int[] LFT={1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    /**加密轮数**/
    private static final int LOOP_NUM=16;

    //初始置换表IP
    private static final int[] IP ={
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32,24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};

    private static final int[] E={
            32,1,2,3,4,5,
            4,5,6,7,8,9,
            8,9,10,11,12,13,
            12,13,14,15,16,17,
            16,17,18,19,20,21,
            20,21,22,23,24,25,
            24,25,26,27,28,29,
            28,29,30,31,32,1};

    private static final int[][][] Sbox = {
            {
                    { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                    { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                    { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                    { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },
            {
                    { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                    { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                    { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                    { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
            {
                    { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                    { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                    { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                    { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },
            {
                    { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                    { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                    { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
                    { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
            {
                    { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                    { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
                    { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                    { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
            {
                    { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                    { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                    { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                    { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
            {
                    { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                    { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                    { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                    { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
            {
                    { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                    { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                    { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                    { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } }
    };

    private static final int[] P={
            16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,
            2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25};

    //逆置换表
    private static final int[] reIP={
            40,8,48,16,56,24,64,32,
            39, 7,47,15,55,23,63,31,
            38, 6,46,14,54,22,62,30,
            37, 5,45,13,53,21,61,29,
            36, 4,44,12,52,20,60,28,
            35, 3,43,11,51,19,59,27,
            34, 2,42,10,50,18,58,26,
            33, 1,41,9,49,17,57,25};

    public static int[][] subKeys=new int[16][48];//16组子密钥集合


    /**
     * 生成子密钥
     * @param key 密钥
     * @return 16组子密钥
     */
    /*
    public static int[][] generateKeys(byte[] key){

        int[][] subKeystmp=new int[16][48];

        byte[] arrKey=new byte[8];
        byte[] arrTmp=key;

        if(key.length>8) {
            for(int i=0;i<arrTmp.length&&i<arrKey.length;i++){
                arrKey[i]=arrTmp[i];
            }
        }
        else{

        }

        int[] k_bit=new int[64];
        //取位值
        for(int i=0;i<8;i++){
            String k_str=Integer.toBinaryString(arrKey[i]&0xff);
            if(k_str.length()<8){
                for(int t=0;t<8-k_str.length();t++){
                    k_str="0"+k_str;
                }
            }
            for(int j=0;j<8;j++){
                int p= (int) k_str.charAt(j);
                if(p==48){
                    p=0;
                }else if(p==49){
                    p=1;
                }else{
                    System.out.println("To bit error!");
                }
                k_bit[i*8+j]=p;
            }
        }
        //k_bit是初始的64位长密钥，下一步开始进行替换

        int [] k_new_bit=new int[56];
        for(int i=0;i<PC1.length;i++){
            k_new_bit[i]=k_bit[PC1[i]-1];
        }

        int[] c0=new int[28];
        int[] d0=new int[28];
        System.arraycopy(k_new_bit,0,c0,0,28);
        System.arraycopy(k_new_bit,28,d0,0,28);
        for(int i=0;i<16;i++){
            int[] c1=new int[28];
            int[] d1=new int[28];
            if(LFT[i]==1){
                System.arraycopy(c0,1,c1,0,27);
                c1[27]=c0[0];
                System.arraycopy(d0,1,d1,0,27);
                d1[27]=d0[0];
            }else if(LFT[i]==2){
                System.arraycopy(c0,2,c1,0,26);
                c1[26]=c0[0];
                c1[27]=c0[1];

                System.arraycopy(d0,2,d1,0,26);
                d1[26]=d0[0];
                d1[27]=d0[1];
            }else{
                System.out.println("LFT Error!");
            }
            int[] tmp=new int[56];
            System.arraycopy(c1,0,tmp,0,28);
            System.arraycopy(d1,0,tmp,28,28);
            for (int j=0;j<PC2.length;j++){//PC2压缩置换
                subKeystmp[i][j]= tmp[PC2[j]-1];
            }
            c0=c1;
            d0=d1;
        }
        return subKeystmp;
    }*/

    public static void getKey(byte[] key) {
        subKeys=Key.getSubKeys(key);

    }
    /**
     * 用于调用各函数完成DES加密整个流程
     * @param textBytes 原文的byte数组
     * @param mode 0为解密，1为加密
     * @return
     */
    public static byte[] desTest(byte[] textBytes,byte[] key,int mode) {
        //System.out.println(textBytes.length);
        subKeys=Key.getSubKeys(key);
        //subKeys=generateKeys(key);

        int textLength=0;//获取原文的byte数

        //加密
        if(mode==1){
            textLength=textBytes.length+1;

            //System.out.println(textLength);

            byte[] tmp=new byte[textLength];
            tmp[0]=(byte)(textLength-1);
            System.arraycopy(textBytes, 0, tmp, 1, textLength - 1);
            textBytes=tmp;
        } else if(mode==0){
            textLength=textBytes.length;
            if(textLength%8!=0){
                //throw new Exception("不是正确的密文");
            }
        }else{
            System.out.println("请输入正确的模式参数(0/1)");
        }
        //对原文进行补齐
        int d=textLength/8;//除数
        int rNum=8-textLength%8;//需要补充的字节数
        byte[] textBytesAdd = new byte[0];//用于存放已经补齐的原文字节
        //对原文进行补齐
        if(rNum<8){
            textBytesAdd=new byte[textLength+rNum];
            for(int i=0;i<textBytesAdd.length;i++){
                if(i<textLength){
                    textBytesAdd[i]=textBytes[i];
                }
                else{
                    textBytesAdd[i]=(byte)rNum;
                }
            }
        }
        else {
            textBytesAdd=textBytes;
        }
        int groupNum=textBytesAdd.length/8;//分组数
        byte[] resultBytes=new byte[textBytesAdd.length];//加密/解密结果数组
        //分组加密过程
        for(int i=0;i<groupNum;i++){
            byte[] group=new byte[8];
            System.arraycopy(textBytesAdd,8*i,group,0,8);//分组保存
            System.arraycopy(descryUnit(group,subKeys,mode),0,resultBytes,i*8,8);
        }
        if(mode==0){//解密：去除补充的字节
            int length= resultBytes[0];
            byte[] mData=new byte[length];

            //System.out.println(length);

            System.arraycopy(resultBytes,1,mData,0,length);
            return mData;
        }
        return resultBytes;
    }

    public static byte[] descryUnit(byte[] group,int subKeys[][],int mode){

        int[] textBits=new int[64];
        int index=0;
        //将每组的8bytes转化为64bits
        for(int i=0;i<8;i++){
            byte tmp=group[i];
            for(int j=7;j>=0;j--){
                textBits[index+j]=(int)(tmp&1);
                tmp=(byte)(tmp>>1);

            }
            index+=8;
        }
        int[] textBitsByIp=new int[64];
        //进行IP置换
        for(int i=0;i<textBitsByIp.length;i++) {
            textBitsByIp[i]=textBits[IP[i]-1];
        }
        //加解密
        if (mode == 1) { // 加密
            for (int i = 0; i < 16; i++) {
                L(textBitsByIp, subKeys[i], mode, i);
            }
        } else if (mode == 0) { // 解密
            for (int i = 15; i > -1; i--) {
                L(textBitsByIp, subKeys[i], mode, i);
            }
        }
        int[] textBitsByReIP=new int[64];
        //使用逆置换表进行逆置换
        for(int i=0;i<64;i++){
            textBitsByReIP[i]=textBitsByIp[reIP[i]-1];
        }
        StringBuilder tmp= new StringBuilder();
        byte[] mData=new byte[8];
        //将64位密文数组转化为8byte字节数组
        for(int i=0;i<64;i++){
            int j=i+1;
            tmp.append(Integer.toString(textBitsByReIP[i]));
            if(j%8==0&&j/8>0){
                mData[(j/8)-1]=(byte)Integer.parseInt(tmp.toString(),2);
                tmp = new StringBuilder();
            }
        }
        return mData;
    }

    public static void L(int[] group,int[] subKey,int mode,int num){
        int[] L0=new int[32];//初始输入
        int[] R0=new int[32];
        int[] L1=new int[32];
        int[] R1=new int[32];
        int[] tmp=new int[32];
        System.arraycopy(group,0,L0,0,32);
        System.arraycopy(group,32,R0,0,32);

        L1=R0;
        tmp=f_Function(R0,subKey);
        //异或操作获得新的R
        for(int i=0;i<R1.length;i++){
            R1[i]=tmp[i]^L0[i];
        }
        //获取下一轮的group
        for(int i=0;i<32;i++){
            // **注意** 加/解密的最后一轮后会进行左右两部分的对换
            if (((mode == 0) && (num == 0)) || ((mode == 1) && (num == 15))) {
                group[i] = R1[i];
                group[i + 32] = L1[i];
            }
            else {
                group[i] = L1[i];
                group[i + 32] = R1[i];
            }
        }

    }

    /**
     * F函数的实现
     * @param textR
     * @param subKey
     * @return
     */
    public static int[] f_Function(int[] textR,int[] subKey){
        int[] R_extend=new int[48];
        //E盒扩展,并与子密钥进行异或
        for(int i=0;i<E.length;i++){
            R_extend[i]=textR[E[i]-1]^subKey[i];
        }

        int[] arr32ByS=new int[32];
        //使用8个S盒将前面48位的结果转化成32位
        for(int i=0;i<48;i++){
            int j=i+1;
            if(j%6==0&&j/6>0){
                int x=R_extend[i-5]*2+ R_extend[i];//行号
                int y= R_extend[i - 1] +R_extend[i-2]*2+R_extend[i-3]*4+R_extend[i-4]*8;//列号
                String tmp=toBinary(Sbox[(j/6)-1][x][y],4);
                for(int k=0;k<4;k++){
                    arr32ByS[4*((j/6)-1)+k]=(int)(tmp.charAt(k)-'0');
                }
            }
        }

        int[] arr32ByP=new int[32];
        //对上面结果进行P盒置换
        for(int i=0;i<32;i++){
            arr32ByP[i]=arr32ByS[P[i]-1];
        }
        return arr32ByP;
    }

    /**
     * 将一个int数字转换为二进制的字符串形式。
     * @param num 需要转换的int类型数据
     * @param digits 要转换的二进制位数，位数不足则在前面补0
     * @return 二进制的字符串形式
     */
    public static String toBinary(int num, int digits) {
        int value = 1 << digits | num;
        String bs = Integer.toBinaryString(value); //0x20 | 这个是为了保证这个string长度是6位数
        return  bs.substring(1);
    }

    /**
     *用于根据密钥字符串，对明文进行加密
     * @param plaintext 字节数组类型的明文序列，字节数如果不为8的倍数，则会自动补足。
     * @param key 字节数组类型的密钥序列，如果超过8个字节，会截取前8个字节；如果少于8个字节则将补0。
     * @return 返回byte数组类型的密文序列
     */
    public static byte[] encrypt(byte[] plaintext,byte[] key)  {
        return desTest(plaintext,key,1);
    }

    /**
     * 用于根据密钥字符串，对密文进行解密
     * @param ciphertext 字节数组类型的密文序列，字节数应为8的倍数
     * @param key 字节数组类型的密钥序列，如果超过8个字节，会截取前8个字节；如果少于8个字节则将补0
     * @return 返回byte数组类型的密文序列
     */
    public static byte[] decrypt(byte[] ciphertext,byte[] key) {
        return desTest(ciphertext,key,0);
    }


    public static void main(String[] args)  {
        String key="zhaohaichao";
        String data="你好,赵海超哈哈哈1233333333455";
        /*
        //getKey(key);
        long startTime = System.currentTimeMillis();    //获取开始时间的时间戳
        byte[] c=desTest(data.getBytes(),key.getBytes(),1);
        System.out.println(data.getBytes().length);
        System.out.println("密文：\n"+new String(c));
        long endTime = System.currentTimeMillis();    //获取结束时间的时间戳
        System.out.println("加密时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

        startTime = System.currentTimeMillis();    //获取开始时间的时间戳
        byte[]p=desTest(c,key.getBytes(),0);

        System.out.println("明文：\n"+new String(p));
        endTime = System.currentTimeMillis();    //获取结束时间的时间戳
        System.out.println("解密时间：" + (endTime - startTime) + "ms");    //输出程序运行时间*/
        byte[] c=encrypt(data.getBytes(),key.getBytes());
        System.out.println("密文：\n"+new String(c));

        System.out.println("明文：\n"+new String(decrypt(c,key.getBytes())));
    }

}
