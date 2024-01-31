package rsp2.attempt2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Entity {

	private Map<Section, Point> genSeq;
	private Map<Item, Integer> currentInv;
	private final float SLOWNESS_FACTOR = 350f; //lower = slower faster
	private final float WALK_SPEED = 500f;
	private float fitness = -1f;
	
	public Entity(boolean firstGen) {
		//IF THIS IS FALSE, GENSEQ MUST ALWAYS BE SET VIA SETGENSEQ
		if(firstGen) {
			generateRandSeq();
		}
		currentInv = AppManager.getInstance().getCopyOfInventory();		
	}
	

	
	private void generateRandSeq() {
		Map<Section, Point> sections = AppManager.getInstance().getCopyOfSectionPoints();
		List<Map.Entry<Section, Point>> entries = new ArrayList<>(sections.entrySet());
		Collections.shuffle(entries);
		
		genSeq = new LinkedHashMap<>();
		for(Map.Entry<Section, Point> entry : entries) {
			genSeq.put(entry.getKey(), entry.getValue());
		}

		
	}
	
	public void calculateFitness() {
		float time = calculateTime();
        fitness = (float)(10000000*Math.pow(1f/time,3));
	}
	
	private float calculateTime() {
		float totalTime = 0f;
	    for (Map.Entry<Section, Point> entry : genSeq.entrySet()) {
	        Section section = entry.getKey();
	        Point point = entry.getValue();
	        System.out.println("Section: " + section + " - Point: " + point);
	    }
		ArrayList<Point> allPoints = new ArrayList<Point>(genSeq.values());
		
		
		
		Point origin = AppManager.getInstance().getOrigin();
		Section firstSection = getSectionForPoint(allPoints.get(0));
		float distanceFromFirst = (float) origin.distance(allPoints.get(0));
		float currentWeight = calculateWeight();
		float finalSpeed = (WALK_SPEED) / ((float)Math.pow(2, currentWeight/SLOWNESS_FACTOR));
		totalTime += (distanceFromFirst/finalSpeed);
		//Goes through inventory and 'drops' off each item that has that same type has the section
	    Iterator<Map.Entry<Item, Integer>> it = currentInv.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Item, Integer> entry = it.next();
	        if (entry.getKey().getType() == firstSection.getType()) {
	            it.remove();
	        }
	    }
		
		
		
		for(int i = 0; i < allPoints.size()-1; i++) {
			Point p1 = allPoints.get(i);
			Point p2 = allPoints.get(i+1);
			Section sec = getSectionForPoint(p2);
			float distance = (float) p1.distance(p2);
			float cWeight = calculateWeight();
			System.out.println("Weight from " + getSectionForPoint(p1).getName() + " to " + sec.getName() + " is " + cWeight);
			float fSpeed = (WALK_SPEED) / ((float)Math.pow(2, cWeight/SLOWNESS_FACTOR));
			totalTime += (distance/fSpeed);
			
	        it = currentInv.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry<Item, Integer> entry = it.next();
	            if (entry.getKey().getType() == sec.getType()) {
	                it.remove();
	            }
	        }
		}
		
		
		float distanceFromLast = (float) origin.distance(allPoints.get(allPoints.size()-1));
		float lastWeight = calculateWeight();
		//Since last section, all previous stuff should've been dropped off
		System.out.println("THIS SHOULD BE 0: " + lastWeight);
		float finalLastSpeed = (WALK_SPEED) / ((float)Math.pow(2, lastWeight/SLOWNESS_FACTOR));
		totalTime += (distanceFromLast/finalLastSpeed);
		System.out.println("Total Time:"+ totalTime);
		return totalTime;
	}
	public Section getSectionForPoint(Point point) {
	    for (Map.Entry<Section, Point> entry : genSeq.entrySet()) {
	        Point p2 = entry.getValue();
	        int x1 = point.x;
	        int y1 = point.y;
	        int x2 = p2.x;
	        int y2 = p2.y;
	        if((x1==x2) && (y1==y2)) {
	        	return entry.getKey();
	        }
	    }
	    return null; // or throw an exception if appropriate
	}

	private float calculateWeight() {
		float n = 0;
		for (Map.Entry<Item, Integer> entry : currentInv.entrySet()) {
		    Item item = entry.getKey();
		    Integer quantity = entry.getValue();
		    n += (item.getWeight()*quantity);
		}
		return n;
	}
	
    public void setGenSeq(Map<Section, Point> seq) {
        genSeq = new LinkedHashMap<>(); // Create a new HashMap
        for (Map.Entry<Section, Point> entry : seq.entrySet()) {
            genSeq.put(entry.getKey(), entry.getValue());
        }
    }
    
    public Map<Section, Point> getGenSeq(){
    	return new LinkedHashMap<Section, Point>(genSeq);
    }
    
    public float getFitness() {
    	return fitness;
    }
    public static Entity orderedCrossover(Entity parent1, Entity parent2) {
        // Assuming genSeq is not null and has the same size in both parents
        Map<Section, Point> childGenSeq = new LinkedHashMap<>();
        Random rand = new Random();
        int size = parent1.genSeq.size();

        // Choose two random points for the crossover
        int start = rand.nextInt(size);
        int end = rand.nextInt(size);

        // Ensure start is less than end
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        // Add the subsequence from parent1 to child
        List<Section> subsection = new ArrayList<>(parent1.genSeq.keySet()).subList(start, end);
        for (Section section : subsection) {
            childGenSeq.put(section, parent1.genSeq.get(section));
        }

        // Add remaining sections from parent2, maintaining order
        for (Map.Entry<Section, Point> entry : parent2.genSeq.entrySet()) {
            if (!childGenSeq.containsKey(entry.getKey())) {
                childGenSeq.put(entry.getKey(), entry.getValue());
            }
        }

        // Create a new entity with the combined genSeq
        Entity child = new Entity(false); // false since we set genSeq manually
        child.setGenSeq(childGenSeq);
        return child;
    }
    public void mutate(float mutationRate) {
        Random rand = new Random();
        int size = genSeq.size();
        
        LinkedHashMap<Section, Point> oldSeq = new LinkedHashMap<>(genSeq);
        ArrayList<Section> keySetList = new ArrayList<>();
        keySetList.addAll(oldSeq.keySet());

        for (int i = 0; i < size; i++) {
            if (rand.nextFloat() < mutationRate) {
                int swapIndex = rand.nextInt(size);
                Collections.swap(keySetList, i, swapIndex);
            }
        }
        
        LinkedHashMap<Section, Point> swappedSeq = new LinkedHashMap<>();
        for(Section oldSwappedKey:keySetList) {
        	swappedSeq.put(oldSwappedKey, oldSeq.get(oldSwappedKey));
        }
    }

	
}
