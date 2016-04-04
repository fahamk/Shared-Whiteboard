import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.Thread;
import java.util.Random;
import java.awt.*;
import java.lang.*;
public class Server {
    public static void main(String[] args) throws IOException {
        ArrayList<String> points = new ArrayList<String>();
        ServerSocket serverSocket = null;
       
        
        
        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Server started on port: 4444");
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
        
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                Color randColor=randomColours();

                String col=String.valueOf(randColor.getRGB());
				
                new Thread(new MultiThreadedRunnable(clientSocket, points, col)).start();
                //System.out.println(points);
            } catch (Exception e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
          
             
        
/*
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
				new InputStreamReader(
				clientSocket.getInputStream()));
        String inputLine, outputLine;
        
        
        while ((inputLine = in.readLine()) != null) {
        	 out.println("50,50,200,200");
        	 points.add(inputLine);
             System.out.println(inputLine);
             
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();*/
    }
    public static Color randomColours(){
    	Random rand=new Random();
    	float a=rand.nextFloat();
    	float b= rand.nextFloat();
    	float c=rand.nextFloat();
    	
    	Color randomColor=new Color(a,b,c);
    	
    	return randomColor;
    
    }
}
