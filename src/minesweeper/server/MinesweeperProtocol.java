package minesweeper.server;

import minesweeper.*;

public class MinesweeperProtocol {

	private final Board board;
	private final boolean debug;
	
	/**
	 * processes the client input
	 * 
	 * make requested mutations on game state if applicable, 
	 * in accordance with the protocol
	 * then return appropriate message
	 * 
	 * @param input
	 * @return
	 */
	
	public MinesweeperProtocol(Board board, boolean debug){
		this.board = board;
		this.debug = debug;
	}
	
	public String processInput(String input) {

		String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|(deflag \\d+ \\d+)|(help)|(bye)";
		if(!input.matches(regex)) {
			//invalid input
			return null;
		}
		String[] tokens = input.split(" ");
		if(tokens[0].equals("look")) {
			// 'look' request
			//TODO Question 5
		} else if(tokens[0].equals("help")) {
			// 'help' request
			//TODO Question 5
		} else if(tokens[0].equals("bye")) {
			// 'bye' request
			//TODO Question 5
		} else {
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			if(tokens[0].equals("dig")) {
				// 'dig x y' request
				//TODO Question 5
			} else if(tokens[0].equals("flag")) {
				// 'flag x y' request
				//TODO Question 5
			} else if(tokens[0].equals("deflag")) {
				// 'deflag x y' request
				//TODO Question 5
			}
		}
		//should never get here
		return "";
	}
}
