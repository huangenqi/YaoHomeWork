package AuthClient;

import Communication.Communication;
import StateMachine.IStateChange;
import Tool.AddByte;
import Tool.DataTypeChange;

import java.io.IOException;
import java.net.Socket;

public class VAuthToClientPKExchange implements IStateChange {

    public void StateChange(Object data){//message+ket+ipv+pk
        Object[] data1= (Object[]) data;

        byte[] key= (byte[]) data1[1];
        byte[] IPv= (byte[]) data1[2];
        byte[] pk= (byte[]) data1[3];

        byte[] message2= new byte[1];;
        message2[0]= DataTypeChange.IntToUnsignedByte(128);

        message2= AddByte.addBytes(message2,pk);

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
