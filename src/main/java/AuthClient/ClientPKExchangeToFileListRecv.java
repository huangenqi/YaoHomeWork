package AuthClient;

import Communication.Communication;
import StateMachine.IStateChange;
import Tool.AddByte;
import Tool.DataTypeChange;

import java.io.IOException;
import java.net.Socket;

public class ClientPKExchangeToFileListRecv implements IStateChange {

    public void StateChange(Object data){////message+ket+ipv
        Object[] data1= (Object[]) data;

        byte[] key= (byte[]) data1[1];
        byte[] IPv= (byte[]) data1[2];


        byte[] message= (byte[]) data1[0];

        byte[] pk= AddByte.subBytes(message,1,256);
        byte[] num=AddByte.subBytes(message,257,4);
        //130
        int number= DataTypeChange.bytesToInt(num);

        byte[] message2= new byte[1];;
        message2[0]= DataTypeChange.IntToUnsignedByte(130);

        message2= AddByte.addBytes(message2,DataTypeChange.intToBytes(number));

        try {

            Socket socket=new Socket(DataTypeChange.bytesToString(IPv),9699);

            Communication.getInstance().getSocketObject(socket);
            Communication.getInstance().getDesKey(key);
            Communication.getInstance().send(message2);
        }catch (IOException e) {
            e.printStackTrace();
        }


    }
}
