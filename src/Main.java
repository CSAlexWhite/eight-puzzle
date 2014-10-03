import java.util.Hashtable;
import java.util.PriorityQueue;
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
	
	static State goalState = new State(GOAL, true);	
	
	static PriorityQueue<State> openList;
	static Hashtable <String, State> closedList;
	static Hashtable <String, State> openTable;
	
	static MisplacedComparator misplaced;
	static ManhattanComparator manhattan;	
	
	static long startTime, endTime;
	
	public static void main(String[] args) {
			
		initialize();
		startTime = System.currentTimeMillis();
		
		AStar.run(new State(WORST, false), new State(GOAL, true), manhattan);
		//DFBnB.run(new State(WORST, false), new State(GOAL, true), manhattan, startTime);
		//IDAStar.run(new State(WORST, false), new State(GOAL, true), manhattan);
		
		endTime = System.currentTimeMillis();
		System.out.println("Computation Time = " + (endTime - startTime) + "ms");
	}
	
	public static void initialize(){
		
		misplaced = new MisplacedComparator();
		manhattan = new ManhattanComparator();	
	}
	
	public static void printTime(){
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.print( sdf.format(cal.getTime()) );
	}
	
	public static class globals{
		
		static final String[] MOVES = new String[]
				{ "", "Right", "Down", "Left", "Up" };
			
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
	}
}
