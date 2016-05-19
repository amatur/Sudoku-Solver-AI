
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tasnim
 */
public class VariableSelection {
    
    public static Integer MRV(Sudoku s)
    {
        int min = Sudoku.N + 1;
        Integer pos = new Integer(-1);
        for (int i = 0; i < Sudoku.N; i++){
            for (int j = 0; j < Sudoku.N; j++)
            {
                if ((s.board[i][j] == 0) && (s.domains[ s.getPos(i, j) ].cardinality() < min))
                {
                    min = s.domains[s.getPos(i, j)].cardinality();
                    pos = s.getPos(i, j);
                }
            }
        }                
        if(min==Sudoku.N + 1) {
            return null;
        }
        return pos;
    }
    /**the degree of a cell is the number of unassigned cells that are constrained with the cell.**/
    public static Integer maxDegreeCell(Sudoku s)
    {
        int count[] = new int[Sudoku.N* Sudoku.N];
        for(int i=0; i<Sudoku.N*Sudoku.N; i++){
            count[i] = -1;
        }    
        
        for (int row = 0; row < Sudoku.N; row++){
            for (int col = 0; col < Sudoku.N; col++){
                int pos = s.getPos(row,col);
                HashSet<Integer> unassignedNeighbours = new HashSet<>();
                if (s.board[row][col] == 0){
                   
                    //look for unassigned neighbours in same column
                    for(int i = 0; i<Sudoku.N ; i++){
			if(s.board[i][col] == 0 && i != row ){
                            unassignedNeighbours.add(s.getPos(i, col));
			}
                    }
                    
                    //look for unassigned neighbours in same row
                    for (int j = 0; j < Sudoku.N; j++) {
                        if (s.board[row][j] == 0 && j != col) {
                            unassignedNeighbours.add(s.getPos(row, j));
                        }
                    }

                    //look for unassigned neighbours in same box
                    VariableCell startCell = s.getGridStartCell(pos);
                    int startRow = startCell.row;
                    int startCol = startCell.col;
                    for (int i = 0; i < Sudoku.NROOT; i++) {
                        for (int j = 0; j < Sudoku.NROOT; j++) {
                            if (s.board[startRow + i][startCol + j] == 0 && (i + startRow) != row && (j + startCol) != col) {
                               unassignedNeighbours.add(s.getPos(startRow + i, startCol + j));
                            }
                        }
                    }
                    count[pos] = unassignedNeighbours.size();
                }
            }
        }
        int maxDeg = -1;
        int retPos = -1;
        for(int i=0; i<Sudoku.N*Sudoku.N; i++){
            if(count[i]>maxDeg){
                retPos = i;
                maxDeg = count[i];
            }
        }
        if(maxDeg!=-1){
            return retPos;
        }
        return null;
    }
    
    public static Integer randomCell(Sudoku s)
    {
        ArrayList<Integer> unassigned = new ArrayList<>();
        for (int i = 0; i < Sudoku.N; i++){
            for (int j = 0; j < Sudoku.N; j++)
                if (s.board[i][j] == 0){
                    unassigned.add(s.getPos(i, j));
                }
        }
        if(unassigned.size()>0){
            int arrListPos =  (new Random()).nextInt(unassigned.size());
            return unassigned.get(arrListPos);
        }
        return null;
    }
    
    public static Integer firstUnassignedCell(Sudoku s)
    {
        for (int i = 0; i < Sudoku.N; i++){
            for (int j = 0; j < Sudoku.N; j++)
                if (s.board[i][j] == 0){
                    return s.getPos(i, j);
                }
        }   
        return null;
    }
}

    