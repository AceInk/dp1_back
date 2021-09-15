/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agente.viajero;

import model.algorithm.Chromosome;
import model.algorithm.Generation;
import model.algorithm.Vertex;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
/**
 *
 * @author Jorge
 */
public class Agente_Viajero extends JFrame implements Runnable{
    
    Container c;
    Mapa mapa;
    JList Caminos;
    Vector cam;
    JButton bt_comenzar,bt_camino;
    JComboBox combo;
    int nodo_inicial,camino=-1;
    Chromosome bestChromosome;
    float mat[][];
//    Tablero tab;
    Thread hilo;
    Generation generacion;
    Agente_Viajero yo;
	
    Agente_Viajero(){
        this.setTitle("AGENTE VIAJERO");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c = this.getContentPane();
        c.setLayout(null);
        yo=this;

        Caminos = new JList();
        cam= new Vector();	
//        tab = new Tablero();

        mapa = new Mapa();
        mapa.setBounds(10,10,500,500);
        c.add(mapa);

        bt_comenzar = new JButton("Aceptar");
        bt_camino   = new JButton("Generar");
        bt_comenzar.addActionListener((ActionEvent e) -> {
            String cad="";
            cam.clear();
            int tam = getDepot();
            String vec[]= new String[tam];
            for (int i=0;i<tam;i++){
                vec[i]=""+(i+1);
                combo.addItem(vec[i]);
            }
            
            bt_comenzar.setEnabled(false);
            bt_camino.setEnabled(true);
        });
        
        combo = new JComboBox();
        combo.setBounds(511,10,100,20);

        bt_comenzar.setBounds(511,40,100,20);
        bt_camino.setBounds(511,70,100,20);
        bt_camino.setEnabled(false);
        c.add(combo);
        c.add(bt_comenzar);
        c.add(bt_camino);
        
        bt_camino.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent e){
                hilo = new Thread(yo);
                hilo.start();
            }
        });
        
        bestChromosome = null;
        
        this.setPreferredSize(new Dimension(630,550));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private Chromosome getAdan(){
        return mapa.setAdan();
    }
    
    private int getCam(){
        return mapa.getCamino();
    }
    
    private int getDepot(){
        return mapa.getDeposito();
    }
    
    private void changePrincipalDepot(Chromosome c){
        c.setCurrentStart(mapa.getDepots().get(combo.getSelectedIndex()));
    }
	
    @Override
    public void run(){
	int i=0;
        
        Chromosome chromosome = getAdan();
        changePrincipalDepot(chromosome);
        
        Generation generation = new Generation();
        generation.initPopulation(chromosome, mapa.getDepots(), 60);
        generation.setN_parents(4);
        generation.setN_directs(4);
        generation.setBest_chromosome(chromosome);
        generation.calculateAllFitness();
        
        double menor = generation.getBest_fitness(),m=0;
        bestChromosome = generation.getBest_chromosome();
        
        System.out.println("Primer fitness: " + menor);
        System.out.println("El chromosoma Adan es ");
        System.out.println(bestChromosome.toString());
        
        // para la impresion de los cromosomas
        ArrayList<Vertex> caminoSleccionado = new ArrayList<>();
        
        // aqui se implementa el algoritmo genetico con 200 generaciones
        while(i < 500){
            try {
                generation.generateNewGeneration(25);
                generation.calculateAllFitness();
                Thread.sleep(10);
                if (generation.getBest_fitness() < menor){
                    bestChromosome = generation.getBest_chromosome();
                    menor = generation.getBest_fitness();
                    caminoSleccionado.clear();
                    caminoSleccionado.add(bestChromosome.getCurrentStart());
                    caminoSleccionado.addAll(bestChromosome.getGenes());
                    caminoSleccionado.add(bestChromosome.getFinalDepot());
                    mostrar(caminoSleccionado);
                }
                i++;
            }
            catch (Exception ex) {
                System.out.println(ex);
            }
        }

        System.out.println("El besto fitness es " + menor);
        System.out.println("El besto chromosoma es ");
        System.out.println(bestChromosome.toString());
        caminoSleccionado.clear();
        caminoSleccionado.add(bestChromosome.getCurrentStart());
        caminoSleccionado.addAll(bestChromosome.getGenes());
        caminoSleccionado.add(bestChromosome.getFinalDepot());
        mostrar(caminoSleccionado);
//	JOptionPane.showMessageDialog(tab,menor+" en el camino "+"\n"+cam.elementAt(camino),"Mejor Ruta",1);
    }

    public void mostrar (ArrayList<Vertex> camino){
        mapa.setResultado(camino);
    }
	
    public static void main (String arg[]){
        Agente_Viajero frame = new Agente_Viajero();
//        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
//        frame.setUndecorated(!true);
//        frame.setSize(size.width,size.height);
        frame.setVisible(true);	
    }
}
