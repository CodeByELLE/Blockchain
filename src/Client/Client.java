package Client;
import protocol.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;
import Tools.*;

public class Client {
	
	int credit;
	int id;
	DatagramSocket ds ;

	public Client(int id) throws IOException {
		this.id = id;
		ds = new DatagramSocket(GlobalConstants.GLOBAL_PORT_CLIENT + id);
		init();
	}

	public void ClientRequest() throws IOException {
		System.out.println("(form : A B Amount");

		int choosenIDServer = CommunFunctions.randomId(0,2);

		Scanner sc = new Scanner(System.in);
		DatagramSocket ds = new DatagramSocket();
		InetAddress ip = InetAddress.getLocalHost();
		byte buf[] = null;
		while (true) {
			System.out.println("Enter transaction : (form : A B Amount)");
			String transaction = sc.nextLine();
			if (transaction.equals("bye"))
				break;
			Message m =  new Message(1,"transaction",transaction, id);

			buf =  CommunFunctions.serialize(m);

			DatagramPacket dp =
					new DatagramPacket(buf, buf.length, ip,GlobalConstants.GLOBAL_PORT+2); //GLOBAL_PORT+choosenIDServer
			ds.send(dp);

		}
	}



	public void ClientRequest0() throws IOException {

		int choosenIDServer = CommunFunctions.randomId(0,2);


		DatagramSocket ds = new DatagramSocket();
		InetAddress ip = InetAddress.getLocalHost();
		byte buf[] = null;
		FileInputStream fstream = new FileInputStream("E:/meryem/javaWorkspace/blockchain3_1/input_100.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));


		String transaction;
		//Read File Line By Line
		while ((transaction = br.readLine()) != null)   {
			System.out.println("transaction : " +transaction );
			Message m =  new Message(1,"transaction",transaction,id);

			buf =  CommunFunctions.serialize(m);

			DatagramPacket dp =
					new DatagramPacket(buf, buf.length, ip,3000); // GLOBAL_PORT+choosenIDServer
			ds.send(dp);
			System.out.println("msg send : " +transaction );

		}
	}

//	public int randomId(int min,int max){
//		int id = min + (int)(Math.random() * ((max - min) + 1));
//		return id;
//	}
//



	public void init() throws IOException {

		Thread t = new Thread(new ListenerClient(this));
		t.start();

		this.ClientRequest();



		//ask for balance - credit
	}

	public static void main(String[] args) throws IOException {
		Scanner sc  =  new Scanner(System.in);
		System.out.println("Enter the Client's ID : ");
		int id  =  sc.nextInt();

		new Client(id);
	}
}
