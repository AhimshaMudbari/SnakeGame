import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GameHandle extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 700;
	static final int SCREEN_HEIGHT = 700;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyparts = 7;
	int applesEaten;
	int appleX;
	int appleY;

	char direction = 'R';
	boolean running = false;

	javax.swing.Timer timer;
	Random random;

	public GameHandle() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (running) {
//			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//
//			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyparts; i++) {
				if (i == 0) {
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 33));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score Board: " + applesEaten,
					(SCREEN_WIDTH - metrics.stringWidth("Score Board: " + applesEaten)) / 2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

	}

	public void move() {
		for (int i = bodyparts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && y[0] == appleY) {
			bodyparts++;
			applesEaten++;
			newApple();
		}
	}

	public void checkColision() {
		// head-body collision
		for (int i = bodyparts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// head-frame collision Left Border
		if ((x[0] < 0)) {
			running = false;
		}
		// head-frame collision Right Border
		if ((x[0] > SCREEN_WIDTH)) {
			running = false;
		}
		// head-frame collision Top Border
		if ((y[0] < 0)) {
			running = false;
		}
		// head-frame collision Bottom Border
		if ((y[0] > SCREEN_HEIGHT)) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		// score
		g.setColor(Color.blue);
		g.setFont(new Font("Ink Free", Font.BOLD, 33));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score Board: " + applesEaten,
				(SCREEN_WIDTH - metrics.stringWidth("Score Board: " + applesEaten)) / 2, g.getFont().getSize());
		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("consolas", Font.BOLD, 44));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over Gai Ko Gover", (SCREEN_WIDTH - metrics1.stringWidth("Game Over Gai Ko Gover")) / 2,
				SCREEN_HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (running) {
			move();
			checkApple();
			checkColision();
		}
		repaint();

	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
