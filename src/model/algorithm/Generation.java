/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.algorithm;

import java.util.*;
import utils.ListUtils;
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
public class Generation {
     @Builder.Default
    private List<Chromosome> chromosomesList = new ArrayList<>();
    private int generation_number;
    private Chromosome best_chromosome;
    @Builder.Default
    private double best_fitness = Double.MAX_VALUE;
    @Builder.Default
    private double ratio_cross = 0.8;
    @Builder.Default
    private double prob_mutate = 0.7;
    private int n_parents;
    private int n_directs;

    public void initPopulation(Chromosome chromosome , int population_size){
        generation_number=0;
        for(int i = 0; i < population_size; i++){
            Chromosome newChromosome = new Chromosome();
            List<Vertex> shuffleGenes = new ArrayList<>(chromosome.getGenes());
            shuffleGenes = ListUtils.shuffle(shuffleGenes);
            newChromosome.setCurrentStart(chromosome.getCurrentStart());
            newChromosome.setGenes(shuffleGenes);
            chromosomesList.add(newChromosome);
        }
    }

    public void generateNewGeneration(int tournamentParticipants){
        List<Chromosome>  directs = new ArrayList<>();
        List<Chromosome>  parents = new ArrayList<>();
        n_parents= (int) Math.round(chromosomesList.size()*ratio_cross);
        n_parents = n_parents % 2 == 0 ? n_parents : n_parents-1;
        n_directs= chromosomesList.size() - n_parents;
        directs = tournament_Selection("Direct", tournamentParticipants);
        parents = tournament_Selection("Crosses", tournamentParticipants);
        parents = cross_parents(parents);
        parents = mutate(parents, prob_mutate);
        chromosomesList.clear();
        chromosomesList.addAll(directs);
        chromosomesList.addAll(parents);
        generation_number += 1;
    }

    private List<Chromosome> mutate(List<Chromosome> parents, double prob_mutate) {
        List<Chromosome> mutations = new ArrayList<>();
        Chromosome mutation;
        for(Chromosome c: parents){
            mutation = mutation(c,prob_mutate);
            mutations.add(mutation);
        }
        return mutations;
    }

    private Chromosome mutation(Chromosome c, double prob_mutate) {
        List<Vertex> auxiliar = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < c.getGenes().size(); i++){
            double j = random.nextDouble();
            if(j < prob_mutate){
                auxiliar = inversion_mutation(c.getGenes());
                c.setGenes(auxiliar);
            }
        }
        return c;
    }

    private List<Vertex> inversion_mutation(List<Vertex> genes) {
        Random random = new Random();
        int index1 = random.nextInt(genes.size());
        int index2 = random.nextInt(genes.size()-index1)+index1;
        List<Vertex> chromosome_mid = new ArrayList<>(genes.subList(index1,index2));
        Collections.reverse(chromosome_mid);
        List<Vertex> chromosome_result= new ArrayList<>(genes.subList(0,index1));
        chromosome_result.addAll(chromosome_mid);
        chromosome_result.addAll(genes.subList(index2,genes.size()));
        return chromosome_result;
    }

    private List<Chromosome> cross_parents(List<Chromosome> parents) {
        List<Chromosome> children = new ArrayList<>();
        int n_parents = parents.size();
        for(int i=0;i<n_parents;i+=2){
            children.addAll(Chromosome.crossover(parents.get(i),parents.get(i+1)));
        }
        return children;
    }


    public List<Chromosome> tournament_Selection(String type, int tournamentParticipants){
        List<Chromosome> winners = new ArrayList<>();
        List<Chromosome> tournamentResults = new ArrayList<>();
        int n = type.equals("Direct") ? n_directs : n_parents;
        ListUtils.shuffle(chromosomesList);
        // la primera mitad del cromosoma hijo se dobla
        // podemos cambiar de seleccion
        for (int j=0; j < 2; j++) {
            for (int i = 0; i < n; i += 2) {
                Chromosome c = chromosomesList.get(i).compareTo(chromosomesList.get(i + 1)) < 0 ? chromosomesList.get(i) : chromosomesList.get(i + 1);
                //Collections.sort(tournamentResults);
                winners.add(c);
            }
        }
        return winners;
    }
    
    public void calculateAllFitness(){
        for(Chromosome c :chromosomesList){
            double fitness = c.getFitness();
            if(fitness < best_fitness){
                best_fitness = fitness;
                best_chromosome = Chromosome.builder()
                        .fitness(c.getFitness())
                        .currentStart(c.getCurrentStart())
                        .genes(c.getGenes())
                        .build();
            }
        }
    }
}
