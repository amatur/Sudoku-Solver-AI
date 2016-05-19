import java.util.*;

/**
 * This class implements a Backtracking search to find
 * a solution to a CSP Problem.
 */
public class Backtrack {

  CSP problem;
  AssignmentBoard start;

  /**
   * Starts the Backtracker given a blank assignment.
   */
  public Backtrack(CSP prob) {
    this(prob, AssignmentBoard.blank());
  }

  /**
   * Starts the Backtracker given a problem and a specific
   * assignment.
   */
  public Backtrack(CSP prob, AssignmentBoard initial) {
    problem = prob;
    start = initial;
  }

  /**
   * Solves the problem.
   * @return An assignment that satisfies all constraints or null
   * if none is found.
   */
  public AssignmentBoard solve() {
    // Start solving from the initial state
    return recursiveSolve(start); 
  }


  private AssignmentBoard recursiveSolve(AssignmentBoard assign) {
    if (problem.satisfiedByAssignment(assign)) {
      return assign;
    }

    // Get an unassigned variable
    VariableCell v = unassignedVar(assign);
    if (v == null) return null;

    // Get the domain values for a variable
    List<Object> values = problem.domainValues(assign, v);
    values = orderValues(assign, values, v);

    for (Object value : values) {
      // Make a new assignment
      AssignmentBoard newAssign = assign.assign(v, value);

      // Try making some inferences
      try {
        newAssign = problem.inference(newAssign, v);
      } catch (IllegalStateException e) {
        continue;
      }

      // Check the consistency
      if (!problem.consistentAssignment(newAssign, v)) { 
        continue; 
      }

      // Recurse
      newAssign = recursiveSolve(newAssign);
      if (newAssign != null) return newAssign;
    }

    // Failed
    return null;
  }

  /**
   * Returns an assigned variable to try to assign.
   * This can be sub-classed to add heuristics.
   */
  protected VariableCell unassignedVar(AssignmentBoard assign) {
    // Find any non-assigned variable
    for (VariableCell v : problem.variables()) {
      if (assign.getValue(v) == null) return v;
    }
    return null;
  }

  /**
   * Returns an ordered list of values to try to assign.
   * May be sub-classed to add heuristics.
   */
  protected List<Object> orderValues(AssignmentBoard asign, List<Object> domain, VariableCell v) {
    return domain;
  }
}
