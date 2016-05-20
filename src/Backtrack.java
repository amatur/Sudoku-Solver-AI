
import java.util.ArrayList;
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
    public static Assignment startSolve(Sudoku sud){
        return backtrack(sud.assignment, sud);
    }
    
    /** return assignment or return null for failure */
    public static Assignment backtrack(Assignment assign, Sudoku s){
        if(assign.complete()){
            //assign.print();
            //assign.printDomains();
            return assign;
        }
        
        Integer varPos = VariableSelection.firstUnassignedCell(new Sudoku(assign));
        System.out.println("selected varpos: "+varPos);
        s.varCount++;
        VariableCell varCell = s.getCell(varPos);
        //probably wont reach here
        if (varPos == null) return null;
        
        ArrayList<Integer> orderDomainValues = new ArrayList<>();
        orderDomainValues = ValueSelection.firstOrder(new Sudoku(assign), varPos);
        System.out.println("ORDER DOMAIN VALUES: "+ orderDomainValues);
        for(Integer value: orderDomainValues){
            System.out.println("selected value: "+value);
            s.valCount++;
            // Make a new assignment
            Assignment newAssign = new Assignment(assign);
            newAssign.board[varCell.row][varCell.col] = value;
            newAssign.domains[varPos].clear();
            newAssign.domains[varPos].set(value-1);
            //newAssign.printDomains();
            
            // Try making some inferences
            try {
                newAssign = s.inference(newAssign, varPos);
            } catch (IllegalStateException e) {
                continue;
            }

            Sudoku newAssignSudoku = new Sudoku(newAssign);
            // Check the consistency
            if (!newAssignSudoku.consistent(varPos)) {
                continue;
            }

            // Recurse
            newAssign = backtrack(newAssign, s);
            if (newAssign != null) {
                newAssign.print();
                newAssign.printDomains();
                return newAssign;
            }
        }
        // Failed
        return null;
    }
    
}
