package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JFrame;


//this implementation is for user input
public class flappyBird implements ActionListener, MouseListener, KeyListener {
	
	//static instance of flappybird
	public static flappyBird flappyBird;
	
	//set the frame variables to be final so it cannot be changed
	public final int WIDTH = 800, HEIGHT = 800;
	
	//this is the bird..
	public Rectangle bird;
	
	//this is going to make our columns.. an array list of rectangles
	public ArrayList<Rectangle> columns;
	
	//instance of render
	public Render renderer;
	
	public Random rand;
	
	public int ticks, yMotion, score;
	
	public boolean gameOver, started;
	
	//Constructor for the flappy bird
	public flappyBird(){
		
		//creating the frame
		JFrame jframe = new JFrame();
		
		//adding timer so that it actually repaints..
		Timer timer = new Timer(20, this);
		
		//initializing render so it is not null..
		renderer = new Render();
		
		rand = new Random();
		
		//adding renderer to the frame.
		jframe.add(renderer);
		//setting a title
		jframe.setTitle("FlappyBird Replica");
		//ability to close the window
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setting frame
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		//makes the frame not be resizable
		jframe.setResizable(false);
		//makes it where the window cannot be displayed
		jframe.setVisible(true);
		
		
		//creating bird and placing it
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		
		//instantiating the columns
		columns = new ArrayList<Rectangle>();
		
		//add columns to the window..
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		//starts timer
		timer.start();
		
	}

	public void addColumn(boolean start) {
		//make sure there is space between columns
		int space = 300;
		//the widrh of the columns
		int width = 100;
		//generates random height from minimum to maximum height for the columns
		int height = 50 + rand.nextInt(300);
		
		if(start) {
		
		//this creates the columns NEED TO LEARN MORE ABOUT THIS!!!@@@
		columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
		columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}
		
		else {
		//append to last column...
		columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT- height - 120, width, height));
		columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
			
		}
		
		
	}
	
	public void jump() {
		if (gameOver) {
			//changeing where out bird is everytime that the mouse or space bar is clicked
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			
			columns.clear();
			yMotion = 0;
			score = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;
		}
		if(!started) {
			
			started = true;
		}
		else if(!gameOver) {
			
			if(yMotion > 0) {
				
				yMotion = 0;
			}
			
			yMotion -= 10;
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		ticks++;
		//speed at which the columns appears on the screen
		int speed = 10;
		//this puts moves the columns onto screen
		for(int i = 0; i < columns.size();i++) {
			
			Rectangle column = columns.get(i);
			
			column.x -= speed;
		}
		
		//makes the bird fall
		if(ticks % 2 == 0 && yMotion < 15) {
			
			yMotion += 2;
			
		}
		//makes the columns keep appearing
		for(int i = 0; i < columns.size();i++) {
			
			Rectangle column = columns.get(i);
			
			if(column.x + column.width < 0) {
				
				columns.remove(column);
				
				if(column.y== 0) {
					
					addColumn(false);
					
				}
			}
		}
		
		bird.y += yMotion;
		
		for(Rectangle column : columns) {
			if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
				
				score++;
			}
			//if the bird crashes into column..ends game
			if(column.intersects(bird)) {
				
				gameOver = true;
				
				if(bird.x <= column.x) {
					
					bird.x = column.x - bird.width;
				}
				
				else {
				
					if(column.y != 0) {
						
						bird.y = column.y - bird.height;
					}
					
					else if(bird.y < column.height) {
						
						bird.y = column.height;
						
					}
				}
			}

		}
		//if the bird hits ground or hits roof...games over
		if(bird.y > HEIGHT - 120 || bird.y < 0) {
			
			gameOver = true;
			
		}
		if(bird.y + yMotion >= HEIGHT - 120) {
			bird.y = HEIGHT - 120 - bird.height;
			
			gameOver = true;
			
		}
		
		//updates window
		renderer.repaint();
		
	}
	
	//this is going to paint the columns
	public void paintColumn(Graphics g, Rectangle column) {
		//sets color of the columns to dark green
		g.setColor(Color.green.darker());
		// fills the color of the column based on coordinates passed when instantiated
		g.fillRect(column.x, column.y, column.width, column.height);
		
	}
	//constantly repaint the window..
	public void repaint(Graphics g) {
		//backgroundcolor
		g.setColor(Color.cyan);
		//filling in color in background
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//making "ground" orange
		g.setColor(Color.orange);
		//choosing the coordinates for the ground..
		g.fillRect(0, HEIGHT-120, WIDTH, 150);
		
		//making "grass" green
		g.setColor(Color.green);
		//choosing coordinates for the grass
		g.fillRect(0, HEIGHT-120, WIDTH, 15);
		
		//setting the color of the bird to red
		g.setColor(Color.red);
		//giving corrdinates to color red..
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		//iterator for the columns, creating columns
		for(Rectangle column : columns) {
			paintColumn(g, column);
		}
		//creating the score board
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));
		
		if(!started) {
			g.drawString("Click to start", 75, HEIGHT / 2 - 50);
			
		}
		if(gameOver) {
			g.drawString("Gameover!", 100, HEIGHT / 2 - 50);
			
		}
		if(!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
			
		}
		
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		flappyBird = new flappyBird();

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
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
			
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		jump();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



}
