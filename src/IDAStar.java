import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;

public abstract class IDAStar {

	static PriorityQueue<State> openList;
	
	public static void run(State start, State goal, Comparator<State> heuristic){
		
		openList = new PriorityQueue<State>(10, heuristic);
		
		State current = null, neighbor = null;
		
		openList.add(start);
		
		int f = start.manhattan + start.cost;
		
		while(true){
			
			current = openList.remove();	// GET THE BEST STATE FROM THE QUEUE
						
			if(current.equals(goal)){ 
				
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
	
}
