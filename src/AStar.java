import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;

public abstract class AStar {

	static PriorityQueue<State> openQueue;
	static Hashtable <String, State> closedList;
	static Hashtable <String, State> openList;
	
	public static void run(State start, State goal, Comparator<State> heuristic) { 
		
		openQueue = new PriorityQueue<State>(10, heuristic);			
		closedList = new Hashtable<String, State>();
		openList = new Hashtable<String, State>();	
		State current = null, neighbor = null;		
		int expanded = 0, maxmoves = 0;
		
		openQueue.add(start);
		while(true){
		
			addToClosed(current = openQueue.poll());// GET BEST STATE FROM QUEUE
			expanded++;								// COUNT IT AS EXPANDED
			
			if(current.cost > maxmoves)	// FOR EVERY NEW DEPTH PRINT AN UPDATE
				printUpdate(maxmoves = current.cost, expanded, current);		
			
			if(current.equals(goal)){ 				// IF A GOAL IS FOUND
				System.out.println("SUCCESS!!");	// PRINT THE MOVES			
				current.printMoves(); break;		// AND BOARD CONFIGURATION					
			}
			
			/*	FOR EACH AVAILABLE MOVE IN THE CURRENT STATE	*/
			for(int move=0; move<current.availableMoves; move++){
				
				/*	CREATE A NEW STATE BASED ON THAT MOVE	*/
				neighbor = new State(current, current.moveableCoords[move]);
					
				/*	IF THERE'S A BETTER DUPLICATE IN THE CLOSED  LIST 	*/
				if(closedList.containsKey(neighbor.key) 
					&& neighbor.cost < closedList.get(neighbor.key).cost){	
					
						takeFromClosed(neighbor);	// MOVE THE NEIGHBOR FROM 
						addToOpen(neighbor);		// CLOSED TO OPEN LIST
				}		//	NOTE: THIS REPLACES THE OLD NEIGHBOR WITH THE NEW 					
				
				/*	IF THERE'S A BETTER DUPLICATE IN THE OPEN LIST	*/
				else if(openList.containsKey(neighbor.key)
					&& neighbor.cost < openList.get(neighbor.key).cost){
					
						takeFromOpen(neighbor);		// REPLACE THE OLD NEIGHBOR			
						addToOpen(neighbor);		// WITH ITS CHEAPER TWIN
				}
				
				else  	addToOpen(neighbor);		// ELSE SAVE IT FOR LATER
			}										// IN ORDER OF ITS SCORE
		}		
	}
	
	/**************************** PRIVATE METHODS *****************************/
	
	private static void addToOpen(State toAdd){
		
		openQueue.add(toAdd);	
		openList.put(toAdd.key, toAdd);
	}
	
	private static void takeFromOpen(State toTake){
		
		openList.remove(toTake.key);
		openQueue.remove(toTake);
	}
	
	private static void addToClosed(State toAdd){
		
		closedList.put(toAdd.key, toAdd);
	}
	
	private static void takeFromClosed(State toTake){
		
		closedList.remove(toTake.key);
	}
	
	private static void printTime(){
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.print( sdf.format(cal.getTime()) );
	}
	
	private static void printUpdate(int maxmoves, int expanded, State current){
		
		if(maxmoves == 1) System.out.println("Depth\tTime\t\tExpanded Nodes");
		System.out.print((maxmoves = current.cost) + "\t\t");
		printTime();
		System.out.println("\t" + expanded + "\t");
	}
}
