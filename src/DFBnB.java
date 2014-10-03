import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;

public abstract class DFBnB {

	static PriorityQueue<State> openList;
	static Hashtable <String, State> closedList;
	static Hashtable <String, State> openTable;
	
	public static void/*long*/ run(State start, State goal, Comparator<State> heuristic, long startTime){
		
		State optimalSolution = null;
		long optimalTime = 0;
		openList = new PriorityQueue<State>(10, heuristic);
		closedList = new Hashtable<String, State>();
		
		int bestScore = Integer.MAX_VALUE;
		int tempScore = 0;
		
		openList.add(start);			// ADD IT TO THE QUEUE
		
		State current = null, neighbor = null;
		
		while(!openList.isEmpty()){
			
			current = openList.remove();
			if(current.equals(goal)){
				
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
	
}
