package BlockChain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Block implements Serializable{

	public String hash;
	public String previousHash;
	private String tran1;
	private String tran2;// data   = transaction.
	private long timeStamp; //as number of milliseconds since 1/1/1970.
	private String nonce;
	int min = 0;
	int max = 1000;
	private String hashListTrans;

	//////////////////////////////////////


	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public String getTran1() {
		return tran1;
	}

	public void setTran1(String tran1) {
		this.tran1 = tran1;
	}

	public String getTran2() {
		return tran2;
	}

	public void setTran2(String tran2) {
		this.tran2 = tran2;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	//////////////////////////////////////////
	//Block Constructor.

	public Block() {

	}
	public Block(String tran1, String tran2,String previousHash, String nonce , String hashListTrans) {
		this.tran1 = tran1;
		this.tran2 = tran2;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash(); //Making sure we do this after we set the other values.
		this.hashListTrans = hashListTrans;
		this.nonce = nonce;

	}

	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256(
				previousHash +
				Long.toString(timeStamp) +
				nonce +
				tran1 + tran2
				);
		return calculatedhash;
	}
	public static String hashstring(String s){

		return StringUtil.applySha256(s);
	}

}