/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
import java.util.*;
/**
 *
 * @author Guest
 */
class Core {
    int [][] grid; //[column][row]
    boolean [][] grid_access;
    int [][] solution;
    Queue debugQueue ;
    UserInterface ui;
    
    Core (){
        debugQueue = new LinkedList ();
        solution = new int [9][9];
        grid = new int [9][9];
        grid_access = new boolean [9][9];
        for (int column = 0; column < 9; column++){
            for (int row = 0; row < 9; row++){
                grid_access[column][row] = true;
            }
        }
    }
    
    void setUI (UserInterface ui){
        this.ui = ui;
    }
    
    
    void setNumber (int column, int row , int num) {
        this.grid [column][row] = num;
        if (this.grid_access[column][row])
            this.solution [column][row] = num;
        //debug
        List a = new ArrayList();
        a.add (column);
        a.add (row);
        a.add (num);
        debugQueue.add(a);
    }
    
    static int getBlockNum (int column, int row){
        return ((int) (column/3)) * 3+ ((int) (row/3)) ; 
    }
    
    int [][] getBlock (int blocknum){ //blocknum >= 0 && blocknum <= 8
        int [][] sub_grid = new int [3][3];
        for (int column = 0; column < 3; column++){
            for (int row = 0; row < 3; row++){
                sub_grid[row][column] = grid[column + (3 * ((int)blocknum/3))][row + (3 * (blocknum%3))];
            }
        }
        return sub_grid;
    }
    
      
    int [] getRow (int row){
        int [] sub_grid = new int [9];
        for (int column = 0; column < 9; column++){
            sub_grid [column]= grid [column][row];
        }
        return sub_grid;
    }
    
    int [] getColumn (int column){
        int [] sub_grid = new int [9];
        for (int row = 0; row < 9; row++){
            sub_grid [row]= grid [column][row];
        }
        return sub_grid;
    }
    
    boolean isValid (){
        for (int i = 0; i < 9; i++){
            int [][] block = getBlock (i);
            int [] row = getRow (i);
            int [] column = getColumn (i); 
            if (isValid(row) == false || isValid(column) == false || isValid(block) == false)
                return false;
        }
        return true;
    }
    
    boolean isValid (int column, int row, int num){
        boolean returnstate = false;
        int oldNum = grid[column][row]; 
        grid[column][row] = num;
        if (isValid(getRow(row)) && isValid(getColumn (column)) && isValid(getBlock(getBlockNum (column, row))) )
            returnstate = true;
        grid[column][row] = oldNum;
        return returnstate;
    }
    
    boolean isValid (int[] line){
        boolean [] checknum = new boolean [10];
        for (int i = 0; i < 10; i++) checknum[i] = false;
        
        for (int i = 0; i < 9; i++){
            if (checknum[line[i]] && line[i] != 0)
                return false;
            else
                checknum[line[i]] = true;
        }
        return true;
    }
    
    boolean isValid (int[][] block){
        boolean [] checknum = new boolean [10];
        for (int i = 0; i < 10; i++) checknum[i] = false;
        
        for (int column = 0; column < 3; column++){
            for (int row = 0; row < 3; row ++){
                if (checknum[block[column][row]] && block[column][row] != 0)
                    return false;
                else
                    checknum[block[column][row]] = true;
            }
        }
        
        return true;
    }
    void resetGrid (){
        for (int column = 0; column < 9; column++){
            for (int row = 0; row < 9; row ++){
                grid[column][row] = 0;
                grid_access[row][column] = true;
            }
        }
    }
    void resetInput (){
        for (int column = 0; column < 9; column++){
            for (int row = 0; row < 9; row ++){
                if ( grid_access[column][row])
                    grid[column][row] = 0;
                    
            }
        }
    }
    
    void randomGrid (int n){
        this.resetGrid ();
        for (int i = 0; i < n;i++){
            int ranX, ranY;
            do{
                ranX = (int)(Math.random() * 8);
                ranY = (int)(Math.random() * 8);
            }
            while (grid[ranX][ranY] != 0);
            grid[ranX][ranY] = (int)(Math.random() * 8)+1;
            if (this.isValid() == false){
                i--;
                grid[ranX][ranY] = 0;
                continue;
            }
            grid_access[ranX][ranY] = false;
        }
    }
    
    boolean check (){
        System.out.println("check");
        boolean returnstate = true;
         for (int i = 0; i < 9; i++){
            int [][] block = getBlock (i);
            int [] row = getRow (i);
            int [] column = getColumn (i); 
            if (isValid(row) == false ){
                System.out.println("false");
                ui.highlite(row);
                returnstate = false;
            }
            if (isValid(column) == false ){
                System.out.println("false");
                ui.highlite(column);
                returnstate = false;
            }
            if (isValid(block) == false){
                System.out.println("false");
                ui.highlite(block);
                returnstate = false;
            }
        }
        return returnstate;
    }
    
    void solve (){
        this.debugQueue.clear();
        System.out.println ("solving");
        boolean solved = solveRek (0);
        if (solved)
            System.out.println ("solved");
    }
    
    boolean solveRek (int position){
        System.out.println ("solving: "+position);
        if (position >= 81)return true;
        
        int x = position % 9;
        int y = (int)(position / 9);
        
        if (!grid_access[x][y]){
            boolean next_isSolved = solveRek (position + 1);
            if (next_isSolved){
                return true;
            }
            else{
                return false;
            }
        }
        
        for (int num = 1; num <= 9; num++){
             System.out.print ("solving: "+position);
             System.out.println ("   try: "+num);
             if (isValid (x, y, num)){
                    System.out.println ("   valid: "+num);
                    this.setNumber(x, y, num);
                    boolean next_isSolved = solveRek (position + 1);
                    if (next_isSolved){
                        return true;
                    }
                    else{
                        this.setNumber (x, y, 0);
                    }
            }
        }
        
        return false;
    }
    
    
    void output (){
        for (int column = 0; column < 9; column++){
            for (int row = 0; row < 9; row++){
                System.out.print ("{ ("+ column +"|"+ row +") = "+ grid[column][row] + "} ");
            }
            System.out.print("\n");
        } 
    }
    
    void output (int [][] block){
        for (int column = 0; column < 3; column++){
            for (int row = 0; row < 3; row++){
                System.out.print ("{"+ block[column][row] + "} ");
            }
            System.out.print("\n");
        }  
    }
    
    void output (int [] line){
        for (int column = 0; column < 3; column++){
            System.out.print ("{"+ line[column]+ "} ");
        }  
        System.out.print("\n");
    }
}
