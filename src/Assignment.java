

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;

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
    
    public boolean allDiff(ArrayList<Integer> al) {
        Collections.sort(al, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        int prev = 0;
        for (int val : al) {
            if (val == prev) {
                return false;
            }
            prev = val;
        }
        return true;
    }
    
    public boolean consistent(){
        
        //same col
        for(int col=0; col<N; col++){
            ArrayList<Integer> al = new ArrayList<>();
            for(int i = 0; i<N ; i++){
                if(board[i][col] != 0){
                    al.add(board[i][col]);
                }
            }
            if(!allDiff(al)){
                return false;
            }    
	}
        
        //same row
        for(int row=0; row<N; row++){
            ArrayList<Integer> al = new ArrayList<>();
            for(int i = 0; i<N ; i++){
                if(board[row][i] != 0){
                    al.add(board[row][i]);
                }
            }
            if(!allDiff(al)){
                return false;
            }    
	}
        
        int arr[] = {0, 3, 6, 27, 30, 33, 54, 57, 60 };
        //same box/region
	for(int pos: arr ){
                ArrayList<Integer> al = new ArrayList<>();
		VariableCell startCell = Sudoku.getCell(pos);
		int startRow = startCell.row; 
		int startCol = startCell.col; 
		for(int i=0; i< Sudoku.NROOT; i++){
                    for(int j=0; j< Sudoku.NROOT; j++){
                            if(board[startRow+i][startCol+j] != 0){
                                     al.add(board[startRow+i][startCol+j]);
                            }
                    }
		}
		if(!allDiff(al)){
                    return false;
                }
	}
        return true;
    }
    
    public void print(){
        System.out.println("");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                
                if (board[i][j] == 0) {
                    System.out.printf("%c ",'.');
                }else{
                    System.out.printf("%d ",board[i][j]);
                }
                if(j%3==2){
                    System.out.printf("| ");
                }
            }
            if(i%3==2){
                     System.out.printf("\n------+-------+-------+");
                }
           System.out.printf("\n");
        }
    }
    
    public void printDomains(){
        System.out.println("");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                
                if (board[i][j] == 0) {
                    
                    System.out.printf("%9s  ",domains[Sudoku.getPos(i, j)].toString().replaceAll(",|\\{|\\}|\\s+", "").trim());
                }else{
                    System.out.printf("%9s  ",domains[Sudoku.getPos(i, j)].toString().replaceAll(",|\\{|\\}|\\s+", "").trim());

                }
                if(j%3==2){
                    System.out.printf("| ");
                }
            }
            if(i%3==2){
                     System.out.printf("\n---------------------------------+----------------------------------+----------------------------------+");
                }
           System.out.printf("\n");
        }
    }
     

}
