import java.io.*;
import java.net.*;
import java.util.*;


public class TCPClient {


	public static void main(String[] args)
	{
		
		String tmp;
		String s = "";
		// COMMENTED OUT BECAUSE THEY ARE DEFINED LATER
		//Scanner inputStream;
		//PrintWriter outputStream;
		//Scanner userinput;
		Socket socket= null;

		try
		{
		// connects to port server app listesing at port 8888 in the same machine
		// in java, when you create a new socket, you are saying "connect somewhere"
		// The two arguments saying is that this socket is suppposed to connect at address localhost and port 8888
		// you have to give the Socket constructor at least those 2 arguments. the address and the port.
		// localhost means we are referring to our own machine
		// if i wanted to connect to an external host, i would put the address or the ip address where i put local host
		// each computer has a set of ports. each port can be used to listen to clients
		// some of those ports are used for specific services. 
		InetAddress IPAddress = InetAddress.getByName("pages.cpsc.ucalgary.ca"); 

		socket = new Socket(IPAddress, 80);

		// Create necessary streams
		
		// in java, we also need to set up the input and output stream.
		// for the client, the input stream will be the data received from the socket.
		// for the client, the output stream is the channel i will be using to send data through this socket
		PrintWriter outputStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		Scanner inputStream = new Scanner(socket.getInputStream(), "UTF-8");
		Scanner userinput = new Scanner(System.in);
		
		// how do you make sure you dont get disconnected or that you keep this connection open?
		// a good way is to have a while true loop which consists of what the client is going to do with the server
		// in this case its just echoing
		// send/receive messages to/from server
		while(true)
		{
			// we read what the user types,
			System.out.println("Enter Text Message for Echo Server: ");	
			tmp = userinput.nextLine(); 
			tmp = "GET /~cyriac.james/sample.txt HTTP/1.0\r\nHost: pages.cpsc.ucalgary.ca\r\nConnection: keep-alive\r\n\r\n";
		
			// Send user input message to server
			outputStream.println(tmp);
			//Flush to make sure message is send
			outputStream.flush(); // output stream has a buffer so we want to flush it
		
			System.out.println("response:");

			// we expect to get a response. if we send a message and get no reply, we will get stuck here
			String responseMessage = "";
			while(inputStream.hasNextLine())
			{
				s = inputStream.nextLine();
				responseMessage = responseMessage + s + "\r\n";
				//System.out.println(s);
			}
			System.out.println("this is the response message:");
			System.out.println(responseMessage);
			
			// Exit if message from server is "bye"
			if(s.equalsIgnoreCase("bye"))
				break;
		
		}
		}	
	
		catch (Exception e)
		{
		System.out.println("Error: " + e.getMessage());
		}
		finally 
		{
			if (socket != null) 
			{
				try 
				{
					socket.close();
				} 
				catch (IOException ex) 
				{
				// ignore
				}	

			}
		}
	}
}
