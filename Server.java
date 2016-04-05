/* By: Theodore Tan & Faham Khan */

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.Thread;
import java.util.Random;
import java.awt.*;
import java.lang.*;
public class Server {
    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            ArrayList<String> points = new ArrayList<String>();
            ServerSocket serverSocket = null;
            int arg = Integer.parseInt(args[0]);
            
            try {
                serverSocket = new ServerSocket(arg);
                System.out.println("Server started on port: "+ arg);
            } catch (IOException e) {
                System.err.println("Could not listen on port: "+ arg);
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
        } else {
            System.err.println("Incorrect Parameters");
            System.exit(1);
        }
        
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
