
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tasnim
 */
public class ConstraintPropagator {
    
    public void forwardCheckElimination(Sudoku s, int pos) throws IllegalStateException  {

        // Get all the affected constraints
        HashSet<Integer> peers = s.getPeers(pos);
        Assignment newAssign = new Assignment(s.assignment);
        Sudoku newSudoku = new Sudoku(newAssign);

        //removing from peers domain the assigned value
        for (Integer peerPos : peers) {
            BitSet revertBS = Sudoku.copyBitset(newSudoku.domains[peerPos]);

            newSudoku.domains[peerPos].clear(newSudoku.get(pos) - 1);
            if (newSudoku.domains[peerPos].cardinality() == 1) {
                VariableCell vc = Sudoku.getCell(peerPos);
                newSudoku.board[vc.row][vc.col] = newSudoku.domains[peerPos].nextSetBit(0) + 1;
                if (!newSudoku.consistent(peerPos)) {
                    newSudoku.domains[peerPos] = revertBS;
                    newSudoku.board[vc.row][vc.col] = 0;
                }
            }
            if (newSudoku.domains[peerPos].cardinality() == 0) {
                throw new IllegalStateException("No remaining assignments for variable: ");
            }
        }
        s.assignment = newAssign;

    }
    
    
    
    public static void singleton(Sudoku s, int pos){
        
        
        int[] frequency = new int[Sudoku.N];
        
        //col
        for (int i = 0; i < Sudoku.N; i++) frequency[i] = 0;
        ArrayList<Integer> peers = s.getColPeers(pos);
        for(int peerPos: peers){        
            for (int i = 0; i < Sudoku.N; i++) {
                BitSet me = s.domains[(peerPos)];
                for (int number = 0; number < 9; number++) {
                        if (me.get(number) == true) {
                            frequency [number]++;
                        }	
                }
            }   
        }
        // Any singletons?
        for (int number = 0; number < Sudoku.N; number++) {
                if (frequency[number] == 1)	{		// YES! We have work to do!
                    for(int peerPos: peers){    
                        BitSet me = s.domains[(peerPos)];
                        if(me.get(number) == true){
                            s.put(peerPos, number+1);
                        }
                    }
                }
        }                
        
    }
    
    public static void twins(Sudoku s, int pos){
        //COL PEERS
        ArrayList<Integer> colPeers = s.getColPeers(pos);
        HashSet<BitSet> twinsToRemove = new HashSet<>();
        for(int peerPos: colPeers){
            if( s.domains[peerPos].cardinality() == 2 ){
                for(int peers2: colPeers){
                    BitSet thisBS = (s.domains[peerPos]);
                    if(thisBS.equals(s.domains[peers2])){
                        twinsToRemove.add(thisBS);
                    }
                }
            }
        }
        for(int peerPos: colPeers){
            for(BitSet remBS : twinsToRemove){
                if(!s.domains[peerPos].equals(remBS)){
                    s.domains[peerPos].andNot(remBS);   //remove the pair
                    if( s.domains[peerPos].cardinality()==1){
                        s.board[Sudoku.getRow(peerPos)][Sudoku.getCol(peerPos)] = s.domains[peerPos].nextSetBit(0) + 1;
                    }
                }
            }
        }
        
        //ROW PEERS
        colPeers = s.getRowPeers(pos);
        twinsToRemove = new HashSet<>();
        for(int peerPos: colPeers){
            if( s.domains[peerPos].cardinality() == 2 ){
                for(int peers2: colPeers){
                    BitSet thisBS = (s.domains[peerPos]);
                    if(thisBS.equals(s.domains[peers2])){
                        twinsToRemove.add(thisBS);
                    }
                }
            }
        }
        for(int peerPos: colPeers){
            for(BitSet remBS : twinsToRemove){
                if(!s.domains[peerPos].equals(remBS)){
                    s.domains[peerPos].andNot(remBS);   //remove the pair
                    if( s.domains[peerPos].cardinality()==1){
                        s.board[Sudoku.getRow(peerPos)][Sudoku.getCol(peerPos)] = s.domains[peerPos].nextSetBit(0) + 1;
                    }
                }
            }
        }
        
        //BOX PEERS
        colPeers = s.getBoxPeers(pos);
        twinsToRemove = new HashSet<>();
        for(int peerPos: colPeers){
            if( s.domains[peerPos].cardinality() == 2 ){
                for(int peers2: colPeers){
                    BitSet thisBS = (s.domains[peerPos]);
                    if(thisBS.equals(s.domains[peers2])){
                        twinsToRemove.add(thisBS);
                    }
                }
            }
        }
        for(int peerPos: colPeers){
            for(BitSet remBS : twinsToRemove){
                if(!s.domains[peerPos].equals(remBS)){
                    s.domains[peerPos].andNot(remBS);   //remove the pair
                    if( s.domains[peerPos].cardinality()==1){
                        s.board[Sudoku.getRow(peerPos)][Sudoku.getCol(peerPos)] = s.domains[peerPos].nextSetBit(0) + 1;
                    }
                }
            }
        }
        
    }
    
}
