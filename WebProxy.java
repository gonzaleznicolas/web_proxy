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
        catch (Exception e){System.out.println("Error1: " + e.getMessage());}

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
                InputStream is = socket.getInputStream();

                //System.out.println("hi");

                // now that we have a connection, wait for the client to send a message.
                //String line = "";
                //String fullMessage = "";
                while(true)
                {
                    //System.out.println("here");
                    //String s = inputStream.nextLine(); // waiting for a message. the server will only get out of this line
                                                // when the client sends a message
                    //System.out.println(s); // print that message

                    byte[] bytes = new byte[10000];


                    boolean bytesLeft = true;
                    int index = 0;
                    int numberOfConsecutiveNorR = 0;
                    int indexAtWhichHeaderLinesEnd = 0;
                    int indexAtWhichDataEnds = 0;
                    boolean haveReachedEndOfHeaderLines = false;
                    while (haveReachedEndOfHeaderLines==false)
                    {
                        //if (index >= 180) break;
                        int numRepresentationOfByte = is.read();
                        if (numRepresentationOfByte==10 || numRepresentationOfByte==13)
                        {
                            numberOfConsecutiveNorR++;
                        }
                        else
                        {
                            numberOfConsecutiveNorR=0;
                        }
                        if (numberOfConsecutiveNorR >= 4 && haveReachedEndOfHeaderLines==false)
                        {
                            indexAtWhichHeaderLinesEnd = index;
                            haveReachedEndOfHeaderLines = true;
                        }
                        byte byteRead = (byte) numRepresentationOfByte;
                        //System.out.printf("0x%02X\n", byteRead);
                        //System.out.printf("%c\n", numRepresentationOfByte);
                        bytes[index] = byteRead;
                        /*
                            System.out.println("index "+index);
                            System.out.println("hasNextLine "+inputStream.hasNextLine());
                            System.out.println("haveReachedEndOfHeaderLines "+haveReachedEndOfHeaderLines);
                            System.out.println("numRepresentationOfByte "+numRepresentationOfByte);
                            System.out.println("numberOfConsecutiveNorR "+numberOfConsecutiveNorR);
                            System.out.println("indexAtWhichHeaderLinesEnd "+indexAtWhichHeaderLinesEnd);
                            System.out.println("indexAtWhichDataEnds "+indexAtWhichDataEnds);
                            System.out.println("--------------------------------");
                        */

                        index++;

                    }

                    byte[] headerLines = new byte[indexAtWhichHeaderLinesEnd+1];
                    for (int i = 0; i <= indexAtWhichHeaderLinesEnd; i++)
                    {
                        headerLines[i] = bytes[i];
                    }

                    String requestMessage = new String(headerLines);
                    System.out.println("HO");
                    System.out.println(requestMessage);
                    System.out.println("HI");
                    System.out.println(headerLines.length);
                    System.out.println(requestMessage.length());
                    System.out.println(headerLines[headerLines.length-1]);

                    // EXTRACT THE HOST NAME FROM THE REQUEST MESSAGE
                    String requestMessageStartingAtHostName = requestMessage.substring(requestMessage.indexOf("Host: ")+6);
                    String hostName = requestMessageStartingAtHostName.substring(0,requestMessageStartingAtHostName.indexOf("\n")-1);
                    System.out.println(hostName);

                    // EXTRACT THE HOST NAME FROM THE REQUEST MESSAGE
                    String pathName = requestMessage.substring(requestMessage.indexOf(hostName)+hostName.length()+1,requestMessage.indexOf("HTTP")-1);
                    System.out.println(pathName);




                    

                    
                }

            }
        }
        catch (Exception e){System.out.println("Error2: " + e.getMessage());}
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
