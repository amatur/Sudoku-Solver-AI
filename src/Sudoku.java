import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class Sudoku {

    public static int N = 9;
    public static int NROOT = 3;
    
    public BitSet[] domains = new BitSet[N * N];
    public int[][] board = new int[N][N];           
    public Assignment assignment;                   /*collection of domains and board**/

    public Sudoku(int[][] board) {
        //set initial domains - all 81 variables 1-9 - all are possible
        for (int i = 0; i < N * N; i++) {
            domains[i] = new BitSet(N);
            domains[i].set(0, N);
        }

        //set up board from the given problem, and reduce the domains to one
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.board[i][j] = board[i][j];
                if (board[i][j] != 0) {
                    domains[getPos(i, j)].clear();
                    domains[getPos(i, j)].set(board[i][j] - 1);
                }
                //System.out.printf("Domain %d: %s\n", getPos(i, j), domains[getPos(i, j)].toString().replaceAll(",|\\{|\\}|\\s+", "").trim());
            }
        }
        assignment = new Assignment(board, domains);

    }

    /** give an assignment the Sudoku wrapper**/
    public Sudoku(Assignment a) {
        assignment = a;
        domains = a.domains;
        board = a.board;
        /*
         for (int i = 0; i < N; i++) {
         for (int j = 0; j < N; j++) {
                
         System.out.printf("Domain %d: %s\n", getPos(i, j), domains[getPos(i, j)].toString().replaceAll(",|\\{|\\}|\\s+", "").trim());
         }
         }*/
    }

    
    /** return the position of all unassigned cells in same column **/
    public ArrayList<Integer> getColPeers(int pos) {
        VariableCell c = getCell(pos);
        ArrayList<Integer> unassignedNeighbours = new ArrayList<>();
        //look for unassigned neighbours in same column
        for (int i = 0; i < Sudoku.N; i++) {
            if (board[i][c.col] == 0 && i != c.row) {
                unassignedNeighbours.add(getPos(i, c.col));
            }
        }
        return unassignedNeighbours;
    }

    /** return the position of all unassigned cells in same row **/
    public ArrayList<Integer> getRowPeers(int pos) {
        VariableCell c = getCell(pos);
        ArrayList<Integer> unassignedNeighbours = new ArrayList<>();
        //look for unassigned neighbours in same row
        for (int j = 0; j < Sudoku.N; j++) {
            if (board[c.row][j] == 0 && j != c.col) {
                unassignedNeighbours.add(getPos(c.row, j));
            }
        }
        return unassignedNeighbours;
    }
    
    /** return the position of all unassigned cells in same box **/
    public ArrayList<Integer> getBoxPeers(int pos) {
        VariableCell c = getCell(pos);
        ArrayList<Integer> unassignedNeighbours = new ArrayList<>();
        //look for unassigned neighbours in same box
        VariableCell startCell = getGridStartCell(pos);
        int startRow = startCell.row;
        int startCol = startCell.col;
        for (int i = 0; i < Sudoku.NROOT; i++) {
            for (int j = 0; j < Sudoku.NROOT; j++) {
                if (board[startRow + i][startCol + j] == 0 && (i + startRow) != c.row && (j + startCol) != c.col) {
                    unassignedNeighbours.add(getPos(startRow + i, startCol + j));
                }
            }
        }
        return unassignedNeighbours;
    }
    
    /** return the position of all unassigned cells in same row, column and box **/
    public HashSet<Integer> getPeers(int pos) {
        VariableCell c = getCell(pos);
        HashSet<Integer> unassignedNeighbours = new HashSet<>();

        //look for unassigned neighbours in same column
        for (int i = 0; i < Sudoku.N; i++) {
            if (board[i][c.col] == 0 && i != c.row) {
                unassignedNeighbours.add(getPos(i, c.col));
            }
        }

        //look for unassigned neighbours in same row
        for (int j = 0; j < Sudoku.N; j++) {
            if (board[c.row][j] == 0 && j != c.col) {
                unassignedNeighbours.add(getPos(c.row, j));
            }
        }

        //look for unassigned neighbours in same box
        VariableCell startCell = getGridStartCell(pos);
        int startRow = startCell.row;
        int startCol = startCell.col;
        for (int i = 0; i < Sudoku.NROOT; i++) {
            for (int j = 0; j < Sudoku.NROOT; j++) {
                if (board[startRow + i][startCol + j] == 0 && (i + startRow) != c.row && (j + startCol) != c.col) {
                    unassignedNeighbours.add(getPos(startRow + i, startCol + j));
                }
            }
        }
        return unassignedNeighbours;

    }

    /** assign a value in given position **/
    public void put(int pos, int i) {
        VariableCell c = getCell(pos);
        board[c.row][c.col] = i;
        domains[pos].clear();
        domains[pos].set(i - 1);
    }

    /** get the board value in given position **/
    public int get(int pos) {
        VariableCell c = getCell(pos);
        return board[c.row][c.col];
    }

    public Assignment inference(Assignment oldAssign, int pos) throws IllegalStateException {
        // Get all the affected constraints
        
        Assignment newAssign = new Assignment(oldAssign);
        Sudoku newSudoku = new Sudoku(newAssign);
        HashSet<Integer> peers = newSudoku.getPeers(pos);
        for (Integer peerPos : peers) {
            newSudoku.domains[peerPos].clear(newSudoku.get(pos) - 1);
            if (newSudoku.domains[peerPos].cardinality() == 1) {
                VariableCell vc = getCell(peerPos);
                newSudoku.board[vc.row][vc.col] = newSudoku.domains[peerPos].nextSetBit(0) + 1;
            }
            if (newSudoku.domains[peerPos].cardinality() == 0) {
                throw new IllegalStateException("No remaining assignments for variable: ");
            }
        }
        return newAssign;
    }

    public static int getPos(int row, int col) {
        return row * N + col;
    }

    public static int getPos(VariableCell c) {
        return c.row * N + c.col;
    }

    public static VariableCell getCell(int pos) {
        int r = pos / N;
        int c = pos % N;
        return new VariableCell(r, c);
    }
    
    public static int getRow(int pos) {
        return pos / N;
    }
    
    public static int getCol(int pos) {
        return pos % N;
    }
    
    public static VariableCell getGridStartCell(int pos) {
        VariableCell tempCell = getCell(pos);
        int gridRow = tempCell.row / NROOT; //can be 0, 1, 2
        int gridCol = tempCell.col / NROOT; //can be 0, 1, 2
        return getCell(NROOT * N * gridRow + gridCol * NROOT); //0, 3, 6, 27, 30, 33
    }

    public static BitSet copyBitset(BitSet given) {
        BitSet copy = new BitSet(N);
        copy.clear();
        copy.or(given);
        return copy;
    }

    public void corrector() throws IllegalStateException  {
        for (int pos = 0; pos < N * N; pos++) {
            if (get(pos) != 0) {
                // Get all the affected constraints
                HashSet<Integer> peers = getPeers(pos);
                Assignment newAssign = new Assignment(assignment);
                Sudoku newSudoku = new Sudoku(newAssign);
                
                //removing from peers domain the assigned value
                for (Integer peerPos : peers) {
                    BitSet revertBS = copyBitset(newSudoku.domains[peerPos]);

                    newSudoku.domains[peerPos].clear(newSudoku.get(pos) - 1);
                    if (newSudoku.domains[peerPos].cardinality() == 1) {
                        VariableCell vc = getCell(peerPos);
                        newSudoku.board[vc.row][vc.col] = newSudoku.domains[peerPos].nextSetBit(0) + 1;
                        if (!newSudoku.consistent(peerPos)) {
                            newSudoku.domains[peerPos] = revertBS;
                            newSudoku.board[vc.row][vc.col] = 0;
                        }
                    }
                    if (newSudoku.domains[peerPos].cardinality() == 0) {
                        throw new IllegalStateException("No remaining assignments for variable: ");
                    }
                }

                assignment = newAssign;
                //assignment.printDomains();
            }
        }

    }

    public boolean consistent(int pos) {
        return all_diff_column(pos) && all_diff_row(pos) && all_diff_box(pos);
    }

    public boolean all_diff_column(int pos) {
        VariableCell c = getCell(pos);
        int value_at_pos = get(pos);
        for (int i = 0; i < N; i++) {
            if (board[i][c.col] == value_at_pos && i != c.row) {
                return false;
            }
        }
        return true;
    }

    public boolean all_diff_row(int pos) {
        VariableCell c = getCell(pos);
        int value_at_pos = get(pos);
        for (int j = 0; j < N; j++) {
            if (board[c.row][j] == value_at_pos && j != c.col) {
                return false;
            }
        }
        return true;
    }

    public boolean all_diff_box(int pos) {
        VariableCell c = getCell(pos);
        VariableCell startCell = getGridStartCell(pos);
        int startRow = startCell.row;
        int startCol = startCell.col;
        int value_at_pos = get(pos);
        for (int i = 0; i < NROOT; i++) {
            for (int j = 0; j < NROOT; j++) {
                if (board[startRow + i][startCol + j] == value_at_pos && (i + startRow) != c.row && (j + startCol) != c.col) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        
        //redirecting output file
//        try {
//            System.setOut(new PrintStream(new File("output-file.txt")));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Scanner s = null;
        try {
          //  s = new Scanner(new File("easy1.txt"));
//            s = new Scanner(new File("easy2.txt"));
//            s = new Scanner(new File("medium1.txt"));
//            s = new Scanner(new File("medium2.txt"));
      //     s = new Scanner(new File("medium3.txt"));
//            s = new Scanner(new File("medium4.txt"));
//            s = new Scanner(new File("hard1.txt"));
//            s = new Scanner(new File("hard2.txt"));
    //        s = new Scanner(new File("worldhard.txt"));
          s = new Scanner(new File("hard3.txt"));
//           s = new Scanner(new File("hard4.txt"));
//            s = new Scanner(new File("hard5.txt"));
//            s = new Scanner(new File("hard6.txt"));
   //         s = new Scanner(new File("p096_sudoku.txt"));
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        int board[][] = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = s.nextInt();
            }
        }
        Sudoku sudoku = new Sudoku(board);
        sudoku.corrector();
        System.out.println("Original Problem: ");
        sudoku.assignment.print();
        sudoku.assignment.printDomains();

        
        long startTime = System.nanoTime();
        Assignment solution = Backtrack.startSolve(sudoku);
        long endTime   = System.nanoTime();

        if (solution == null) {
            System.out.println("Failed to find a solution!");
        } else {
            System.out.println("Solution:");
            solution.print();
            //solution.printDomains();
        }
        System.out.println("\nReport:");
//        System.out.println("Count of variable choice: \t" + Sudoku.varCount);
//        System.out.println("Count of value choice: \t" + Sudoku.valCount);
//        System.out.println("Count of backtracking: \t" + Sudoku.numBacktracking);
//        long totalTime = endTime - startTime;
//        double tot = totalTime/1000000;
//        System.out.println("Time: \t" + tot + " ms" );
        System.out.println(Sudoku.varCount);
        System.out.println(Sudoku.valCount);
        System.out.println(Sudoku.numBacktracking);
        long totalTime = endTime - startTime;
        double tot = totalTime/1000000;
        System.out.println(tot);

    }
    
    public static int varCount = 0;
    public static int valCount = 0;
    public static int numBacktracking = 0;
}
