package protocol;

import java.util.ArrayList;

import BlockChain.Block;

public abstract class Protocol {
    public static final int SEND = 1;
    public static final int BROADCAST = 2;
    public static final int ANSWER = 3;

    public static Message broadcast_prepare_leader (int type,String message,int[] ballotnum,ArrayList<Integer>  ListIDClients){
        return new Message(BROADCAST ,"prepare",ballotnum,ListIDClients);

    }
    public static Message send_prepare_cohort(int type, String message, int[] AcceptNum, Block Acceptval, int destination,ArrayList<Integer>  ListIDClients) {
        return new Message(SEND,"ack", AcceptNum, Acceptval,destination,ListIDClients);
    }
    public static Message broadcast_accept_leader(int type,String message,int[] BallotNum,Block myVal,ArrayList<Integer>  ListIDClients){
        return new Message(BROADCAST,"acceptL",BallotNum,myVal,ListIDClients);
    }
    public static Message send_accept_cohort(int type,String message,int[] b,Block v,int destination, ArrayList<Integer>  ListIDClients){
        return new Message(SEND,"acceptC",b,v,destination,ListIDClients);
    }
    public static Message broadcast_decsion(int type,String message,int[] b,Block v, ArrayList<Integer>  ListIDClients){
        return new Message(BROADCAST,"decision",b,v,ListIDClients);
    }
    public static Message ClientRequest(String trans, int IDClient){
        return new Message(SEND,"transaction",trans,IDClient);
    }
    public static Message answerClient(String answer,ArrayList<Integer>  ListIDClients){
        return new Message(ANSWER,"answer",answer,ListIDClients);
    }

}
