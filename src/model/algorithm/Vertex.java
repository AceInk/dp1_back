/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Jorge
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vertex {
    private int coordX;
    private int coordY;
    @Builder.Default
    private boolean isBlocked = false;
    @Builder.Default
    private boolean isDepot = false;
    @Builder.Default
    private boolean isPrincipalDepot = false;

    public Vertex(Vertex v) {
        this.coordX = v.getCoordX();
        this.coordY = v.getCoordY();
        this.isBlocked = v.isBlocked();
        this.isDepot = v.isDepot();
        this.isPrincipalDepot = v.isPrincipalDepot();
    }
    
    public float getDistancia(Vertex other){

        int a,b,c,d;
        float r,r1;
        a = this.getCoordX();
        b = other.getCoordX();
        c = this.getCoordY();
        d = other.getCoordY();
        r = Math.abs((float) b - a);
        r1 = Math.abs((float) d - c);

        return r + r1; // Distancia Manhattan
    }

    @Override
    public boolean equals(Object obj) {
        Vertex v = (Vertex) obj;
        return this.coordX == v.getCoordX() && this.coordY == v.getCoordY();
    }
    
    @Override
    public String toString(){
        return String.format("[%d,%d]",coordX,coordY);
    }
}
