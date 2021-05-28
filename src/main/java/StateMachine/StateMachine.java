package StateMachine;

import java.util.HashMap;

/**
 * 状态机类,
 * 调用AddStateChange添加状态转变,
 * 调用Start启动状态机
 */
public class StateMachine {
    private static StateMachine instance;
    private StateMachine()
    {
        stateChangeMap=new HashMap<String, StateChangeInfo>();
    }
    public static StateMachine GetInstance()
    {
        if(instance==null)
        {
            instance=new StateMachine();
        }
        return instance;
    }

    private String nowState=null;
    private HashMap<String, StateChangeInfo> stateChangeMap=null;


    public void SetStartState(String state)
    {
        if(nowState==null)
        {
            nowState=state;
        }
    }


    /**
     * 添加状态转变
     * @param stateChange 状态转变类
     * @return 当前状态转变类总数(-1stateChange为null，-2stateChange已存在)
     */
    public int addStateChange(StateChangeInfo stateChange)
    {
        if(stateChange==null)
        {
            //log
            return -1;
        }
        String hashKey=stateChange.getNowState()+stateChange.getChange();
        if(stateChangeMap.get(hashKey)!=null)
        {
            //log
            return -2;
        }
        stateChangeMap.put(hashKey,stateChange);
        //log
        return stateChangeMap.size();
    }


    public String StateChange(String change,Object data)
    {
        String hashKey=nowState+change;
        StateChangeInfo stateChangeInfo=stateChangeMap.get(hashKey);
        if(stateChangeInfo==null)
        {
            //log
            return nowState;
        }
        //log
        if(change!=null)
        {
            stateChangeInfo.stateChangeExec(data);
        }
        //log
        nowState=stateChangeInfo.getNextState();
        return nowState;
    }

    public void Start(IStateMachineStart stateMachineStart,String startState)
    {
        nowState=startState;
        stateMachineStart.stateStart();
    }
}
