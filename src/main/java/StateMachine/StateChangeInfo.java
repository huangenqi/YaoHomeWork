package StateMachine;

public class StateChangeInfo {
    private String nowState;
    private String change;
    private String nextState;
    private IStateChange stateChange;

    public StateChangeInfo(String nowState,String change,String nextState,IStateChange stateChange)
    {
        this.nowState=nowState;
        this.change=change;
        this.nextState=nextState;
        this.stateChange=stateChange;
    }

    public String getNowState()
    {
        return nowState;
    }

    public String getChange()
    {
        return change;
    }

    public String getNextState()
    {
        return nextState;
    }

    public void stateChangeExec(Object data)
    {
        stateChange.StateChange(data);
    }
}
