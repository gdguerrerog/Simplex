/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.util.Arrays;
import simplexprogram.SimplexProgram;
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
    public final double[] cBasicos;
    public final double[] PI;
    public final double[] c;
    public final double Z;
    
    public SimplexState(double[][] matrix, double[] FO, boolean isAuxiliar){
        if(FO.length != matrix[0].length - 1) 
            throw new IllegalArgumentException(
                    String.format("The lenght of FO is %d, but most be %d", FO.length, matrix[0].length - 1));
        this.FO = FO;
        this.A = A(matrix);
        this.b = b(matrix);
        this.ROWS = b.length;
        this.COLS = FO.length;
        this.isAuxiliar = isAuxiliar;
        this.base = null;
        this.BInv = null;
        this.cBasicos = null;
        this.PI = null;
        this.Z = Double.NaN;
        this.c = null;
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
        this.cBasicos = null;
        this.PI = null;
        this.Z = Double.NaN;
        this.c = null;
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
        this.cBasicos = cBasicos();
        this.PI = PI();
        this.Z = Z();
        this.c = c();
    }
    
    private double[][] A(double[][] matrix){
        double[][] exit = new double[matrix.length][];
        for(int i = 0; i < matrix.length; i++){
            exit[i] = new double[matrix[i].length - 1];
            for(int j = 0; j < matrix[i].length - 1; j++) exit[i][j] = matrix[i][j];       
        }
        return exit;
    }
    
    private double[] b(double[][] matrix){
        double[] exit = new double[matrix.length];
        
        for(int i = 0; i < matrix.length; i++) exit[i] = matrix[i][matrix[i].length - 1];
        
        return exit;
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
    
    private double[] PI(){
        return MatrixUtils.multiplyVector(cBasicos, BInv);
    }
    
    private double[] cBasicos(){
        double[] exit = new double[base.length];
        for(int i = 0; i < base.length; i++) 
            exit[i] = FO[base[i]];
        return exit;
    }
    
    public double[] solution(){
        return MatrixUtils.multiplyVector(BInv, b);
    }
    
    private double Z(){
        double result = 0;
        for(int i = 0; i< PI.length; i++) result += PI[i]*b[i];
        return result;
    }
    
    private double[] c(){
        return MatrixUtils.subtract(FO, MatrixUtils.multiplyVector(PI, A));
    }
    
    @Override
    public String toString(){
        String exit = String.format("Aux: %b\nA:\n", isAuxiliar);
        exit += SimplexProgram.matrixToString(A) + "\nb:";
        exit += Arrays.toString(b) + "\nBase:\n";
        exit += Arrays.toString(base) + "\nBInv:\n";
        exit += SimplexProgram.matrixToString(BInv) + "\nFO:\n";
        exit += Arrays.toString(FO) + "\ncBásicos:";
        exit += Arrays.toString(cBasicos) + "\nPI: ";
        exit += Arrays.toString(PI) + "\nZ: " + Z + "\nc:";
        exit += Arrays.toString(c);
        return exit;
    }
    
}
