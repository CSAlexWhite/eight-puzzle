import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Vector;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {
	
	static final String GOAL = 	"123804765";
	//static final String GOAL = 	"012345678";
	static final String TEST = 	"724506381";
	static final String EASY = 	"134862705";
	static final String MEDIUM = "281043765";
	static final String HARD = 	"281463075";
	static final String WORST = "567408321";
	
	static final String[] MOVES = new String[]
		{ "", "right", "down", "left", "up" };
	
	static final int[][] ADJACENCE = new int[][]{
			
		{ 0, 1, 0, 2, 0, 0, 0, 0, 0 },
		{ 3, 0, 1, 0, 2, 0, 0, 0, 0 },
		{ 0, 3, 0, 0, 0, 2, 0, 0, 0 },
		{ 4, 0, 0, 0, 1, 0, 2, 0, 0 },
		{ 0, 4, 0, 3, 0, 1, 0, 2, 0 },
		{ 0, 0, 4, 0, 3, 0, 0, 0, 2 },
		{ 0, 0, 0, 4, 0, 0, 0, 1, 0 },
		{ 0, 0, 0, 0, 4, 0, 3, 0, 1 },
		{ 0, 0, 0, 0, 0, 4, 0, 3, 0 }};
		
	static final int[][] STEPS_AWAY = new int[][]{
		
		{ 0, 1, 2, 1, 2, 3, 2, 3, 4 },
		{ 1, 0, 1, 2, 1, 2, 3, 2, 3 },
		{ 2, 1, 0, 3, 2, 1, 4, 3, 2 },
		{ 1, 2, 3, 0, 1, 2, 1, 2, 3 },
		{ 2, 1, 2, 1, 0, 1, 2, 1, 2 },
		{ 3, 2, 1, 2, 1, 0, 1, 2, 1 },
		{ 2, 3, 4, 1, 2, 1, 0, 1, 2 },
		{ 3, 2, 3, 2, 1, 2, 1, 0, 1 },
		{ 4, 3, 2, 3, 2, 1, 2, 1, 0 }};
	
	static State goalState = new State(GOAL);
	
	static PriorityQueue<State> openList;
	static Hashtable <String, State> closedList;
	static Hashtable <String, State> openTable;
	static Vector<State> stateList;
	
	static MisplacedComparator misplaced;
	static ManhattanComparator manhattan;	
	
	static long startTime, endTime;
	
	public static void main(String[] args) {
			
		initialize();
		startTime = System.currentTimeMillis();
		
		AStar.run(new State(HARD), new State(GOAL), manhattan);
		
		//A_Star(misplaced);
		//A_Star(manhattan);		
		//dF_BnB(startTime);		
		//idA_Star();
		
		endTime = System.currentTimeMillis();
		System.out.println("Computation Time = " + (endTime - startTime) + "ms");
	}

	public static void A_Star(Comparator<State> heuristic) { 
		
		Boolean success = false;
		int iterations = 0;
		openList = new PriorityQueue<State>(10, heuristic);
		openList.add(new State(WORST));
		
		closedList = new Hashtable<String, State>();
		openTable = new Hashtable<String, State>();
			
		State current = null, neighbor = null;
		
		int expanded = 0;
		int maxmoves = 0;
		
		while(!success){
		
			current = openList.poll();	// GET THE BEST STATE FROM THE QUEUE
			
			if(current.cost > maxmoves){ 
				
				System.out.print((maxmoves = current.cost) + "\t");
				printTime();
				System.out.println("\t" + expanded + "\t" + openList.size() + "\t");
			}
			
			if(current.equals(goalState)){ 
				
				success = true; 
				System.out.println("SUCCESS!!");
				
				current.printMoves(); break;	
			}
										
			closedList.put(current.key, current);
			expanded++;
			for(int move=0; move<current.availableMoves; move++){
				
				neighbor = new State(current, current.moveableCoords[move]);
				
				if(closedList.containsKey(neighbor.key) 
						&& neighbor.cost < closedList.get(neighbor.key).cost){		// IF WE'VE ALREADY SEEN THIS STATE
																									// AND IT'S PATH IS COSTLIER THAN THAT
					openList.add(neighbor);	
					openTable.put(neighbor.key, neighbor);
														// OF THE CURRENT NODE, OPEN UP THE NEIGHBOR
					closedList.remove(neighbor.key);	// AND TAKE IT OFF THE CLOSED LIST						
				}
				
				else if(openTable.containsKey(neighbor.key) 
						&& neighbor.cost < openTable.get(neighbor.key).cost){
					
					openTable.remove(neighbor.key);
					openList.remove(neighbor);
					
					openList.add(neighbor);
				}
				
				else { 
					
					openList.add(neighbor); 
					openTable.put(neighbor.key, neighbor);
				}
			}			
		}		
	}

	
	public static void/*long*/ dF_BnB(long startTime){
	
		State optimalSolution = null;
		long optimalTime = 0;
		openList = new PriorityQueue<State>(10, manhattan);
		closedList = new Hashtable<String, State>();
		
		State start = new State(WORST);	// CREATE FIRST STATE
		int bestScore = Integer.MAX_VALUE;
		int tempScore = 0;
		
		openList.add(start);			// ADD IT TO THE QUEUE
		
		State current = null, neighbor = null;
		
		while(!openList.isEmpty()){
			
			current = openList.remove();
			if(current.equals(goalState)){
				
				long endTime = System.currentTimeMillis();
				tempScore = current.cost;
				if(tempScore < bestScore) bestScore = tempScore;
				System.out.println("Solution Time: " + (endTime - startTime) + "ms\tMoves: " + current.cost);
				optimalSolution = current; 
				optimalTime = endTime - startTime;//break;
			}
			
			else {
				
				for(int move=0; move<current.availableMoves; move++){
					
					neighbor = new State(current, current.moveableCoords[move]);
					
					if(closedList.containsKey(neighbor.key)) continue;
					if((neighbor.cost + neighbor.manhattan) > bestScore){
						closedList.put(neighbor.key, neighbor);
						continue;
					}
					
					openList.add(neighbor);
				}
			}
		}
		
		System.out.println("Optimal Time: " + (optimalTime) + "ms");
		optimalSolution.printMoves();	
	}
	
	public static void idA_Star(){
		
		openList = new PriorityQueue<State>(10, misplaced);
		
		State start = new State(WORST), current = null, neighbor = null;
		
		openList.add(start);
		
		int f = start.manhattan + start.cost;
		
		while(true){
			
			current = openList.remove();	// GET THE BEST STATE FROM THE QUEUE
						
			if(current.equals(goalState)){ 
				
				System.out.println("SUCCESS!!");				
				current.printMoves(); break;	
			}
		
			// BRANCH AND BOUND
			for(int move=0; move<current.availableMoves; move++){
				
				neighbor = new State(current, current.moveableCoords[move]);
				openList.add(neighbor);
				if((neighbor.cost + neighbor.manhattan) > f){

					continue;
				}
				
				if((neighbor.cost + neighbor.manhattan) < f)
					f = (neighbor.cost + neighbor.manhattan);												
			}
		}	
		
	}
	
	public static void initialize(){
		
		stateList = new Vector<State>(0);
		misplaced = new MisplacedComparator();
		manhattan = new ManhattanComparator();	
	}
	
	public static void printTime(){
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.print( sdf.format(cal.getTime()) );
	}
}
