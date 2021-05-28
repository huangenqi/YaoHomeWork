package AuthClient;

import Communication.Communication;
import StateMachine.IStateChange;
import Tool.AddByte;
import Tool.DataTypeChange;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TGSAuthToVAuth implements IStateChange {

    public void StateChange(Object data){ //message+uid
        //接受 IDv+IPv+ST3+LT3+KEY(C,V)+Ticket2	int+byte[4]+long+long+byte[8]+byte[32]

        Object[] data1= (Object[]) data;
        long uid= (long) data1[1];
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] IP=DataTypeChange.stringToBytes(addr.getHostAddress());

        byte[] message= (byte[]) data1[0];
        byte[] IPv= AddByte.subBytes(message,5,4);
        byte[] Ticket=AddByte.subBytes(message,25,8);
        byte[] key=AddByte.subBytes(message,33,32);


        //发送 IDc+IPc+ST4+LT4+Ticket2	long+byte[4]+long+long+byte[32]
        byte[] message2= new byte[1];;
        message2[0]= DataTypeChange.IntToUnsignedByte(5);

        message2=AddByte.addBytes(message2,DataTypeChange.longToBytes(uid));
        message2= AddByte.addBytes(message2,IP);



        long TG3=  System.currentTimeMillis();
        long LT3=  System.currentTimeMillis()+100000;

        message2=AddByte.addBytes(message2,DataTypeChange.longToBytes(TG3));
        message2=AddByte.addBytes(message2, DataTypeChange.longToBytes(LT3));
        message2=AddByte.addBytes(message2,Ticket);


        try {

            Socket socket=new Socket(DataTypeChange.bytesToString(IPv),9699);

            Communication.getInstance().getSocketObject(socket);
            Communication.getInstance().getDesKey(key);
            Communication.getInstance().send(message2);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
