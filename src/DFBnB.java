import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.PriorityQueue;

public abstract class DFBnB {

	static PriorityQueue<State> openQueue;
	static Hashtable <String, State> closedList;
	
	public static void run(State start, State goal, Comparator<State> heuristic){
		
		openQueue = new PriorityQueue<State>(10, heuristic);
		closedList = new Hashtable<String, State>();
		State optimalSolution = null, current = null, neighbor = null;
		int expanded = 0, tempScore = 0, bestScore = Integer.MAX_VALUE;
		long startTime = System.currentTimeMillis(), optimalTime = 0;
			
		openQueue.add(start);			
		while(!openQueue.isEmpty()){
			
			current = openQueue.poll();
			expanded++;
						
			if(current.equals(goal)){
				
				long endTime = System.currentTimeMillis();
				tempScore = current.cost;
				if(tempScore < bestScore) bestScore = tempScore;
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
					
					openQueue.add(neighbor);
				}
			}
		}
		
		System.out.println("Expanded: " + expanded);
		System.out.println("Optimal Time: " + (optimalTime) + "ms");
		System.out.println("Total Time: " + (System.currentTimeMillis() - startTime) + "ms\n");
		optimalSolution.printMoves();	
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
