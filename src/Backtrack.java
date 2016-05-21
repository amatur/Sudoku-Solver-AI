
import java.util.ArrayList;
import java.util.BitSet;

public class Backtrack {

    public static Assignment startSolve(Sudoku sud) {
        return backtrack(sud.assignment);
    }

    /**
     * return assignment or return null for failure
     */
    public static Assignment backtrack(Assignment assign) {
        if (assign.complete()) {
            if(Sudoku.debug>0) assign.print();
            if(Sudoku.debug>0) assign.printDomains();
            return assign;
        }

        
        Integer varPos = VariableSelection.MRV(new Sudoku(assign));
        //System.out.println("selected varpos: "+varPos);
        Sudoku.varCount++;
        VariableCell varCell = Sudoku.getCell(varPos);
        //probably wont reach here
        if (varPos == null) {
            return null;
        }

        ArrayList<Integer> orderDomainValues = new ArrayList<>();
        orderDomainValues = ValueSelection.LRV(new Sudoku(assign), varPos);

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
            //ConstraintPropagator.twins(newAssignSudoku, varPos);
            //ConstraintPropagator.singleton(newAssignSudoku, varPos);

            //try {
           //     ConstraintPropagator.triplet(newAssignSudoku, varPos);
           // } catch (IllegalStateException e) {
            //    if(Sudoku.debug>0) System.out.println("ILLEGAL INFERENCE------------------------");
           //     continue;
            //}

       // }
            // Try making some inferences
            try {
                ConstraintPropagator.myMacWithSingletonTwins(newAssignSudoku, varPos);
            } catch (IllegalStateException e) {
                if(Sudoku.debug>0) System.out.println("ILLEGAL INFERENCE------------------------");
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
                return newAssign;
            }

        }
        // Failed
        return null;
    }

}
