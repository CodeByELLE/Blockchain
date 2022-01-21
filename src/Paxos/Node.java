package Paxos;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import BlockChain.Block;
import protocol.*;
import static protocol.Protocol.*;
import Tools.*;

public class Node{

	//static final int GLOBAL_PORT = 3000;
	public ArrayList<Message> transBuff =  new ArrayList<>();
	private int[] AcceptNum = {0,0};
	private Block AcceptVal = new Block("","","","","");
	private int[] ballot = {0,0};
	private DatagramSocket socket;
	private InetAddress address = InetAddress.getByName("localhost");
	private Block val = new Block("","","","","");;
	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	ArrayList<Message> Majority =  new ArrayList<>();
	public ArrayList<Integer> ListIDClients = new ArrayList<>();


	public Node(int id) throws IOException, ClassNotFoundException, InterruptedException {

		this.setBallot1(id);
		System.out.println("I am server : " + this.getBallot1());
		System.out.println("----------------------------------------");
		NodeHandler();

	}
	
	
	public ArrayList<Message> getTransBuff(){
		return transBuff;
	}
	public void SetTransBuff(ArrayList<Message> transBuff) {
		this.transBuff = transBuff;
	}
	public void setBallot1(int id) {
		this.ballot[1] = id;
	}

	public Block getVal() {
		return val;
	}

	public void setVal(Block val) {
		this.val = val;
	}

	public static ArrayList<Block> getBlockchain() {
		return blockchain;
	}

	public static void setBlockchain(ArrayList<Block> blockchain) {
		Node.blockchain = blockchain;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getPort() {
		return GlobalConstants.GLOBAL_PORT+getBallot1();
	}

	public int[] getAcceptNum() {
		return AcceptNum;
	}

	public Block getAcceptVal() {
		return AcceptVal;
	}

	public int[] getBallot() {
		return ballot;
	}

	public void setAcceptNum(int[] acceptNum) {
		AcceptNum = acceptNum;
	}

	public void setAcceptVal(Block val) {
		AcceptVal = val;
	}

	/*public void setBallot(int[] ballot) {
		this.ballot = ballot;
	}*/

	public void setBallot0(int ballot) {
		this.ballot[0] = ballot;
	}
	public int getBallot0() {
		return (this.ballot)[0];
	}
	public int getBallot1() {
		return (this.ballot)[1];
	}
	public void sendMessage(protocol.Message message) throws ClassNotFoundException, InterruptedException, IOException {

		DatagramSocket ds = new DatagramSocket();
		InetAddress ip = InetAddress.getLocalHost();
		byte buf[] = null;

		if(message.getType() == SEND) {
			buf =  CommunFunctions.serialize(message);
			int dest =  message.getDestination();
			DatagramPacket dp = new DatagramPacket(buf,buf.length,this.getAddress(),dest+GlobalConstants.GLOBAL_PORT); // to one server  GLOBAL_PORT+choosenIDServer
			ds.send(dp);
		}
		else if(message.getType() == BROADCAST) {
			buf =  CommunFunctions.serialize(message);
			for(int i=0;i<3;i++) {                                   
				DatagramPacket dp = new DatagramPacket(buf,buf.length,this.getAddress(),i+GlobalConstants.GLOBAL_PORT);
				ds.send(dp);
			}

		}
		else if(message.getType() == ANSWER) {
			buf =  CommunFunctions.serialize(message);
			for(int i=0;i<2;i++) {    
				DatagramPacket dp = new DatagramPacket(buf,buf.length,this.getAddress(),4323+ message.getListIDClients().get(i));
				ds.send(dp);
			}

		}

	}

	public void prepareLeader() throws InterruptedException, IOException, ClassNotFoundException {
		this.setBallot0(this.getBallot()[0]+1);
		sendMessage(broadcast_prepare_leader (2,"prepare",this.getBallot(), this.ListIDClients));
		ListIDClients 	= new ArrayList<>();

	}
	public void prepareCohort(Message m) throws InterruptedException, IOException, ClassNotFoundException {
		this.ListIDClients = m.getListIDClients();
		if(compareBallot(m.getBallotnum(),this.getBallot())) {
			this.setBallot0(m.getBallotnum()[0]);
			sendMessage(send_prepare_cohort(1,"ack",this.getAcceptNum(),m.getAcceptVal(),m.getBallotnum()[1],ListIDClients));
			ListIDClients 	= new ArrayList<>();
		}
	}

	public void acceptLeader(ArrayList<Message> mes) throws InterruptedException, IOException, ClassNotFoundException {
		this.ListIDClients = mes.get(0).getListIDClients();

		boolean b = true;
		for(int i=0; i< mes.size();i++){
			if(!mes.get(i).getVal().equals("")){
				b = false;
			}
		}
		if( b == true){
			this.setAcceptVal(this.getAcceptVal());

		} else {
			int max =0;
			Block maxVal =null;
			for(int i =0;i<mes.size();i++){
				if(mes.get(i).getBallot0()>max){
					max = mes.get(i).getBallot0();
					maxVal =mes.get(i).getVal();
				}
			}

			this.setAcceptVal(maxVal);}


		sendMessage(broadcast_accept_leader(2,"acceptL",this.getBallot(),this.getAcceptVal(), ListIDClients));
		ListIDClients 	= new ArrayList<>();
	}
	public void acceptCohort(Message mes) throws InterruptedException, IOException, ClassNotFoundException {
		this.ListIDClients = mes.getListIDClients();
		if(compareBallot(mes.getBallotnum(),this.getBallot())){
			this.setBallot0(mes.getBallot0());
			this.setAcceptVal(mes.getAcceptVal());
			sendMessage(send_accept_cohort(1,"acceptC",this.getBallot(),this.getAcceptVal(), mes.getBallotnum()[1],ListIDClients));
			ListIDClients 	= new ArrayList<>();
		}


	}
	public void decision(ArrayList<Message> mes) throws InterruptedException, IOException, ClassNotFoundException {
		this.ListIDClients = mes.get(0).getListIDClients();
		if(mes.size() >= 2){
			sendMessage((broadcast_decsion(2,"decision",mes.get(0).getBallotnum(),mes.get(0).getAcceptVal(),ListIDClients)));
			sendMessage(answerClient("ok",mes.get(0).getListIDClients()));
			ListIDClients 	= new ArrayList<>();
		}

	}

	public boolean compareBallot(int[] b1, int[] b2){
		if(b1[0]>b2[0]||(b1[0] == b2[0] && b1[1] > b2[1])) return true;
		else
			return false;
	}




	public void NodeHandler() throws IOException, ClassNotFoundException, InterruptedException {

		DatagramSocket ds = new DatagramSocket(GlobalConstants.GLOBAL_PORT+this.getBallot1());//port of each node//this.getBallot1()+GLOBAL_PORT
		byte[] transReceived = new byte[2048];
		DatagramPacket DpReceive = null;
		while (true) {
			DpReceive = new DatagramPacket(transReceived, transReceived.length);
			ds.receive(DpReceive);
			Message  m = (Message)CommunFunctions.deserialize(transReceived);
			//System.out.println("Message received of type : "+m.getMessage());
			Thread t =  new Thread(new MsgProcessing(this,  m ));
			t.start();

		}
	}

	public  String ConvtoString(byte[] a)
	{
		if (a == null)
			return null;
		StringBuilder ret = new StringBuilder();
		int i = 0;
		while (a[i] != 0)
		{
			ret.append((char) a[i]);
			i++;
		}
		return ret.toString();
	}

	public void printBlockChain() {
		for(int i=0;i<blockchain.size();i++) {
			System.out.println("Block " + (i+1) + " : " + blockchain.get(i).getHash());
		}
	}

}


