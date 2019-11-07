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
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
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
        //frame.revalidate();
        //frame.repaint();
        
    }
    
    
    private void addCol(){
       update(rows, cols + 1);
    }
     
    private void addRow(){
        update(rows + 1, cols);
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
    
    private void add(Container container, Component element, GridBagConstraints c){
        JPanel tmp = new JPanel();
        tmp.add(element);
        container.add(tmp, c);
    }
}
