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
		printGrid();
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
	}

	public void up() {

	}

	public void right() {

	}

	public void down() {

	}

	public void left() {

	}


}