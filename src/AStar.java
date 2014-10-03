import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Vector;

public abstract class AStar {

	static PriorityQueue<State> openList;
	static Hashtable <String, State> closedList;
	static Hashtable <String, State> openTable;
	static Vector<State> stateList;
	
	public static void run(State start, State goal, Comparator<State> heuristic) { 
		
		openList = new PriorityQueue<State>(10, heuristic);
		openList.add(start);
		
		closedList = new Hashtable<String, State>();
		openTable = new Hashtable<String, State>();
			
		State current = null, neighbor = null;
		
		int expanded = 0;
		int maxmoves = 0;
		
		while(true){
		
			current = openList.poll();	// GET THE BEST STATE FROM THE QUEUE
			
			if(current.cost > maxmoves){ 
				
				System.out.print((maxmoves = current.cost) + "\t");
				printTime();
				System.out.println("\t" + expanded + "\t" + openList.size() + "\t");
			}
			
			if(current.equals(goal)){ 
				
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
	
	public static void printTime(){
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.print( sdf.format(cal.getTime()) );
	}
}
