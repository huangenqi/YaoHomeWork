package AuthClient;


import Communication.Communication;
import StateMachine.*;
import Tool.DataTypeChange;

import java.io.IOException;
import java.net.Socket;

public class ClientStart implements IStateMachineStart {

    private static ClientStart instance;
    public static synchronized ClientStart getInstance() {
        if (instance == null) {
            instance = new ClientStart();
        }
        return instance;
    }

    public void stateStart(){
        long uid=2018100;
        Object[] data=new Object[2];
        data[0]=uid;
        data[1]="192.168.43.216";
        StateMachine.GetInstance().StateChange("Log",data);
        /*System.out.println("!!");
        Socket socket= null;
        try {
            socket = new Socket("192.168.43.216",9699);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Communication client=Communication.getInstance();
        client.getSocketObject(socket);
        byte[] message = new byte[]
                DataTypeChange.stringToBytes("sahngwenkai");
        Communication.getInstance().sendNotDes(message);*/
    }

}
