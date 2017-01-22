package minesweeper;
import java.util.*;

public class Board {
	private final int width;
	private final int length;
	private final List<Square> board = new ArrayList<>();
	
	//Rep invariant:
	// 	width and length are > 0
	//
	//  a square returned by getSquare(x,y)
	//  must have as its neighbours
	//  only the squares returned by 
	//  getSquare(x+i,y+j)
	//  for all |i|,|j| <= 1, i*j + i + j != 0 ,
	// 	for which 
	//  withinBounds(x+i,y+j)
	//  returns true
	// 
	//  the bomb counts of each square is
	//  the sum of the number of its 
	//  neighbours which contain bombs - I think this is 
	//  something that is tested by the addNeighbour
	//  method and should not be regarded as a rep invariant
	//
	//Abstraction function:
	// 	represents a minesweeper board 
	// 	of dimensions width x length 
	//
	//Safety from rep exposure
	// 	All fields are private
	// 	width and length are immutable
	// 	board contains mutable Squares
	// 	but these created within
	// 	and only accessible via the
	// 	board object as no references 
	// 	are leaked to them.
	//  Also after construction
	//  the references to the Squares,
	//  cannot be modified as there are
	//  no mutator methods that 
	//  modify the list itself 
	//  rather than its elements 
	//
	//Thread safety argument
	//  width and length are final and immutable
	//  so getWidth and getLength are not guarded 
	//  by Board's lock 
	// 
	//  The Squares in the board list are mutable 
	//  but all accesses to them happen 
	// 	within Board methods which are guarded by 
	//  Board's lock
	//
	//  The Square class is not itself threadsafe
	//  but the Squares in the list are initialised
	//  from within the Board and are never exposed
	//  to a client. Since they are only accessed
	//  via methods guarded by Board's lock, they 
	//  are not reachable from any other thread
	//  than the one which owns Board's lock 
	//
	//  The private methods are not guarded by the
	//  locks but they are only accessed from within
	//  the guarded methods so they are not 
	//  accessible to any thread than the one
	//  which owns the lock
	
	/**
	 * Clients may synchronize with each other using the Board object itself 
	 */
	public Board(){
			this.width = 10;
			this.length = 10;
			this.addSquares();
			this.addNeighbours();
			checkRep();
	
	}
	
	//TODO: add documentation for this once function has been finalized
	public Board(int width, int length){
			assert width > 0;
			assert length > 0;
			this.width = width;
			this.length = length;
			this.addSquares();
			this.addNeighbours();
			checkRep();
		
	}

	public Board(int width, int length, List<Boolean> bombStatuses){
		assert width >0;
		assert length >0;
		assert bombStatuses.size() == width*length;
		this.width = width;
		this.length = length;
		for(Boolean bombStatus: bombStatuses){
			this.board.add(new Square(bombStatus));
		}
		this.addNeighbours();
		checkRep();
	}
	
	private void addSquares(){
		synchronized (this){
			Random random = new Random();
			for(int i = 0; i < this.width*this.length; i++){
				this.board.add(new Square(random.nextDouble() < 0.25));
			}
		}
	}
	
	//Adjust so only new neighbours added
	private void addNeighbours(){
		synchronized(this){
			for(int i = 0; i < this.width;i++){
				for(int j = 0; j < this.length; j++){
					for(int di = -1; di <= 1; di++){
						Square square = this.getSquare(i, j);
						for(int dj = -1; dj <=1; dj++){
							int ii = i + di;
							int jj = j + dj; 
							if(this.withinBounds(ii, jj)){
								square.addNeighbour(this.getSquare(ii, jj));
							}
						}
					}
				}
			}
		}
	}
	
	private void checkRep(){
		assert this.width > 0;
		assert this.length > 0;
		for(int i = 0; i < this.width;i++){
			for(int j = 0; j < this.length; j++){
				Square square = this.getSquare(i, j);
				for(int di = -1; di <= 1; di++){
					for(int dj = -1; dj <=1; dj++){
						int ii = i + di;
						int jj = j + dj; 
						if(this.withinBounds(ii, jj)){
							assert square.isNeighbour(this.getSquare(ii, jj));
						}
					}
				}
			}
		}
	}
	
	
	
	/**
	 * 
	 * @return integer representing width of board
	 */
	public int getWidth(){
		return this.width;
	}
	
	/**
	 * 
	 * @return integer representing length of board
	 */
	public int getLength(){
		return this.length;
	}
	

	/**
	 * Deals with a DIG request, with the effect that 
	 * if x and y represent a valid board position, the square at that position
	 * is modified in accordance with the protocol, otherwise has no effect
	 * @param x - the x-coordinate of the square to be modified
	 * @param y - the y-coordinate of the square to be modified
	 * @return true if x and y are valid coordinates and the square contains a bomb, otherwise false
	 */
	public boolean handleDig(int x, int y){
		synchronized(this){
			if(this.withinBounds(x, y)){
				Square square = this.getSquare(x, y);
				boolean bomb = square.hasBomb();
				square.dig();
				return bomb;
			}
			return false;
		}
	}
	
	/**
	 * Executes the FLAG part of the Minesweeper protocol with the effect that
	 * the square with coordinates x and y is modified in accordance with the protocol
	 * if x and y represent a valid board position, otherwise has no effect
	 * @param x - the x-coordinate of the square to be modified
	 * @param y - the y-coordinate of the square to be modified
	 */
	public void handleFlag(int x, int y){
		synchronized (this) {
			if (this.withinBounds(x, y)) {
				this.getSquare(x, y).flag();
			}
		}
	}
	
	/**
	 * Executes the DEFLAG part of the Minesweeper protocol with the effect that
	 * the square with coordinates x and y is modified in accordance with the protocol
	 * if x and y represent a valid board position, otherwise has no effect
	 * @param x - the x-coordinate of the square to be modified
	 * @param y - the y-coordinate of the square to be modified
	 */
	public void handleDeflag(int x, int y){
		synchronized (this) {
			if (this.withinBounds(x, y)) {
				this.getSquare(x, y).deflag();
			}
		}
	}
	
	
	/**
	 * 
	 * @param x - the x-coordinate of the square to be modified, must be >= 0 and < width
	 * @param y - the y-coordinate of the square to be modified, must be >= 0 and < length
	 * @return square with the coordinates x and y
	 */
	private Square getSquare(int x, int y){
		assert (x >= 0) & (x < this.width);
		assert (y >= 0) & (y < this.length);
		return this.board.get(this.getSquareIndex(x, y));
	}
	
	
	/**
	 * Determines if a pair of coordinates is represented
	 * by the board 
	 * @param x - represents an x-coordinate 
	 * @param y - represents a y-coordinate 
	 * @return true if the x and y correspond to an
	 * index in the board list, otherwise false 
	 */
	private boolean withinBounds(int x, int y){
		return (x >= 0) & (x < this.width) & (y >= 0) & (y < this.length);
	}
	
	/**
	 * 
	 * @param x - the x-coordinate of the square to be modified, must be >= 0 and < width
	 * @param y - the y-coordinate of the square to be modified, must be >= 0 and < length
	 * @return
	 */ 
	private int getSquareIndex(int x, int y){
		return this.length*x + y;
	}
	
	@Override
	public String toString(){
		synchronized (this) {
			return "";
		}
	}
	
}
