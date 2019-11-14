/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Logic.SimplexAlg;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import simplexprogram.SimplexProgram;
/**
 *
 * @author German le yo
 */
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
public class GUI implements ActionListener{
    
    private SimplexProgram simplexProgram;
    private JFrame frame;
    private JPanel labelPanel, systemPanel, FOPanel, BPanel, GPanel;
    private GridLayout labelLayout, systemLayout, FOLayout, BLayout;
    private JTextField[][] system;
    private JTextField[] FO, B;
    private JLabel[] labels;
    private JLabel infoLabel;
    GridBagConstraints c;
    
    private JButton buttonAddCol, buttonAddRow, buttonRemoveCol, buttonRemoveRow, solve;
    private int rows, cols;
    
    public GUI(SimplexProgram simplexProgram){
        this.simplexProgram = simplexProgram;
        
        rows = cols = 0;
        
        frame = new JFrame("Simplex Revisado");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        GPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        labelPanel = new JPanel(labelLayout = new GridLayout(1, 0));    
        //labelPanel.setBorder(new LineBorder(Color.BLACK));
        GPanel.add(labelPanel, c);
        
        c.gridx = 1;
        c.gridy = 0;
        JPanel tmp = new JPanel();
        tmp.add(new JLabel("b"));
        GPanel.add(tmp, c);
        
        //add(GPanel, new JLabel("b"));
        
        c.gridx = 0;
        c.gridy = 1;
        systemPanel = new JPanel(systemLayout = new GridLayout(1, 0));        
        GPanel.add(systemPanel, c);
        
        c.gridy = 1;
        c.gridx = 1;
        BPanel = new JPanel(BLayout = new GridLayout(1, 0));        
        GPanel.add(BPanel, c);
             
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 20;
        FOPanel = new JPanel(FOLayout = new GridLayout(1, 0));        
        GPanel.add(FOPanel, c);
        
        c.gridx = 1;
        c.gridy = 2;
        tmp = new JPanel();
        tmp.add(new JLabel("Z (a min)"));
        GPanel.add(tmp, c);
        
        //add(GPanel, new JLabel("Z (a min)"));
        
        frame.add(GPanel, BorderLayout.CENTER);
        
        tmp = new JPanel();
        
        buttonAddCol = new JButton("+ COL");
        buttonAddCol.addActionListener(this);
        tmp.add(buttonAddCol);
        
        buttonAddRow = new JButton("+ ROW");
        buttonAddRow.addActionListener(this);
        tmp.add(buttonAddRow);
        
        buttonRemoveCol = new JButton("- COL");
        buttonRemoveCol.addActionListener(this);
        tmp.add(buttonRemoveCol);
        
        buttonRemoveRow = new JButton("-  ROW");
        buttonRemoveRow.addActionListener(this);
        tmp.add(buttonRemoveRow);
        
        c.gridx = 0;
        c.gridy = 3;
        GPanel.add(tmp, c);
        
        
        solve = new JButton("Solve");
        solve.addActionListener(this);
        
        c.gridx = 1;
        c.gridy = 3;
        GPanel.add(solve, c);
        //add(GPanel, solve);
        
        
        frame.add(GPanel, BorderLayout.CENTER);
        frame.add(infoLabel = new JLabel(), BorderLayout.NORTH);
        
        system = new JTextField[0][0];
        FO = new JTextField[0];
        labels = new JLabel[0];
        B = new JTextField[0];
        
        update(1, 1);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(buttonAddCol)) addCol();
        else if(e.getSource().equals(buttonAddRow)) addRow();
        else if(e.getSource().equals(buttonRemoveCol)) removeCol();
        else if(e.getSource().equals(buttonRemoveRow)) removeRow();
        else if(e.getSource().equals(solve)) solve();
    }
    
    private void update(int newRows, int newCols){
        
        systemPanel.removeAll();
        systemLayout.setRows(newRows);
        systemLayout.setColumns(newCols);
        
        FOPanel.removeAll();
        FOLayout.setRows(1);
        FOLayout.setColumns(newCols);
        
        labelPanel.removeAll();
        labelLayout.setRows(1);
        labelLayout.setColumns(newCols);
        
        BPanel.removeAll();
        BLayout.setRows(newRows);
        BLayout.setColumns(1);
        
        JTextField[][] newSystem = new JTextField[newRows][newCols];
        JTextField[] newFO = new JTextField[newCols];
        JLabel[] newLabels = new JLabel[newCols];
        JTextField[] newB = new JTextField[newRows];
        
        int i = 0;
        for(; i < cols && i < newCols; i++){
            add(FOPanel, newFO[i] = FO[i]);
            add(labelPanel, newLabels[i] = labels[i]);
        }
        for(; i < newCols; i++){
            add(FOPanel, newFO[i] = getTextField());
            add(labelPanel, newLabels[i] = new JLabel("X_" + i));
        }
        
        i = 0;
        for(; i < rows && i < newRows; i++)
            add(BPanel, newB[i] = B[i]);
        for(; i < newRows; i++)
            add(BPanel, newB[i] = getTextField());
        
        i = 0;
        for(; i < cols && i < newRows; i++){
            int j = 0;
            for (; j > rows && j < newCols; j++)
                add(systemPanel, newSystem[i][j] = system[i][j]);
            for(;j < newCols; j++) 
                add(systemPanel, newSystem[i][j] = getTextField());
        }
        
        for(; i < newRows; i++)
            for (int j = 0; j < newCols; j++)
                add(systemPanel, newSystem[i][j] = getTextField());
        
        
        system = newSystem;
        FO = newFO;
        B = newB;
        labels = newLabels;
        
        cols = newCols;
        rows = newRows;
        
        frame.pack(); 
    }
    
    
    private void addCol(){
       update(rows, cols + 1);
    }
     
    private void addRow(){
        if(rows < cols) update(rows + 1, cols);
    }
    
    private void removeCol(){
        if(cols > 1) update(rows, cols - 1);
    }
    
    private void removeRow(){
        if(rows > 1) update(rows - 1, cols);
        
    }   
    
    private void solve(){
        
        SimplexAlg.AlgResult res = null;
        double[][] matrix = new double[rows][cols];
        double[] FO = new double[cols];
        double[] B = new double[rows];
        
        infoLabel.setText("");
        
        try{
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < cols; j++)
                    matrix[i][j] = Double.parseDouble(system[i][j].getText());
                   
            for(int i = 0; i < cols; i++)
                FO[i] = Double.parseDouble(this.FO[i].getText());
            
            for(int i = 0; i < rows; i++)
                B[i] = Double.parseDouble(this.B[i].getText());
               
        }catch(NumberFormatException ex){
            infoLabel.setText("Hay un campo no numérico :c");
            return;
        }
        
        res = simplexProgram.solveEqSistem(matrix, B, FO);
        
        new AnswerDialog(res);
        
        res.printHistorical();
        res.printSolution();
    }
    
    private JTextField getTextField(){
        JTextField field = new JTextField("0"){
            @Override
            public Dimension getMaximumSize(){
                return new Dimension(50, 20);
            }
            
            @Override
            public Dimension getPreferredSize(){
                return getMaximumSize();
            }
        };
        return field;
    }
    
    private void add(Container container, Component element){
        JPanel tmp = new JPanel();
        tmp.add(element);
        container.add(tmp);
    }
    
    private class AnswerDialog extends JDialog{
        
        JPanel answerPanel;
        JScrollPane scrollHistoric;
        
        public AnswerDialog(SimplexAlg.AlgResult result){
            super(frame);
            setTitle("Historico");
            setModal(true);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            
            answerPanel = new JPanel();
            answerPanel.setLayout(new GridLayout(result.historic.size(), 1));
            
            JPanel tmp, tmp2;
            BoxLayout tmpLayout;
            JTable table;
            DefaultTableModel tableModel;
            String[][] tableValues;
            
            for(SimplexAlg.HistoricElement element: result.historic){
                tmp = new JPanel();
                tmpLayout = new BoxLayout(tmp, BoxLayout.Y_AXIS);
                tmp.setLayout(tmpLayout);
                
                tmp2 = new JPanel();
                tmpLayout = new BoxLayout(tmp2, BoxLayout.X_AXIS);
                tmp2.setLayout(tmpLayout);
                tmp2.add(new JLabel("Operation:"));
                tmp2.add(new JLabel(element.operation.name()));
                
                tmp.add(tmp2);
                
                tmp2 = new JPanel();
                tmpLayout = new BoxLayout(tmp2, BoxLayout.X_AXIS);
                tmp2.setLayout(tmpLayout);
                
                tmp2.add(new JLabel("Is Auxiliar:"));                
                tmp2.add(new JLabel(String.valueOf(element.state.isAuxiliar)));
                
                
                
                tmp.add(tmp2);
                
                table = getTable(element.state.A, element.state.COLS);
                tmp.add(new JLabel("A:"));
                tmp.add(table);
                
                table = getTable(element.state.b, element.state.ROWS);
                tmp.add(new JLabel("b:"));
                tmp.add(table);
                
                table = getTable(element.state.FO, element.state.COLS);
                tmp.add(new JLabel("c:"));
                tmp.add(table);
                
                
                if(element.state.BInv != null){
                    table = getTable(element.state.BInv, element.state.ROWS);              
                    tmp.add(new JLabel("B inversa:"));
                    tmp.add(table);
                }
                
                if(element.state.base != null){
                    table = getTable(element.state.base, element.state.ROWS);
                    tmp.add(new JLabel("Columnas de la base: "));
                    tmp.add(table);
                }                
                
                tmp.add(tmp2);
                
                if(result.state.Z != Double.NaN) {
                    tmp.add(new JLabel("Z: " + result.state.Z));
                }
                
                tmp.setBorder(new LineBorder(Color.BLACK));
                answerPanel.add(tmp);
            }
            
            scrollHistoric = new JScrollPane(answerPanel) {
                @Override
                public Dimension getPreferredSize(){
                    return new Dimension(500, 300);
                }                
            };
            add(scrollHistoric, BorderLayout.CENTER);
            
            if(result.resultType == SimplexAlg.RESULT_TYPE.OK){
                double[] solution = result.state.fullSolution();
            
                tmp = new JPanel(new GridLayout(solution.length + 2, 1));
                tmp.add(new JLabel("Solución: "));

                for(int i = 0; i < solution.length; i++){
                    tmp.add(new JLabel("X_" + i + ": " + String.valueOf(solution[i])));
                }

                tmp.add(new JLabel("Z: " + result.state.Z));

                add(tmp, BorderLayout.EAST);
            }
            
            String labelValue = "Tipo de solución: ";
            switch(result.resultType){
                case OK: labelValue += "Con solución ´óptima"; break;
                case NO_ACOTADA: labelValue += "No acotada"; break;
                case NO_FCF: labelValue += "No hay solución"; break;
            }
            
            add(new JLabel(labelValue), BorderLayout.SOUTH);
            
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
        
        
        private DefaultTableModel getModel(String[][] values, int cols){
            return new DefaultTableModel(values, new String[cols]){
                @Override
                public boolean isCellEditable(int row, int col){
                    return false;
                }
            };
        }
        
        private JTable getTable(double[][] info, int cols){
            String[][] tableValues = matrixToStringArr(info);
            return new JTable(getModel(tableValues, cols));
        }
        
        private JTable getTable(double[] info, int cols){
            String[] tableValues = arrToStringArr(info);
            return new JTable(getModel(new String[][]{tableValues}, cols));
        }
        
        private JTable getTable(int[] info, int cols){
            String[] tableValues = arrToStringArr(info);
            return new JTable(getModel(new String[][]{tableValues}, cols));
        }
        
        private String[][] matrixToStringArr(double[][] arr){
            String[][] exit = new String[arr.length][];
            for(int i = 0; i < exit.length; i++){
                exit[i] = new String[arr[i].length];
                for(int j = 0; j < exit[i].length; j++){
                    exit[i][j] = String.valueOf(arr[i][j]);
                }
            }
            return exit;
        }

        private String[] arrToStringArr(double[] arr){
            String[] exit = new String[arr.length];
            for(int i = 0; i < exit.length; i++){
                exit[i] = String.valueOf(arr[i]);
            }
            return exit;
        }
        
        private String[] arrToStringArr(int[] arr){
            String[] exit = new String[arr.length];
            for(int i = 0; i < exit.length; i++){
                exit[i] = String.valueOf(arr[i]);
            }
            return exit;
        }
    }
}
