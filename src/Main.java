import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Vector;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {
	
	static final String GOAL = 	"123804765";
	static final String TEST = 	"724506831";
	static final String EASY = 	"134862705";
	static final String MEDIUM = "281043765";
	static final String HARD = 	"281463075";
	static final String WORST = "567408321";
	
	static final int[][] ADJACENCE = new int[][]{
			
		  { 0, 1, 0, 1, 0, 0, 0, 0, 0 },
		  { 1, 0, 1, 0, 1, 0, 0, 0, 0 },
		  { 0, 1, 0, 0, 0, 1, 0, 0, 0 },
		  { 1, 0, 0, 0, 1, 0, 1, 0, 0 },
		  { 0, 1, 0, 1, 0, 1, 0, 1, 0 },
		  { 0, 0, 1, 0, 1, 0, 0, 0, 1 },
		  { 0, 0, 0, 1, 0, 0, 0, 1, 0 },
		  { 0, 0, 0, 0, 1, 0, 1, 0, 1 },
		  { 0, 0, 0, 0, 0, 1, 0, 1, 0 }};
	
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
	
	public static void main(String[] args) {
			
		initialize();
		
		//A_Star(misplaced);
		A_Star(manhattan);
		//dF_BnB();
	}

	private static void A_Star(Comparator<State> heuristic) { 
		
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
				
				printMoves(current); break;	
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

	
	public static void dF_BnB(){
	
		openList = new PriorityQueue<State>(10, manhattan);
		closedList = new Hashtable<String, State>();
		
		State start = new State(HARD);	// CREATE FIRST STATE
		int bestScore = Integer.MAX_VALUE;
		int tempScore = 0;
		
		openList.add(start);			// ADD IT TO THE QUEUE
		
		State current = null, neighbor = null;
		
		while(!openList.isEmpty()){
			
			current = openList.remove();
			if(current.equals(goalState)){
				
				tempScore = current.cost;
				if(tempScore < bestScore) bestScore = tempScore;

				printMoves(current); break;
			}
			
			else {
				
				for(int move=0; move<current.availableMoves; move++){
					
					neighbor = new State(current, current.moveableCoords[move]);
					
					if(closedList.contains(neighbor)) continue;
					if(neighbor.manhattan() > bestScore){
						
						closedList.put(neighbor.key, neighbor);
						continue;
					}
					
					openList.add(neighbor);
				}
			}		
		}
		
		System.out.println("DONE");
	}
	
	public static void idA_Star(){
		
		
	}
	
	public static void initialize(){
		
		stateList = new Vector<State>(0);
		misplaced = new MisplacedComparator();
		manhattan = new ManhattanComparator();	
	}
	
	public static void printMoves(State current){
		
		stateList.add(current);
		while(current.parent != null){
			
			current = current.parent;
			stateList.add(current);		
		}
		
		for(int i=stateList.size()-1; i>=0; i--){
			
			stateList.elementAt(i).print();
		}
	}
	
	public static void printTime(){
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.print( sdf.format(cal.getTime()) );
	}
}
