package minesweeper;
import java.util.*;
public class Square {
	private SquareState state = SquareState.UNTOUCHED;
	private boolean bomb;
	private int bombCounts = 0;
	private Set<Square> neighbours = new HashSet<>();
	
	
	//Rep invariant:
	// bomb must be false if state is DUG
	// bombCounts must be >= 0
	//Abstraction function:
	// represents a square on a minesweeper board
	//Safety from rep exposure
	// All fields are private
	// fields are necessarily mutable but only via the object's
	// mutator methods 
	// state is an Enum so can only be UNTOUCHED, DUG or FLAGGED
	
	//TODO: possibly add co-ordinates - so that a neighbour has the appropriate
	//co-ordinates 
	//Then you could have a better RI for board whereby you determine the 
	//number of neighbours of each square based on its position 
	//The Square itself ensures that the neighbours are in the right positions
	
	public Square(boolean bomb){
		this.bomb = bomb;
	}
	
	private void checkRep(){
		assert ((this.state==SquareState.DUG) & this.bomb) == false;
		assert this.bombCounts >= 0;
	}
	
	/**
	 * 
	 * @param square - a square which is not identical to this one
	 */
	public void addNeighbour(Square square){
		//TODO:add assertion to ensure that same square is not added
		this.neighbours.add(square);
		if(square.hasBomb()){
			this.addBombCounts();
		}
		if(!square.isNeighbour(this)){
			square.addNeighbour(this);
		}
		checkRep();
	}
	
	/**
	 * Increments the bombCounts variable
	 */
	private void addBombCounts(){
		this.bombCounts++;
		checkRep();
	}

	/**
	 * Returns the the number of bombs in the neighbouring 
	 * squares
	 * @return int which is the number of bombs
	 * in the neighbouring squares
	 */
	public int getBombCounts(){
		return this.bombCounts;
	}
	
	/**
	 * Returns the state of the square 
	 * @return SquareState representing the 
	 * present state of the square
	 */
	public SquareState getState(){
		return this.state;
	}
	
	/**
	 * Decrements the bombCounts variable if it is > 0,
	 * otherwise raises an exception 
	 */
	
	//TODO: add exception 
	private void subtractBombCounts(){
		this.bombCounts--;
		checkRep();
	}
	
	/**
	 * 
	 * @return true if the square represents a bomb square
	 */
	public boolean hasBomb(){
		return this.bomb;
	}
	
	/**
	 * Modifies the state of the square
	 * @param state to which the Square's state should be
	 * modified
	 */
	private void setState(SquareState state){
		this.state = state;
		checkRep();
	}
	
	
	/**
	 * Modifies the square according to the protocol, with the effect
	 * that its state becomes DUG if it was UNTOUCHED 
	 * and if a bomb is present, it is deleted 
	 */
	public void dig(){
		if(this.getState() == SquareState.UNTOUCHED){
			if(this.hasBomb()){
				this.bomb = false;
				for(Square neighbour : this.neighbours){
					neighbour.subtractBombCounts();
				}
			}
			this.setState(SquareState.DUG);
			if(this.getBombCounts() == 0){
				for(Square neighbour: this.neighbours){
					neighbour.dig();
				}
			}
		}
		checkRep();
	}
	
	/**
	 * Modifies the square according to the minesweeper protocol, with the effect
	 * that its state becomes FLAGGED if it was UNTOUCHED 
	 */
	public void flag(){
		if(this.getState() == SquareState.UNTOUCHED){
			this.setState(SquareState.FLAGGED);
		}
		checkRep();
	}
	
	/**
	 * Modifies the square according to the minesweeper protocol, with the effect
	 * that its state becomes UNTOUCHED if it was FLAGGED
	 */
	public void deflag(){
		if(this.getState() == SquareState.FLAGGED){
			this.setState(SquareState.UNTOUCHED);
		}
		checkRep();
	}
	
	/**
	 * Indicates whether another Square is a neighbour of this one
	 * @param square - a Square object for which the function
	 * determines whether or not it is a neighbour of this 
	 * @return true if it the other Square is a neighbour, otherwise false
	 */
	public boolean isNeighbour(Square square){
		return this.neighbours.contains(square);
	}
	
	@Override
	public String toString(){
		return "";
	}
	
/*	@Override
	*//**
	 * @param other - object for which to determine equality 
	 * @return true if object is a Square and all the fields are the same, else false 
	 *//*
	public boolean equals(Object other){
		if(other instanceof Square){
			Square otherSquare = (Square)other;
			if(otherSquare.getState() != this.state){return false;}
			if(otherSquare.hasBomb() != this.bomb){return false;}
			if(otherSquare.getBombCounts() != this.bombCounts){return false;}
			if(!otherSquare.neighbours.equals(this.neighbours)){return false;}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		int result = 37;
		return result;
	}*/
	
}
