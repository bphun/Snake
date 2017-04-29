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

	private Direction currDirection;
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
		startTimer();
	}

	private void startTimer() {
		t = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
	
				move();
				checkEatFood();
				checkHitWall();
				checkHitSelf();

				panel.refresh();
			}
		});
		t.start();
	}

	private void initSnake() {
		snakeParts = new ArrayList<>();
		currDirection = Direction.LEFT;
		int row = (int)(Math.random() * ROWS - 2);
		int col = (int)(Math.random() * COLS - 2);
		int length = (int)(Math.random() * 5);
		if (length <= 1) { length += 2; }
		if (col + length > COLS) { 
			while (col + length > COLS) {
				length--;
			}
		}
		for (int i = 0; i < length; i++) {
			snakeParts.add(new Coordinate(row, col++));
		}

		panel.setSnake(snakeParts);
		System.out.println("Init Snake: Length = " + length + " startCoord = " + "(" + snakeParts.get(0).toString() + ")");
	}

	private void putFood() {
		foodLoc = new Coordinate((int)(Math.random() * ROWS), (int)(Math.random() * COLS));
		panel.setFoodLoc(foodLoc);
	}

	private boolean hitWall(int row, int col) {
		return ((row < ROWS && row > 0) && (col < COLS && col > 0));
	}

	private void checkEatFood() {
		if (snakeParts.get(0).equals(foodLoc)) { 
			putFood(); 
			Coordinate snakePart = null;
			Coordinate last = snakeParts.get(snakeParts.size() - 1);
			switch (currDirection) {
				case UP:
					snakePart = new Coordinate(last.row() - 1, last.col());
					break;
				case RIGHT:
					snakePart = new Coordinate(last.row(), last.col() + 1);
					break;
				case DOWN:
					snakePart = new Coordinate(last.row() + 1, last.col());
					break;
				case LEFT:
					snakePart = new Coordinate(last.row(), last.col() - 1);
					break;
			}
			snakeParts.add(snakePart);
			panel.ateFood();
		}
	}

	private void checkHitWall() {
		Coordinate snakeHead = snakeParts.get(0);
		switch (currDirection) {
			case UP:
				if (snakeHead.row() <= 0) {
					panel.snakeDidDie();
				}
				break;
			case RIGHT:
				if (snakeHead.col() == COLS) {
					panel.snakeDidDie();
				}
				break;
			case DOWN:
				if (snakeHead.row() == ROWS) {
					panel.snakeDidDie();
				}
				break;
			case LEFT:
				if (snakeHead.col() <= 0) {
					panel.snakeDidDie();
				}
				break;
		}
	}

	private void checkHitSelf() {
		Coordinate snakeHead = snakeParts.get(0);
		for (int i = 1; i < snakeParts.size(); i++) {
			if (snakeParts.get(i).equals(snakeHead)) {
				panel.snakeDidDie();
				return;
			}

		}
	}

	public void restart() {
		initSnake();
		putFood();
		currDirection = Direction.LEFT;
	}

	public List<Coordinate> getSnake() {
		return this.snakeParts;
	}

	public void setDirection(Direction dir) {
		this.currDirection = dir;
	}

	public Direction direction() {
		return this.currDirection;
	}

	public void move() {
		if (snakeParts == null) { return; }

		Coordinate prev = new Coordinate(snakeParts.get(0).row(), snakeParts.get(0).col());
		for (int i = 1; i < snakeParts.size(); i++) {
			Coordinate tmp = new Coordinate(snakeParts.get(i).row(), snakeParts.get(i).col());
			snakeParts.set(i, prev);
			prev = tmp;
		}

		switch (currDirection) {
			case UP:
				snakeParts.get(0).decrementRow();
				break;
			case RIGHT:
				snakeParts.get(0).incrementCol();
				break;
			case DOWN:
				snakeParts.get(0).incrementRow();
				break;
			case LEFT:
				snakeParts.get(0).decrementCol();
				break;
		}
	}


	public void printGrid() {
		System.out.println("Snake Length: " + snakeParts.size());
		if (snakeParts == null) { return; }
		int[][] grid = new int[ROWS][COLS];

		for (Coordinate c : snakeParts) {
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
}


