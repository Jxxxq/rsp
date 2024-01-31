package rsp2.attempt2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Population {

	private float mutationRate;
	private int popMax;
	private int elitismCount;
	private ArrayList<Entity> population = new ArrayList<>();
	private ArrayList<Entity> matingPool = new ArrayList<>();
	private Entity bestEntity;
	
	public Population(float mutationRate, int popMax, int elitismCount) {
		this.mutationRate = mutationRate;
		this.popMax = popMax;
		this.elitismCount = elitismCount;
		for(int i = 0; i < popMax; i++) {
			population.add(new Entity(true));
		}

	}
	
	public void calcFitnessOfAllEntities() {
		for(Entity e : population) {
			e.calculateFitness();
		}
	}
	
	public void poolSelection() {
		Entity maxFitnessEntity = population.get(0);
		bestEntity = population.get(0);
		
		for(int i = 0; i < popMax; i++) {
			if(population.get(i).getFitness() > maxFitnessEntity.getFitness()) {
				maxFitnessEntity = population.get(i);
				bestEntity = population.get(i);
			}
		}
		matingPool.clear();
		for(int i = 0; i < popMax; i++) {
			double fitness = map(population.get(i).getFitness(), 0, maxFitnessEntity.getFitness(), 0, 1);
			int n = (int)Math.floor(fitness*1000);
			System.out.println(n);
			for(int j = 0; j < n; j++) {
				matingPool.add(population.get(i));
			}
		}
	}
	public void generate() {
		population.clear();
		Random r = new Random();
        for (int i = 0; i < elitismCount; i++) {
        	Entity e = new Entity(false);
        	e.setGenSeq(bestEntity.getGenSeq());
            population.add(e);
        }
        while (population.size() < popMax) {
            int a = r.nextInt(matingPool.size());
            int b = r.nextInt(matingPool.size());
            Entity partnerA = matingPool.get(a);
            Entity partnerB = matingPool.get(b);
            Entity child = Entity.orderedCrossover(partnerA, partnerB);
            child.mutate(mutationRate);
            population.add(child);
        }	
       }
	private double map(double value, double start1, double stop1, double start2, double stop2) {
	    double outgoing = start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
	    return outgoing;
	}
	public Entity getBestEntity() {
		Entity e = new Entity(false);
		e.setGenSeq(bestEntity.getGenSeq());
		e.calculateFitness();
		return e;
	}
	
}
