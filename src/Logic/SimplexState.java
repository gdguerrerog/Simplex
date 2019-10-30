/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.util.Arrays;
/**
 *
 * @author German le yo
 */
public class SimplexState {
    
    public final double[][] A;
    public final double[] b;
    public final double [] FO;
    public final int ROWS, COLS;
    public final boolean isAuxiliar;
    public final int[] base;
    public final double[][] BInv;
    
    public SimplexState(double[][] matrix, double[] FO, boolean isAuxiliar){
        if(FO.length != matrix[0].length - 1) 
            throw new IllegalArgumentException(
                    String.format("The lenght of FO is %d, but most be %d", FO.length, matrix[0].length - 1));
        
        this.FO = FO;
        A = new double[matrix.length][];
        b = new double[matrix.length];
        for(int i = 0; i < matrix.length; i++){
            A[i] = new double[matrix[i].length - 1];
            for(int j = 0; j < matrix[i].length; j++){
                if(j == matrix[i].length - 1) b[i] = matrix[i][j];
                else A[i][j] = matrix[i][j];
            } 
        }
        ROWS = b.length;
        COLS = FO.length;
        this.isAuxiliar = isAuxiliar;
        this.base = null;
        this.BInv = null;
        
    }
    
    
    public SimplexState(double[][] A, double[] b, double [] FO, boolean isAuxiliar){
        this.A = A;
        this.b = b;
        this.FO = FO;
        ROWS = b.length;
        COLS = FO.length;
        this.isAuxiliar = isAuxiliar;
        this.base = null;
        this.BInv = null;
    }
    
    public SimplexState(SimplexState lastState, int[] base, double[][] BInv){
        this.A = lastState.A;
        this.b = lastState.b;
        this.FO = lastState.FO;
        this.isAuxiliar = lastState.isAuxiliar;
        this.ROWS = lastState.ROWS;
        this.COLS = lastState.COLS;
        this.base = base;
        this.BInv = BInv;
    }
    
    public double[][] getFullMatrix(){
        double[][] exit = new double[A.length][];
        for(int i = 0; i < A.length; i++){
            exit[i] = new double[A[i].length + 1];
            System.arraycopy(A[i], 0, exit[i], 0, A[i].length);
            exit[i][exit[i].length - 1] = b[i];
        }
        return exit;
    }
}
