/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
public class GUI implements ActionListener{
    
    private SimplexProgram simplexProgram;
    private JFrame frame;
    private JPanel labelPanel, systemPanel, FOPanel, BPanel, GPanel;
    private GridLayout labelLayout, systemLayout, FOLayout, BLayout;
    private JTextField[][] system;
    private JTextField[] FO, B;
    private JLabel[] labels;
    
    private JButton buttonAddCol, buttonAddRow, buttonRemoveCol, buttonRemoveRow;
    private int rows, cols;
    
    public GUI(SimplexProgram simplexProgram){
        this.simplexProgram = simplexProgram;
        
        rows = cols = 0;
        
        frame = new JFrame("Simplex Revisado");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GPanel = new JPanel(new BorderLayout());
        
        systemPanel = new JPanel(systemLayout = new GridLayout(1, 0));        
        GPanel.add(systemPanel, BorderLayout.CENTER);
        
        labelPanel = new JPanel(labelLayout = new GridLayout(1, 0));        
        GPanel.add(labelPanel, BorderLayout.NORTH);
        
        FOPanel = new JPanel(FOLayout = new GridLayout(1, 0));        
        GPanel.add(FOPanel, BorderLayout.SOUTH);
        
        BPanel = new JPanel(BLayout = new GridLayout(1, 0));        
        GPanel.add(FOPanel, BorderLayout.EAST);
        
        frame.add(GPanel, BorderLayout.CENTER);
        
        JPanel tmp = new JPanel();
        BoxLayout tmpLayout = new BoxLayout(tmp, BoxLayout.Y_AXIS);
        tmp.setLayout(tmpLayout);
        
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
        
        frame.add(tmp, BorderLayout.EAST);
        
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
            FOPanel.add(newFO[i] = FO[i]);
            labelPanel.add(newLabels[i] = labels[i]);
        }
        for(; i < newCols; i++){
            FOPanel.add(newFO[i] = new JTextField());
            labelPanel.add(newLabels[i] = new JLabel("X_" + i));
        }
        
        i = 0;
        for(; i < rows && i < newRows; i++)
            BPanel.add(newB[i] = B[i]);
        for(; i < newRows; i++)
            BPanel.add(newB[i] = new JTextField());
        
        i = 0;
        for(; i < cols && i < newRows; i++){
            int j = 0;
            for (; j > rows && j < newCols; j++)
                systemPanel.add(newSystem[i][j] = system[i][j]);
            for(;j < newCols; j++) 
                systemPanel.add(newSystem[i][j] = new JTextField());
        }
        
        for(; i < newRows; i++)
            for (int j = 0; j < newCols; j++) 
                systemPanel.add(newSystem[i][j] = new JTextField());
        
        
        system = newSystem;
        FO = newFO;
        B = newB;
        labels = newLabels;
        
        cols = newCols;
        rows = newRows;
        
        frame.revalidate();
        frame.repaint();
        
    }
    
    
    private void addCol(){
       update(rows, cols + 1);
    }
     
    private void addRow(){
        update(rows + 1, cols);
    }
    
    private void removeCol(){
        if(cols > 1){
            update(rows, cols - 1);
        }
    }
    
    private void removeRow(){
        if(rows > 1){
            update(rows - 1, cols);
        }
    }   
}
