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
public class MatrixUtils {
    
    public static double[][] copy(double[][] matrix){
        double[][] exit = new double[matrix.length][];
        for(int i = 0; i < matrix.length; i++) {
            exit[i] = new double[matrix[i].length];
            System.arraycopy(matrix[i], 0, exit[i], 0, matrix[i].length);   
        }
                    
        return exit;
    }
    
    public static double[][] multiply(double[][] matrixA, double[][] matrixB){
        if(matrixA[0].length != matrixB.length) 
            throw new IllegalArgumentException("Sizes of matrix differ");
        
        double[][] exit = new double[matrixA.length][matrixB[0].length];
        for(int i = 0; i < exit.length; i++)   
            for(int j = 0; j < exit[i].length; j++)
                for(int k = 0; k < matrixB.length; k++)
                    exit[i][j] += matrixA[i][k]*matrixB[k][j];
        
        return exit;
    }
    
    public static double[][] trans(double[][] matrix){
        double[][] exit = new double[matrix[0].length][matrix.length];
        for(int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++) 
                exit[j][i] = matrix[i][j];
        return exit;
    }
    
    public static double[][] multiplyRow(double[][] matrix, int row, double factor){
        double[][] exit = copy(matrix);
        for(int i  = 0; i < matrix[row].length; i++) 
            exit[row][i] *= factor;
        return exit;
    }
    
}


