/**
 * WebProxy Class
 * 
 * @author	  Nicolas Gonzalez
 * @version	 1.1, 20 Jan 2017
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;


public class WebProxy {

	public ServerSocket serverSocket = null;
    public Socket socket = null;

	 /*
	 *  Constructor that initalizes the server listenig port
	 * @param port	  Proxy server listening port
	 */

	public WebProxy(int port)
    {
    	/* Intialize server listening port */
        try { this.serverSocket = new ServerSocket(port); }
        catch (Exception e){System.out.println("Error: " + e.getMessage());}
        finally
        {
            if (this.serverSocket != null)
            {
                try { serverSocket.close();}
                catch (IOException ex){/* ignore */}
            }
       }
	}



	 /**
	 * The webproxy logic goes here 
	 */
	public void start()
    {
        try
        {
            while (true)
            {
                socket = serverSocket.accept(); // instantiate new socket

                // make streams
                PrintWriter outputStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                Scanner inputStream = new Scanner(socket.getInputStream(), "UTF-8");
                System.out.println("hi");

                // now that we have a connection, wait for the client to send a message.
                while(true)
                {
                    System.out.println("here");
                    String s = inputStream.nextLine(); // waiting for a message. the server will only get out of this line
                                                // when the client sends a message
                    System.out.println(s); // print that message
                    
                    /*
                    // exit if message from client is "bye"
                    if(s.equalsIgnoreCase("bye"))
                    {
                        outputStream.println("bye");
                        outputStream.flush();
                        break;
                    }
                    outputStream.println(s);
                    outputStream.flush();
                    */
                    
                }

            }
        }
        catch (Exception e){System.out.println("Error: " + e.getMessage());}
        finally
        {
            if (this.socket != null)
            {
                try { socket.close();}
                catch (IOException ex){/* ignore */}
            }
       }

	}



/**
 * A simple test driver
*/
	public static void main(String[] args)
	{
		String server = "localhost"; // webproxy and client runs in the same machine
		int server_port = 0;
		try 
        {
			// check for command line arguments
			if (args.length == 1)
			{
				server_port = Integer.parseInt(args[0]);
			}
			else
			{
				System.out.println("wrong number of arguments, try again.");
				System.out.println("usage: java WebProxy port");
				System.exit(0);
			}
    		WebProxy proxy = new WebProxy(server_port);
			System.out.printf("Proxy server started...\n");
			proxy.start();
		}
        catch (Exception e)
		{
			System.out.println("Exception in main: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
}
