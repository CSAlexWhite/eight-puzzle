import java.util.Stack;

public class State{
	
	/********************** REFERENCES TO STATIC OBJECTS **********************/
	
	 State parent;			// THE PARENT
	 State goal;			// THE GOAL
	 int[][] adjacence;		// THE SHAPE OF THE PUZZLE BOARD
	 int[][] stepsAway;		// THE TABLE FOR MANHATTAN COMPUTATION
	 String[] moves;		// NAMES OF THE MOVES (LEFT, RIGHT, ETC.)

	/*************************** INSTANCE VARIABLES ***************************/
	
	String key;				// A STRING REPRESENTATION OF THE BOARD FOR HASHING
	int[] moveableCoords;	// THE COORDINATES OF THE PIECES 
	int availableMoves;		// HOW MANY AVAILABLE MOVES
	int cost;				// HOW MANY MOVES TO GET TO THIS NODE
	int misplaced;			// THE VALUE OF THE MISPLACED HEURISTIC FOR THIS BOARD
	int manhattan;			// THE VALUE OF THE MANHATTAN HEURISTIC FOR THIS BOAR
	
	private String action;	// THE MOVE ON THE PARENT WHICH PRODUCED THIS BOARD
	private int[] board;	// THE INT ARRAY REPRESENTATION OF THE PUZZLE BOARD
	private int empty;		// THE COORDINATE OF THE EMPTY SPACE
	
	
	/****************************** CONSTRUCTORS ******************************/
	
	/**
	 * Makes a new State out of a nine-digit integer, where each digit represents
	 * the value of a tile on the board
	 * @param input
	 */
	public State(String startString, boolean isGoal){
		
		parent = null;					// INITIALIZE THE IMPORTANT REFERENCES
		goal = Main.goalState;			//
		adjacence = Main.ADJACENCE;		//
		stepsAway = Main.STEPS_AWAY;	//
		moves = Main.MOVES;				//
			
		cost = 0;					// COST FOR THE START STATE IS ZERO									
		board = new int[9];			// INITIALIZE THE BOARD
		for(int i=0; i<9; i++)
			board[i] = Character.getNumericValue(startString.charAt(i));
		
		action = "start";		// THE ACTION TO GET HERE IS 'START'
		key = startString;		// THE STARTING HASH KEY IS A PARAMETER
		
		initActions();			// FIND THE EMPTY PIECE, ASSIGNS MOVES IT HAS
		
		if(!isGoal) setHeuristics(); // HEURISTICS ARE CALCULATED
	}

	/** Makes a new State given the previous state and which of the set of 
	 *  moveable tiles to swap with the empty tile.  Increments the number of moves
	 * @param last - the State from which to create children
	 * @param toMove - the move to make (out of the 2-4 available).
	 */
	public State(State last, int toMove){  
		
		parent = last;					// INITIALIZE THE IMPORTANT REFERENCES
		goal = Main.goalState;			//
		adjacence = Main.ADJACENCE;		//
		stepsAway = Main.STEPS_AWAY;	//
		moves = Main.MOVES;				//
			
		cost = last.cost + 1;			// THIS NODES COST INCREMENTS THE LAST'S
		
		board = new int[9]; 		// THE BOARD IS INITIALZED FROM THE PARENT
		for(int i=0; i<9 ; i++) 
			board[i] = last.board[i];			
		empty = last.empty;				
		move(toMove);				// THEN THE ASSIGNED MOVE IS MADE		
		
		initActions();		// EMPTY PIECE IS FOUND, POSSIBLE MOVES ARE ASSIGNED
		setHeuristics();	// THE HEURISTICS FOR THIS POSITION ARE CALCULATED
		key = getCode();	// AND THE HASH KEY FOR THIS BOARD IS CALCULATED
	}
	
	/***************************** PUBLIC METHODS *****************************/
	
	/** Retraces the path from the current node to the root and prints the cost, 
	 *  the move direction and the state of each board along the way. 
	 *  @callsMethod printBoard()
	 */
	public void printMoves(){	
		
		Stack<State> stateList = new Stack<State>();	// TO TRACK THE MOVES	
		State current = this;							

		stateList.add(current);				// BUILD THE STACK OF MOVES
		while(current.parent != null){			
			current = current.parent; stateList.add(current);		
		}
		
		while(!stateList.isEmpty()){		// THEN PRINT IT OUT TOP TO BOTTOM
			current = stateList.pop(); 
			printBoard(current.cost, current.action);
		}
	}
	
	/** Prints the 8-puzzle board in its normal 3x3 layout.
	 *  @param cost - number of moves to get to this state
	 *  @param action - what was done to the previous board to get here
	 */
	public void printBoard(int cost, String action){
		
		if(cost != 0) System.out.println(cost + ": Move " + action);
		else System.out.println(cost + ":" + action);
		
		int k=0;						// PRINTS THE 3X3 BOARD
		for(int i=0; i<3; i++){			// AND LEAVES AN EMPTY SPACE WHERE
			for(int j=0; j<3; j++){ 	// IT FINDS A ZERO
				
				if(board[k] == 0){ System.out.print("  "); k++;}
				else System.out.print(board[k++] + " ");	
			}	System.out.println();		
		}		System.out.println();	
	}
	
	/** Checks the board of the current node against an input node's board.
	 *  @param input State against which to compare this
	 *  @return if the two boards are equal
	 */
	public Boolean equals(State input){
		
		for(int i=0; i<9; i++) if (board[i] != input.board[i]) return false;
		return true;
	}

	/*************************** OVERRIDDEN METHODS ***************************/
	
	/** Necessary to override the Object class's equals method, in order that 
	 *  the contains() methods of PriorityQueue and Hashtable function correctly.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		State other = (State) obj;
		if (key == null) { if (other.key != null) return false;
		} else if (!key.equals(other.key)) return false;
		return true;
	}
	
	/**************************** PRIVATE METHODS *****************************/
	
	/** Assigns values to the misplaced and manhattan fields. */
	private void setHeuristics(){
		
		misplaced = setMisplaced();
		manhattan = setManhattan();
	}
	
	/** Finds the empty tile and allocates space to store the potential moves. */
	private void initActions(){
		
		for(int i=0; i<9; i++) if( board[i] == 0 ) empty = i; 						
		if(empty == 4) moveableCoords = new int[availableMoves = 4];		
		else if(empty%2 == 1) moveableCoords = new int[availableMoves = 3];	
		else moveableCoords = new int[availableMoves = 2];					
		assignMoveable();
	}

	/** Looks to the adjacency matrix to see where the empty tile can go. */
	private void assignMoveable(){
		
		int j=0;
		for(int i=0; i<9; i++){
			if (adjacence[empty][i] != 0) moveableCoords[j++] = i;
		}
	}
	
	/** Swaps the empty tile with the tile at the target coordinate. */
	private void move(int target){
		
		int temp = board[empty];
		board[empty] = board[target];
		board[target] = temp;
		action = moves[adjacence[empty][target]];
	}
	
	/** Calculates the number of misplaced tiles on the board */
	private int setMisplaced(){
		
		int count = 0;
		for(int i=0; i<9; i++) 
			if(board[i] != 0 && (board[i] != goal.board[i])) count++;
		return count;
	}
	
	/** Calculates the manhattan distance from between this and the goal board */
	private int setManhattan(){
		
		int x = 0, y = 0;
		int count = 0;
		for(int i=0; i<9; i++){
			
			if(board[i] != 0){  // FINDS THE POSITIONS OF EACH NUMBER ON EACH 
								// THE CURRENT BOARD AND THE GOAL BOARD
				
				for(int j=0; j<9; j++) if(board[j] == i) x = j;
				for(int j=0; j<9; j++) if(goal.board[j] == i) y = j;				
			}
			
			count += stepsAway[x][y];	// RETRIEVES THE DISTANCE FROM THE TABLE	
		}
		return count;
	}
	
	/** If not already assigned, converts the board into a String for hashing */
	private String getCode(){
		
		String stringCode = "";
		for(int i=0; i<9; i++) stringCode += board[i];
		return stringCode;
	}
}
