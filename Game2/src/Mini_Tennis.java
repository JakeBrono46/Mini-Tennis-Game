import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

//Racquet Class
class Racquet{
	private static final int Y = 330;
	private static final int WIDTH = 60;
	private static final int HEIGHT = 10;
	
	int x = 0;
	int xa = 0;
	
	private Mini_Tennis game;
	
	public Racquet(Mini_Tennis game) {
		this.game = game;
	}
	
	public void move() {
		if(x + xa > 0 && x + xa < game.getWidth() - WIDTH) {
			x = x + xa;
		}
	}
	
	public void paint(Graphics2D g) {
		g.fillRect(x, Y, WIDTH, HEIGHT);
	}
	
	public void keyReleased(KeyEvent e) {
		xa = 0;
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			xa = -game.speed;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			xa = game.speed;
		}
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, Y, WIDTH, HEIGHT);
	}
	
	public int getTopY() {
		return Y;
	}
}

//Ball Class
class Ball{
	private static final int DIAMETER = 30;
	
	int x = 0;
	int y = 0;
	int xa = 1;
	int ya = 1;
	
	private Mini_Tennis game;
	
	public Ball(Mini_Tennis game) {
		this.game = game;
	}
	
	void move() {
		boolean changeDirection = true;
		
		if(x + xa < 0) {
			xa = game.speed;
		}
		if(x + xa > game.getWidth() - DIAMETER) {
			xa = -game.speed;
		}
		if(y + ya < 0) {
			ya = game.speed;
		}
		if(y + ya > game.getHeight() - DIAMETER) {
			game.gameOver();
		}
		if(collision()) {
			ya = -game.speed;
			y = game.racquet.getTopY() - DIAMETER;
			game.speed++;
		}else {
			changeDirection = false;
		}
		x = x + xa;
		y = y + ya;
	}
	
	private boolean collision() {
		return game.racquet.getBounds().intersects(getBounds());
	}

	private Rectangle getBounds() {
		return new Rectangle(x, y, DIAMETER, DIAMETER);
	}

	public void paint(Graphics2D g) {
		g.fillOval(x, y, DIAMETER, DIAMETER);
	}
}


@SuppressWarnings("serial")
public class Mini_Tennis extends JPanel{

	Ball ball = new Ball(this);
	Racquet racquet = new Racquet(this);
	
	int speed = 1;
	
	private int getScore() {
		return speed - 1;
	}
	
	public Mini_Tennis() {
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				racquet.keyPressed(e);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				racquet.keyReleased(e);
			}
		});
		setFocusable(true);
	}
	
	private void move() {
		ball.move();
		racquet.move();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ball.paint(g2d);
		racquet.paint(g2d);
		
		g2d.setColor(Color.GRAY);
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		g2d.drawString(String.valueOf(getScore()), 10, 30); 
	}
	
	public void gameOver() {
		JOptionPane.showMessageDialog(this, "Score: " + getScore(), "Game Over", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}
	
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("Mini Tennis");
		Mini_Tennis game = new Mini_Tennis();
		frame.add(game);
		frame.setSize(300, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while(true) {
			game.move();
			game.repaint();
			Thread.sleep(10);
		}
	}

}
