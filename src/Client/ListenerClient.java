package Client;
import protocol.*;
import java.io.IOException;
import java.net.DatagramPacket;

import Tools.CommunFunctions;

public class ListenerClient implements Runnable {
	Client client;

	public ListenerClient(Client client) throws IOException {
		this.client = client;
	}

	public void init() throws IOException, ClassNotFoundException {
		byte[] response = new byte[1024];

		DatagramPacket dpReceive = null;
		while (true) {
			dpReceive = new DatagramPacket(response, response.length);
			client.ds.receive(dpReceive);
			Message m = (Message) CommunFunctions.deserialize(response);
			System.out.println("feedback received : " + m.getAnswer());
			response = new byte[1024];
		}
	}



	public String toString(byte[] a) {
		if (a == null)
			return null;
		StringBuilder ret = new StringBuilder();
		int i = 0;
		while (a[i] != 0) {
			ret.append((char) a[i]);
			i++;
		}
		return ret.toString();
	}

	public void run() {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}



