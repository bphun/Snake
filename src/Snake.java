import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Snake {

	private SnakePanel panel;
	private JFrame frame;
	private Timer t;

	private static int ROWS = 31;
	private static int COLS = 55;

	private int[][] grid;
	private int snakeLength;

	public static void main(String[] args) {
		new Snake().start();
	}

	private void start() {
		grid = new int[ROWS][COLS];
		panel = new SnakePanel(this, ROWS, COLS, grid);
		frame = new JFrame("Snake");
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initSnake();
		putFood();
	}

	private void startTimer() {
		t = new Timer(11, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.refresh();
			}
		});
		t.start();
	}

	private void initSnake() {
		int row = (int)(Math.random() * ROWS - 2);
		int col = (int)(Math.random() * COLS - 2);
		int length = (int)(Math.random() * 5);
		if (length <= 1) { length += 2; }
		if (col + length > COLS) { 
			while (col + length > COLS) {
				length--;
			}
		}

		for (int c = col; c <= col + length; c++) {
			grid[row][c] = 1;
		}
		snakeLength = length;
	}

	private void putFood() {
		int row = (int)(Math.random() * ROWS);
		int col = (int)(Math.random() * COLS);
		grid[row][col] = 2;
	}

	private void printGrid() {
		for (int[] r : grid) {
			for (int c : r) {
				switch (c) {
					case 1:
						System.out.print("~");
					case 2:
						System.out.print("*");
					case 0:
						System.out.print(".");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private boolean hitWall(int row, int col) {
		return ((row < ROWS && row > 0) && (col < COLS && col > 0));
	}

	private void die() {

	}

	public void up() {
		System.out.println("Up");
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				if (!hitWall()) {
					if (grid[r][c] == 1) {
						//	Use linked list of Locations to store coordinates of each square of snake, remove grid array and create local variable that contains the location of the food item
					}
				} else {
					die();
				}
			}
		}
	}

	public void right() {
		System.out.println("right");
	}

	public void down() {
		System.out.println("down");
	}

	public void left() {
		System.out.println("left");
	}


}