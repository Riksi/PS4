package minesweeper;

public class BoardState {
	private boolean bombFound = false;
	private String boardString;
	
	public void setBombFound(){
		bombFound = true;
	}
	
	public boolean getBombFound(){
		return bombFound;
	}
	
	public void setBoardString(String boardString){
		this.boardString = boardString;
	}
	
	public String getBoardString(){
		return boardString;
	}
}
