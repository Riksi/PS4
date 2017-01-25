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
