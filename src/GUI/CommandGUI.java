/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.Scanner;
import simplexprogram.SimplexProgram;
import simplexprogram.Result.*;

/**
 *
 * @author German le yo
 */
public class CommandGUI {
 
    private Scanner scan;
    private SimplexProgram main;
    
    private static final String WELCOME = "Bienvenido al solucionador de PPL con método simplex revisado";
    
    
    public CommandGUI(SimplexProgram main){
        scan = new Scanner(System.in).useLocale(java.util.Locale.US);
        this.main = main;
        System.out.println(WELCOME);
        mainMenu();
    }
    
    public void mainMenu(){
        System.out.println("Que desea hacer: ");
        System.out.println("1) Solucionar PPL con método simplex revisado");
        System.out.println("2) Salir");
        int readed = 1; // scan.nextInt();
        switch(readed){
            case 1: readEqSystem(); break;
            case 2: System.out.println("Bye");break;
            default : System.out.println("Opcion no reconocida."); mainMenu();
        }
    }
    
    public void readEqSystem(){
        
        if(true){
            
            double[][] testMatrix = 
            {   {1, 0, 0, 0, 2},
                {0, 1, 2, 1, 11}, 
                {0, 2, 1, 0, 4.4}};
            double[] testFO = 
                {1, 2, 4, 3};
            main.solveEqSistem(testMatrix, testFO);
            return;
        }
        
        int filas, columnas;
        System.out.println("Inserte la matriz de un sistema de ecuanciónes en forma canónica factible:");
        System.out.print("\t-Filas: ");
        filas = scan.nextInt();
        System.out.print("\t-Columnas: ");
        columnas = scan.nextInt();
        
        double[][] eqSystem = new double[filas][columnas];
        
        for(int i = 0; i < columnas - 1; i++) System.out.printf("X_%d\t", i);
        System.out.println("b_j");
        for(int i = 0; i < filas; i++)
            for(int j = 0; j < columnas; j++) 
                eqSystem[i][j] = scan.nextDouble();
        
        System.out.println("Escriba el vector de la función objetivo: ");
        
        double[] FO = new double[columnas - 1];
        
        for(int i = 0; i < columnas - 1; i++) FO[i] = scan.nextDouble();
        
        SolveSystemResult result = main.solveEqSistem(eqSystem, FO);
        
        if(result.OK){
            System.out.println("Problema Solucionado: ");
            for(int i = 0; i< result.values.length; i++)
                System.out.printf("X_%d = %f\n", i, result.values[i]);
        }else System.out.println("Error solucionando el problema :'c");
        
        mainMenu();    
    }
}
