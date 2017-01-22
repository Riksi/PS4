package minesweeper.server;

import java.net.*;
import java.io.*;
import minesweeper.*;
import java.util.*;

//TODO: Abstract complicated methods into separate classes
//BoardBuilder
//MinesweeperProtocol - for handleRequest
//Try to wrap up soon

public class MinesweeperServer {

	private final static int PORT = 4444;
	private ServerSocket serverSocket;
	private final Board board;
	private final boolean debug;

    /**
     * Make a MinesweeperServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535.
     */
    public MinesweeperServer(int port,Board board,boolean debug) throws IOException {
        serverSocket = new ServerSocket(port);
        this.board = board;
        this.debug = debug;
    }
    
    /**
     * Run the server, listening for client connections, concurrently creating 
     * a thread to handle each one  
     * Never returns unless an exception is thrown.
     * @throws IOException if the main server socket is broken
     * (IOExceptions from individual clients do *not* terminate serve()).
     */
    public void serve() throws IOException {
        while (true) {
            
            
            try {
            	// the call to accept inside will block until a client connects
            	// then a new thread will be created to handle the client 
            	new Thread(new MinesweeperServerThread(this.serverSocket.accept(),
            			this.board, debug)).start();
            }
            
            catch (IOException e) {
                e.printStackTrace(); // but don't terminate serve()
            }
        }
    }
    
    
    public static int[] readBoardDims(BufferedReader in) throws IOException{
    	String firstLine = in.readLine();
    	String firstLineRegex = "\\d+ \\d+";
    	if(!(firstLine.matches(firstLineRegex))){
    		throw new RuntimeException();
    	}
		Scanner firstLineScanner =  new Scanner(firstLine);
		int colms = firstLineScanner.nextInt();
		int rows =  firstLineScanner.nextInt();
		firstLineScanner.close();
		if(!(colms >0 & rows >0)){
			throw new RuntimeException();
		}
		
		int[] dims = {colms,rows}; 
		
		return dims;
    }
    
    public static List<Boolean> readBoardRows(BufferedReader in, int colms, int rows) throws IOException{
    	List<Boolean> bombStatuses = new ArrayList<>();
		String rowLine;
		String rowLineRegex = String.format("\\((0|1) ){%i}(0|1)",colms-1);
		
		while((rowLine = in.readLine()) != null){
			if(!(rowLine.matches(rowLineRegex))){
				throw new RuntimeException();
			}
			Scanner rowScanner = new Scanner(rowLine);
			while(rowScanner.hasNext()){
				bombStatuses.add(rowScanner.nextInt() > 0);
			}
			rowScanner.close();
		}
		
		if(bombStatuses.size() != (colms*rows)){
			throw new RuntimeException();
		}
		
		return bombStatuses;
    }
    

    
    /**
     * Start a MinesweeperServer running on the default port.
     */
    public static void main(String[] args) {
        //try {
        //    MinesweeperServer server = new MinesweeperServer(PORT);
        //    server.serve();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    	
    	int i = 0;
    	String arg;
    	boolean debug = false;
    	int port = PORT;
    	boolean boardFromDims = false;
    	boolean boardFromFile = false;
    	String boardfile = "";
    	Board board = new Board();
    	
    	//TODO: 
    	//[]Give threads access to board 
    	
    	while(i < args.length && args[i].startsWith("--")){
    		arg = args[i++];
    		//--debug
        	if (arg.equals("debug")){
        		System.out.println("Debug mode on");
        		debug = true;
        	}
        	//--port
        	else if(arg.equals("port")){
        		port = Integer.parseInt(args[i++]);
        		if((port < 0)||(port > 65535)){
        			System.err.println("Port number must be an integer between 0 and 65535");
        			break;
        		}
        		
        	}
        	//--size
        	else if(arg.equals("size")){
        		//Actually need to look for regex INT,INT
        		//and split 
        		if(!boardFromFile){
	        		if(i<args.length){
		        		String dimsRegex = "\\d+,\\d+";
		        		String dims = args[i++];
		        		if (dims.matches(dimsRegex)){
		        			Scanner dimScanner = new Scanner(dims);
		        			dimScanner.useDelimiter(",");
		        			int x = dimScanner.nextInt();
		        			int y = dimScanner.nextInt();
		        			dimScanner.close();
		        			if((x <= 0)|(y<= 0)){ System.err.println("Board dimensions must be positive integers");}
		        			else{
		        				boardFromDims = true;
		        				board = new Board(x,y);
		        				System.out.println(String.format("Intialised {%i} x {%i} board",x,y));
		        			}
		        		}
		        		else{
		        			System.err.println("Usage: MinesweeperServer [--debug | --no-debug] [--port PORT] "
		        					+ "[--size SIZE_X,SIZE_Y | --file FILE]");
		        			break;
		        		}
	        		}
	        		else{
	        			System.err.println("--size requires x and y dimensions");
	        			break;
	        		}
        		}
        		else{
        			System.err.println("--file and --size may not be specified simultaneously");
        			break;
        		}
        	}
        	//--file
        	else if(arg.equals("file")){
        		if(!boardFromDims){
	        		if(i<args.length){
	        			boardfile = args[i++];
	        			boardFromFile = true;
	        			try(BufferedReader in = new BufferedReader(new FileReader(boardfile));){
	        				BoardReader boardReader = new BoardReader(in);
	        				boardReader.read();
	        				int x = boardReader.getColms();
	        				int y = boardReader.getRows();
	        				board = new Board(x,y,boardReader.getBombs());
	        				System.out.println(
	        						String.format("Intialising a {%i} x {%i} board from file: {%s}...",
	        								board.getWidth(),board.getLength(),boardfile));

	        			}
	        	    	catch (IOException ioe){
	        	    		ioe.printStackTrace();
	        	    		System.err.println(String.format("There was an error reading from file: {%s}",boardfile));
	        	    		break;
	        	    	}
	        		}
	        		else{
	        			System.err.println("--file requires a filename");
	        			break;
	        		}
        		}
        		else{
        			System.err.println("--file and --size may not be specified simultaneously");
        			break;
        		}
        	}
        	
        	else{
    			System.err.println("Usage: MinesweeperServer [--debug | --no-debug] [--port PORT] "
    					+ "[--size SIZE_X,SIZE_Y | --file FILE]");
    			break;
    			
        	}
        	
        	if(i==args.length){
        		try
        		{
        			MinesweeperServer server = new MinesweeperServer(port,board,debug);
        			server.serve();
        		}
        		catch(IOException ioe){
        			ioe.printStackTrace();
        		}
        	}

    	}
    	

    	
    }

}