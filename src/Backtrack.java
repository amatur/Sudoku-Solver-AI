
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

    public static Assignment startSolve(Sudoku sud) {
        return backtrack(sud.assignment);
    }

    /**
     * return assignment or return null for failure
     */
    public static Assignment backtrack(Assignment assign) {
        if (assign.complete()) {
            assign.print();
            assign.printDomains();
            return assign;
        }

//        for(int pos = 0; pos<Sudoku.N*Sudoku.N; pos++){
//            
//            ConstraintPropagator.twins(new Sudoku(assign), pos);
//            ConstraintPropagator.singleton(new Sudoku(assign), pos);
//       
//       try {
//              ConstraintPropagator.triplet(new Sudoku(assign), pos);
//            } catch (IllegalStateException e) {
//                System.out.println("ILLEGAL INFERENCE------------------------");
//                continue;
//            }
//        
//        }
        Integer varPos = VariableSelection.MRV(new Sudoku(assign));
        //System.out.println("selected varpos: "+varPos);
        Sudoku.varCount++;
        VariableCell varCell = Sudoku.getCell(varPos);
        //probably wont reach here
        if (varPos == null) {
            return null;
        }

        ArrayList<Integer> orderDomainValues = new ArrayList<>();
        orderDomainValues = ValueSelection.randomOrder(new Sudoku(assign), varPos);

        //System.out.println("ORDER DOMAIN VALUES: "+ orderDomainValues);
        for (Integer value : orderDomainValues) {
            //System.out.println("selected value: "+value);
            Sudoku.valCount++;
            // Make a new assignment (makes copy)
            Assignment newAssign = new Assignment(assign);
            newAssign.board[varCell.row][varCell.col] = value;
            newAssign.domains[varPos].clear();
            newAssign.domains[varPos].set(value - 1);
            //newAssign.printDomains();

            //every time we make a choice of a value for a variable, we infer
            Sudoku newAssignSudoku = new Sudoku(newAssign);
            // Try making some inferences
            // constraint propagation
//            try {
//                newAssign = newAssignSudoku.inference(newAssign, varPos);
//            } catch (IllegalStateException e) {
//                System.out.println("ILLEGAL INFERENCE------------------------");
//                continue;
//            }

        //for(int pos = 0; pos<Sudoku.N*Sudoku.N; pos++){
            ConstraintPropagator.twins(newAssignSudoku, varPos);
           // ConstraintPropagator.singleton(newAssignSudoku, varPos);

            try {
                ConstraintPropagator.triplet(newAssignSudoku, varPos);
            } catch (IllegalStateException e) {
                System.out.println("ILLEGAL INFERENCE------------------------");
                continue;
            }

       // }
            // Try making some inferences
            try {
                ConstraintPropagator.MAC(newAssignSudoku, varPos);
            } catch (IllegalStateException e) {
                System.out.println("ILLEGAL INFERENCE------------------------");
                continue;
            }

            // Check the consistency
            if (!newAssign.consistent()) {
                continue;
            }
            if (!newAssignSudoku.consistent(varPos)) {
                continue;
            }

            // Recurse
            newAssign = backtrack(newAssign);
            if (newAssign != null) {
                //newAssign.print();
                // newAssign.printDomains();
                return newAssign;
            }

            //Sudoku.numBacktracking++;

        }
        // Failed
        return null;
    }

}
