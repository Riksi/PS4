package minesweeper.server;

import java.net.*;
import java.io.*;
import minesweeper.*;

public class MinesweeperServerThread implements Runnable{
	private Socket socket; 
	private final Board board;
	private final boolean debug;
	
	public MinesweeperServerThread(Socket socket,Board board, boolean debug){
		this.socket = socket;
		this.board = board;
		this.debug = debug;
	}
	
	/**
	 * handler for client input
	 * 
	 * make requested mutations on game state if applicable, then return appropriate message
	 * 
	 * @param input
	 * @return
	 */
	private static String handleRequest(String input) {

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
	
	
	/**
	 * Handles connection from a single client by reading from input stream in
	 * and writing to the output stream out 
	 * @param out output stream which should be open
	 * @param in input stream  which should be open
	 * @throws IOException
	 */
    public void handleConnection(PrintWriter out, BufferedReader in) throws IOException {
        // Method has been designed so that it can be tested without using a real socket 
        try {
        	for (String line = in.readLine(); line != null; line = in.readLine()) {
        		String output = handleRequest(line);
        		if(output != null) {
        			out.println(output);
        		}
        	}
        } catch (IOException e){
			e.printStackTrace();
		}
    }
	
	
	public void run(){
		//The handleConnection part of the MinesweeperServer should appear here 
		try(
				PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(this.socket.getInputStream()));
				)
		{
			this.handleConnection(out, in);
			
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

}
