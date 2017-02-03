/**
 * WebProxy Class
 * 
 * @author      Nicolas Gonzalez
 * @version     1.1, 20 Jan 2017
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
     * @param port      Proxy server listening port
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
                OutputStream os = socket.getOutputStream();
                InputStream is = socket.getInputStream();

                //System.out.println("hi");

                // now that we have a connection, wait for the client to send a message.
                //while(true)
                //{
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

                        bytes[index] = byteRead;

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

                    // if it is not a request message

                    
                    
                    if (!requestMessage.contains("GET") || requestMessage.contains("If-modified-since:") || requestMessage.contains("If-Modified-Since:"))
                    {
                        System.out.println("BAD REQUEST!!!!!!!!!!!");
                        String badReq = "HTTP/1.1 400 Bad Request\r\n\r\n";
                        byte[] br = badReq.getBytes();
                        os.write(br);
                    }
                    
                    

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



                    // GET REPLY FROM SERVER AND PUT IT INTO TWO ARRAYS: responseHeaderLines AND data
                    byte[] responseBytes = new byte[10000];

                    boolean responseBytesLeft = true;
                    int responseIndex = 0;
                    int responseNumberOfConsecutiveNorR = 0;
                    int responseIndexAtWhichHeaderLinesEnd = 0;
                    int responseIndexAtWhichDataEnds = 0;
                    boolean responseHaveReachedEndOfHeaderLines = false;
                    while (responseHaveReachedEndOfHeaderLines==false)
                    {
                        int responseNumRepresentationOfByte = clientInputStream.read();
                        if (responseNumRepresentationOfByte==10 || responseNumRepresentationOfByte==13)
                        {
                            responseNumberOfConsecutiveNorR++;
                        }
                        else
                        {
                            responseNumberOfConsecutiveNorR=0;
                        }
                        if (responseNumberOfConsecutiveNorR >= 4 && responseHaveReachedEndOfHeaderLines==false)
                        {
                            responseIndexAtWhichHeaderLinesEnd = responseIndex;
                            responseHaveReachedEndOfHeaderLines = true;
                        }
                        byte responseByteRead = (byte) responseNumRepresentationOfByte;
                        //System.out.printf("0x%02X\n", byteRead);
                        //System.out.printf("%c\n", numRepresentationOfByte);
                        responseBytes[responseIndex] = responseByteRead;

                        responseIndex++;

                    }

                    byte[] responseHeaderLines = new byte[responseIndexAtWhichHeaderLinesEnd+1];
                    for (int i = 0; i <= responseIndexAtWhichHeaderLinesEnd; i++)
                    {
                        responseHeaderLines[i] = responseBytes[i];
                    }

                    String responseMessage = new String(responseHeaderLines);
                    System.out.println("HOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                    System.out.println(responseMessage);
                    System.out.println("HIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
                    
                    // now that we have the response header lines, we want to extract the Content-Length so we know how many bytes of data to read 
                    String contentLengthTemp = responseMessage.substring(responseMessage.indexOf("Content-Length:")+16);
                    String contentLengthTemp2 = contentLengthTemp.substring(0,contentLengthTemp.indexOf("\n")-1);
                    int contentLength = new Integer (contentLengthTemp2);  // contentLength is the integer representation

                    // create a byte array of the length of the data, then fill it with the remaining data in the
                    // input stream (i.e. the web page since we already red all the header lines)
                    byte[] data = new byte[contentLength];
                    for (int i = 0; i < contentLength; i++)
                    {
                        int b = clientInputStream.read();
                        data[i] = (byte) b;
                    }

                    // make one array with the full response message: header lines and data
                    int fullResponseLength = responseHeaderLines.length + data.length;
                    byte[] fullResponseMessage = new byte[fullResponseLength];
                    System.arraycopy( responseHeaderLines, 0, fullResponseMessage, 0, responseHeaderLines.length);
                    System.arraycopy( data, 0, fullResponseMessage, responseHeaderLines.length, data.length );



                    // PUT THE DATA PORTION INTO A FILE WITH THE DIRECTORY STRUCTURE ACCORDING TO THE URL

                    // create file
                    String urlWOfileName = hostName + "/" + pathName.substring(0,pathName.lastIndexOf("/"));
                    String fileName = pathName.substring(pathName.lastIndexOf("/")+1, pathName.length());
                    System.out.println(urlWOfileName);
                    System.out.println(fileName);
                    String cumulativePathName = "";
                    String[] ary = urlWOfileName.split("/");
                    for (int i = 0; i<ary.length; i++)
                    {
                        System.out.println(ary[i]);
                        cumulativePathName = cumulativePathName + ary[i] + "/";
                        File element = new File(cumulativePathName);
                        boolean val = element.mkdir();
                        System.out.println(val);
                    }

                    String fullFileName = cumulativePathName+fileName;
                    File f = new File(fullFileName);
                    try
                    {
                        f.createNewFile();
                    }
                    catch (Exception e)
                    {
                        System.out.println("Exception creating file: " + e.getMessage());
                    }

                    // write the data to the cache

                    FileOutputStream fout = null;
                    try
                    {
                        fout = new FileOutputStream(fullFileName);
                        for (int i = 0; i < data.length; i++)
                        {
                            fout.write((int) data[i]);
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Exception writing to file: " + e.getMessage());
                    }
                    finally
                    {
                        try
                        {
                            if (fout != null)
                            {
                                fout.close();
                                System.out.println("closing file");
                            }
                        }
                        catch (IOException ex)
                        {
                            System.out.println("error closing file.");
                        }
                    }
                    
                    

                    // NOW SEND THIS MESSAGE BACK TO THE CLIENT
                    os.write(fullResponseMessage);
                    os.flush();
           

                    
                //}

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
