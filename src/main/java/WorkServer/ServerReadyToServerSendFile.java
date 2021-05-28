package WorkServer;

import Filemanger.FileManager;
import StateMachine.IStateChange;
import Tool.DataTypeChange;
import Communication.*;

import java.util.Arrays;

public class ServerReadyToServerSendFile implements IStateChange {

    @Override
    public void StateChange(Object data) {
        //参数转换为byte数组
        byte[] dataBytes=(byte[])data;
        //获取报文信息
        int fileID= DataTypeChange.bytesToInt(Arrays.copyOfRange(dataBytes,1,5));
        int fileNameLen=DataTypeChange.bytesToInt(Arrays.copyOfRange(dataBytes,5,9));
        String fileName=DataTypeChange.bytesToString(Arrays.copyOfRange(dataBytes,9,dataBytes.length));

      //  FileManager.getInstance().downloadFile(fileName);
        byte[] fileSizeBytes=DataTypeChange.longToBytes(FileManager.getInstance().getFileSize(fileName));
        byte[] fileHashBytes=FileManager.getInstance().HashFile(fileName);
        byte[] sendMsg=new byte[fileSizeBytes.length+fileHashBytes.length+1];
        sendMsg[0]=DataTypeChange.IntToUnsignedByte(136);
        System.arraycopy(fileSizeBytes,0,sendMsg,1,fileSizeBytes.length);
        System.arraycopy(fileHashBytes,0,sendMsg,1+fileSizeBytes.length,fileHashBytes.length);
        Communication.getInstance().send(sendMsg);
    }
}
