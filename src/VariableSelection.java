
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
                if ((s.board[i][j] == 0) && (s.domains[ s.getPos(i, j) ].cardinality() == min))
                {
                    min = Math.max(s.getPeers(pos).size(), s.getPeers(s.getPos(i, j)).size() );
                    if(s.getPeers(pos).size()<s.getPeers(s.getPos(i, j)).size() )
                        pos = s.getPos(i, j);
                }
            }
        }                
        
        return pos;
      
        
    }
    
    public static Integer randomCell(Sudoku s)
    {
        ArrayList<Integer> unassigned = new ArrayList<>();
        for (int i = 0; i < Sudoku.N; i++){
            for (int j = 0; j < Sudoku.N; j++)
                if (s.board[i][j] == 0){
                    unassigned.add(Sudoku.getPos(i, j));
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

    