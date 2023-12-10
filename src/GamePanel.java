// Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
	
	// Configuration
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75; // game speed
	
	// Cords of body parts
	final int _body_x[] = new int[GAME_UNITS];
	final int _body_y[] = new int[GAME_UNITS];
	
	// Game statistics
	int _bodyParts = 6;
	int _coffesEaten;
	int _coffeX;
	int _coffeY;
	char _direction = 'R'; // Right direction at beginning
	boolean _running = false;
	
	// Utilities
	Timer timer;
	Random random;
	
	
	// Game Constructor
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true); 
		this.addKeyListener(new MyKeyAdapter()); // Key listener
		startGame();
	}
	
	// Game start method
	public void startGame(){
		newCoffee(); // Draw new coffee
		_running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g){
		for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
			g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
		}
	}
	
	public void newCoffee() {
		
	}
	
	public void move() {
		
	}
	
	public void checkCoffee() {
		
	}
	
	public void checkCollisions() {
		
	}
	
	public void gameOver(Graphics g) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			
		}
	}
}
