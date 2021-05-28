package AuthClient;

import StateMachine.*;

public class Main {
    public static void main(String[] args) {
        StateMachine.GetInstance().addStateChange(new StateChangeInfo("ClientStart","Log","ASAuth",new ClientStartToASAuth()));
        /*StateMachine.GetInstance().addStateChange(new StateChangeInfo("ASAuth","2","TGSAuth",new ASAuthToTGSAuth()));
        StateMachine.GetInstance().addStateChange(new StateChangeInfo("TGSAuth","4","VAuth",new TGSAuthToVAuth()));
        StateMachine.GetInstance().addStateChange(new StateChangeInfo("VAuth","6","ClientPKExchange",new VAuthToClientPKExchange()));
        StateMachine.GetInstance().addStateChange(new StateChangeInfo("ClientPKExchange","129","FileListRecv",new ClientPKExchangeToFileListRecv()));
        StateMachine.GetInstance().addStateChange(new StateChangeInfo("FileListRecv","131","ClientReady",new FileListRecvToClientReady()));
*/
        StateMachine.GetInstance().Start( ClientStart.getInstance(),"ClientStart");
    }

}
