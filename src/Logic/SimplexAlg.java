/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import simplexprogram.SimplexProgram;

/**
 *
 * @author German le yo
 */
public class SimplexAlg {
    
    public static enum OP_TYPES{
        INITIAL, VERIFY_NON_NEGATIVE;
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
        List<Integer> auxiliarVarRows = auxiliarVarRows();
        
        if(!auxiliarVarRows.isEmpty()) FASE_I(auxiliarVarRows);
        FASE_II();
        
        
        //printHistorical();
    }
    
    private void FASE_I(List<Integer> auxiliarVarRows){
        System.out.println("FASE I");
        
        double[][] auxiliarA = new double[current.A.length]
                [current.A[0].length + auxiliarVarRows.size()];
        for(int i = 0; i < auxiliarA.length; i++) 
            System.arraycopy(current.A[i], 0, auxiliarA[i], 0, current.A[i].length);
        
        double[] auxiliarFO = new double[current.FO.length + auxiliarVarRows.size()];
        
        int cCount = 0;
        for(Integer i : auxiliarVarRows){
            auxiliarA[i][current.A[i].length + cCount] = 1;
            auxiliarFO[current.A[i].length + cCount] = 1;
            cCount++;
        }
        
        SimplexAlg auxiliar = new SimplexAlg(new SimplexState(auxiliarA, current.b, auxiliarFO, false));
        auxiliar.solve();
        
    }
    private void FASE_II(){
        System.out.println("FASE II");
        
        int[] BIndexes = BIndexes();
        double[][] BInv = MatrixUtils.identity(BIndexes.length, BIndexes.length);
        double[] cBasicos = cBasicos(BIndexes);
        double[] PI = PI(cBasicos, BInv);
        double[] newC = MatrixUtils.subtract(cBasicos, MatrixUtils.multiplyVector(PI, current.A));
        
        System.out.println("Printring");
        System.out.println(SimplexProgram.matrixToString(current.getFullMatrix()));
        System.out.println(Arrays.toString(BIndexes));
        System.out.println(Arrays.toString(cBasicos));
        System.out.println(Arrays.toString(newC));
        System.out.println(Arrays.toString(current.FO));
        
    }
    
    private double[] PI(double[] cBasicos, double[][] BInv){
        return MatrixUtils.multiplyVector(cBasicos, BInv);
    }
    
    private int[] BIndexes(){
        int[] exit = new int[current.ROWS];
        boolean colValid;
        int rowCol;
        for(int i = 0; i < current.A.length; i++){
            rowCol = -1;
            for(int j = 0; j < current.A[i].length && rowCol == -1; j++){             
                if(current.A[i][j] == 1) {
                    colValid = true;
                    for (int k = 0; k < current.A.length && colValid; k++) 
                        if(k != i && current.A[k][j] != 0) colValid = false;
                    
                    if(colValid) rowCol = j;
                }
            }
            if(rowCol != -1) exit[i] = rowCol;
        }
        return exit;
    }
    
    private void verifyBNonNegative(){
        double[][] tmpA;
        double[] tmpB;
        for(int i = 0; i < current.b.length; i++) if (current.b[i] < 0){
            tmpA = MatrixUtils.multiplyRow(current.A, i, -1);
            tmpB = MatrixUtils.copy(new double[][]{current.b})[0];
            tmpB[i] *= -1;
            setCurrent(new SimplexState(tmpA, tmpB, current.FO, current.isAuxiliar), 
                    OP_TYPES.VERIFY_NON_NEGATIVE);
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
    
    private List<Integer> auxiliarVarRows(){
        LinkedList<Integer> rowAuxiliarIndex = new LinkedList();
        boolean colValid, rowValid;
        for(int i = 0; i < current.A.length; i++){
            rowValid = false;
            for(int j = 0; j < current.A[i].length && !rowValid; j++){             
                if(current.A[i][j] == 1) {
                    colValid = true;
                    for (int k = 0; k < current.A.length && colValid; k++) 
                        if(k != i && current.A[k][j] != 0) colValid = false;
                    
                    if(colValid) rowValid = true;
                }
            }
            if(!rowValid) rowAuxiliarIndex.add(i);
        }
        return rowAuxiliarIndex;
    }
    
    private double[] cBasicos(int[] BIndexes){
        double[] exit = new double[BIndexes.length];
        for(int i = 0; i < BIndexes.length; i++) 
            exit[i] = current.FO[BIndexes[i]];
        return exit;
    }
    
}
