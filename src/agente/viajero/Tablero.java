/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agente.viajero;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Jorge
 */
class Tablero extends JPanel{
	
    JTextField m[][];
		
    Tablero (){}

    public void setMatriz(float mat[][]){
        this.removeAll();
        m = new JTextField[mat.length][mat[0].length];
        this.setLayout(new GridLayout(mat.length,mat[0].length) );

        for (int i = 0; i<mat.length; i++){  
            for (int j = 0; j<mat[0].length; j++){
                m[i][j]= new JTextField();
                m[i][j].setEditable(false);
                m[i][j].setAutoscrolls(false);
                m[i][j].setFont((new Font("Arial", Font.BOLD,10)));
                m[i][j].setText(""+mat[i][j]);
                m[i][j].setCaretPosition(0);
                add(m[i][j]);	
            }
        }
        this.updateUI();
    }
}
