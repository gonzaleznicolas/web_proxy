import java.io.*;


public class Test
{
	public static void main(String[] args)
	{
		String hostName = "pages.cpsc.ucalgary.ca";
		String pathName = "one/hi.txt";
        String urlWOfileName = hostName + "/" + pathName.substring(0,pathName.lastIndexOf("/"));
        String fileName = pathName.substring(pathName.lastIndexOf("/")+1, pathName.length());
        System.out.println(urlWOfileName);
        System.out.println(fileName);
        //cumulative path name
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
        File f = new File(cumulativePathName+fileName);
        try
        {
            f.createNewFile();
        }
        catch (Exception e)
        {
            System.out.println("exception");
        }
        
	}

}