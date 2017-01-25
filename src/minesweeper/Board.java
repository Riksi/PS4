package minesweeper;
import java.util.*;

public class Board {
	private final int width;
	private final int length;
	private final List<Square> board = new ArrayList<>();
	private int players = 0;
	
	//Rep invariant:
	// 	width and length are > 0
	//	players >= 0 
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
	 * Adds a new player to the game
	 */
	public void addPlayer(){
		synchronized(this){
			this.players++;
			checkRep();
		}
	}
	
	/**
	 * 
	 * @return number of players playing the game
	 */
	public int getNumPlayers(){
		synchronized(this){
			return this.players;
		}
	}
	
	
	/**
	 * A compound method
	 * that updates the number of players and returns the updated number
	 * of players playing the game
	 * @return the number of players in the game
	 */
	public int addAndGetPlayers(){
		synchronized(this){
			this.addPlayer();
			return this.getNumPlayers();
		}
	}
	
	/**
	 * Requires that players > 0
	 * Deletes a player from the game
	 */
	public void deletePlayer(){
		synchronized(this){
			this.players--;
			checkRep();
		}
	}
	
	
	/**
	 * A compound method that calls the dig method with the 
	 * effect that if the board is modified
	 * and a bomb is found, the number of players is reduced by one
	 * since the player who dug that square can no longer play
	 * @param x - the x-coordinate of the square to be modified
	 * @param y - the y-coordinate of the square to be modified
	 * @return BoardState representing result of modification
	 */
	public BoardState digAndDelete(int x, int y){
		synchronized(this){
			BoardState state = this.dig(x, y);
			if(state.getBombFound()){
				this.deletePlayer();
			}
			return state;
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
	 * @return BoardState representing result of modification
	 */
	public BoardState dig(int x, int y){
		synchronized(this){
			return modify(x,y,BoardModification.DIG);
		}
	}
	
	/**
	 * Executes the FLAG part of the Minesweeper protocol with the effect that
	 * the square with coordinates x and y is modified in accordance with the protocol
	 * if x and y represent a valid board position, otherwise has no effect
	 * @param x - the x-coordinate of the square to be modified
	 * @param y - the y-coordinate of the square to be modified
	 * @return BoardState representing result of modification
	 */
	public BoardState flag(int x, int y){
		synchronized (this) {
			return modify(x,y,BoardModification.FLAG);
		}
	}
	
	/**
	 * Executes the DEFLAG part of the Minesweeper protocol with the effect that
	 * the square with coordinates x and y is modified in accordance with the protocol
	 * if x and y represent a valid board position, otherwise has no effect
	 * @param x - the x-coordinate of the square to be modified
	 * @param y - the y-coordinate of the square to be modified
	 * @return BoardState representing result of modification
	 */
	public BoardState deflag(int x, int y){
		synchronized (this) {
			return modify(x,y,BoardModification.DEFLAG);
		}
	}
	
	
	/**
	 * Executes the modification specified by the enum mod with the effect that
	 * the square with coordinates x and y is modified in accordance with the protocol
	 * if x and y represent a valid board position, otherwise has no effect
	 * @param x - the x-coordinate of the square to be modified
	 * @param y - the y-coordinate of the square to be modified
	 * @param mod - the type of modification to apply 
	 * @return
	 */
	public BoardState modify(int x, int y, BoardModification mod){
		synchronized (this) {
			BoardState state = new BoardState();
			if (this.withinBounds(x, y)) {
				Square square=this.getSquare(x, y);
				switch(mod){
				case DIG: {
					state.setBombFound(square.hasBomb());
					square.dig();
				}
				case FLAG: {
					square.flag();
				}
				case DEFLAG: {
					square.deflag();
				}
				//default needed?
				}
			}
			state.setBoardString(this.toString());
			return state;
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
