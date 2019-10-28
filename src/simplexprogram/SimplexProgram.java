/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexprogram;

import GUI.CommandGUI;
import Logic.MatrixUtils;
import Logic.SimplexAlg;
import Logic.SimplexState;
import simplexprogram.Result.*;

/**
 *
 * @author German le yo
 */
public class SimplexProgram {
    
    private SimplexProgram(){
        new CommandGUI(this);
    }
    
    public static String matrixToString(double[][] matrix){
        String exit = "";
        for(double[] row: matrix){
            for(double col: row) exit += String.format("%.2f\t", col);
            exit += "\n";
        }
        return exit;
    }    
    
    public SolveSystemResult solveEqSistem(double[][] matrix, double[] FO){
        new SimplexAlg(new SimplexState(matrix, FO)).solve();
        return new SolveSystemResult(true, FO);
    }
    
    public static void main(String[] args) {
        new SimplexProgram();
        //System.out.println(matrixToString(MatrixUtils.multiply(new double[][]{{1, 2}, {2, 3}}, 
        //        new double[][]{{1, 2, 5}, {3, 1, 0}})));
        //System.out.println(matrixToString(MatrixUtils.trans(new double[][]{{1, 2}, {23, 22}, {100, 1}, {3, 2}})));
    }
    
}
