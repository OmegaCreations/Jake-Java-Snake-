import javax.swing.JFrame;

public class GameFrame extends JFrame{
	// Whole frame of game
	GameFrame(){
		this.add(new GamePanel());
		this.setTitle("Jake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.pack(); // pack everything
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
