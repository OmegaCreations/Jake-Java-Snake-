// Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
	
	// Global variables
	public enum _STATES {
		MENU,
		PLAY,
		OVER,
		LOGIN,
	}
	
	_STATES _running = _STATES.MENU;
	boolean _isSignedIn = false;
	
	
	// Panel Configuration
	static final int SCREEN_WIDTH = 1200;
	static final int SCREEN_HEIGHT = 720;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75; // game speed
	
	// Coordinates of body parts
	final int _body_x[] = new int[GAME_UNITS];
	final int _body_y[] = new int[GAME_UNITS];
	
	
	// Game statistics =======================
	int _bodyParts = 6;
	int _coffeesDrank;
	int _coffeeX;
	int _coffeeY;
	char _direction = 'R'; // Right direction at beginning
	
	
	// Game Utilities =============================
	Timer timer = new Timer(DELAY, this);
	Random random;
	Color _user_color = new Color(0x10b981);
	Color _coffee_brown = new Color(0x8A624A);
	Image _coffeeImage = Toolkit.getDefaultToolkit().getImage("coffee.png"); // coffee icon
	
	// Action Buttons ========================
	JButton playButton = new JButton("Play");
	JButton exitButton = new JButton("Exit");
	JButton loginButton = new JButton("LogIn");
	JButton logoutButton = new JButton("LogOut");
	private JTextField loginField;
    private JPasswordField passwordField;
	
	// Game Constructor ================
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true); 
		this.addKeyListener(new MyKeyAdapter()); // Key listener
		startGame();
	}
	
	// Game start method ===============
	public void startGame(){
		newCoffee(); // Draw new coffee
		_running = _STATES.PLAY;
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g){
		
		if(_running == _STATES.PLAY) {
		// if start
			
			// grid lines
			/*
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}*/
			
			g.setColor(_coffee_brown);
			// Draw coffee
			g.drawImage(_coffeeImage, _coffeeX, _coffeeY, UNIT_SIZE, UNIT_SIZE, this);
			
			// draw snake
			for(int i = 0; i < _bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(_body_x[i], _body_y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(_user_color);
					g.fillRect(_body_x[i], _body_y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			// draw score
			drawScore(g);
		// if end
		} else if (_running == _STATES.OVER){
			gameOver(g);
		} else if (_running == _STATES.MENU){
			menu(g);
		} else if (_running == _STATES.LOGIN){
			loginScreen(g);
		}
	}
	
	// Spawn new coffee at random
	public void newCoffee() {
		_coffeeX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		_coffeeY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	// Snake move method
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
	
	// Check if snake collides with coffee
	public void checkCoffee() {
		if((_body_x[0] == _coffeeX) && (_body_y[0] == _coffeeY)) {
			_bodyParts++;
			_coffeesDrank++;
			newCoffee();
		}
	}
	
	// Check death collisions
	public void checkCollisions() {
		// head collision with body
		for(int i = _bodyParts; i > 0; i--) {
			if((_body_x[0] == _body_x[i])&&(_body_y[0] == _body_y[i])) {
				_running = _STATES.OVER;
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
			_running = _STATES.OVER;
		}
	}
	
	// Game over screen
	public void gameOver(Graphics g) {
		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		drawScore(g);
		
		// Text - Press R to restart
		g.setColor(Color.white);
		g.setFont(new Font("Ink free", Font.BOLD, 35));
		g.drawString("Press R to restart", (SCREEN_WIDTH - metrics.stringWidth("Press R to restart"))/2, SCREEN_HEIGHT/2+200);
		
		// Text - Press M for menu screen
		g.setColor(Color.white);
		g.setFont(new Font("Ink free", Font.BOLD, 35));
		g.drawString("Press M for menu", (SCREEN_WIDTH - metrics.stringWidth("Press M for menu"))/2, SCREEN_HEIGHT/2+300);
				
	}
	
	// Menu screen
	public void menu(Graphics g) {
        
		exitButton.setBackground(Color.WHITE);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        if(_isSignedIn) {
        	playButton.setBackground(Color.WHITE);
    		playButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	restartGame();
                }
            });
    		
        	playButton.setBounds(50, 150, 80, 30); 
        	this.add(playButton);	
        } else {
        	loginButton.setBackground(Color.WHITE);
    		loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	_running = _STATES.LOGIN;
                }
            });
    		
        	loginButton.setBounds(50, 150, 80, 30); 
        	this.add(loginButton);
        }
        
        exitButton.setBounds(150, 150, 80, 30); 
        this.add(exitButton);
        
	}
	
	// Login screen
	public void loginScreen(Graphics g) {
		// remove buttons
		this.remove(exitButton);
		this.remove(loginButton);
		

        JLabel loginLabel = new JLabel("Username:");
        loginLabel.setBounds(50, 100, 80, 30); 
        loginField = new JTextField();
        loginField.setBounds(50, 150, 80, 30); 

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(150, 100, 80, 30);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 150, 80, 30); 

        JButton submitButton = new JButton("Log in");
        submitButton.setBounds(250, 150, 80, 30); 

        this.add(loginLabel);
        this.add(loginField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(new JLabel());
        this.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = loginField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);
                
                timer.start();
                boolean response = validateUser(username, password);
            }
        });
        
        timer.stop();
	}
	
	public boolean validateUser(String username, String password) {
		
		return false;
	}
	
	// Restart game instance
	public void restartGame() {
		_bodyParts = 6;
		_coffeesDrank = 0;
		_direction = 'R'; // Right direction at beginning
		_body_x[0] = 0;
		_body_y[0] = 0;
		
		// remove buttons
		this.remove(exitButton);
		this.remove(playButton);
		
		
		requestFocusInWindow();
		_running = _STATES.PLAY;
	}
	
	// Draw player score
	public void drawScore(Graphics g) {
		// draw score
		g.setColor(_coffee_brown);
		g.setFont(new Font("Ink free", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: "+_coffeesDrank, (SCREEN_WIDTH - metrics.stringWidth("Score"+_coffeesDrank))/2, g.getFont().getSize());
	}
	
	
	// Key actions
	@Override
	public void actionPerformed(ActionEvent e) {
		if(_running == _STATES.PLAY) {
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
			// Go left
			case KeyEvent.VK_LEFT:
				if(_direction != 'R') {
					_direction = 'L';
				}
				break;
			// Go right
			case KeyEvent.VK_RIGHT:
				if(_direction != 'L') {
					_direction = 'R';
				}
				break;
			// Go up
			case KeyEvent.VK_UP:
				if(_direction != 'D') {
					_direction = 'U';
				}
				break;
			// Go down
			case KeyEvent.VK_DOWN:
				if(_direction != 'U') {
					_direction = 'D';
				}
				break;
			// Restart game
			case KeyEvent.VK_R:
				if(_running != _STATES.PLAY) {
					restartGame();
				}
				break;
			// Go to Menu screen
			case KeyEvent.VK_M:
				if(_running != _STATES.MENU) {
					_running = _STATES.MENU;
				}
				break;
			}
		}
	}
}
