
import java.util.BitSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tasnim
 */
public class Backtrack {
    public Assignment startSolve(Sudoku sud){
        return backtrack(sud.assignment, sud);
    }
    
    /** return assignment or return null for failure */
    public Assignment backtrack(Assignment assign, Sudoku sud){
        if(assign.complete()){
            return assign;
        }
        int var = VariableSelection.firstUnassignedCell(sud);
        
        
    }
    
}
