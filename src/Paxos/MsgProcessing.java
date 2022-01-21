package Paxos;

import BlockChain.Block;
import protocol.Message;
import java.io.IOException;
import java.util.ArrayList;
import static BlockChain.Block.hashstring;

public class MsgProcessing implements Runnable{
	private Node server;
	Message msg;

	public MsgProcessing(Node server, Message msg){
		this.server = server;
		this.msg =  msg;

	}
	public  void Processing() throws IOException, ClassNotFoundException, InterruptedException {

		if(msg.getMessage().equals("transaction")) {


			server.getTransBuff().add(msg);
			server.ListIDClients.add(msg.getIDClient());


			if (server.transBuff.size() == 2) {
				Block currentBlock;
				String hashListTrans = hashstring(server.transBuff.get(0).getTransaction() + server.transBuff.get(1).getTransaction());
				String nonce = mineBlock(hashListTrans);
				int size = server.getBlockchain().size() - 1;

				if (size < 0) {
					currentBlock = new Block(server.transBuff.get(0).getTransaction(), server.transBuff.get(1).getTransaction(), "0", nonce, hashListTrans);
				} else {
					currentBlock = new Block(server.transBuff.get(0).getTransaction(), server.transBuff.get(1).getTransaction(), server.getBlockchain().get(size).hash, nonce, hashListTrans);
				}

				server.setAcceptVal(currentBlock);
				server.prepareLeader();
				System.out.println("Election has started ....");
				server.transBuff = new ArrayList<>();

			}

		}else if (msg.getMessage().equals("prepare")){
			System.out.println("PREPARE received ");
			server.prepareCohort(msg);
			

		}else if(msg.getMessage().equals("ack")){
			System.out.println("ACK received ");
			server.Majority.add(msg);
			if(server.Majority.size() >= 2){
				server.acceptLeader(server.Majority);
				server.Majority = new ArrayList<>();
			}

		}else if(msg.getMessage().equals("acceptC")){
			System.out.println("ACCEPT COHORT received ");
			server.Majority.add(msg);
			if(server.Majority.size() >= 2){
				server.decision(server.Majority);
				server.Majority = new ArrayList<>();
			}

		}else if(msg.getMessage().equals("acceptL")){
			System.out.println("ACCEPT LEADER received ");
			server.acceptCohort(msg);
			
		}else if(msg.getMessage().equals("decision")){
			System.out.println("DECESION received ");
			server.getBlockchain().add(msg.getAcceptVal());
			System.out.println("Block received :" + msg.getAcceptVal());
			server.printBlockChain();
		}
	}
	public int randomNonce(int min,int max){
		int nonce = min + (int)(Math.random() * ((max - min) + 1));
		return nonce;
	}

	public  String mineBlock(String hashListTrans) {
		String nonce,hashConcat;
		nonce =  Integer.toString(randomNonce(0,1000));
		String concat = nonce + hashListTrans;
		hashConcat = hashstring(concat);
		char subChar = hashConcat.substring( hashConcat.length()-1, hashConcat.length()).charAt(0);
		while("0123456789abcdefghijklmn".indexOf(subChar) == -1) {
			System.out.println("inside mineBlock while");
			nonce =  Integer.toString(randomNonce(0,1000));
			hashConcat = hashstring(concat);
		}
		System.out.println("Block Mined!!! : " + hashConcat);
		return nonce;
	}


	public static String ConvtoString(byte[] a)
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
	public boolean compareBallot(int[] b1, int[] b2){
		if(b1[0]>b2[0]||(b1[0] == b2[0] && b1[1] > b2[1])) return true;
		else
			return false;
	}

	@Override
	public void run() {
		try {
			Processing();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
