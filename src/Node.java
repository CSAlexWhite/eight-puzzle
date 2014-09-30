
public class Node {

	Node parent;			// THE PARENT NODE
	int[] state;			// CURRENT BOARD CONFIGURATION
	int pathCost;			// HOW MANY MOVES TO GET TO THIS NODE
	int action;
	
	
	int[] moveableCoords;	// THE COORDINATES OF THE PIECES THAT CAN MOVE
	int empty;				// THE COORDINATE OF THE EMPTY SPACE
	int availableMoves;		// HOW MANY AVAILABLE MOVES
	String[] moveNames;		// WHAT THE MOVES ARE CALLED	
	int heuristic;			// DISTANCE TO THE GOAL
	String key;				// STRING REPRESENTATION (FOR HASHING)
	
	
}
