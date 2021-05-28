package AuthClient;

import Tool.AddByte;
import Tool.DataTypeChange;
import StateMachine.IStateChange;
import Communication.Communication;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientStartToASAuth implements IStateChange {


    public void StateChange(Object data){  //uid + IPas
        Object[] data1= (Object[]) data;
        String  IPas= (String) data1[1];
        long TG1;
        byte[] IPc;
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Local HostAddress: "+addr.getHostAddress());
        IPc=DataTypeChange.stringToBytes(addr.getHostAddress());


      TG1= System.currentTimeMillis();

        // return new Date();
//long+btye[4]+long
        long uid= (long) data1[0];
        System.out.println(uid);
        System.out.println(IPc);
        System.out.println(TG1);

        byte[] message = new byte[1];
        message[0]= DataTypeChange.IntToUnsignedByte(1);
        System.out.println(message[0]);
        System.out.println("未合并："+DataTypeChange.bytesToHex(message));
        message= AddByte.addBytes(message,DataTypeChange.longToBytes(uid));
        System.out.println("合并："+DataTypeChange.bytesToHex(message));
        System.out.println("uid="+DataTypeChange.bytesToHex(DataTypeChange.longToBytes(uid)));
        byte[] a=DataTypeChange.longToBytes(uid);
        System.out.println("改后："+DataTypeChange.bytesToLong(a));
       message= AddByte.addBytes(message, IPc);
        System.out.println("IPc="+DataTypeChange.bytesToHex(IPc));
        message=AddByte.addBytes(message,DataTypeChange.longToBytes(TG1));
        System.out.println("TG1="+DataTypeChange.bytesToHex(DataTypeChange.longToBytes(TG1)));
        System.out.println("message="+DataTypeChange.bytesToHex(message));
        System.out.println(message.length);
        String IP=IPas;
        try {
            Socket socket=new Socket(IP,9699);
            Communication client=Communication.getInstance();
            client.getSocketObject(socket);
            Communication.getInstance().sendNotDes(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
