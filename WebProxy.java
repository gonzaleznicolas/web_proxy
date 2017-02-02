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

                    // CHECK THAT CLIENT MADE A "get" REQUEST. IF NOT, SEND A RESPONSE MESSAGE WITH
                    // STATUS CODE "400 Bad Request"

                    /* ####################################################################
                    #######################################################################
                    #######################################################################
                    #######################################################################
                    #######################################################################
                    ###################################################################### */

                    // EXTRACT THE HOST NAME FROM THE REQUEST MESSAGE
                    String requestMessageStartingAtHostName = requestMessage.substring(requestMessage.indexOf("Host: ")+6);
                    String hostName = requestMessageStartingAtHostName.substring(0,requestMessageStartingAtHostName.indexOf("\n")-1);
                    System.out.println(hostName);

                    // EXTRACT THE HOST NAME FROM THE REQUEST MESSAGE
                    String pathName = requestMessage.substring(requestMessage.indexOf(hostName)+hostName.length()+1,requestMessage.indexOf("HTTP")-1);
                    System.out.println(pathName);

                    // CHECK IF THE OBJECT REQUESTED IS AVAILABLE IN THE LOCAL CACHE
                    /* ####################################################################
                    #######################################################################
                    #######################################################################
                    #######################################################################
                    #######################################################################
                    ###################################################################### */

                    // IF SO, RETURN IT FROM THE LOCAL CACHE
                    /* ####################################################################
                    #######################################################################
                    #######################################################################
                    #######################################################################
                    #######################################################################
                    ###################################################################### */

                    // ELSE, GET IT FROM THE INTERNET
                    InetAddress ipOfServer = InetAddress.getByName(hostName); // get IP address of server

                    Socket clientSocket = new Socket(ipOfServer, 80);

                    PrintWriter clientOutputStream = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
                    InputStream clientInputStream = clientSocket.getInputStream();


                    // Send http request message to the server
                    clientOutputStream.println(requestMessage);
                    //Flush to make sure message is send
                    clientOutputStream.flush(); // output stream has a buffer so we want to flush it

                    // im at the stage where i have forwarded the client's request to the server, and now i want to read what
                    // the server responded so i can 1) write it to a file 2) send it back to the client.
                    // question:
                        // can i do that by reading one byte from that input stream, and immediately writing that same byte to a file output
                        // stream and to the output stream that speaks to the client?
                    // if so, how do i identify the end of the data? bcause what i noticed when getting the request message from the client,
                    // was that when the data is done, -1 is not returned by read(), instead it just waits for more data.

                    //TA ANSWER: the response message has a field that says how many bytes the answer is. read that many bytes.
                    // or, make conection "close" so the server closes the connection after.







                    

                    
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
