
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

        public CountTuple(int domainValue, int count) {
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
    public static ArrayList<Integer> LRV(Sudoku s, int pos){
        PriorityQueue minHeap = new PriorityQueue(10, new MyComparator());
        BitSet bs = s.domains[pos];
        ArrayList<Integer> domains = new ArrayList<>();
        ArrayList<Integer> domainsCount = new ArrayList<>();
        // To iterate over the true bits in a BitSet, use the following loop:
        for (int i = bs.nextSetBit(0); i >=0; i = bs.nextSetBit(i+1)) {
           // operate on index i here
            domains.add(i+1);
            if (i == Integer.MAX_VALUE) {
                break; // or (i+1) would overflow
            }
        }
        
        HashSet<Integer> peers = s.getPeers(pos);
        
        for (int i=0; i<domains.size(); i++){
            int count = 0;
            int valueExamined = domains.get(i);
            for(Integer peerPos: peers){
                BitSet oneSetBit = new BitSet(Sudoku.N);
                BitSet peerDomain = new BitSet(Sudoku.N);
                 peerDomain.clear();
                 peerDomain.or( s.domains[peerPos]);
                oneSetBit.set(valueExamined-1);
                peerDomain.and(oneSetBit);
                
                //so peer's domain has that bit true
                if(peerDomain.equals(oneSetBit)){
                    count++;
                }
            }
            domainsCount.add(new Integer(count));
            minHeap.add(new CountTuple(valueExamined, count));
        }
       
        ArrayList<Integer> al = new ArrayList<>();
        while(!minHeap.isEmpty()){
            al.add(((CountTuple)minHeap.peek()).domainValue);
            minHeap.remove();
        }
         //System.out.println("DOmains" + domains);
       //  System.out.println("DOmainsCOUNT" + domainsCount);
        return al;
    }
    
    public static ArrayList<Integer> randomOrder(Sudoku s, int pos){
        BitSet bs = s.domains[pos];
        ArrayList<Integer> domains = new ArrayList<>();
        // To iterate over the true bits in a BitSet, use the following loop:
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
           // /operate on index i here
            domains.add(i+1);
            if (i == Integer.MAX_VALUE) {
                break; // or (i+1) would overflow
            }
        }
        Collections.shuffle(domains, new Random());
        System.out.println(domains);
        return domains;
    }
    
    public static ArrayList<Integer> firstOrder(Sudoku s, int pos){
        BitSet bs = s.domains[pos];
        ArrayList<Integer> domains = new ArrayList<>();
        // To iterate over the true bits in a BitSet, use the following loop:
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
           // operate on index i here
            domains.add(i+1);
            if (i == Integer.MAX_VALUE) {
                break; // or (i+1) would overflow
            }
        }
         System.out.println("DOmains" + domains);
        return domains;
    }
}
