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
   
    public static void forwardCheckElimination(Sudoku newSudoku, int pos) throws IllegalStateException  {
        // Get all the affected constraints
        HashSet<Integer> peers = newSudoku.getPeers(pos);
        //removing from peers domain the assigned value
        for (Integer peerPos : peers) {
            BitSet revertBS = Sudoku.copyBitset(newSudoku.domains[peerPos]);

            newSudoku.domains[peerPos].clear(newSudoku.get(pos) - 1);
            if (newSudoku.domains[peerPos].cardinality() == 1) {
                VariableCell vc = Sudoku.getCell(peerPos);
                newSudoku.board[vc.row][vc.col] = newSudoku.domains[peerPos].nextSetBit(0) + 1;
                if (!newSudoku.consistent(peerPos)) {
                    throw new IllegalStateException("No remaining assignments for variable: ");
                }
            }
            if (newSudoku.domains[peerPos].cardinality() == 0) {
                throw new IllegalStateException("No remaining assignments for variable: ");
            }
        }
        
    }
    
    public static void MAC(Sudoku newSudoku, int pos) throws IllegalStateException  {
        // Get all the affected constraints
        HashSet<Integer> peers = newSudoku.getPeers(pos);
        //removing from peers domain the assigned value
        for (Integer peerPos : peers) {

            newSudoku.domains[peerPos].clear(newSudoku.get(pos) - 1);
            if (newSudoku.domains[peerPos].cardinality() == 1) {
                VariableCell vc = Sudoku.getCell(peerPos);
                newSudoku.board[vc.row][vc.col] = newSudoku.domains[peerPos].nextSetBit(0) + 1;
                if (!newSudoku.consistent(peerPos)) {
                    throw new IllegalStateException("No remaining assignments for variable: ");
                }
                MAC(newSudoku, peerPos);
            }
            if (newSudoku.domains[peerPos].cardinality() == 0) {
                throw new IllegalStateException("No remaining assignments for variable: ");
            }
        }        
    }
        
    public static void triplet(Sudoku s, int pos) throws IllegalStateException{
        triplet( s, pos, s.getColPeers(pos), 3);
        triplet( s, pos, s.getRowPeers(pos), 3);
        triplet( s, pos, s.getBoxPeers(pos), 3);
//        triplet( s, pos, s.getColPeers(pos), 4);
//        triplet( s, pos, s.getRowPeers(pos), 4);
//        triplet( s, pos, s.getBoxPeers(pos), 4);
    }
    public static void triplet(Sudoku s, int pos, ArrayList<Integer> colPeers, int subsetSize) throws IllegalStateException{ 
        //System.err.println(colPeers);
        Object[] colPeersA =  (colPeers.toArray());
        int N = colPeers.size(); // the number of elements in the set:
       //  int subsetSize = 3; //the number of elements in the subsets:
        if(N<subsetSize) return;
       
        int[] binary = new int[(int) Math.pow(2, N)];
        for (int i = 0; i < Math.pow(2, N); i++) 
        {
            int b = 1;
            binary[i] = 0;
            int num = i, count = 0;
            while (num > 0) 
            {
                if (num % 2 == 1)
                    count++;
                binary[i] += (num % 2) * b;
                num /= 2;
                b = b * 10;
            }
            if (count == subsetSize) 
            {
                ArrayList<Integer> trip = new ArrayList<>();
                //System.out.print("{ ");
                    BitSet bs = new BitSet(9);
                    bs.clear();
                    int maxCardinality = 0;
                for (int j = 0; j < N; j++) 
                {
                    
                    
                    if (binary[i] % 10 == 1){
                       
                      //  System.out.print((Integer)(colPeers.get(j)));//try out
                        trip.add(colPeers.get(j));
                        if(s.domains[colPeers.get(j)].cardinality() > maxCardinality){
                            maxCardinality = s.domains[colPeers.get(j)].cardinality();
                            
                        }
                        bs.or(s.domains[colPeers.get(j)]);
                        
                    }                        
                    binary[i] /= 10;
                }
                //System.out.println("}");
                if((maxCardinality == subsetSize)){
                        if(bs.cardinality()==subsetSize){
                            if(Sudoku.debug>0) System.out.println("triplet found");
                            if(Sudoku.debug>0) System.out.println(bs);
                            
                            for(Integer iii: trip){
                               if(Sudoku.debug>0) System.out.print(iii + " ");
                            }
                            if(Sudoku.debug>0) System.out.println(trip);
                            if(Sudoku.debug>0) s.assignment.printDomains();
                            for(int peerPos:colPeers){
                                if(trip.contains((Integer)peerPos)) continue;
                                BitSet bscopy = Sudoku.copyBitset(bs);
                                bscopy.andNot(s.domains[peerPos]);
                                if(!(bscopy.equals(s.domains[peerPos]))){
                                    s.domains[peerPos].andNot(bs);
                                    
                                    if(s.domains[peerPos].cardinality()==0){
                                        throw new IllegalStateException("No remaining assignments for variable: ");
                                    }
                                    if(s.domains[peerPos].cardinality()==1){
                                        s.board[Sudoku.getRow(pos)][Sudoku.getCol(pos)] = s.domains[peerPos].nextSetBit(0)+1;
                                        triplet(s, peerPos);
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
    
    
    
    public static void myMacWithSingletonTwins(Sudoku newSudoku, int pos) throws IllegalStateException  {

        twins(newSudoku, pos);
        singleton(newSudoku, pos);
        //triplet(newSudoku, pos);
        // Get all the affected constraints
        HashSet<Integer> peers = newSudoku.getPeers(pos);
        //removing from peers domain the assigned value
        for (Integer peerPos : peers) {

            newSudoku.domains[peerPos].clear(newSudoku.get(pos) - 1);
            if (newSudoku.domains[peerPos].cardinality() == 1) {
                VariableCell vc = Sudoku.getCell(peerPos);
                newSudoku.board[vc.row][vc.col] = newSudoku.domains[peerPos].nextSetBit(0) + 1;
                if (!newSudoku.consistent(peerPos)) {
                    throw new IllegalStateException("No remaining assignments for variable: ");
                }
                myMacWithSingletonTwins(newSudoku, peerPos);
            }
            if (newSudoku.domains[peerPos].cardinality() == 0) {
                throw new IllegalStateException("No remaining assignments for variable: ");
            }
        }
        
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
                if (frequency[number] == 1)	{
                    System.out.println("YES! Found singleton.");
                    // YES! We have work to do!
                    for(int peerPos: peers){    
                        BitSet me = s.domains[(peerPos)];
                        if(me.get(number) == true){
                            s.put(peerPos, number+1);
                            myMacWithSingletonTwins(s, peerPos);
                        }
                    }
                }
        }         
        
         //row
        for (int i = 0; i < Sudoku.N; i++) frequency[i] = 0;
        peers = s.getRowPeers(pos);
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
                if (frequency[number] == 1)	{
                    System.out.println("YES! Found singleton.");
                    // YES! We have work to do!
                    for(int peerPos: peers){    
                        BitSet me = s.domains[(peerPos)];
                        if(me.get(number) == true){
                            s.put(peerPos, number+1);
                             myMacWithSingletonTwins(s, peerPos);
                        }
                    }
                }
        }   
        
         //Box
        for (int i = 0; i < Sudoku.N; i++) frequency[i] = 0;
        peers = s.getColPeers(pos);
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
                if (frequency[number] == 1)	{
                    System.out.println("YES! Found singleton.");
                    // YES! We have work to do!
                    for(int peerPos: peers){    
                        BitSet me = s.domains[(peerPos)];
                        if(me.get(number) == true){
                            s.put(peerPos, number+1);
                             myMacWithSingletonTwins(s, peerPos);
                        }
                    }
                }
        }   
        
        
    }
    
    public static void twins(Sudoku s, int pos){
        //COL PEERS
        ArrayList<Integer> colPeers = s.getColPeers(pos);
        ArrayList<Integer> colPeers2 = s.getColPeers(pos);
        HashSet<BitSet> twinsToRemove = new HashSet<>();
        for(int peerPos: colPeers){
            if( s.domains[peerPos].cardinality() == 2 ){
                for(int peers2: colPeers2){
                    BitSet thisBS = (s.domains[peerPos]);
                    if(thisBS.equals(s.domains[peers2]) && peers2!=peerPos){
                        if(Sudoku.debug>0)  System.out.println("YES! found twins");
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
        colPeers2 = s.getRowPeers(pos);
        twinsToRemove = new HashSet<>();
        for(int peerPos: colPeers){
            if( s.domains[peerPos].cardinality() == 2 ){
                for(int peers2: colPeers2){
                    BitSet thisBS = (s.domains[peerPos]);
                    if(thisBS.equals(s.domains[peers2]) && peers2!=peerPos){
                        if(Sudoku.debug>0) System.out.println("YES! found twins");
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
        colPeers2 = s.getBoxPeers(pos);
        twinsToRemove = new HashSet<>();
        for(int peerPos: colPeers){
            if( s.domains[peerPos].cardinality() == 2 ){
                for(int peers2: colPeers2){
                    BitSet thisBS = (s.domains[peerPos]);
                    if(thisBS.equals(s.domains[peers2]) && peers2!=peerPos){
                        if(Sudoku.debug>0) System.out.println("YES! found twins");
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
