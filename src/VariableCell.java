
import java.util.HashSet;

public class VariableCell {

    public HashSet<Integer> domain = new HashSet<>();
    int row;
    int col;
    int pos;

    VariableCell(int x, int y) {
        row = x;
        col = y;
        pos = row * Sudoku.N + col;
    }
}
