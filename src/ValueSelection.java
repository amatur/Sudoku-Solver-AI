
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tasnim
 */

public class ValueSelection {
    public static class CountTuple {
        int domainValue;
        int count;

        public CountTuple() {
            this.domainValue = domainValue;
            this.count = count;
        }
        
    }

    public static class MyComparator implements Comparator<CountTuple> {
        public int compare(CountTuple x, CountTuple y) {
            return x.count - y.count;
        }
    }
    
    /*In this heuristic, we choose the value which make the least trouble for the chosen cell peers; 
    for example, if in our cell we have the possibilities 7 and 9, we check in how many of this cell's peers 7 appears in the domain, 
    and we do the same for 9; the algorithm then chooses the number that least appears in the cell's peers' domain.
    */
    public static Integer LRV(Sudoku s, int pos){
        PriorityQueue minHeap = new PriorityQueue(10, new MyComparator());
        BitSet bs = s.domains[pos];
        ArrayList<Integer> domains = new ArrayList<>();
        ArrayList<Integer> domainsCount = new ArrayList<>();
        // To iterate over the true bits in a BitSet, use the following loop:
        for (int i = bs.nextSetBit(0); i < Sudoku.N; i = bs.nextSetBit(i+1)) {
           // operate on index i here
            domains.add(i+1);
        }
        
        HashSet<Integer> peers = s.getPeers(pos);
        
        for (int i=0; i<domains.size(); i++){
            int count = 0;
            int valueExamined = domains.get(i);
            for(Integer peerPos: peers){
                BitSet oneSetBit = new BitSet(Sudoku.N);
                BitSet peerDomain = s.domains[peerPos];
                oneSetBit.set(valueExamined-1);
                peerDomain.and(oneSetBit);
                
                //so peer's domain has that bit true
                if(peerDomain.equals(oneSetBit)){
                    count++;
                }
            }
            domainsCount.add(i, new Integer(count));
            minHeap.add(new CountTuple())
        }
        
        int min = 999999;
        int retValue = -1;
        for (int i=0; i<domains.size(); i++){
            if(domainsCount.get(i)<min){
                min = domainsCount.get(i);
                retValue = domains.get(i);
            }
        }
        
        
        if(retValue==-1){            
            return null;
        }
        return retValue;
    }
    
    public static ArrayList<Integer> randomOrder(Sudoku s, int pos){
        BitSet bs = s.domains[pos];
        ArrayList<Integer> domains = new ArrayList<>();
        // To iterate over the true bits in a BitSet, use the following loop:
        for (int i = bs.nextSetBit(0); i < Sudoku.N; i = bs.nextSetBit(i+1)) {
           // operate on index i here
            domains.add(i+1);
        }
        Collections.shuffle(domains, new Random());
        return domains;
    }
    
    public static ArrayList<Integer> firstOrder(Sudoku s, int pos){
        BitSet bs = s.domains[pos];
        ArrayList<Integer> domains = new ArrayList<>();
        // To iterate over the true bits in a BitSet, use the following loop:
        for (int i = bs.nextSetBit(0); i < Sudoku.N; i = bs.nextSetBit(i+1)) {
           // operate on index i here
            domains.add(i+1);
        }
        return domains;
    }
}
