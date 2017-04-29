public class Coordinate {
	
	public int row,col;
	
	public Coordinate(int row, int col){
		this.row = row;
		this.col = col;
	}
	
	public int row() {
		return row;
	}
	
	public int col() {
		return col;
	}

	public void incrementRow() {
		this.row++;
	}
	public int rowUp() {
		return this.row++;
	}

	public void decrementRow() {
		this.row--;
	}

	public void incrementCol() {
		this.col++;
	}

	public void decrementCol() {
		this.col--;
	}

	@Override
	public String toString() {
		return "Row: " + row + " Col: " + col;
	}
  
	@Override 
	public boolean equals(Object o) {
		Coordinate c = (Coordinate)o;
		
		if ((this.row == c.row) && this.col == c.col) { return true;}
		return false; 
	}

}