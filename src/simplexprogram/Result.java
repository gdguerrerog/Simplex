/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexprogram;

/**
 *
 * @author German le yo
 */
public class Result {
    
    public final boolean OK;
    
    public Result(boolean OK){
        this.OK = OK;
    }
    
    public static class SolveSystemResult extends Result{
        
        public final double[] values;
        
        public SolveSystemResult(boolean OK, double[] values){
            super(OK);
            this.values = values;
        }
    }
}
