package minesweeper.server;
import java.util.*;
import java.io.*;

public class BoardReader{
	
	private final BufferedReader in;
	private int colms;
	private int rows;
	private List<Boolean> bombs = new ArrayList<>();
	
	public BoardReader(BufferedReader in){
		this.in = in;
	}
	
	
	public void read() throws IOException {
		readDims();
		readRows();
	}
	
	private void readDims() throws IOException{
    	String firstLine = in.readLine();
    	String firstLineRegex = "\\d+ \\d+";
    	if(!(firstLine.matches(firstLineRegex))){
    		throw new RuntimeException();
    	}
		Scanner firstLineScanner =  new Scanner(firstLine);
		colms = firstLineScanner.nextInt();
		rows =  firstLineScanner.nextInt();
		firstLineScanner.close();
		if(!(colms >0 & rows >0)){
			throw new RuntimeException();
		}
		
	}
	
	private void readRows() throws IOException{
		String rowLine;
		String rowLineRegex = String.format("\\((0|1) ){%i}(0|1)",colms-1);
		
		while((rowLine = in.readLine()) != null){
			if(!(rowLine.matches(rowLineRegex))){
				throw new RuntimeException();
			}
			Scanner rowScanner = new Scanner(rowLine);
			while(rowScanner.hasNext()){
				bombs.add(rowScanner.nextInt() > 0);
			}
			rowScanner.close();
		}
		
		if(bombs.size() != (colms*rows)){
			throw new RuntimeException();
		}
	}
	
	public int getColms(){
		return colms;
	}
	
	public int getRows(){
		return rows;
	}
	
	public List<Boolean> getBombs(){
		return new ArrayList<>(bombs);
	}
	
	
}
