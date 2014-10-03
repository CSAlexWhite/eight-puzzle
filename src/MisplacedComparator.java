import java.util.Comparator;


public class MisplacedComparator implements Comparator<State> {

	@Override
	public int compare(State board1, State board2) {
		
		if ( ( board1.misplaced + board1.cost) < ( board2.misplaced + board2.cost)) return -1;
		if ( ( board1.misplaced + board1.cost) > ( board2.misplaced + board2.cost)) return 1;
		return 0;
	}
}
