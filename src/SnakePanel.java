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
import java.util.List;
import java.util.ArrayList;
import javax.swing.JComponent;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SnakePanel extends JPanel {

	private Snake snake;

	private static final Dimension PANEL_DIMENSIONS = new Dimension(1650, 1000);
	private static final int SQUARE_SIZE = 25;
	private static final int LINE_THICKNESS = 1;

	private int rows;
	private int cols;
	private int currDirection; 
	private boolean snakeDidDie;

	private Coordinate foodLoc;
	private List<Coordinate> snakeParts;

	private Coordinate foodLoc;
	private List<Coordinate> snakeParts;

	public SnakePanel(Snake snake, int rows, int cols) {
		this.snake = snake;
		this.rows = rows;
		this.cols = cols;

		this.setPreferredSize(PANEL_DIMENSIONS);
		this.setBackground(new Color(33, 33, 33));
		this.setUpKeyMappings();
	}

  private void setUpClickListener() {
		this.requestFocusInWindow();

		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent click) {
				snake.restart();
				setUpKeyMappings();
				snakeDidDie = false;
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
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
		if (snakeDidDie) { return; }
		switch(currDirection) {
			case 'w':
				snake.setDirection(0);
				break;
			case 'a':
				snake.setDirection(3);
				break;
			case 's':
				snake.setDirection(2);
				break;
			case 'd':
				snake.setDirection(1);

				break;
		}
		snakeParts = snake.getSnake();
		repaint();
	}

	public void snakeDidDie() {
		snakeDidDie = true;
	}

	public void refresh() {
		repaint();
	}

	public void setSnake(List<Coordinate> snakeParts) {
		this.snakeParts = snakeParts;
	}

	public void setFoodLoc(Coordinate foodLoc) {
		this.foodLoc = foodLoc;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawGrid(g2);
		drawSquares(g2);
		if (snakeDidDie) {
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("AvenirNext", Font.PLAIN, 30));
			g2.drawString("You Died. Click to restart.", PANEL_DIMENSIONS.width / 2, PANEL_DIMENSIONS.height / 2);
			setUpClickListener();
		}

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
		if (snakeParts.size() == 0 || snakeParts == null) { return; }

		for (Coordinate c : snakeParts) {
			g2.setColor(new Color(56, 142, 60));
			g2.fillRect(c.col() * SQUARE_SIZE + LINE_THICKNESS, c.row() * SQUARE_SIZE + LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS);
			g2.setColor(Color.BLACK);		
		}
		g2.setColor(new Color(255, 152, 0));
		g2.fillRect(foodLoc.col() * SQUARE_SIZE + LINE_THICKNESS, foodLoc.row() * SQUARE_SIZE + LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS);		
	}
}