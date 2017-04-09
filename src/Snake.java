import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class Snake {

	private SnakePanel panel;
	private JFrame frame;
	private Timer t;

	private static final int ROWS = 37;
	private static final int COLS = 66;

	private Coordinate foodLoc;
	private List<Coordinate> snakeParts;

	public static void main(String[] args) {
		new Snake().start();
	}

	private void start() {
		panel = new SnakePanel(this, ROWS, COLS);
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

	public void printGrid() {
		System.out.println("Snake Length: " + snakeParts.size());
		int[][] grid = new int[ROWS][COLS];

		for (Coordinate c : snakeParts) {
			System.out.println(c.toString());
			grid[c.row()][c.col()] = 1;
		}
		grid[foodLoc.row()][foodLoc.col()] = 2;

		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				switch (grid[r][c]) {
					case 0:
						System.out.print(".");
						break;
					case 1:
						System.out.print("~");
						break;
					case 2:
						System.out.print("*");
						break;
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private void initSnake() {
		snakeParts = new ArrayList<>();
		int row = (int)(Math.random() * ROWS - 2);
		int col = (int)(Math.random() * COLS);
		int length = (int)(Math.random() * 5);
		if (length <= 1) { length += 2; }
		if (col + length > COLS) { 
			while (col + length > COLS) {
				length--;
			}
		}

		for (int i = length; i >= 0; i--) {
			snakeParts.add(new Coordinate(row, col++));
		}

		panel.setSnake(snakeParts);
		for (Coordinate c : snakeParts) {
			System.out.println(c.toString());
		}
	}

	private void putFood() {
		foodLoc = new Coordinate((int)(Math.random() * ROWS), (int)(Math.random() * COLS));
		panel.setFoodLoc(foodLoc);
	}

	private boolean hitWall(int row, int col) {
		return ((row < ROWS && row > 0) && (col < COLS && col > 0));
	}

	private void checkEatFood() {
		if (snakeParts.get(0).equals(foodLoc)) { putFood(); }
	}

	private void die() {
	}

	public List<Coordinate> getSnake() {
		return this.snakeParts;
	}

	public void up() {
		Coordinate prev = null;
		for (int i = 0; i < snakeParts.size(); i++) {
			switch (i) {
				case 0:
					prev = snakeParts.get(0);
					snakeParts.get(i).decrementRow();
					break;
				default:
					Coordinate temp = snakeParts.get(i);
					snakeParts.set(i, prev);
					prev = temp;
					break;
			}
		}
		printGrid();
		// checkEatFood();
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