/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
/**
 *
 * @author Guest
 */
public class Sudoku {
    Core c;
    UserInterface ui;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       new Sudoku ().run (); 
    }
    
    public void run (){
        c = new Core ();
        ui = new UserInterface (c);
        c.setUI (ui);
    }
    
}
