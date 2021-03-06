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

	private static final int SQUARE_SIZE = 25;
	private static final int LINE_THICKNESS = 1;

	private int rows;
	private int cols;
	private boolean mouseListenerActive;
	private Dimension panelDimension;

	private int points;
	private Snake snake;
	private boolean snakeDidDie;
	private Direction currDirection; 

	private Coordinate foodLoc;
	private List<Coordinate> snakeParts;

	public SnakePanel(Snake snake, int rows, int cols, Dimension dim) {
		this.snake = snake;
		this.rows = rows;
		this.cols = cols;
		this.panelDimension = dim;
		this.snakeDidDie = false;
		this.currDirection = Direction.LEFT;

		this.setPreferredSize(panelDimension);
		this.setBackground(new Color(33, 33, 33));
		this.setUpKeyMappings();
		this.setUpClickListener();
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
				if (!mouseListenerActive) { return; }
				snake.restart();
				setUpKeyMappings();
				snakeDidDie = false;
				mouseListenerActive = false;
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}

	private void setUpKeyMappings() {
		this.requestFocusInWindow();
		// int[] arrowKeys = {KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT};
		char[] letterKeys = {'w', 'd', 's', 'a'};
		// Direction[] directions = {Direction.UP, Direction.RIGHT ,Direction.DOWN , Direction.LEFT};

		// Set up mappings for WASD keys
		for (char key : letterKeys) {
			this.getInputMap().put(KeyStroke.getKeyStroke(key), key);
		}
		this.getActionMap().put('w', new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!currDirection.equals(Direction.DOWN)){
					currDirection = Direction.UP;
					move();
				}
			}

		});
		this.getActionMap().put('a', new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!currDirection.equals(Direction.RIGHT)){
					currDirection = Direction.LEFT;
					move();
				}
			}
		});
		this.getActionMap().put('s', new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!currDirection.equals(Direction.UP)){
					currDirection = Direction.DOWN;
					move();
				}
			}

		});
		this.getActionMap().put('d', new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!currDirection.equals(Direction.LEFT)){
					currDirection = Direction.RIGHT;
					move();
				}
			}
		});
	}

	private void move() {
		if (snakeDidDie) { return; }
		switch(currDirection) {
			case UP:
				snake.setDirection(Direction.UP);
				break;
			case RIGHT:
				snake.setDirection(Direction.RIGHT);
				break;
			case DOWN:
				snake.setDirection(Direction.DOWN);
				break;
			case LEFT:
				snake.setDirection(Direction.LEFT);
				break;
		}
		snakeParts = snake.getSnake();
		repaint();
	}

	public void ateFood() {
		points += 5;
	}

	public void snakeDidDie() {
		snakeDidDie = true;
		mouseListenerActive = true;
		points = 0;
		repaint();
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

	public void updateScreenDim(int rows, int cols, Dimension dim) {
		this.rows = rows;
		this.cols = cols;
		this.panelDimension = dim;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawPoints(g2);
		// drawGrid(g2);
		drawSquares(g2);
		if (snakeDidDie) {
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("AvenirNext", Font.PLAIN, 30));
			g2.drawString("You Died... click to restart", panelDimension.width / 2, panelDimension.height / 2);
		}
	}

	private void drawPoints(Graphics2D g2) {
		g2.setFont(new Font("AvenirNext", Font.PLAIN, 20));
		g2.setColor(Color.WHITE);
		g2.drawString("Points: " + points, panelDimension.width - 250, 30);
		g2.setColor(Color.BLACK);
	}

	private void drawGrid(Graphics2D g2) {
		g2.setColor(new Color(144, 164, 174));
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				//	Draws the vertical line
				g2.drawLine(SQUARE_SIZE * c, 0, SQUARE_SIZE * c, (panelDimension.height));
			}
			//	Draws the horizontal line
			g2.drawLine(0, (SQUARE_SIZE * r), panelDimension.width, (SQUARE_SIZE * r)); 
		}
	}

	private void drawSquares(Graphics2D g2) {
		if (snakeParts.size() == 0 || snakeParts == null) { return; }
		g2.setColor(new Color(255, 152, 0));
		g2.fillRect(foodLoc.col() * SQUARE_SIZE + LINE_THICKNESS, foodLoc.row() * SQUARE_SIZE + LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS);		
		g2.setColor(new Color(56, 142, 60));
		for (Coordinate c : snakeParts) {
			g2.fillRect(c.col() * SQUARE_SIZE + LINE_THICKNESS, c.row() * SQUARE_SIZE + LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS);		
		}
		g2.setColor(Color.BLACK);
	}

}