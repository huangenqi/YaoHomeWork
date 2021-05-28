package Communication.des;

import java.io.UnsupportedEncodingException;

public class Key {
    //密钥置换表PC-1
    public static int[] PC1 = {
            57,49,41,33,25,17,9,
            1,58,50,42,34,26,18,
            10,2,59,51,43,35,27,
            19,11,3,60,52,44,36,
            63,55,47,39,31,23,15,
            7,62,54,46,38,30,22,
            14,6,61,53,45,37,29,
            21,13,5,28,20,12,4};
    //密钥置换表PC-1
    private static int[] PC2={
            14,17,11,24,1,5,
            3,28,15,6,21,10,
            23,19,12,4,26,8,
            16,7,27,20,13,2,
            41,52,31,37,47,55,
            30,40,51,45,33,48,
            44,49,39,56,34,53,
            46,42,50,36,29,32};

    //移位次数表
    private static int[] len={ 1,1,2,2,2,2,2,2,1,2, 2, 2, 2, 2, 2, 1};

    /**
     * 处理用户输入，获取8字节的key
     * 不足补0，超出截取前8个字节
     * @param key
     * @return
     */
    public static byte[] getKey(byte[] key) {
        byte[] arrKey=new byte[8];
        //byte[] arrTmp=key.getBytes("unicode");//1个汉字为4byte，其他字符为2byte
        byte[] arrTmp=key;
        for(int i=0;i<arrTmp.length&&i<arrKey.length;i++){
            arrKey[i]=arrTmp[i];
        }
        //System.out.println(Arrays.toString(arrKey));
        return arrKey;
    }

    /**
     * 使用密钥置换表PC-1
     * 将64位key置换位56位
     * @param arrKey
     * @return
     */
    public static int[] shiftByPC1(byte[] arrKey){
        int[] key64=new int[64];
        int[] key56=new int[56];
        int index=0;
        //转化类型：将byte转化为8位二进制数组
        for(int i=0;i<arrKey.length;i++){
            byte tmp=arrKey[i];
            for(int j=7;j>=0;j--){
                key64[index+j]=(int)(tmp&1);
                //System.out.println(key64[index+j]);
                tmp=(byte)(tmp>>1);
            }
            index+=8;
        }
        //System.out.println(Arrays.toString(key64));
        //置换压缩长度
        for(int i=0;i<PC1.length;i++){
            key56[i]=key64[PC1[i]-1];
        }
        //System.out.println(Arrays.toString(key56));
        return key56;
    }

    /**
     * 对56位key进行分组循环，进行16次，并压缩置换为48位
     * 生成16组子密钥
     * @param key56
     * @return
     */
    public static int[][] divideAndShift(int[] key56){
        int[] C0=new int[28];
        int[] D0=new int[28];
        int[] tmpC=new int[28];
        int[] tmpD=new int[28];
        int[] tmp56=new int[56];
        int[][] subKeys=new int[16][48];

        //分类
        for(int i=0;i<28;i++){
            C0[i]=key56[i];
            D0[i]=key56[i+28];
        }
        //移位
        for(int i=0;i<len.length;i++){
            //循环左移
            for(int j=0;j<C0.length;j++){
                tmpC[j]=C0[(j+len[i])%28];
                tmpD[j]=D0[(j+len[i])%28];
            }
            for(int j=0;j<C0.length;j++){
                C0[j]=tmpC[j];
                D0[j]=tmpD[j];
            }
            //合并
            for(int j=0;j<tmp56.length;j++){
                if(j<28){
                    tmp56[j]=tmpC[j];
                }
                else{
                    tmp56[j]=tmpD[j-28];
                }

            }
            //压缩置换PC-2
            for(int j=0;j<PC2.length;j++){
                subKeys[i][j]=tmp56[PC2[j]-1];
            }
        }
        return subKeys;
    }

    public static int[][] getSubKeys(byte[] key) {

        byte[] arrkey= new byte[0];//获取8字节密钥
        arrkey = getKey(key);
        int[] key56=shiftByPC1(arrkey);//使用PC-1表压缩置换

        return divideAndShift(key56);//16个48位子密钥
    }

    public static void main(String[] args) {

        String key="133457799BBCDFF1";
        //int[] key56={1,1,1,1,0,0,0,0,1,1,0,0,1,1,0,0,1,0,1,0,1,0,1,0,1,1,1,1,0,1,0,1,0,1,0,1,0,1,1,0,0,1,1,0,0,1,1,1,1,0,0,0,1,1,1,1};
        int[][] subKey=getSubKeys(key.getBytes());
        //int[][] subKey=divideAndShift(key56);
        //System.out.println(Arrays.toString(subKey[15]));
        /*
        String test="赵/";
        byte[] t=test.getBytes("unicode");
        System.out.println(t.length);*/
    }
}
