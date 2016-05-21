
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
    
    public static void singleton(Sudoku s, int pos){
        
        HashSet<Integer> peers = s.getPeers(pos);
        
        for(int peerPos: peers){
            int count[] = new int[Sudoku.N];
            
            
        }
                
        
    }
    
    
}
