import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Snake {

	private SnakePanel panel;
	private JFrame frame;
	private Timer t;

	private Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	private int rows = (screenDim.height / 25) - 2;
	private int cols = screenDim.width / 25;

	private Coordinate foodLoc;
	private Direction currDirection;
	private List<Coordinate> snakeParts;

	public static void main(String[] args) {
		new Snake().start();
	}

	private void start() {
		panel = new SnakePanel(this, rows, cols, screenDim);
		frame = new JFrame("Snake");
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		// frame.addComponentListener(new ComponentAdapter() {
		// 	@Override
		// 	public void componentResized(ComponentEvent e) {
		// 		screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		// 		rows = (screenDim.height / 25) - 2;
		// 	 	cols = screenDim.width / 25;
		// 	 	panel.updateScreenDim(rows, cols, screenDim);
		// 	}
		// });
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initSnake();
		putFood();
		startTimer();
	}

	private void startTimer() {
		t = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkEatFood();
				move();
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
		int row = (int)(Math.random() * rows - 2);
		int col = (int)(Math.random() * cols - 2);
		int length = (int)(Math.random() * 5);
		if (length <= 1) { length += 2; }
		if (col + length >= cols) { 
			while (col + length > cols) {
				col--;;
			}
		} else if (col + length <= 10) {
			while (col + length < 10) {
				col++;
			}
		}
		for (int i = 0; i < length; i++) {
			snakeParts.add(new Coordinate(row, col++));
		}

		panel.setSnake(snakeParts);
		System.out.println("Init Snake: Length = " + length + " startCoord = " + "(" + snakeParts.get(0).toString() + ")");
	}

	private void putFood() {
		foodLoc = new Coordinate((int)(Math.random() * (rows - 5)), (int)(Math.random() * (cols - 5)));
		panel.setFoodLoc(foodLoc);
	}

	private boolean hitWall(int row, int col) {
		return ((row < rows && row > 0) && (col < cols && col > 0));
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
				if (snakeHead.col() == cols) {
					panel.snakeDidDie();
				}
				break;
			case DOWN:
				if (snakeHead.row() == rows) {
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
		int[][] grid = new int[rows][cols];

		for (Coordinate c : snakeParts) {
			grid[c.row()][c.col()] = 1;
		}
		grid[foodLoc.row()][foodLoc.col()] = 2;

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
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


