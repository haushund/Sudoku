/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.awt.Color;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;

/**
 *
 * @author Guest
 */
public class UserInterface extends JFrame{
    
    private JTextField [][] GridField = new JTextField [9][9];
    private JPanel ControlPanel;
    private JButton Generate;
    private JButton  Check;
    private JButton  Solve;
    private JButton  Debug;
    private Core c;
    private int scx, scy;
    UserInterface (Core core){
        c = core;
    
        this.getContentPane().setLayout(null);
        
        scx= java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        scy = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        
        int width = (int) (scy * 0.85);
        int height = (int) (scy * 0.7);
        int posX = (int) (scx/2 - width/2);
        int posY = (int) (scy/2 - height/2);
        
        this.setBounds (posX, posY, width, height);
        
        for (int column = 0; column < 9; column++){
            for (int row = 0; row < 9; row++){
                GridField[column][row] = new JTextField ();
                this.add (GridField[column][row]);
                
                GridField[column][row].getDocument().addDocumentListener(new DocumentListener (){
                   @Override
                    public void insertUpdate(DocumentEvent e) {
                        updateView();
                    }
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        updateView();
                    }
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        updateView();
                    }
                });
            }
        }
        
        ControlPanel = new JPanel ();
        ControlPanel.setLayout(null);
        
        Generate = new JButton ("Generate");
        Generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenerateActionPerformed(evt);
            }
        });
        
        
        Check = new JButton ("Ckeck");
        Check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckActionPerformed(evt);
            }
        });
        
        Solve = new JButton ("Solve");
        Solve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SolveActionPerformed(evt);
            }
        });
        
        Debug = new JButton ("Debug");
        Debug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DebugActionPerformed(evt);
            }
        });
        
        
        ControlPanel.add(Generate);
        ControlPanel.add(Check);
        ControlPanel.add(Solve);
        ControlPanel.add(Debug);
        
        this.add(ControlPanel);
        
        this.updateView();
        this.setVisible(true);
    }
    
    void GenerateActionPerformed(java.awt.event.ActionEvent evt) {
          c.randomGrid(15); 
          updateUIGrid();
            
    }
    
    void CheckActionPerformed(java.awt.event.ActionEvent evt) {
        this.updateGrid();
        c.check();
           
    }
    
    void SolveActionPerformed(java.awt.event.ActionEvent evt) {
        c.solve();
        updateUIGrid ();
    }
    
    void DebugActionPerformed(java.awt.event.ActionEvent evt) {
       c.resetInput ();
       debugAction();
    }
    
    void debugAction (){
        if (!c.debugQueue.isEmpty()){ 
            List a = (List)c.debugQueue.poll();
            final int column = (int)a.get(0);
            final int row = (int)a.get(1);
            final int num = (int)a.get(2);
            new java.util.Timer().schedule(new java.util.TimerTask(){
                public void run(){
                    c.grid[column][row] = num;
                    updateUIGrid();
                    GridField[column][row].setBackground(Color.pink);
                    if (!c.check ()) {GridField[column][row].setBackground(Color.blue); new java.util.Timer().schedule(new java.util.TimerTask(){ public void run(){debugAction();}},200);}
                    else debugAction();
                }},200);
        
        }
    }
    
    void updateUIGrid (){
        for (int column = 0; column < 9; column++){
            for (int row = 0; row < 9; row++){
                GridField[column][row].setText(""+c.grid[column][row]);
            }
        }
    }
    void updateGrid (){
        for (int column = 0; column < 9; column++){
            for (int row = 0; row < 9; row++){
                c.grid[column][row] = Integer.parseInt(GridField[column][row].getText());
            }
        }
    }
    
    void updateView (){
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);        
        
        int width = this.getWidth();
        int height = this.getHeight();
        int marginX = (int) (width * 0.215);
        int marginY = (int) (height * 0.005);
        int etxraMarginX = 0;
        int etxraMarginY = 0;
        
        ControlPanel.setBounds(0, 0, marginX, height);
        Generate.setBounds ((int)(ControlPanel.getWidth() * 0.1),(int)(ControlPanel.getHeight() * 0.07), (int)(ControlPanel.getWidth() * 0.8), (int)(ControlPanel.getHeight() * 0.06));
        Check.setBounds ((int)(ControlPanel.getWidth()* 0.1),(int)(ControlPanel.getHeight() * 0.14), (int)(ControlPanel.getWidth() * 0.8), (int)(ControlPanel.getHeight() * 0.06));
        Solve.setBounds ((int)(ControlPanel.getWidth()* 0.1),(int)(ControlPanel.getHeight() * 0.86), (int)(ControlPanel.getWidth() * 0.8), (int)(ControlPanel.getHeight() * 0.06));
        Debug.setBounds ((int)(ControlPanel.getWidth()* 0.1),(int)(ControlPanel.getHeight() * 0.79), (int)(ControlPanel.getWidth() * 0.8), (int)(ControlPanel.getHeight() * 0.06));
             
        for (int column = 0; column < 9; column++){
            if (column%3 == 0) etxraMarginX += (int) (height * 0.01);
            for (int row = 0; row < 9; row++){
                if (row%3 == 0) etxraMarginY += (int) (height * 0.01);
                
                GridField[column][row].setBounds((int)(marginX + column * (height * 0.1) + etxraMarginX), (int)(marginY + row * (height * 0.1) + etxraMarginY) , (int)(height * 0.09), (int)(height * 0.09));
                
                String input = GridField[column][row].getText();
                if (input != null && !input.equalsIgnoreCase("")){
                    if (Integer.parseInt(input) != 0){
                        if (c.grid_access[column][row])
                            GridField[column][row].setBackground(Color.LIGHT_GRAY);
                        else
                            GridField[column][row].setBackground(Color.gray);
                    }
                    else {
                        if (c.grid_access[column][row])
                            GridField[column][row].setBackground(Color.white);
                        else{
                            GridField[column][row].setBackground(Color.black);
                        }
                    }
                }
            }
            etxraMarginY = 0;
        }
    }
    
    void highlite (int[] line){
                            System.out.println("highlite");
        for (int column = 0; column < 9; column++){
           if (Arrays.equals(c.getColumn(column), line)){
                for (int row = 0; row < 9; row++){
                    GridField[column][row].setBackground(Color.red);
                }
            }
        }
        
        for (int row = 0; row < 9; row++){
           if (Arrays.equals(c.getRow(row), line)){
                for (int column = 0; column < 9; column++){
                    GridField[column][row].setBackground(Color.red);
                }
            }
        }
    }
    
    void highlite (int[][] block){
                            System.out.println("highlite");
        for (int column = 0; column < 9; column+=3){
            for (int row = 0; row < 9; row+=3){
                if (compareArrays(c.getBlock (Core.getBlockNum (column, row)), block)) {
                    for (int sub_column = 0; sub_column < 3; sub_column++){
                        for (int sub_row = 0; sub_row < 3; sub_row++){
                            GridField[column + sub_column][row + sub_row].setBackground(Color.red);
                            System.out.println("red");
                        }
                    }
                }
            }
        }
    }
    
    boolean compareArrays (int [][] a1, int [][] a2){
        for (int c = 0; c < 3; c++){
            for (int r = 0; r < 3; r++){
                if (a1[c][r] != a2[c][r])
                    return false;
            }
        }
        return true;
    }
}
