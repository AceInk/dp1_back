/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agente.viajero;

import model.algorithm.Chromosome;
import model.algorithm.Vertex;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;
/**
 *
 * @author Jorge
 */

class Mapa extends JPanel{
    int X, Y, y;
    Image Buffer;
    Graphics2D graphic;
    Point puntos[];
    Point puntosd[];
    int n_puntos = 0;
    int d_puntos = 0;
    Vector pos, x, y1;
    boolean result = false;
    ArrayList<Vertex> camino;
	
    Mapa(){
        this.setBackground(Color.black);
        puntos = new Point[100];
        puntosd = new Point[100];
        pos    = new Vector();
        y1     = new Vector();
        x      = new Vector();
        this.setBorder( BorderFactory.createBevelBorder(1));

        this.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseMoved(MouseEvent e){
                X=e.getX()/20;
                y=e.getY();
                Y= ((y-getHeight())*-1);
                repaint();
            }
        });

        this.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked (MouseEvent e){
                    if (!((X==24)||(Y/20==24))){
                        if(e.getButton() == MouseEvent.BUTTON1){
                            puntos[n_puntos++]=e.getPoint();
                            pos.add("("+e.getX()/20+","+Y/20+")");
                            x.add(e.getX()/20);y1.add(Y/20);
                            repaint();
                        }
                        else if(e.getButton() == MouseEvent.BUTTON3){
                            puntosd[d_puntos++]=e.getPoint();
                            pos.add("("+e.getX()/20+","+Y/20+")");
                            x.add(e.getX()/20);y1.add(Y/20);
                            repaint();
                        }
                    }
            }
        });	
    }
    
    public ArrayList<Vertex> getDepots(){
        ArrayList<Vertex> depots = new ArrayList<>();
        for(int i = 0; i < d_puntos; i++){
            Point p = puntosd[i];
            Vertex v = Vertex.builder().coordX(p.x).coordY(p.y).isBlocked(false)
                        .isDepot(true).isPrincipalDepot(false).build();
            depots.add(v);
        }
        depots.get(0).setPrincipalDepot(true);
        return depots;
    }
    
    public ArrayList<Vertex> getEntregas(){
        ArrayList<Vertex> entregas = new ArrayList<>();
        for(int i = 0; i< n_puntos; i++){
            Point p = puntos[i];
            Vertex v = Vertex.builder().coordX(p.x).coordY(p.y).isBlocked(false)
                        .isDepot(false).build();
            entregas.add(v);
        }
        return entregas;
    }
    
    public Chromosome setAdan(){
        Chromosome adan = new Chromosome();
        
        ArrayList<Vertex> genes = new ArrayList<>();
        for(int i = 0; i < n_puntos; i++){
            Point p = puntos[i];
            genes.add(Vertex.builder().coordX(p.x).coordY(p.y).build());
        }
        
        Point pd = puntosd[0];
        adan.setCurrentStart(Vertex.builder().coordX(pd.x).coordY(pd.y).isDepot(true)
                .isPrincipalDepot(true).build());
        adan.setGenes(genes);
        
        return adan;
    }

    @Override
    public void update(Graphics g){
        paint(g);
    }

    @Override
    public void paintComponent(Graphics g){
        Buffer = this.createImage(this.getWidth(),this.getWidth());
        graphic =(Graphics2D)Buffer.getGraphics(); 
        pintar(graphic);
        g.drawImage(Buffer,0,0,this.getWidth(),this.getWidth(),this);
    }

    public void pintar(Graphics2D g){
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //para mejorar la calidad de la pintada	
        g.setColor(Color.white);
        g.fillRect(0,0,getWidth(),getHeight() );
        g.setColor(Color.LIGHT_GRAY);
        for(int i=0;i<100;i+=4){
            g.drawLine(5*i,500,5*i,0);
            g.drawLine(0,5*i,500,5*i);
        }    	
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD,12));
        g.drawString("("+X+","+Y/20+")",X*20 +2,y);


        if (this.result)
            this.resultado(g);

        for (int i=0;i<n_puntos;i++){
            g.setColor(Color.BLUE);	
            g.fillOval(puntos[i].x-8,puntos[i].y-8,18,18);
            g.setColor(Color.white);
            g.drawString(""+(i+1),(puntos[i].x-10) +9/2,(puntos[i].y-9)+15);
            g.setColor(Color.black);
            g.drawString(""+pos.elementAt(i),puntos[i].x-9,puntos[i].y-18);
        }
        
        for (int i=0;i<d_puntos;i++){
            g.setColor(Color.ORANGE);	
            g.fillOval(puntosd[i].x-8,puntosd[i].y-8,18,18);
            g.setColor(Color.white);
            g.drawString(""+(i+1),(puntosd[i].x-10) +9/2,(puntosd[i].y-9)+15);
            g.setColor(Color.black);
            g.drawString(""+pos.elementAt(i),puntosd[i].x-9,puntosd[i].y-18);
        }
    }

    public int getDeposito(){
        return this.d_puntos;
    }
    
    public int getCamino(){
        return this.n_puntos;
    }


    public int getx(int n){
        return Integer.parseInt( ""+x.elementAt(n));
    }

    public int gety(int n){
        return Integer.parseInt( ""+y1.elementAt(n));
    }

    public void setResultado(ArrayList<Vertex> camino){
        this.result=true;
        this.camino=camino;
        repaint();
    }

    public void resultado(Graphics2D g ){
        int temp,temp2;
        g.setColor(Color.red);
        for(int i = 1; i < camino.size(); i++){
            if(camino.get(i-1) == null){
                System.out.println("El vertice "+ (i-1) + " esta vacio");
                System.exit(1);
            }
            Point p1 = new Point(camino.get(i-1).getCoordX(), camino.get(i-1).getCoordY());
            Point p2 = new Point(camino.get(i).getCoordX(), camino.get(i).getCoordY());
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
            g.drawString("" + i, (int)((((p1.x+p2.x)/2)+p1.x)/2), 
                    (int)((((p1.y+p2.y)/2)+p1.y)/2)); 
        }
    }
}
