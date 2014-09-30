import java.util.Comparator;


public class ManhattanComparator implements Comparator<State> {

	@Override
	public int compare(State board1, State board2) {
		
		if ( ( board1.manhattan + board1.cost) < ( board2.manhattan + board2.cost)) return -1;
		if ( ( board1.manhattan + board1.cost) > ( board2.manhattan + board2.cost)) return 1;
		
		return 0;
	}
}
