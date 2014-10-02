public class State {
							// SOME POINTERS TO IMPORTANT OBJECTS:
	State parent;			// THE PARENT
	State goal;				// THE GOAL
	int[][] adjacence;		// THE SHAPE OF THE BOARD
	int[][] stepsAway;		// THE MANHATTAN TABLE
	
	int[] board;			// THE REPRESENTATION OF THE PUZZLE BOARD
	int[] moveableCoords;	// THE COORDINATES OF THE PIECES THAT CAN MOVE
	int empty;				// THE COORDINATE OF THE EMPTY SPACE
	int availableMoves;		// HOW MANY AVAILABLE MOVES
	int cost;			// HOW MANY MOVES TO GET TO THIS NODE
	int heuristic;
	String key;
	int misplaced;
	int manhattan;
	
	/**
	 * Makes a new State out of a nine-digit integer, where each digit represents
	 * the value of a tile on the board
	 * @param input
	 */
	public State(String input){
		
		adjacence = Main.ADJACENCE;
		stepsAway = Main.STEPS_AWAY;
		goal = Main.goalState;
		
		parent = null;
		cost = 0;
		board = new int[9];
		
		// TRANSLATE THE STRING INPUT INTO THE BOARD ARRAY
		for(int i=0; i<9; i++) board[i] = Character.getNumericValue(input.charAt(i));
			
		key = input;
				
		for(int i=0; i<9; i++) if( board[i] == 0 ) empty = i; 				// FIND EMPTY		
		if(empty == 4) moveableCoords = new int[availableMoves = 4];		// ADD MOVEABLE PIECES
		else if(empty%2 == 1) moveableCoords = new int[availableMoves = 3];	// AND TRACK POTENTIAL
		else moveableCoords = new int[availableMoves = 2];					// EXPANSIONS
		
		assignMoveable();
	}

	/**
	 * Makes a new State given the previous state and which of the set of moveable
	 * tiles to swap with the empty tile.  Increments the number of moves
	 * 
	 * @param last
	 * @param toMove
	 */
	public State(State last, int toMove){  
		
		adjacence = Main.ADJACENCE;
		stepsAway = Main.STEPS_AWAY;
		goal = Main.goalState;
		
		parent = last;
		cost = last.cost + 1;
		board = new int[9]; for(int i=0; i<9 ; i++) board[i] = last.board[i];
		
		empty = last.empty;

		key = getCode();
				
		move(toMove);			// PLUS SWAPPING A MOVEABLE PIECE
			
		for(int i=0; i<9; i++) if( board[i] == 0 ) empty = i; 				// FIND EMPTY		
		if(empty == 4) moveableCoords = new int[availableMoves = 4];		// ADD MOVEABLE PIECES
		else if(empty%2 == 1) moveableCoords = new int[availableMoves = 3];	// AND TRACK POTENTIAL
		else moveableCoords = new int[availableMoves = 2];					// EXPANSIONS
		
		assignMoveable();
		misplaced = misplaced();
		manhattan = manhattan();
	}
	
	/**
	 * Calculate which moves are available on this board, in this case 
	 * which tile coordinates can be swapped with the empty coordinate
	 */
	public void assignMoveable(){
		
		int j=0;
		for(int i=0; i<9; i++){
			if (adjacence[empty][i] != 0) moveableCoords[j++] = i;
		}
	}
	
	/**
	 * Make the move, i.e. swap pieces in the empty and target coordinates 
	 * @param from
	 */
	public void move(int from){
		
		int temp = board[empty];
		board[empty] = board[from];
		board[from] = temp;
	}
	
	/**
	 * A method to calculate the out of place heuristic
	 * @return
	 */
	public int misplaced(){
		
		int count = 0;
		for(int i=0; i<9; i++) if(board[i] != 0 && (board[i] != goal.board[i])) count++;
		return count;
	}
	
	public int manhattan(){
		
		int x = 0, y = 0;
		int count = 0;
		for(int i=0; i<9; i++){
			
			if(board[i] != 0){  // FIND THE POSITIONS OF EACH NUMBER ON EACH THE
								// CURRENT BOARD AND THE GOAL BOARD
				
				for(int j=0; j<9; j++) if(board[j] == i) x = j;
				for(int j=0; j<9; j++) if(goal.board[j] == i) y = j;				
			}
			
			count += stepsAway[x][y];	// THEN REFER TO THE TABLE TO CALCULATE DISTANCE	
		}
		
		//System.out.println(count);
		return count;
	}
	
	/**
	 * A method which will return the integer representation of the board
	 * @return
	 */
	public String getCode(){
		
		String stringCode = "";
		for(int i=0; i<9; i++) stringCode += board[i];
		return stringCode;
	}

	/**
	 * A test to determine whether two States are equivalent
	 * @param input
	 * @return
	 */
	public Boolean equals(State input){
		
		for(int i=0; i<9; i++) if (board[i] != input.board[i]) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	/**
	 * Print the board to console, in the board orientation
	 */
	public void print(){
		
		System.out.println("Move " + cost);
		
		int k=0;
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){ 
				
				if(board[k] == 0){ System.out.print("  "); k++;}
				else System.out.print(board[k++] + " ");	
			}	
			System.out.println();
		}
		System.out.println();
	}	
}
