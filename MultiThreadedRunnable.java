/* By: Theodore Tan & Faham Khan */

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.lang.*;

public class MultiThreadedRunnable implements Runnable {
	final static String CRLF = "\r\n";
	Socket socket;
	ArrayList<String> points = new ArrayList<String>();
	int currentSize;
	String color;
	
	public MultiThreadedRunnable (Socket socket, ArrayList<String> points, String color) throws Exception {
		this.socket = socket;
		this.points = points;
		this.currentSize = -1;
		this.color= color;
		//this.color=color;
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
	
	private void update(int currentSize, PrintWriter out) {
		
	}
	
	private void processRequest() throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		String inputLine, outputLine;
		System.out.println("Color"+":"+color);
		out.println("Color"+":"+color);
		
		//while ((inputLine = in.readLine()) != null) {
		while (true) {
			if (currentSize >= 0) {
			
				if (currentSize < points.size()) {
					for (int i = currentSize; i < points.size(); i++) {
						out.println(points.get(i));
					}
					currentSize = points.size();
				}
			}
			//inputLine = in.readLine();
			if (in.ready()) {
				if (currentSize >= 0 && currentSize < points.size()) {
					for (int i = currentSize; i < points.size(); i++) {
						out.println(points.get(i));
					}
					currentSize = points.size();
				}
			
			
				inputLine = in.readLine();
				if (inputLine != null && !"ready".equals(inputLine) && !"waiting".equals(inputLine)) {
					points.add(inputLine);
					//System.out.println(currentSize);
					currentSize++;
				} else if ("ready".equals(inputLine)) {
					currentSize = 0;
				}
			}
			
			
		}
		//out.close();
		//in.close();
		//socket.close();
	}
}
