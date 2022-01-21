import Paxos.Node;
import java.io.IOException;
import java.util.Scanner;

public class LaunchNodes {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
     
    	Scanner sc  =  new Scanner(System.in);
    	System.out.println("Enter the Node's ID : ");
    	int id  =  sc.nextInt();
    	new Node(id);
  	
}
}
