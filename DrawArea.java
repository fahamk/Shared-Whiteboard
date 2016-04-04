
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import java.io.*;
import java.net.*;
 
 
import javax.swing.JComponent;


/**
* Component for drawing !
*
* @author sylsau
*
*/
public class DrawArea extends JComponent implements Runnable {
  public Socket kkSocket = null;
  public static BufferedReader in = null;
  public static PrintWriter out = null;
  public static String color=null;
  // Image in which we're going to draw
  private Image image;
  // Graphics2D object ==> used to draw on
  private Graphics2D g2;
  // Mouse coordinates
  private int currentX, currentY, oldX, oldY;
  boolean connect=true;
  
  public DrawArea(String IP, String Port) throws IOException{

		try { 	
		    kkSocket = new Socket(IP, Integer.parseInt(Port));
		    in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream())); 
		    connect=false;
		} catch (UnknownHostException e) {
		    System.err.println("Don't know about host: taranis.");
		

		} catch (IOException e) {
		    System.err.println("Couldn't get I/O for the connection to: taranis.");

		} catch (NumberFormatException e){
			new SwingPaint();

		
		}
    
    /////////// Recieving Data From Server in another thread
    Runnable DrawArea = new Runnable(){  
        public void run(){
            System.out.println("Runnable running");
            String inputLine;
            try {
                /*while (true) {
                    if (in.ready()) {
                        if ((inputLine = in.readLine()) != null) {
                            String[] retval = inputLine.split(",");
                            if (g2!=null){
                                g2.drawLine(Integer.parseInt(retval[0]), Integer.parseInt(retval[1]), Integer.parseInt(retval[2]), Integer.parseInt(retval[3]));
                            }
                            repaint();
                        }
                    }
                }*/
                
                
                while ((inputLine = in.readLine()) != null) {
                	if(inputLine.contains("Color")){
                		String[] getColor=inputLine.split(":");
                		color=getColor[1];

                	}
                	
                	
                	
                    String[] retval= inputLine.split(",");
                    

                    if(g2!=null){
                    	try{
		                	int colorRGB=Integer.parseInt(retval[4]);
		                	Color lineColor=new Color(colorRGB);
		                	g2.setPaint(lineColor);
		                    g2.drawLine(Integer.parseInt(retval[0]), Integer.parseInt(retval[1]), Integer.parseInt(retval[2]), Integer.parseInt(retval[3]));
		                }catch(ArrayIndexOutOfBoundsException e){
		                	System.out.println("");
		                
		                }
                    }
                    repaint();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
            	System.out.println("Enter proper stuff");
            }
            
        }
    };

    Thread thread = new Thread(DrawArea);
    thread.start();
    out = new PrintWriter(kkSocket.getOutputStream(), true);
    
    
  ////////// Above is the
  
  
  //Creating drawing implementation 
    setDoubleBuffered(false);
    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        // save coord x,y when mouse is pressed
        oldX = e.getX();
        oldY = e.getY();
      }
    });
 
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        // coord x,y when drag mouse
        currentX = e.getX();
        currentY = e.getY();
        black();
 
        if (g2 != null) {
          // draw line if g2 context not null
          g2.drawLine(oldX, oldY, currentX, currentY);
        try {
            
			sender(oldX,oldY,currentX,currentY, kkSocket, color);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
          // refresh draw area to repaint
          repaint();
          // store current coords x,y as olds x,y
          oldX = currentX;
          oldY = currentY;
        }
      }
    });
    
    if (g2 == null) {
        out.println("waiting");
    } else {
        out.println("ready");
    }
  }
  
  // sends drawn data to server 
  public void sender(int ox, int oy, int cx, int cy, Socket kkSocket, String color) throws IOException{
  	 //PrintWriter out = null;


     //out = new PrintWriter(kkSocket.getOutputStream(), true);
	String sending= Integer.toString(ox)+","+Integer.toString(oy)+","+Integer.toString(cx)+","+Integer.toString(cy)+","+color;
        out.println(sending);
        //out.println("---------------------------------------------");
  }
 
  protected void paintComponent(Graphics g) {
    if (image == null) {
      // image to draw null ==> we create
      image = createImage(getSize().width, getSize().height);
      g2 = (Graphics2D) image.getGraphics();
      // enable antialiasing
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      // clear draw area
      clear();
    }
 
    g.drawImage(image, 0, 0, null);
  }
 
  // now we create exposed methods
  public void clear() {
    g2.setPaint(Color.white);
    // draw white on entire draw area to clear
    g2.fillRect(0, 0, getSize().width, getSize().height);
    g2.setPaint(Color.black);
    repaint();
  }
 
 
  public void black() {
    g2.setPaint(Color.black);
  }
  
  public void connect(){
  
  }
 

  public void run(){
  }
 
}
