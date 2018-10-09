import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Stack{
	public static void main(String Args[]){
		JFrame frame = new JFrame("Stack");
		GameComponents THE_GAME = new GameComponents();
		frame.setContentPane(THE_GAME);
		frame.setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screenSize.width/2 - THE_GAME.PANEL_WIDTH/2, screenSize.height/2 - THE_GAME.PANEL_HEIGHT/2);
		frame.pack();
		frame.setResizable(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
	}
}

class GameComponents extends JPanel implements KeyListener{
	public static int PANEL_HEIGHT = 500;
	public static int PANEL_WIDTH = 400;
	public static int DELTA_SQUARE = 175;
	public static int DELTA_FROM_DOWN_OF_PAGE = 200;
	public static double cos30 = Math.cos(30 * Math.PI / 180);
	public static double sin30 = Math.sin(30 * Math.PI / 180);
	
	public static double X_0_START_FROM_RIGHT = PANEL_WIDTH/2 + DELTA_SQUARE * cos30;
	public static double X_0_START_FROM_LEFT = PANEL_WIDTH/2 - DELTA_SQUARE * cos30;
	public static double Y_0_START = (double)(PANEL_HEIGHT - DELTA_FROM_DOWN_OF_PAGE - Square.heightOfNormalSquare + DELTA_SQUARE * sin30);
			
	public int UPDATE_RATE = 60;
	
	public static ArrayList<Color> colors = new ArrayList<>();
	
	public double currentWidthOfSquare = 100;
	public double currentLengthOfSquare = 100;
	public double XOfSquareToGenerate = 100;
	public double YOfSquareToGenerate = 100;
	
	public int leftORright = 1;
	public int velosity = 1;
	public boolean stateOfMovingStackDown = false;
	public boolean stateOFMovingtheTopSquare = false;
	public boolean stateOfAddingASquareOnTop = true;
	public int howManyDowns = 0;// goes up to 20 then resets to 0
	
	public boolean key_for_fade = false;
	
	public ArrayList<Square> squares = new ArrayList<>();
	
	//public static FontClass font_1 = new FontClass("src/FONT1.ttf");
	
	public void f(){
		System.out.println("	we in F");
		if(stateOFMovingtheTopSquare){
			turnToStateOfMovingStack();
		}
	}
	
	public boolean endOrNot(){
		if( getSharedLength(squares.get(squares.size()-1), squares.get(squares.size()-2)) == 0 &&
				getSharedWidth(squares.get(squares.size()-1), squares.get(squares.size()-2)) == 0)
			return true;
		return false;
	}
	
	public void changeDimensionsOfLastSquare(){
		double lengthOfOutput;
		double widthOfOutput;
		double XOfOutput;
		double YOfOutput;
		if( endOrNot() == true ){
			widthOfOutput = 0;
			lengthOfOutput = 0;
			squares.set(squares.size()-1, new Square(widthOfOutput, lengthOfOutput,Square.heightOfNormalSquare, X_0_START_FROM_LEFT, Y_0_START, colors.get((squares.size()-1)%8)));
			return;
		}
		if(squares.size()%2==0){//in this case, from right
			widthOfOutput = squares.get(squares.size()-1).widthOfSquare;
			lengthOfOutput = getSharedLength(squares.get(squares.size()-1), squares.get(squares.size()-2));
			if(squares.get(squares.size()-1).XOfSquare   <  squares.get(squares.size()-2).XOfSquare ){
				XOfOutput = squares.get(squares.size()-1).XOfSquare;
				YOfOutput = squares.get(squares.size()-1).YOfSquare;
			}
			else{
				XOfOutput = squares.get(squares.size()-2).XOfSquare;
				YOfOutput = squares.get(squares.size()-2).YOfSquare - Square.heightOfNormalSquare;
			}
		}
		else{//in this case, from left
			widthOfOutput = getSharedWidth(squares.get(squares.size()-1), squares.get(squares.size()-2));
			lengthOfOutput = squares.get(squares.size()-1).lengthOfSquare;
			if(squares.get(squares.size()-1).XOfSquare   <  squares.get(squares.size()-2).XOfSquare ){
				XOfOutput = squares.get(squares.size()-2).XOfSquare;
				YOfOutput = squares.get(squares.size()-2).YOfSquare - Square.heightOfNormalSquare;
			}
			else{
				XOfOutput = squares.get(squares.size()-1).XOfSquare;
				YOfOutput = squares.get(squares.size()-1).YOfSquare;
			}
		}
		squares.set(squares.size()-1, new Square(widthOfOutput, lengthOfOutput, Square.heightOfNormalSquare, XOfOutput, YOfOutput, colors.get((squares.size()-1)%8)));
	}	

	public double getSharedLength(Square sq1, Square sq2){
		if(sq1.XOfSquare > sq2.XOfSquare){
			if(sq1.XOfSquare - sq2.XOfSquare < sq1.lengthOfSquare * cos30)
				return sq1.lengthOfSquare - (sq1.XOfSquare - sq2.XOfSquare)/cos30;
		}
		else if(sq1.XOfSquare < sq2.XOfSquare){
			if( sq2.XOfSquare - sq1.XOfSquare < sq1.lengthOfSquare * cos30 ){
				return sq1.lengthOfSquare - (sq2.XOfSquare - sq1.XOfSquare)/cos30;
			}
		}
		else if(sq1.XOfSquare == sq2.XOfSquare){//roo ham
			return sq1.lengthOfSquare;
		}
		return 0;
	}
	
	public double getSharedWidth(Square sq1, Square sq2){
		if(sq1.XOfSquare > sq2.XOfSquare){
			if(sq1.XOfSquare - sq2.XOfSquare < sq1.widthOfSquare * cos30)
				return sq1.widthOfSquare - (sq1.XOfSquare - sq2.XOfSquare)/cos30;
		}
		else if(sq1.XOfSquare < sq2.XOfSquare){
			if( sq2.XOfSquare - sq1.XOfSquare < sq1.widthOfSquare * cos30 ){
				return sq1.widthOfSquare - (sq2.XOfSquare - sq1.XOfSquare)/cos30;
			}
		}
		else if(sq1.XOfSquare == sq2.XOfSquare){//roo ham
			return sq1.widthOfSquare;
		}
		
		return 0;
	}
	
	public GameComponents(){
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE){
					f();
				}
				
			}
		});
		
		colors.add(Color.BLUE);//0
		colors.add(Color.CYAN);//1
		colors.add(Color.GREEN);//2
		colors.add(Color.ORANGE);//3
		colors.add(Color.MAGENTA);//4
		colors.add(Color.PINK);//5
		colors.add(Color.YELLOW);//6
		colors.add(Color.RED);//7
		
		
		squares.add(new Square(currentWidthOfSquare, currentLengthOfSquare, Square.heightOfFistSquare, PANEL_WIDTH/2 , PANEL_HEIGHT - DELTA_FROM_DOWN_OF_PAGE, Color.DARK_GRAY));
		System.out.println("keshidi?");
		repaint();
		Thread gameThread = new Thread() {
	    	  @Override
	         public void run() {
	    		  while(true){
	    		  
	    		  ///
	    		  if(stateOfMovingStackDown){
	    			  for(int i = 0 ; i < squares.size() ; i++){	
	    				  squares.get(i).YOfSquare++;
	    			  }
	    			  howManyDowns++;
	    			  if(howManyDowns == Square.heightOfNormalSquare){
	    				  turnToStateOfAddSquare();
	    				  howManyDowns = 0;
	    				  leftORright++;
	    			  }
	    		  }
	    		  
	    		  ///
	    		  else if(stateOfAddingASquareOnTop){
	    			  //width : az vasat be rast
	    			  //length: az vasat be chap
	    			  System.out.println("SIZE BEFORE:" + squares.size());
	    			  
	    			  
	    			  //TODO velosity down here!
	    			  
	    			  if(squares.size() % 5 == 0){
	    				  velosity += 1;
	    			  }
	    			  if(squares.size() > 1) {
	    				  changeDimensionsOfLastSquare();
	    				  if(squares.size()%2==0){
	    					  if(getSharedLength(squares.get(squares.size()-1), squares.get(squares.size()-2))==0){
	    						  System.out.println("THE END!!! %2==0");
	    						  repaint();
	    						  this.suspend();
	    					  }
	    				  }
	    				  else{
	    					  if(getSharedWidth(squares.get(squares.size()-1), squares.get(squares.size()-2))==0){
	    						  System.out.println("THE END!!! %2==1");
	    						  repaint();
	    						  this.suspend();
	    					  }
	    				  }
	    			  }
	    			  
	    			  System.out.println("size of squares:" + squares.size());
	    			  if(squares.size() == 1){
	    				  currentWidthOfSquare = squares.get(squares.size()-1).widthOfSquare;
	    			      currentLengthOfSquare = squares.get(squares.size()-1).lengthOfSquare;
	    			      XOfSquareToGenerate = X_0_START_FROM_RIGHT;
	    			      YOfSquareToGenerate = Y_0_START;
	    			  }
	    			  
	    			  //TODO
	    			  
	    			  else{
	    			      currentWidthOfSquare = squares.get(squares.size()-1).widthOfSquare;
	    				  currentLengthOfSquare = squares.get(squares.size()-1).lengthOfSquare;
	    			  }
	    			  //XOfSquareToGenerate = ;
	    			  //YOfSquareToGenerate = ;
	    			  
	    			  
	    			  if(leftORright%2 == 1){
	    				  squares.add(new Square(currentWidthOfSquare, currentLengthOfSquare, Square.heightOfNormalSquare,
	    						  (double)(squares.get(squares.size()-1).XOfSquare + DELTA_SQUARE * cos30),
	    						  (double)( squares.get(squares.size()-1).YOfSquare  - Square.heightOfNormalSquare + DELTA_SQUARE * sin30),
	    						  colors.get((squares.size())%8)));
	    				  System.out.println("alan right? " + squares.size());
	    				  repaint();
	    			  }
	    			  else{
	    				  squares.add(new Square(currentWidthOfSquare, currentLengthOfSquare, Square.heightOfNormalSquare,
	    						  (double)(squares.get(squares.size()-1).XOfSquare - DELTA_SQUARE * cos30), 
	    						  (double)(squares.get(squares.size()-1).YOfSquare - Square.heightOfNormalSquare + DELTA_SQUARE * sin30),
	    						  colors.get((squares.size())%8)));
	    				  System.out.println("alan left? " + squares.size());
	    				  repaint();
	    			  }
	    			  turnToStateOfMovingTopSquare();
	    			  System.out.println("SIZE AFTER:"+ squares.size());
	    		  }
	    		  
	    		  ///
	    		  else if(stateOFMovingtheTopSquare){
	    			  if(squares.get(squares.size() - 1).direction == 1){		//moving forward
	    				  if(leftORright%2 == 1){//right
	    					  if(squares.get(squares.size() - 1).XOfSquare > PANEL_WIDTH/2 - DELTA_SQUARE * cos30){
	    						  squares.get(squares.size() - 1).moveInDirection(-1, -1, velosity);
	    					  }
	    					  else{
	    						  squares.get(squares.size() - 1).changeDirection();
	    					  }
	    				  }
	    				  else{//left
	    					  if(squares.get(squares.size() - 1).XOfSquare < PANEL_WIDTH/2 + DELTA_SQUARE * cos30){
	    						  squares.get(squares.size() - 1).moveInDirection(1, -1, velosity);
	    					  }
	    					  else{
	    						  squares.get(squares.size() - 1).changeDirection();
	    						  //System.out.println("change dir1?");
	    					  }
	    				  }
	    			  }
	    			  
	    			  else if(squares.get(squares.size() - 1).direction == -1){		//moving backward
	    				  if(leftORright%2 == 1){//right
	    					  if(squares.get(squares.size() - 1).XOfSquare < PANEL_WIDTH/2 + DELTA_SQUARE * cos30){
	    						  squares.get(squares.size() - 1).moveInDirection(1, 1, velosity);
	    					  }
	    					  else{
	    						  squares.get(squares.size() - 1).changeDirection();
	    					  }
	    				  }
	    				  else{//left
	    					  if(squares.get(squares.size() - 1).XOfSquare > PANEL_WIDTH/2 - DELTA_SQUARE * cos30){
	    						  squares.get(squares.size() - 1).moveInDirection(-1, 1, velosity);
	    					  }
	    				      else{
	    				    	  squares.get(squares.size() - 1).changeDirection();
	    				    	  //System.out.println("change dir2?");
	    				      }
	    				  }
	    			  }
	    			  
	    			  //to-be-added
	    			  
	    			  
	    			  
	    		  }
	    		  
	    		 // System.out.println("bazam?");
	    		  repaint(); // Callback paintComponent()
	               // Delay for timing control and give other threads a chance
	               try {
	                  Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
	               } catch (InterruptedException ex) { }
	    		  }
	    	  }
	    };
	    gameThread.start();
	    
	    
	    
	    this.requestFocus();
	    this.setFocusable(true);
	}
	
//	public boolean stateOfMovingStackDown = false;
//	public boolean stateOFMovingtheTopSquare = false;
//	public boolean stateOfAddingASquareOnTop = true;
	
	
	public void turnToStateOfMovingStack(){
		stateOfMovingStackDown = true;//
		stateOFMovingtheTopSquare = false;
		stateOfAddingASquareOnTop = false;
	}
	
	public void turnToStateOfMovingTopSquare(){
		stateOfMovingStackDown = false;
		stateOFMovingtheTopSquare = true;//
		stateOfAddingASquareOnTop = false;
	}
	
	public void turnToStateOfAddSquare(){
		stateOfMovingStackDown = false;
		stateOFMovingtheTopSquare = false;
		stateOfAddingASquareOnTop = true;//
	}
	
	public void paintSquares(ArrayList<Square> squares, Graphics g){
		for(int i = 0 ; i < squares.size() ; i++){
			double x = squares.get(i).XOfSquare;
			double y = squares.get(i).YOfSquare;
			double width = squares.get(i).widthOfSquare;
			double length = squares.get(i).lengthOfSquare;
			double height = squares.get(i).heightOfSquare;
			double cos30 = Math.cos(30 * Math.PI / 180);
			double sin30 = Math.sin(30 * Math.PI / 180);
			//layeye rooyi
			int[] xpoints = {(int)x , (int)(x + width * cos30), (int)(x + (width - length) * cos30), (int)(x - length * cos30)};
			int[] ypoints = {(int)y , (int)(y - width * sin30), (int)(y - (width + length) * sin30), (int)(y - length * sin30)};
			g.setColor(squares.get(i).color);
			g.fillPolygon(xpoints, ypoints, 4);
			
			//layeye rasti
			int[] xpoints1 = {(int)x , (int)(x + width * cos30), (int)(x + width * cos30), (int)x};
			int[] ypoints1 = {(int)y , (int)(y - width * sin30), (int)(y - width * sin30 + height), (int)(y + height)};
			g.setColor(squares.get(i).color.darker().darker());
			g.fillPolygon(xpoints1, ypoints1, 4);
			
			//layeye chapi
			int[] xpoints2 = {(int)x , (int)(x - length * cos30), (int)(x - length * cos30), (int)x};
			int[] ypoints2 = {(int)y , (int)(y - length * sin30), (int)(y - length * sin30 + height), (int)(y + height)};
			g.setColor(squares.get(i).color.darker());
			g.fillPolygon(xpoints2, ypoints2, 4);
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.WHITE);
	    g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
	    g.setColor(Color.decode("000000"));// hint : black!
		//g.drawLine(PANEL_WIDTH/2, PANEL_HEIGHT , PANEL_WIDTH/2, 0);
		paintSquares(squares, g);
		StringBuilder sb = new StringBuilder();
	    Formatter formatter = new Formatter(sb);
	    formatter.format("%d", squares.size()-1);
	    g.setColor(colors.get((squares.size()-1)%(colors.size())));
	    Font FONT1 = new Font(Font.SANS_SERIF, Font.BOLD, 40);
	    g.setFont(FONT1);
	    int x = PANEL_WIDTH/2 - FONT1.getSize()*sb.length()/4;
	    int y = PANEL_HEIGHT/4;
	    g.drawString(sb.toString(), x, y);
	    if(squares.size()== 2 && howManyDowns == 0 && key_for_fade == false){
	    	StringBuilder sb_new = new StringBuilder();
		    Formatter formatter_new = new Formatter(sb_new);
		    FONT1 = new Font(Font.SANS_SERIF, Font.BOLD, 30);
		    formatter_new.format("%d", squares.size()-1);
		    g.setColor(Color.BLACK);
		    x = x - 150;
		    g.drawString("Height:", x, y);
	    }
	    if(squares.size()==2 && howManyDowns > 0){
	    	key_for_fade = true;
	    	Graphics2D g2d = (Graphics2D) g;
	    	float tempo = 1.f - (float)howManyDowns/(float)Square.heightOfNormalSquare;
	    	
	    	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,tempo));
	    	
	    	StringBuilder sb_new = new StringBuilder();
		    Formatter formatter_new = new Formatter(sb_new);
		    FONT1 = new Font(Font.SANS_SERIF, Font.BOLD, 30);
		    formatter_new.format("%d", squares.size()-1);
		    g.setColor(Color.BLACK);
		    x = x - 150;
		    if(squares.size()<=2)
		    	g2d.drawString("Height:", x, y);
	    }
	    formatter.close();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}


class Square{
	public double lengthOfSquare;//first : 100
	public double widthOfSquare;//first : 100
	public double heightOfSquare;
	public static double heightOfNormalSquare =  15;
	public static double heightOfFistSquare =  300;
	public double XOfSquare;
	public double YOfSquare;
	
	public int direction = 1;// only 1 or -1 : 1 is moving forward & -1 is moving backward
	
	public Color color;
	public Square(double widthOfSquare, double lengthOfSquare, double heightOfSquare, double XOfSquare, double YOfSquare, Color color){
		this.widthOfSquare = widthOfSquare;
		this.lengthOfSquare = lengthOfSquare;
		this.heightOfSquare = heightOfSquare;
		this.XOfSquare = XOfSquare;
		this.YOfSquare = YOfSquare;
		this.color = color;
	}
	
	public void changeDirection(){
		if(direction == 1)
			direction = -1;
		else if(direction == -1)
			direction = 1;
		return;
	}
	
	public void moveInDirection(int i, int j, int velosity){
		double sin30 = Math.sin(30 * Math.PI / 180);
		double cos30 = Math.cos(30 * Math.PI / 180);
		if(i == 1 && j == 1){
			this.XOfSquare += 3* cos30 * velosity;
			this.YOfSquare += 3* sin30 * velosity;
		}
		else if(i == -1 && j == 1){
			this.XOfSquare -= 3* cos30 * velosity;
			this.YOfSquare += 3* sin30 * velosity;
		}
		else if(i == 1 && j == -1){
			this.XOfSquare += 3* cos30 * velosity;
			this.YOfSquare -= 3* sin30 * velosity;
		}
		else if(i == -1 && j == -1){
			this.XOfSquare -= 3* cos30 * velosity;
			this.YOfSquare -= 3* sin30 * velosity;
		}
		return;
	}
}



class FontClass{
	Font font;
	public FontClass(String address)
	  {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT,new File(address));
		} catch (FontFormatException e) {
			e.printStackTrace();
	    } catch (IOException e){
	    	e.printStackTrace();
	    }
	  }
}
