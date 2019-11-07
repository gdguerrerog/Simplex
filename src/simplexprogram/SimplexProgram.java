/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexprogram;

import GUI.CommandGUI;
import GUI.GUI;
import Logic.MatrixUtils;
import Logic.SimplexAlg;
import Logic.SimplexState;
import java.util.Arrays;

/**
 *
 * @author German le yo
 */
public class SimplexProgram {
    
    private SimplexProgram(){
        //new CommandGUI(this);
        new GUI(this);
    }
    
    public static String matrixToString(double[][] matrix){
        if(matrix == null) return "NULL";
        String exit = "";
        for (int i = 0; i < matrix.length; i++) {
            if(i != 0) exit += "\n";
            for(double col: matrix[i]) exit += String.format("%.2f\t", col);
        }
        return exit;
    }    
    
    public SimplexAlg.AlgResult solveEqSistem(double[][] matrix, double[] b, double[] FO){
        SimplexAlg alg = new SimplexAlg(new SimplexState(matrix, b, FO, false));
        
        try{
            SimplexAlg.AlgResult res = alg.solve();
            return res;
        }catch(Exception ex){
            return null;
        }
    }
    
    public static void main(String[] args) {
        new SimplexProgram();
    }
    
}
