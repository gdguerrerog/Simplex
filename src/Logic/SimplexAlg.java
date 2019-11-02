/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author German le yo
 */
public class SimplexAlg {
    
    public static enum OP_TYPES{
        INITIAL, VERIFY_NON_NEGATIVE, ITERATION;
    }
    
    public static class HistoricElement{
        public final SimplexState state;
        public final OP_TYPES operation;
        private HistoricElement(SimplexState state, OP_TYPES operation){
            this.state = state;
            this.operation = operation;
        }
        
        @Override
        public String toString(){
            return "";
        }
    }
    
    public enum RESULT_TYPE{
        OK, NO_FCF, NO_ACOTADA;
    }
    
    public class AlgResult{
        public final RESULT_TYPE resultType;
        public final SimplexState state;
        public final LinkedList<HistoricElement> historic;
        public AlgResult(RESULT_TYPE resultType, SimplexState state, LinkedList<HistoricElement> historic){
            this.resultType = resultType;
            this.state = state;
            this.historic = historic;
        }
        
        public void printSolution(){
            if(resultType != RESULT_TYPE.OK) System.out.println(resultType.name());
            else {
                double[] solution = state.solution();
                for(int i = 0; i < state.ROWS; i++) 
                    System.out.printf("X_%d = %f\n", state.base[i], solution[i]);
            }  
        }
        
        public void printHistorical(){
            for(HistoricElement hisElement: historic) {
                System.out.println("\n\n--------------------------------");
                System.out.printf("Operation: %s, State:\n", hisElement.operation.name());
                System.out.println(hisElement.state); 
            }      
        }
    }
    
    private final LinkedList<HistoricElement> historic;
    private SimplexState current;
    
    public SimplexAlg(SimplexState initialState){
        historic = new LinkedList();
        setCurrent(initialState, OP_TYPES.INITIAL);  
    }
    
    public SimplexAlg(SimplexState initialState, LinkedList<HistoricElement> historic){
        this.historic = historic;
        setCurrent(initialState, OP_TYPES.INITIAL); 
    }
    
    public List<HistoricElement> getHistoric(){
        return historic;
    }
    
    public AlgResult solve(){
        
        verifyBNonNegative();
        Pair<int[], List<Integer>> pair = verifyFCF();
        
        if(!pair.getValue().isEmpty()) { //No esta en FCF, pues hay variables sin pivote
            AlgResult res = FASE_I(pair.getValue());
            if(res.resultType != RESULT_TYPE.OK) return res;
            return FASE_II(res.state.base, res.state.BInv);
        }else return FASE_II(pair.getKey(), MatrixUtils.identity(current.ROWS, current.ROWS));

    }
    
    private AlgResult FASE_I(List<Integer> auxiliarVarRows){
        System.out.println("Fase I");
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
        
        SimplexAlg auxiliar = new SimplexAlg(new SimplexState(auxiliarA, current.b, auxiliarFO, true), historic);
        AlgResult res = auxiliar.solve();
        return res;
    }
    private AlgResult FASE_II(int[] initialBase, double[][] initialInv){
        System.out.println("Fase II");
        setCurrent(new SimplexState(current, initialBase, initialInv), OP_TYPES.ITERATION);

        int colPivote;
        
        Pair<Integer, double[]> rowAndNewA;
        Pair<double[][], int[]> invAndNewBase;
        
        while(true){         
            if(pruebaOptimalidad(current.c)){
                if(current.isAuxiliar && current.Z != 0) return new AlgResult(RESULT_TYPE.NO_FCF, current, historic);
                else return new AlgResult(RESULT_TYPE.OK, current, historic);
            }
            colPivote = selectColPivote();
            rowAndNewA = selectFilaPivote(colPivote);
            
            if(verifySolucionNoAcotada(rowAndNewA.getValue())) return new AlgResult(RESULT_TYPE.NO_ACOTADA, current, historic);
            
            invAndNewBase = calcNewBInv(rowAndNewA.getValue(), rowAndNewA.getKey(), colPivote);
            setCurrent(new SimplexState(current, invAndNewBase.getValue(), invAndNewBase.getKey()), OP_TYPES.ITERATION);
            
        }      
    }
    
    
    
    
    /**
     * 
     * @return una pareja que contiene o la base, o la lista de las filas que necesitan
     * una varable auxiliar
     */
    private Pair<int[], List<Integer>> verifyFCF(){
    
        // Contiene las filas donde es necesario añadir una variable auxiliar
        // Si esta vacia el problema esta en FCF pues no se necesitan variables auxiliares
        LinkedList<Integer> rowAuxiliarIndex = new LinkedList(); 
        
        // Dice donde esta el pivote en cada fila. 
        int[] pivotes = new int[current.ROWS];
        for(int i = 0; i < pivotes.length; i++) pivotes[i] = -1;
        
        // Si ya hay una base definida, cambie la matriz A por 
        // la obtenida con esa base
        double[][] A;
        if(current.BInv != null) A = MatrixUtils.multiply(current.BInv, current.A);
        else A = current.A;
        
        // Para saber si una columna puede ser pivote. No lo sera si contiene un valor
        // diferente de 0 o 1
        boolean[] discartedPivote = new boolean[current.COLS];
        for(int i = 0; i < A.length; i++){
            for (int j = 0; pivotes[i] == -1 && j < A[i].length; j++) if(A[i][j] == 1){
                for (int k = 0; k < A.length && !discartedPivote[j]; k++) 
                    if(k != i && current.A[k][j] != 0)
                        discartedPivote[j] = true;
                
               // Si la columna puede ser pivote de la fila i
               // notese que en este punto el elemento j de la fila i es 1
                if(!discartedPivote[j]) { 
                    pivotes[i] = j;
                    discartedPivote[j] = true;
                }
            }
            // Si no se encontro una columna pivote en esa fila
            // Como se añade un elemento a rowAuxiliarIndex, el problema no esta
            // en FCF
            if(pivotes[i] == -1) rowAuxiliarIndex.add(i);
        }
        return new Pair(pivotes, rowAuxiliarIndex);  
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
    
    private void setCurrent(SimplexState newState, OP_TYPES operation){
        historic.addLast(new HistoricElement(newState, operation));
        current = newState;
    }

    private boolean pruebaOptimalidad(double[] cNew){
        for(double c: cNew) if(c < 0) return false;
        return true;
    }
    
    private int selectColPivote(){
        int minValIndex = 0;
        for(int i = 1; i < current.c.length; i++) 
            if(current.c[i] < current.c[minValIndex])
                minValIndex = i;
        return minValIndex;
    }
    
    
    private Pair<Integer, double[]> selectFilaPivote(int colPivote){
        // Calculo de la columna A en la nueva base
        double[] newA = new double[current.ROWS];
        for(int i = 0; i < current.ROWS; i++) newA[i] = current.A[i][colPivote];
        newA = MatrixUtils.multiplyVector(current.BInv, newA);
        
        //Calculo de b en la nuv base
        double[] newb = MatrixUtils.multiplyVector(current.BInv, current.b);
        
        // Buscando el minimo
        int minIndex = -1;
        for(int i = 0; i < newA.length; i++)
            if(newA[i] > 0 && 
                (minIndex == -1 || newb[i]/newA[i] < newb[minIndex]/newA[minIndex]))minIndex = i;
        return new Pair(minIndex, newA);
    }
    
    private Pair<double[][], int[]> calcNewBInv(double[] newA, int rowPivote, int colPivote){
        
        int[] newBase = new int[current.base.length];
        for(int i = 0; i < newBase.length; i++)
            if(i == rowPivote) newBase[i] = colPivote; // Columna que cambia
            else newBase[i] = current.base[i];
        
        // Calculo de la matriz P
        double[][] P = MatrixUtils.identity(current.ROWS, current.ROWS);
        
        // Calculando columna RHO
        for(int i = 0; i < P.length; i++)
            if(i == rowPivote) P[i][rowPivote] = 1/newA[rowPivote];
            else P[i][rowPivote] = newA[i]/newA[rowPivote];   
        
        return new Pair(MatrixUtils.multiply(P, current.BInv), newBase);
    }
    
    private boolean verifySolucionNoAcotada(double[] newA){
        for(double a: newA) if(a > 0) return false;
        return true;
    }
    
}
