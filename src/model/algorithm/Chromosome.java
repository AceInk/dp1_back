/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.algorithm;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import utils.CountUtils;
/**
 *
 * @author Jorge
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chromosome {
    private List<Vertex> genes;
    @Builder.Default
    private double fitness = Double.MAX_VALUE;
    private Vertex currentStart;
    private Vertex finalDepot;

    private void computeFitness() {
        // cojer todos los genes y calcular sus distancias para obtener el fitness
        if(currentStart == null)
            throw new NullPointerException("currentStart es null");
        
        if(finalDepot == null)
            throw new NullPointerException("currentStart es null");
        
        int i = 0;
        fitness = 0;
        fitness += currentStart.getDistancia(genes.get(0));
        for(; i < genes.size() - 1; i++){
            fitness += genes.get(i).getDistancia(genes.get(i + 1));
        }
        fitness += finalDepot.getDistancia(genes.get(i));
    }
    
    public void setGenes(Vertex current, List<Vertex> v, Vertex finalD){
        currentStart = current;
        ArrayList<Vertex> vertexList = new ArrayList<>();
        vertexList.addAll(v);
        genes = vertexList;
        finalDepot = finalD;
    }
    
    public Double getFitness(){
        if(fitness == Double.MAX_VALUE)
            computeFitness();
        
        return fitness;
    }

    public static List<Chromosome> crossover(Chromosome parent1, Chromosome parent2) {
        
        List<Chromosome> children = new ArrayList<>();
        if (parent1.getGenes().size() == 1) {
            children.add(parent1);
            children.add(parent2);
            return children;
        }
        Random random = new Random();
        int i = 0;
        while (i == 0) {
            i = random.nextInt(parent1.getGenes().size());
        }
        List<Vertex> firstC1 = new ArrayList<>(parent1.getGenes().subList(0, i));
        List<Vertex> firstC2 = new ArrayList<>(parent2.getGenes().subList(0, i));
        List<Vertex> lastC1 = new ArrayList<>(parent1.getGenes().subList(i, parent1.getGenes().size()));
        List<Vertex> lastC2 = new ArrayList<>(parent2.getGenes().subList(i, parent2.getGenes().size()));
        firstC1.addAll(lastC2);
        firstC2.addAll(lastC1);
        Chromosome child1 = Chromosome.builder().currentStart(parent1.getCurrentStart()).genes(firstC1).finalDepot(parent1.getFinalDepot()).build();
        Chromosome child2 = Chromosome.builder().currentStart(parent2.getCurrentStart()).genes(firstC2).finalDepot(parent2.getFinalDepot()).build();
        children = process_gen_repeted(child1, child2, parent1, parent2, i);
        return children;
    }
    
    private static List<Chromosome> process_gen_repeted(final Chromosome child1, final Chromosome child2,
                                                        Chromosome parent1, Chromosome parent2, int pos) {
        List<Chromosome> modifiedChildren = new ArrayList<>();
        Chromosome child1Aux = new Chromosome();
        child1Aux.setGenes(child1.getCurrentStart(), child1.getGenes(), child1.getFinalDepot());
        Chromosome child2Aux = new Chromosome();
        child2Aux.setGenes(child2.getCurrentStart() ,child2.getGenes(), child2.getFinalDepot());
        int count1 = 0;
        List<Vertex> sublist1 = new ArrayList<>(child1.getGenes().subList(0, pos));
        List<Vertex> sublist2 = new ArrayList<>(child2.getGenes().subList(0, pos));
        for (Vertex v1 : sublist1) {
            int repeat = 0;
            repeat = CountUtils.countOcurrences(v1, child1Aux.getGenes());
            if (repeat > 1) {
                int count2 = 0;
                List<Vertex> subparent1 = new ArrayList<>(parent1.getGenes().subList(pos, parent1.getGenes().size()));
                for (Vertex v2 : subparent1) {
                    if (!child1Aux.getGenes().contains(v2)) {
                        child1Aux.getGenes().remove(count1);
                        //child1Aux.getGenes().set(count1,parent1.getGenes().get(count2));
                        child1Aux.getGenes().add(count1, subparent1.get(count2));
                    }
                    count2++;
                }
            }
            count1++;
        }

        count1 = 0;
        for (Vertex v1 : sublist2) {
            int repeat = 0;
            repeat = CountUtils.countOcurrences(v1, child2Aux.getGenes());
            if (repeat > 1) {
                int count2 = 0;
                List<Vertex> subparent2 = new ArrayList<>(parent2.getGenes().subList(pos, parent2.getGenes().size()));
                for (Vertex v2 : subparent2) {
                    if (!child2Aux.getGenes().contains(v2)) {
                        child2Aux.getGenes().remove(count1);
                        //child2Aux.getGenes().set(count1,parent2.getGenes().get(count2));
                        child2Aux.getGenes().add(count1, subparent2.get(count2));
                    }
                    count2++;
                }
            }
            count1++;
        }
        modifiedChildren.add(child1Aux);
        modifiedChildren.add(child2Aux);
        return modifiedChildren;

    }
    
    public int compareTo(Chromosome o) {
        return Double.compare(this.getFitness(), o.getFitness());
    }

    @Override
    public String toString() {
        StringBuilder chromosome = new StringBuilder();
        chromosome.append("Chromosome: ").append(currentStart).append(genes).append(finalDepot).append("\n");
        chromosome.append("Fitness: ").append(fitness).append("\n");
        return chromosome.toString();
    }
}
