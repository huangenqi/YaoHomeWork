package Communication;

import java.io.*;
import java.net.Socket;
import Communication.des.Des;

public class Communication {
    private String  ip;
    private int port;
    private Socket socket;
    private boolean bool;
    private byte[] key;

    /*******饿汉式创建单例模式代码*****/

    private static final Communication instance =new Communication();
    private Communication(){ }
    public static Communication getInstance() {
        return instance;
    }

    /*******饿汉式创建单例模式代码*****/
    public void getSocketObject(Socket socket){
        this.socket=socket;
    }
    public void getDesKey(byte[] key){
        this.key=key;
    }

    /*
    //构造函数
    public Communication(Socket socket){
        this.socket=socket;
    }*/

    /**
     *通过客户端与服务器之间的socket对象，对报文数据/文件数据进行明文发送
     * @param message 以byte数组保存的数据
     * @return 返回值为发送的数据的字节数
     */
    public int sendNotDes(byte[] message) {
        try {
            socket.getOutputStream().write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message.length;
    }

    /**
     * 通过客户端与服务器之间的socket对象，对报文数据/文件数据进行加密发送
     * @param message 以byte数组保存的数据
     * @return 返回值为发送的数据的字节数
     */
    public int send(byte[] message)  {


        byte[] result=Des.encrypt(message,key);

        try {
            socket.getOutputStream().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(result.length);
        return result.length;
    }


    /**
     * 通过客户端与服务器之间的socket对象，对数据进行接收
     * @return 接收到的数据解密后的结果
     */
    public byte[] recieve()  {
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        InputStream socketInputStream = null;
        try {
            socketInputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //接收socket传输的byte数组
        byte[] datas = new byte[1024];

        int length= 0;
        try {
            length = socketInputStream.read(datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] tmp=new byte[length];
        System.arraycopy(datas,0,tmp,0,length);
        //System.out.println(length);
        return Des.decrypt(tmp,key);
        //System.out.println(new String(Des.decrypt(datas,key.getBytes())));
    }

    /**
     * 通过客户端与服务器之间的socket对象，对数据进行明文接收
     * @return 接收到的数据
     */
    public byte[] recieveNotDes()  {
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        InputStream socketInputStream = null;
        try {
            socketInputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //接收socket传输的byte数组
        byte[] datas = new byte[1024];


        int length= 0;
        try {
            length = socketInputStream.read(datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(datas.toString());
        byte[] tmp=new byte[length];
        System.arraycopy(datas,0,tmp,0,length);
        return tmp;
        //System.out.println(new String(Des.decrypt(datas,key.getBytes())));
    }

    /**
     * 修改客户端与服务器之间的socket对象，实现与新的对象进行
     * @param ip 通信对象的IP地址
     * @param port 通信对象的端口号
     */
    public void changeCommunicator(String ip, int port)  {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket=socket;
    }
}
