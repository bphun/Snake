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

	private int currDirection;

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
		t = new Timer(11, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.refresh();
        move();
			}
		});
		t.start();
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
		if (snakeParts.get(0).equals(foodLoc)) { 
			putFood(); 
			Coordinate snakePart = null;
			Coordinate last = snakeParts.get(snakeParts.size() - 1);
			switch (currDirection) {
				case 0:
					snakePart = new Coordinate(last.row() - 1, last.col());
					break;
				case 1:
					snakePart = new Coordinate(last.row(), last.col() + 1);
					break;
				case 2:
					snakePart = new Coordinate(last.row() + 1, last.col());
					break;
				case 3:
					snakePart = new Coordinate(last.row(), last.col() - 1);
					break;
			}
			snakeParts.add(snakePart);
		}
	}

	private void checkHitWall() {
		Coordinate snakeHead = snakeParts.get(0);
		switch (currDirection) {
			case 0:
				if (snakeHead.row() == 0) {
					panel.snakeDidDie();
				}
				break;
			case 1:
				if (snakeHead.col() == COLS) {
					panel.snakeDidDie();
				}
				break;
			case 2:
				if (snakeHead.row() == ROWS) {
					panel.snakeDidDie();
				}
				break;
			case 3:
				if (snakeHead.col() == 0) {
					panel.snakeDidDie();
				}
				break;
		}
	}

	private void checkHitSelf() {
		Coordinate snakeHead = snakeParts.get(0);
		for (Coordinate c : snakeParts) {
			if (snakeHead.equals(c)) {
				panel.snakeDidDie();
				return;
			}
		}
	}

	public void restart() {
		initSnake();
		putFood();
		currDirection = 3;
  }
    
	public List<Coordinate> getSnake() {
		return this.snakeParts;
	}

	public void setDirection(int dir) {
		this.currDirection = dir;
	}

	public int direction() {
		return this.currDirection;
	}

	public void move() {
		if (snakeParts == null) { return; }
		for (int i = snakeParts.size() - 1; i > 0; i--) {
			snakeParts.set(i,  snakeParts.get(i - 1));
		}
		switch (currDirection) {
			case 0:
				snakeParts.get(0).decrementRow();
				break;
			case 1:
				snakeParts.get(0).incrementCol();
				break;
			case 2:
				snakeParts.get(0).incrementRow();
				break;
			case 3:
				snakeParts.get(0).decrementCol();
				break;
		}

		checkEatFood();
		checkHitWall();
		// checkHitSelf();
		// printGrid();
		for (Coordinate c : snakeParts) {
			System.out.println(c.toString());
		}	
		System.out.println();
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