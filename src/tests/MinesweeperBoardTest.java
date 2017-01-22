package tests;

import minesweeper.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.*;

public class MinesweeperBoardTest {
	//TODO: write tests for the Board methods, then write
	// and test all the methods themselves
	@Test
	public void testSquare(){
		//Newly created square should have bombCounts = 0
		//and be in state UNTOUCHED
		
		//Change in bombCounts after
		//Should increase/decrease by 1 each time
		//Should not decrease below zero 
		
		//TODO: test for exception
		
		//Modifications to state
		//{UNTOUCHED, DUG, FLAGGED} x {flag, deflag}
		//{UNTOUCHED, DUG, FLAGGED} x {bomb, no bomb} x {dig}
		//Can just repeat a lot of these tests for Board???
		
		
		Square s = new Square(false);
		assertEquals(s.getBombCounts(),0);
		assertEquals(s.getState(),SquareState.UNTOUCHED);
		assertEquals(s.hasBomb(),false);
		Square sbomb = new Square(true);
		assertEquals(sbomb.hasBomb(),true);
		
		//Adding bombs
		//s.addBombCounts();
		//assertEquals(s.getBombCounts(), 1);
		//s.addBombCounts();
		//assertEquals(s.getBombCounts(), 2);
		
		//Flag untouched, flagged
		s.flag();
		assertEquals(s.getState(), SquareState.FLAGGED);
		s.flag();
		assertEquals(s.getState(), SquareState.FLAGGED);
		
		
		//Dig, flagged, no bomb, bomb
		s.dig();
		assertEquals(s.getState(),SquareState.FLAGGED);
		sbomb.flag();
		sbomb.dig();
		assertEquals(sbomb.getState(),SquareState.FLAGGED);
		
		//Deflag flagged, untouched, no bomb, bomb
		s.deflag();
		assertEquals(s.getState(), SquareState.UNTOUCHED);
		s.deflag();
		assertEquals(s.getState(), SquareState.UNTOUCHED);
		
		sbomb.deflag();
		assertEquals(s.getState(), SquareState.UNTOUCHED);
		sbomb.deflag();
		assertEquals(s.getState(), SquareState.UNTOUCHED);
		
		//Dig, untouched no bomb, dug 
		s.dig();
		assertEquals(s.getState(),SquareState.DUG);
		s.dig();
		assertEquals(s.getState(),SquareState.DUG);
		
		//Dig, untouched, bomb
		sbomb.dig();
		assertEquals(sbomb.getState(),SquareState.DUG);
		sbomb.dig();
		assertEquals(sbomb.getState(),SquareState.DUG);
		assertEquals(sbomb.hasBomb(), false);
		
		//Flag dug
		s.flag();
		assertEquals(s.getState(),SquareState.DUG);
		s.deflag();
		assertEquals(s.getState(),SquareState.DUG);
		
	}
	
	/*public void testNeighbSquares(){
		//If neighbour squares don't satisfy
		//all of the conditions that they 
		//are untouched, have no bomb,
		//and have zero bomb counts, then 
		//they are not dug
		//Partition into squares who satisfy
		//and don't satisfy condition
		//and assume among them the
		//the states of dug, flagged, with bomb,
		//without bomb, with bomb neighbours,
		//without bomb neighbours
		
		//|*1U|*1U|
		//| 2U| 2U|
		//| 0U| 0U|
		//Going left to right
		
		Square s1 = new Square(true);
		
		Square s2 = new Square(true);
		
		Square s3 = new Square(true);
		
		Square s4 = new Square(false);
		
		Square s5 = new Square(false);
		Square s6 = new Square(false);
		
		
		s1.addNeighbour(s2);
		s1.addNeighbour(s3);
		s1.addNeighbour(s4);
		
		s2.addNeighbour(s1);
		s2.addNeighbour(s3);
		s2.addNeighbour(s4);
		
		s4.addNeighbour(s1);
		s4.addNeighbour(s2);
		s4.addNeighbour(s3);
		s4.addNeighbour(s5);
		s4.addNeighbour(s6);
		
		s3.addNeighbour(s1);
		s3.addNeighbour(s2);
		s3.addNeighbour(s4);
		s3.addNeighbour(s5);
		s3.addNeighbour(s6);
		
		s5.addNeighbour(s3);
		s5.addNeighbour(s4);
		s5.addNeighbour(s6);
		
		s6.addNeighbour(s3);
		s6.addNeighbour(s5);
		s6.addNeighbour(s6);
		
		//Test that counts is sum of neighbours
		//with bombs
		assertEquals(s1.getBombCounts(),1);
		assertEquals(s2.getBombCounts(),1);
		assertEquals(s3.getBombCounts(),2);
		assertEquals(s4.getBombCounts(),2);
		assertEquals(s5.getBombCounts(),0);
		assertEquals(s6.getBombCounts(),0);
		
		s6.flag();
		s1.dig();
		
		//| 1D|*0U|
		//| 1U| 1U|
		//| 0U| 0F|
		//Going left to right
		
		//Won't worry about behaviour of initially dug square 
		//as we have already tested that
		
		assertEquals(s2.getBombCounts(),0);
		assertEquals(s2.getState(),SquareState.FLAGGED);
		
		assertEquals(s3.getBombCounts(),1);
		assertEquals(s3.getState(),SquareState.UNTOUCHED);
		
		assertEquals(s4.getBombCounts(),1);
		assertEquals(s4.getState(),SquareState.UNTOUCHED);
		
		assertEquals(s5.getBombCounts(),0);
		assertEquals(s5.getState(),SquareState.UNTOUCHED);
		
		assertEquals(s6.getBombCounts(),0);
		assertEquals(s6.getState(),SquareState.FLAGGED);
		
		//| 1D|*0U|
		//| 1U| 1U|
		//| 0U| 0F|
		
		s2.dig();
		
		
		//| 0D| 0D|
		//| 0D| 0D|
		//| 0D| 0F|
		assertEquals(s1.getBombCounts(),0);
		assertEquals(s1.getState(),SquareState.DUG);
		
		assertEquals(s3.getBombCounts(),0);
		assertEquals(s3.getState(),SquareState.DUG);
		
		assertEquals(s4.getBombCounts(),0);
		assertEquals(s4.getState(),SquareState.DUG);
		
		assertEquals(s5.getBombCounts(),0);
		assertEquals(s5.getState(),SquareState.DUG);
		
		assertEquals(s6.getBombCounts(),0);
		assertEquals(s6.getState(),SquareState.FLAGGED);
		
	}*/
	
	//I think that to test board - since the behaviour of the 
	//squares has already been tested, can just test the string
	//representation each time - having tested the toString
	//method first
	

	@Test
	public void testAddAndIsNeighbour(){
		Square s0 = new Square(false);
		Square s1 = new Square(true);
		assertEquals(s0.isNeighbour(s1),false);
		assertEquals(s1.isNeighbour(s0),false);
		s0.addNeighbour(s1);
		assertEquals(s0.isNeighbour(s1),true);
		assertEquals(s1.isNeighbour(s0),true);
	}
	

	
	@Test
	public void testNeighbourCounts(){
		Square s0 = new Square(true);
		assertEquals(s0.getBombCounts(),0);
		Square s1 = new Square(true);
		s0.addNeighbour(s1);
		assertEquals(s0.getBombCounts(),1);
		assertEquals(s1.getBombCounts(),1);
		Square s2 = new Square(true);
		s1.addNeighbour(s2);
		assertEquals(s1.getBombCounts(),2);
		assertEquals(s2.getBombCounts(),1);
		
		s1.dig();
		assertEquals(s0.getBombCounts(),0);
		assertEquals(s1.getBombCounts(),2);
		assertEquals(s2.getBombCounts(),0);
	}
	
	@Test
	public void testNeighbourState(){
		//Previously we ensured that counts of neighbours
		//decreased if a square had a bomb, now we ensure
		//that state is modified 
		
		//('u', '~b', '~n') -> ('d', '~b', '~n')
		Square s0 = new Square(false);
		Square s1 = new Square(false);
		s0.addNeighbour(s1);
		s0.dig();
		assertEquals(s1.getState(),SquareState.DUG);
		
		//('u', '~b', 'n') -> ('d', '~b', 'n')
		Square s2 = new Square(false);
		Square s3 = new Square(false);
		Square s3n = new Square(true);
		s2.addNeighbour(s3);
		s3.addNeighbour(s3n);
		s2.dig();
		assertEquals(s3.getState(),SquareState.DUG);
		
		//('u', 'b', '~n') -> ('u', 'b', '~n')
		Square s4 = new Square(false);
		Square s5 = new Square(true);
		s4.addNeighbour(s5);
		s4.dig();
		assertEquals(s5.getState(),SquareState.UNTOUCHED);
		
		//('u', 'b', 'n') -> ('u', 'b', 'n')
		Square s6 = new Square(false);
		Square s7 = new Square(true);
		Square s7n = new Square(true);
		s6.addNeighbour(s7);
		s7.addNeighbour(s7n);
		s6.dig();
		assertEquals(s7.getState(),SquareState.UNTOUCHED);
		
		
		//('f', '~b', '~n') -> ('f', '~b', '~n')
		Square s8 = new Square(false);
		Square s9 = new Square(false);
		s8.addNeighbour(s9);
		s9.flag();
		s8.dig();
		assertEquals(s9.getState(),SquareState.FLAGGED);
		//Now since we have a pair 
		//('d', '~b', '~n'),('f', '~b', '~n')
		//can deflag s9 and dig it and test 
		//('d', '~b', '~n') -> ('d', '~b', '~n')
		//for s8
		s9.deflag();
		s9.dig();
		assertEquals(s8.getState(),SquareState.DUG);
		
		//('f', '~b', 'n') -> ('f','~b','n')
		Square s10 = new Square(false);
		Square s11 = new Square(false);
		Square s11n = new Square(true);
		s10.addNeighbour(s11);
		s11.addNeighbour(s11n);
		s11.flag();
		s10.dig();
		assertEquals(s11.getState(),SquareState.FLAGGED);

		
		//('f', 'b', '~n') -> ('f', 'b', '~n') 
		Square s12 = new Square(false);
		Square s13 = new Square(true);
		s12.addNeighbour(s13);
		s13.flag();
		assertEquals(s13.getState(),SquareState.FLAGGED);
		//Now we have
		//('d', '~b', 'n'),('f', 'b', '~n')
		//so can unflag s13 and dig it and test 
		//('d', '~b', 'n') -> ('d', '~b', 'n')
		//for s12
		s13.deflag();
		s13.dig();
		assertEquals(s12.getState(),SquareState.DUG);
		
		//('f', '~b', '~n') -> ('f', '~b', '~n') 
		Square s14 = new Square(false);
		Square s15 = new Square(false);
		s14.addNeighbour(s15);
		s15.flag();
		s14.dig();
		assertEquals(s15.getState(),SquareState.FLAGGED);

	}
		
	@Test
	public void TestBoard(){
		//Basically test the configurations as above
		//but calling the functions from the board class
		//Three versions of constructor
		//Call the ones where dimensions specified with x = y, x < y, x > y
		
		Board b1 = new Board();
		assertEquals(10,b1.getWidth());
		assertEquals(10,b1.getLength());
		
		Board b2 = new Board(7,7);
		assertEquals(7,b2.getWidth());
		assertEquals(7,b2.getLength());
		
		Board b3 = new Board(9,7);
		assertEquals(9,b3.getWidth());
		assertEquals(7,b3.getLength());
		
		Board b4 = new Board(7,9);
		assertEquals(7,b4.getWidth());
		assertEquals(9,b4.getLength());
		
		Boolean[] bombs = {true,false,false,false,
				   false,true,false,false,
				   false,false,true,false,
				   false,false,false,true,};
		List<Boolean> bombStatuses = Arrays.asList(bombs);

 		Board b5 = new Board(4,4,bombStatuses);
 		assertEquals(4,b5.getWidth());
 		assertEquals(4,b5.getLength());
 		
 		Board b6 = new Board(2,8,bombStatuses);
 		assertEquals(2,b6.getWidth());
 		assertEquals(8,b6.getLength());
 		
 		Board b7 = new Board(8,2,bombStatuses);
 		assertEquals(8,b7.getWidth());
 		assertEquals(2,b7.getLength());
	}
	
	@Test
	public void TestBoardState(){
		//Default  bombFound is false
		//setBombFound can only make it true
		
		BoardState state = new BoardState();
		assertEquals(false,state.getBombFound());
		state.setBombFound();
		assertEquals(true,state.getBombFound());
		state.setBombFound();
		assertEquals(true,state.getBombFound());
		
		
		
	}
	
}
