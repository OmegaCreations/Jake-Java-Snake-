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
	int _coffeesDrank;
	int _coffeeX;
	int _coffeeY;
	char _direction = 'R'; // Right direction at beginning
	boolean _running = false;
	
	// Utilities
	Timer timer;
	Random random;
	Color _coffee_brown = new Color(0x8A624A);
	
	
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
		
		if(_running) {
		// if start
			
			// grid lines
			/*
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}*/
			
			g.setColor(_coffee_brown);
			g.fillOval(_coffeeX, _coffeeY, UNIT_SIZE, UNIT_SIZE);
			
			// draw snake
			for(int i = 0; i < _bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(_body_x[i], _body_y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(_body_x[i], _body_y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			// draw score
			drawScore(g);
		// if end
		} else {
			gameOver(g);
		}
		
	}
	
	public void newCoffee() {
		_coffeeX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		_coffeeY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	
	public void move() {
		for(int i = _bodyParts; i > 0; i--) {
			// Shifting all coordinates by one
			_body_x[i] = _body_x[i-1];
			_body_y[i] = _body_y[i-1];
		}
		
		// Move possibilities
		switch(_direction) {
		case 'U': 
			_body_y[0] = _body_y[0] - UNIT_SIZE;
			break;
		case 'D': 
			_body_y[0] = _body_y[0] + UNIT_SIZE;
			break;
		case 'L': 
			_body_x[0] = _body_x[0] - UNIT_SIZE;
			break;
		case 'R': 
			_body_x[0] = _body_x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkCoffee() {
		if((_body_x[0] == _coffeeX) && (_body_y[0] == _coffeeY)) {
			_bodyParts++;
			_coffeesDrank++;
			newCoffee();
		}
	}
	
	public void checkCollisions() {
		// head collision with body
		for(int i = _bodyParts; i > 0; i--) {
			if((_body_x[0] == _body_x[i])&&(_body_y[0] == _body_y[i])) {
				_running = false;
			}
		}
		
		// check collision with border
		if(
				_body_x[0] < 0 
				|| (_body_x[0] > SCREEN_WIDTH) 
				|| (_body_y[0] < 0) 
				|| (_body_y[0] > SCREEN_HEIGHT)
				)
			{
			_running = false;
		}
	}
	
	public void gameOver(Graphics g) {
		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		drawScore(g);
	}
	
	public void drawScore(Graphics g) {
		// draw score
		g.setColor(_coffee_brown);
		g.setFont(new Font("Ink free", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: "+_coffeesDrank, (SCREEN_WIDTH - metrics.stringWidth("Score"+_coffeesDrank))/2, g.getFont().getSize());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(_running) {
			move();
			checkCoffee();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			
			// Control snake and limit to 90 degree turns
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(_direction != 'R') {
					_direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(_direction != 'L') {
					_direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(_direction != 'D') {
					_direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(_direction != 'U') {
					_direction = 'D';
				}
				break;
			}
		}
	}
}
