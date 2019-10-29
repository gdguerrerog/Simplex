/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.util.LinkedList;
import java.util.List;
import simplexprogram.SimplexProgram;

/**
 *
 * @author German le yo
 */
public class SimplexAlg {
    
    public static enum OP_TYPES{
        INITIAL, VERFY_NON_NEGATIVE;
    }
    
    public static class HistoricElement{
        public final SimplexState state;
        public final OP_TYPES operation;
        private HistoricElement(SimplexState state, OP_TYPES operation){
            this.state = state;
            this.operation = operation;
        }
    }
    
    private LinkedList<HistoricElement> historic;
    private SimplexState current;
    
    
    public SimplexAlg(SimplexState initialState){
        historic = new LinkedList();
        setCurrent(initialState, OP_TYPES.INITIAL);    
    }
    
    public List<HistoricElement> getHistoric(){
        return historic;
    }
    
    public void solve(){
        
        System.out.println(
            SimplexProgram.matrixToString(current.getFullMatrix()) +"\n\n"); 
        
        verifyBNonNegative();
        
        if(!isFCF())FASE_I();
        FASE_II();
        
        
        //printHistorical();
    }
    
    private void FASE_I(){
        System.out.println("FASE I");
    }
    private void FASE_II(){
        System.out.println("FASE II");
    }
    
    private void verifyBNonNegative(){
        double[][] tmpA;
        double[] tmpB;
        for(int i = 0; i < current.B.length; i++) if (current.B[i] < 0){
            tmpA = MatrixUtils.multiplyRow(current.A, i, -1);
            tmpB = MatrixUtils.copy(new double[][]{current.B})[0];
            tmpB[i] *= -1;
            setCurrent(new SimplexState(tmpA, tmpB, current.FO), OP_TYPES.VERFY_NON_NEGATIVE);
        }
    }
    
    
    public void printHistorical(){
        for(HistoricElement hisElement: historic)
            System.out.println(
                    SimplexProgram.matrixToString(hisElement.state.getFullMatrix()) +"\n\n");       
    }
    private void setCurrent(SimplexState newState, OP_TYPES operation){
        historic.addLast(new HistoricElement(newState, operation));
        current = newState;
    }
    
    private boolean isFCF(){
        boolean colValid, rowValid;
        for(int i = 0; i < current.A.length; i++){
            rowValid = false;
            for(int j = 0; j < current.A[i].length && !rowValid; j++){
                if(current.A[i][j] == 1) {
                    colValid = true;
                    for (int k = 0; k < current.A.length && colValid; k++) {
                        if(k != i && current.A[k][j] != 0) colValid = false;
                    }
                    if(colValid) rowValid = true;
                    System.out.printf("Col: %d, valid\n: %b", i, colValid);
                }
            }
            if(!rowValid) return false;
        }
        return true;
    }
    
    
    
    
    
    
}
