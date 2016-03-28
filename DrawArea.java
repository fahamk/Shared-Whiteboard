
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

  // Image in which we're going to draw
  private Image image;
  // Graphics2D object ==> used to draw on
  private Graphics2D g2;
  // Mouse coordinates
  private int currentX, currentY, oldX, oldY;
 
  public DrawArea() throws IOException{
	

    


    try {
        kkSocket = new Socket("localhost", 4444);
        in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream())); 
    } catch (UnknownHostException e) {
        System.err.println("Don't know about host: taranis.");
        System.exit(1);
    } catch (IOException e) {
        System.err.println("Couldn't get I/O for the connection to: taranis.");
        System.exit(1);
    }
    /////////// Recieving Data From Server in another thread
    Runnable DrawArea = new Runnable(){  
      public void run(){
         System.out.println("Runnable running");
         String inputLine;
         try {
			while ((inputLine = in.readLine()) != null) {
				
			     //System.out.println(inputLine);
			     String[] retval= inputLine.split(",");
			     if(g2!=null){
			     	//System.out.println("Its good");
			     	g2.drawLine(Integer.parseInt(retval[0]), Integer.parseInt(retval[1]), Integer.parseInt(retval[2]), Integer.parseInt(retval[3]));
			     	//System.out.println(Integer.parseInt(retval[0]));
			     }
			     repaint();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
      }
    };

    Thread thread = new Thread(DrawArea);
    thread.start();
    
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
 
        if (g2 != null) {
          // draw line if g2 context not null
          g2.drawLine(oldX, oldY, currentX, currentY);
        try {
			sender(oldX,oldY,currentX,currentY, kkSocket);
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
  }
  
  // sends drawn data to server 
  public void sender(int ox, int oy, int cx, int cy, Socket kkSocket) throws IOException{
  	 PrintWriter out = null;


     out = new PrintWriter(kkSocket.getOutputStream(), true);
	 String sending= Integer.toString(ox)+","+Integer.toString(oy)+","+Integer.toString(cx)+","+Integer.toString(cy);
     
     out.println(sending);
  	 out.println("---------------------------------------------");
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
 
  public void red() {
    // apply red color on g2 context
    g2.setPaint(Color.red);
  }
 
  public void black() {
    g2.setPaint(Color.black);
  }
 
  public void magenta() {
    g2.setPaint(Color.magenta);
  }
 
  public void green() {
    g2.setPaint(Color.green);
  }
 
  public void blue() {
    g2.setPaint(Color.blue);
  }

  public void run(){
  }
 
}
