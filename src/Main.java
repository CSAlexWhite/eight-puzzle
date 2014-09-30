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
	static final String HARDER = "231463705";
	static final String WORST = "567408123";
	
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
		
		//test();
		
		//A_Star(misplaced);
		A_Star(manhattan);
		//dF_BnB();
	}
	
	public static void test(){
		
		openList = new PriorityQueue<State>(10, manhattan);
		openList.add(new State(EASY));
		openList.add(new State(MEDIUM));
		openList.add(new State(HARD));
		openList.add(new State(WORST));
		openList.add(new State(GOAL));
		
		State hello = new State(EASY);
		State goodbye = new State(EASY);
		State whatsup = new State(HARD);
		
		if(hello.equals(whatsup)) System.out.println("OKAY");
		else System.out.println("FUCKED");
		
		if(openList.contains(GOAL)) System.out.println("CONTAINED");
		if(openList.remove(GOAL)) System.out.println("REMOVED");
		
		while(!openList.isEmpty()) openList.remove().print();
	}

	private static void A_Star(Comparator<State> heuristic) { 
		
		boolean success = false;
		int iterations = 0;
		openList = new PriorityQueue<State>(10, heuristic);
		openList.add(new State(WORST));
		
		closedList = new Hashtable<String, State>();
		openTable = new Hashtable<String, State>();
			
		State current = null, neighbor = null;
		
		int expanded = 0;
		int maxmoves = 0;
		while(!success){
		
			current = openList.poll();		// GET THE BEST STATE FROM THE QUEUE
			
			if(current.cost > maxmoves){ 
				printTime();
				System.out.println("\t" + (maxmoves = current.cost) + "\t" + expanded + "\t");
				System.out.println(openList.size());
			}
			
			if(current.equals(goalState)){ 
				
				success = true; 
				System.out.println("SUCCESS!!");
				
				printMoves(current); break;	
			}
			
			openList.size();
			
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
						&& neighbor.cost < openTable.get(neighbor.key).cost){ // TODO get the cost of the neighbor that's already in the queue
					System.out.println("SHOULD REMOVE");
					if(openList.remove(neighbor)) System.out.println("REMOVED");					
					openTable.remove(neighbor.key);					
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
	
	private static void initialize(){
		
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
}
