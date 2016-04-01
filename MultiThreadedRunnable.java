/* By: Theodore Tan & Faham Khan */

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiThreadedRunnable implements Runnable {
	final static String CRLF = "\r\n";
	Socket socket;
	ArrayList<String> points = new ArrayList<String>();
	
	public MultiThreadedRunnable (Socket socket, ArrayList<String> points) throws Exception {
		this.socket = socket;
		this.points = points;
	}
	
	public void run() {
		try {
			System.out.println("New client started.");
			processRequest();
			System.out.println("Client disconnected.");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	private void processRequest() throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		String inputLine, outputLine;
		
		//outputLine = dp.processInput(null);
		//out.println(outputLine);
		
		while ((inputLine = in.readLine()) != null) {
                    //out.println("50,50,200,200");
                    points.add(inputLine);
                    System.out.println(points.size());
			
		}
		out.close();
		in.close();
		socket.close();
	}
}
