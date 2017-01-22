package minesweeper;


public class BoardPosition {
	private final int x;
	private final int y;
	
	//Rep invariant
	//  x >= 0
	//  y >= 0
	//Abstraction function
	//  represents a board position starting where 
	//  coordinates are strictly positive
	//Safety from rep exposure:
	// All fields are private and final
	
	/**
	 * 
	 * @param x an integer which is the x-coordinate represented by the object
	 * must be >= 0 
	 * @param y an integer which is the y-coordinate represented by the object
	 * must be >= 0
	 */
	public BoardPosition(int x, int y){
		this.x = x;
		this.y = y;
		checkRep();
	}
	
	/**
	 * 
	 * @return an integer which is the x-coordinate represented by the object
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * 
	 * @return an integer which is the y-coordinate represented by the object
	 */
	public int getY(){
		return this.y;
	}
	
	private void checkRep() {
		assert x >= 0;
		assert y >= 0;
	}
	
	/**
	 * @return true if the other object is a Position and both its x and y fields have 
	 * the same values as this object 
	 */
	@Override
	public boolean equals(Object other){
		if (!(other instanceof BoardPosition)) return false;
		BoardPosition otherPosition = (BoardPosition) other;
		return this.x == otherPosition.getX() && this.y == otherPosition.getY();
	}
	
}
