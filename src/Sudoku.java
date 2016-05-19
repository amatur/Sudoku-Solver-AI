import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sudoku {
    public static int N = 9;
    public static int NROOT = 3;
    List<VariableCell> positions;
    List<Constraint> constraints;
    public AssignmentBoard initialBoard = new AssignmentBoard();
    /*************************************************************
            ***************** CLASSES *****************
     ***********************************************************/
    public class VariableCell {
        public HashSet<Integer> domain = new HashSet<>();
        int row;
        int col;
        int pos;
        VariableCell(int x, int y) {
            row = x;
            col = y;
            pos = row * N + col;
        }
    }
    /**************************************************************/
    /**
     * The assignment may be partial or complete.
     */
    public class AssignmentBoard {

        Map<VariableCell, Integer> assignments = null;
        Map<VariableCell, HashSet<Integer>> domain = null;
        public int[][] board = new int[N][N];
        
        public AssignmentBoard() {
            assignments = new HashMap<VariableCell, Integer>();
            domain = new HashMap<VariableCell, HashSet<Integer>>();
        }
        public void put(int pos, int i){
		VariableCell c = getCell(pos);
		board[c.row][c.col] = i;
	}
	public int get(int pos){
		VariableCell c = getCell(pos);
		return board[c.row][c.col];
	}

        /**
         * *
         * get a copy of this assignment board *
         */
        public AssignmentBoard assign(VariableCell v, Integer val) {
            //get a copy of this assignment board
            AssignmentBoard n = new AssignmentBoard();
            n.assignments = new HashMap<VariableCell, Integer>(assignments);
            n.domain = new HashMap<VariableCell, HashSet<Integer>>(domain);
            //populate the board copy with new assignment
            n.assignments.put(v, val);
            // Restrict the domain to only a single value
            HashSet<Integer> varDomain = new HashSet<>();
            varDomain.add(val);
            n.domain.put(v, varDomain);
            return n;
        }

        public Integer getValue(VariableCell v) {
            return assignments.get(v);
        }

        public HashSet<Integer> getDomain(VariableCell v) {
            return domain.get(v);
        }

        public int size() {
            return assignments.size();
        }
    }
    /*************************************************************
          ***************** END of CLASSES *****************
     ***********************************************************/
    public int getPos(VariableCell c) {
        return c.row * N + c.col;
    }
    public VariableCell getCell(int pos){
        int r = pos/N;
        int c = pos%N; 
        return new VariableCell(r,c);
    }
    public VariableCell getGridStartCell(int pos){
        VariableCell tempCell = getCell(pos);
        int gridRow = tempCell.row / NROOT; //can be 0, 1, 2
        int gridCol = tempCell.col / NROOT; //can be 0, 1, 2
        return getCell(NROOT*N*gridRow + gridCol*NROOT); //0, 3, 6, 27, 30, 33
    }
    
    
    public boolean satisfiedByAssignment(AssignmentBoard assign) {
        if (positions.size() > assign.size()) {
            return false;
        }
        //CHECK ALL CONSTRAINS
        for (Constraint c : constraints) {
            if (!c.satisfied(assign)) {
                return false;
            }
        }
        return true;
    }
    public boolean consistentAssignment(AssignmentBoard assign, VariableCell v) {
        for (Constraint c : constraints) {
            if (!c.consistent(assign)) {
                return false;
            }
        }
        return true;
    }
    
    public Sudoku() {
        //COULDDO: init global domain
        positions = new ArrayList<VariableCell>(N * N);
        constraints = new ArrayList<Constraint>(N * 3);
        //initialize variables, constraints
        constraints.add(new AllDiffColumn());
        constraints.add(new AllDiffRow());
        constraints.add(new AllDiffBox());
    }
    
    
    /*************************************************************
          ***************** CONSTRAINTS *****************
     ***********************************************************/
    public abstract class Constraint {
        public abstract boolean satisfied(AssignmentBoard asgn);
        public abstract boolean consistent(AssignmentBoard asgn); // It is possible to be consistent without being satisfied.
        public List<VariableCell> afftecedVariables = new LinkedList<>();;
    }
    /**
     * Simple constraint that requires all variables to have a unique value.
     */
    public class AllDiffColumn extends Constraint {
        public boolean satisfied(AssignmentBoard asign) {
            boolean[] seen = new boolean[N + 1];
            for (VariableCell v : afftecedVariables) {
                Integer val = (Integer) asign.getValue(v);
                if (val == null || seen[val]) {
                    return false;
                }
                seen[val] = true;
            }
            return true;
        }
    }

    
    

    /**
     * Returns a new assignment based on some inferences. We will override the
     * domain of other variables
     */
    public AssignmentBoard inference(AssignmentBoard assign, VariableCell v) throws IllegalStateException {
        // Get all the affected constraints
        List<Constraint> constr = variableConstraints(v);

        // Get the assigned value
        Object val = assign.getValue(v);

        // Get all the affected variables
        for (Constraint c : constr) {
            for (VariableCell rel : c.reliesOn()) {
                // Skip the current variable
                if (rel == v) {
                    continue;
                }

                List<Object> domain = domainValues(assign, rel);
                if (domain.contains(val)) {
                    domain = new LinkedList<Object>(domain);
                    domain.remove(val);
                    assign.restrictDomain(rel, domain);
                    if (assign.getValue(rel) == null) {
                        if (domain.size() == 1) {
                            assign = assign.assign(rel, domain.get(0));
                        } else if (domain.size() == 0) {
                            throw new IllegalStateException("No remaining assignments for variable: " + rel.description());
                        }
                    }
                }
            }
        }

        return assign;
    }
    public static void main(String[] args) {
        Sudoku sudoku;
        Scanner s;
        try {
            s = new Scanner(new File("problem.txt"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                brd.board[i][j] = s.nextInt();
            }
        }
        /*
        AssignmentBoard solution = sudoku.solve();
        if (solution == null) {
            System.out.println("Failed to find a solution!");
            System.exit(1);
        }*/
        System.out.println("Solution:");
    }
}
