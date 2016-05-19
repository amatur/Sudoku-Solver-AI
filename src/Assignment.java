

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
public class Assignment {
    int N = Sudoku.N;
    public BitSet[] domains = new BitSet[N * N];
    public int[][] board = new int[N][N];

    public Assignment(int[][] board, BitSet[] domains) {
        this.board = board;
        this.domains = domains;
    }
    
    /** make a copy **/
    public Assignment(Assignment old) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = old.board[i][j];
             }
        }
        for (int i = 0; i < N * N; i++) {
            domains[i] = new BitSet(N);
            domains[i].clear();
            domains[i].or(old.domains[i]);
        }
    }
    
    public boolean complete(){
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
