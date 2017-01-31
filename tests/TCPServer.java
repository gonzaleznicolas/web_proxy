import java.io.*;
import java.net.*;
import java.util.*;


public class TCPServer {


	public static void main(String[] args)
	{
		
		String s;
		
		// in java , we have the serverSocket class which is in charge of accepting connections and managing connections.
		// in the server, instead of instatiating a single socket, we instantiate a server socket
		ServerSocket serverSocket = null;
		// and also instantiate a socket
		Socket socket = null;
		try
		{
		// we can pass a ServerSocker constructor 3 parameters.
		// 1: the port that the socket will be listening to. of course, it must be 8888 (the port we made the client connect to)
		//		we chose 8888 because the TA doesnt know of anothr service which uses that port, so it should be okay.
		//		its important to pick a port that no over process is listening to already
		//		because most OSs only allow one process to listen to each port
		//		some ports require some special permission. reserved for OS. those are the lower number ports.
		// 2: optional parameter. it is the queue number. it is the number of connections that i am willing to accept
		//		before i start rejecting connections. default queue number for serverSocket is 50. You can establish 50 connections
		//		using a single socket. it doesnt mean you can communicate with 50 clients at once. it means you can establish 50
		//		before you start rejecting new connections
		// 3: address.
		serverSocket = new ServerSocket(8888);
        
		// do a while true loop to accept connections.
        while(true)
        {

		socket = serverSocket.accept(); // instantiate new socket
		// Connected to client
		
		// make some streams like for the lient
		PrintWriter outputStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		Scanner inputStream = new Scanner(socket.getInputStream(), "UTF-8");


		// now that we have a connection, wait for the client to send a message.
		// Respond to messages from the client
		while(true)
		{
			s = inputStream.nextLine(); // waiting for a message. the server will only get out of this line
										// when the client sends a message
			System.out.println(s); // print that message
			
			// exit if message from client is "bye"
			if(s.equalsIgnoreCase("bye"))
			{
				outputStream.println("bye");
	            outputStream.flush();
				break;
			}
			outputStream.println(s);
			outputStream.flush();
            
		} //inner while loop
        } // outer while loop
        
        // one problem with this code is th 
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
                        if (serverSocket != null)
                        {
                                try
                                {
                                        serverSocket.close();
                                }
                                catch (IOException ex)
                                {
                                // ignore
                                }

                        }

                }

	}
}
