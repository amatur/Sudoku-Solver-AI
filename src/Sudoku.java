
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sudoku {

    public static int N = 9;
    public static int NROOT = 3;
    public BitSet[] domains = new BitSet[N * N];
    public int[][] board = new int[N][N];
    public Assignment assignment;
    
    public Sudoku(int[][] board) {
        //set initial domains - all 81 variables 1-9
        for (int i = 0; i < N * N; i++) {
            domains[i] = new BitSet(N);
            domains[i].set(0, N);
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.board[i][j] = board[i][j];
                if (board[i][j] != 0) {
                    domains[getPos(i, j)].clear();
                    domains[getPos(i, j)].set(board[i][j] - 1);
                }
                System.out.printf("Domain %d: %s\n", getPos(i, j), domains[getPos(i, j)].toString().replaceAll(",|\\{|\\}|\\s+", "").trim());
            }
        }
        assignment = new Assignment(board, domains);
    }

    public int getPos(int row, int col) {
        return row * N + col;
    }
    
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

    public int getPos(VariableCell c) {
        return c.row * N + c.col;
    }

    public void put(int pos, int i) {
        VariableCell c = getCell(pos);
        board[c.row][c.col] = i;
    }

    public int get(int pos) {
        VariableCell c = getCell(pos);
        return board[c.row][c.col];
    }

    public void assign(int pos, int value) {

    }

    public VariableCell getCell(int pos) {
        int r = pos / N;
        int c = pos % N;
        return new VariableCell(r, c);
    }

    public VariableCell getGridStartCell(int pos) {
        VariableCell tempCell = getCell(pos);
        int gridRow = tempCell.row / NROOT; //can be 0, 1, 2
        int gridCol = tempCell.col / NROOT; //can be 0, 1, 2
        return getCell(NROOT * N * gridRow + gridCol * NROOT); //0, 3, 6, 27, 30, 33
    }

    public static void main(String[] args) {
        Scanner s = null;
        try {
            s = new Scanner(new File("problem.txt"));
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
        /*
         AssignmentBoard solution = sudoku.solve();
         if (solution == null) {
         System.out.println("Failed to find a solution!");
         System.exit(1);
         }*/
        System.out.println("Solution:");
    }
}
