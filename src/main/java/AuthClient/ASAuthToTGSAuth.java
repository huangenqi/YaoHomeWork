package AuthClient;

import Communication.Communication;
import Tool.AddByte;
import StateMachine.IStateChange;
import Tool.DataTypeChange;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ASAuthToTGSAuth implements IStateChange {


    public void StateChange(Object data){ //message+uid+idv
        Object[] data1= (Object[]) data;
        long uid= (long) data1[1];

        int IDv= (int) data1[2];

        byte[] message= (byte[]) data1[0];  //byte[4]+long+long+byte[40]+byte[8] IPtgs+TS2+LT2+Ticket+KEY(c,tgs)
        byte[] IPtgs= AddByte.subBytes(message,1,4);
        byte[] Ticket=AddByte.subBytes(message,21,40);
        byte[] key=AddByte.subBytes(message,61,8);
        //IDc+IPc+TS3+Ticket+IDv	long+byte[4]+long+byte[40]+int


        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] IP=DataTypeChange.stringToBytes(addr.getHostAddress());



        byte[] message2= new byte[1];;
        message2[0]= DataTypeChange.IntToUnsignedByte(3);
        message2=AddByte.addBytes(message2,DataTypeChange.longToBytes(uid));
        message2= AddByte.addBytes(message2,IP);

        //byte[] message = new byte[21];
       // message[0]= DataTypeChange.IntToUnsignedByte(1);

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long TG3=  System.currentTimeMillis();
        message2=AddByte.addBytes(message2,DataTypeChange.longToBytes(TG3));
        message2=AddByte.addBytes(message2,Ticket);
        message2=AddByte.addBytes(message2,DataTypeChange.intToBytes(IDv));

        try {

        Socket socket=new Socket(DataTypeChange.bytesToString(IP),9699);

        Communication.getInstance().getSocketObject(socket);
        Communication.getInstance().getDesKey(key);
        Communication.getInstance().send(message2);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
