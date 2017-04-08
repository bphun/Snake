import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.RenderingHints;

public class SnakePanel extends JPanel {

	private Snake snake;

	private static final Dimension PANEL_DIMENSIONS = new Dimension(1650, 1000);
	private static final int SQUARE_SIZE = 30;
	private static final int LINE_THICKNESS = 1;

	private int rows;
	private int cols;
	private int currDirection; 
	private int[][] grid;

	public SnakePanel(Snake snake, int rows, int cols, int[][] grid) {
		this.snake = snake;
		this.rows = rows;
		this.cols = cols;
		this.grid = grid;

		this.setPreferredSize(PANEL_DIMENSIONS);
		this.setBackground(new Color(33, 33, 33));
		this.setUpKeyMappings();
	}

	private void setUpKeyMappings() {
		this.requestFocusInWindow();

		int[] arrowKeys = {KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT};
		char[] letterKeys = {'w', 'd', 's', 'a'};

		// Set up mappings for the keyboard arrow keys
		for (int key : arrowKeys) {
			switch (key) {
				case KeyEvent.VK_UP:
					this.getInputMap().put(KeyStroke.getKeyStroke(key, 0, false),letterKeys[0]);
					break;
				case KeyEvent.VK_RIGHT:
					this.getInputMap().put(KeyStroke.getKeyStroke(key, 0, false),letterKeys[1]);
					break;
				case KeyEvent.VK_DOWN:
					this.getInputMap().put(KeyStroke.getKeyStroke(key, 0, false),letterKeys[2]);
					break;
				case KeyEvent.VK_LEFT:
					this.getInputMap().put(KeyStroke.getKeyStroke(key, 0, false),letterKeys[3]);
					break;
			}
		}
		for (int key : arrowKeys) {
			switch (key) {
				case KeyEvent.VK_UP:
					this.getActionMap().put(letterKeys[0], new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							currDirection = letterKeys[0];
						}
					});					
					break;
				case KeyEvent.VK_RIGHT:
					this.getActionMap().put(letterKeys[1], new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							currDirection = letterKeys[1];
						}
					});	
					break;
				case KeyEvent.VK_DOWN:
					this.getActionMap().put(letterKeys[2], new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							currDirection = letterKeys[2];
						}
					});				
					break;
				case KeyEvent.VK_LEFT:
					this.getActionMap().put(letterKeys[3], new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent e) {
							currDirection = letterKeys[3];
						}
					});					
					break;
			}
			move();
		}
		// for (int i = 0; i < arrowKeys.length; i++) {
		// 	this.getActionMap().put(letterKeys[i], new AbstractAction() {
		// 		@Override
		// 		public void actionPerformed(ActionEvent e) {
		// 			currDirection = i;
		// 		}
		// 	});
		// }

		// Set up mappings for WASD keys
		for (char key : letterKeys) {
			this.getInputMap().put(KeyStroke.getKeyStroke(key), key);
		}
		for (char key : letterKeys) {
			this.getActionMap().put(key, new AbstractAction(){
				@Override
				public void actionPerformed(ActionEvent e) {
					currDirection = key;
					move();
				}
			});	
		}
	}

	private void move() {
		switch(currDirection) {
			case 'w':
				snake.up();
				break;
			case 'a':
				snake.left();
				break;
			case 's':
				snake.down();
				break;
			case 'd':
				snake.right();
				break;
		}

		repaint();
	}

	public void refresh() {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawGrid(g2);
		drawSquares(g2);
	}

	private void drawGrid(Graphics2D g2) {
		g2.setColor(new Color(144, 164, 174));
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				//	Draws the vertical line
				g2.drawLine(SQUARE_SIZE * c, 0, SQUARE_SIZE * c, (PANEL_DIMENSIONS.height));
			}
			//	Draws the horizontal line
			g2.drawLine(0, (SQUARE_SIZE * r), PANEL_DIMENSIONS.width, (SQUARE_SIZE * r)); 
		}
	}

	private void drawSquares(Graphics2D g2) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				switch (grid[r][c]) {
					case 1: // Snake part
						g2.setColor(new Color(56, 142, 60));
						g2.fillRect(c * SQUARE_SIZE + LINE_THICKNESS, r * SQUARE_SIZE + LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS);
						g2.setColor(Color.BLACK);		
						break;
					case 2: // Food
						g2.setColor(new Color(255, 152, 0));
						g2.fillRect(c * SQUARE_SIZE + LINE_THICKNESS, r * SQUARE_SIZE + LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS);	
						break;
				}
			}
		}
	}

}