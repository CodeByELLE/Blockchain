package protocol;

import BlockChain.Block;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
	private String message;
	private int type;
	private int[] ballotnum;
	private int[] AcceptNum;
	private Block AcceptVal = new Block("","","","","");
	private Block val = new Block("","","","","");
	private int destination;
	private String transaction;
	private String answer;
	private ArrayList<Integer> ListIDClients;
	private int IDClient;






	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public ArrayList<Integer> getListIDClients() {
		return ListIDClients;
	}

	public void setListIDClients(ArrayList<Integer> listIDClients) {
		ListIDClients = listIDClients;
	}

	public int getIDClient() {
		return IDClient;
	}

	public void setIDClient(int iDClient) {
		IDClient = iDClient;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int[] getBallotnum() {
		return ballotnum;
	}

	public void setBallotnum(int[] ballotnum) {
		this.ballotnum = ballotnum;
	}

	public int[] getAcceptNum() {
		return AcceptNum;
	}

	public void setAcceptNum(int[] acceptNum) {
		AcceptNum = acceptNum;
	}

	public Block getAcceptVal() {
		return AcceptVal;
	}

	public void setAcceptVal(Block acceptVal) {
		AcceptVal = acceptVal;
	}

	public Block getVal() {
		return val;
	}

	public void setVal(Block val) {
		this.val = val;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public int getBallot0() {
		return (this.ballotnum)[0];
	}
	public int getBallot1() {
		return (this.ballotnum)[1];
	}
	public Message(int type, String message, int[] ballotnum,ArrayList<Integer>  ListIDClients){//prepare leader
		this.type = type;
		this.message = message;
		this.ballotnum = ballotnum;
		this.ListIDClients = ListIDClients;

	}
	public Message(int type,String message,int[] bal,int[] AcceptNum,Block AcceptVal,int destination,ArrayList<Integer>  ListIDClients){ //prepare cohort
		this.type = type;
		this.message = message;
		this.ballotnum= bal;
		this.AcceptNum = AcceptNum;
		this.AcceptVal = AcceptVal;
		this.destination = destination;
		this.ListIDClients = ListIDClients;

	}

	public Message(int type,String message,int[] ballotnum,Block val,ArrayList<Integer>  ListIDClients){ //accept leader and cohort- decide
		this.type = type;
		this.message = message;
		this.ballotnum= ballotnum;
		this.val= val;
		this.ListIDClients = ListIDClients;


	}
	public Message(int type,String message,int[] ballotnum,Block val,int destination,ArrayList<Integer>  ListIDClients){ //accept leader and cohort- decide
		this.type = type;
		this.message = message;
		this.ballotnum= ballotnum;
		this.val= val;
		this.destination = destination;
		this.ListIDClients = ListIDClients;


	}
	public Message(int type,String message, String transaction, int IDClient)
	{
		this.transaction =transaction;
		this.type = type;
		this.message =  message;
		this.IDClient = IDClient;
	}

	public Message(int type,String message, String answer, ArrayList<Integer>  ListIDClients)
	{
		this.answer =answer;
		this.type = type;
		this.message =  message;
		this.ListIDClients = ListIDClients;
	}



}
