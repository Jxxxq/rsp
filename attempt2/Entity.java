package me.capstone;

import java.awt.Point;
import java.awt.Rectangle;
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
	private final double SLOWNESS_FACTOR = 350f; //lower = slower faster
	private final double WALK_SPEED = 500f;
	private double fitness = -1f;
	
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
		double time = calculateTime();
        fitness = (10000000*Math.pow(1f/time,2));
	}
	
	private double calculateTime() {
		double totalTime = 0f;
	    for (Map.Entry<Section, Point> entry : genSeq.entrySet()) {
	        Section section = entry.getKey();
	        Point point = entry.getValue();
	        //System.out.println("Section: " + section + " - Point: " + point);
	    }
		ArrayList<Point> allPoints = new ArrayList<Point>(genSeq.values());
		
		
		
		Point origin = AppManager.getInstance().getOrigin();
		Section firstSection = getSectionForPoint(allPoints.get(0));
		double distanceFromFirst = (float) origin.distance(allPoints.get(0));
		double currentWeight = calculateWeight();
		double finalSpeed = (WALK_SPEED) / (Math.pow(2, currentWeight/SLOWNESS_FACTOR));
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
			double distance = (float) p1.distance(p2);
			double cWeight = calculateWeight();
			//System.out.println("Weight from " + getSectionForPoint(p1).getName() + " to " + sec.getName() + " is " + cWeight);
			double fSpeed = (WALK_SPEED) / (Math.pow(2, cWeight/SLOWNESS_FACTOR));
			totalTime += (distance/fSpeed);
			
	        it = currentInv.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry<Item, Integer> entry = it.next();
	            if (entry.getKey().getType() == sec.getType()) {
	                it.remove();
	            }
	        }
		}
		
		
		double distanceFromLast = origin.distance(allPoints.get(allPoints.size()-1));
		double lastWeight = calculateWeight();
		//Since last section, all previous stuff should've been dropped off
		//System.out.println("THIS SHOULD BE 0: " + lastWeight);
		double finalLastSpeed = (WALK_SPEED) / (Math.pow(2, lastWeight/SLOWNESS_FACTOR));
		totalTime += (distanceFromLast/finalLastSpeed);
		//System.out.println("Total Time:"+ totalTime);
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
	    return null; 
	}

	private double calculateWeight() {
		double n = 0;
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
    
    public double getFitness() {
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
    public boolean checkCollisionWithBarriers() {
        Map<Point, Point> barriers = AppManager.getInstance().getCopyOfBarrierPoints();
        ArrayList<Point> pathPoints = new ArrayList<>(genSeq.values());

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            Point p1 = pathPoints.get(i);
            Point p2 = pathPoints.get(i + 1);

            for (Map.Entry<Point, Point> barrierEntry : barriers.entrySet()) {
                Point barrierP1 = barrierEntry.getKey();
                Point barrierP2 = barrierEntry.getValue();

                if (lineIntersectsRectangle(p1, p2, barrierP1, barrierP2)) {
                    return true; 
                }
            }
        }

        return false; 
    }
    private boolean lineIntersectsRectangle(Point lineP1, Point lineP2, Point rectP1, Point rectP2) {
        int rectX = Math.min(rectP1.x, rectP2.x);
        int rectY = Math.min(rectP1.y, rectP2.y);
        int rectWidth = Math.abs(rectP1.x - rectP2.x);
        int rectHeight = Math.abs(rectP1.y - rectP2.y);
        Rectangle rectangle = new Rectangle(rectX, rectY, rectWidth, rectHeight);

        if (rectangle.contains(lineP1) || rectangle.contains(lineP2)) {
            return true;
        }

        Point topLeft = new Point(rectX, rectY);
        Point topRight = new Point(rectX + rectWidth, rectY);
        Point bottomLeft = new Point(rectX, rectY + rectHeight);
        Point bottomRight = new Point(rectX + rectWidth, rectY + rectHeight);

        return lineIntersectsLine(lineP1, lineP2, topLeft, topRight)
            || lineIntersectsLine(lineP1, lineP2, topRight, bottomRight)
            || lineIntersectsLine(lineP1, lineP2, bottomRight, bottomLeft)
            || lineIntersectsLine(lineP1, lineP2, bottomLeft, topLeft);
    }
    private boolean lineIntersectsLine(Point l1p1, Point l1p2, Point l2p1, Point l2p2) {
        int s1_x = l1p2.x - l1p1.x;     
        int s1_y = l1p2.y - l1p1.y;
        int s2_x = l2p2.x - l2p1.x;     
        int s2_y = l2p2.y - l2p1.y;

        int s = (-s1_y * (l1p1.x - l2p1.x) + s1_x * (l1p1.y - l2p1.y)) / (-s2_x * s1_y + s1_x * s2_y);
        int t = ( s2_x * (l1p1.y - l2p1.y) - s2_y * (l1p1.x - l2p1.x)) / (-s2_x * s1_y + s1_x * s2_y);

        return (s >= 0 && s <= 1 && t >= 0 && t <= 1);
    }
    
    private float calcDistanceAroundRect(Point start, Point end, Rectangle rect) {
    	return 0; // do this
    	
    	
    }

	
}