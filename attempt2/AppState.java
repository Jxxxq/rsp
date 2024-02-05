package me.capstone;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AppState {
	
    private Map<Section, Point> sectionPoints;
    private Map<Item, Integer> inventory;
    private Point origin;
	
	public Map<Section, Point> getSectionPoints() {
		return new LinkedHashMap<Section,Point>(this.sectionPoints);
	}
	
	public void setSectionPoints(Map<Section, Point> sectionPoints) {
		this.sectionPoints = new LinkedHashMap<Section,Point>(sectionPoints);
	}
	
	public Map<Item, Integer> getInventory() {
		return new HashMap<>(this.inventory);
	}
	
	public void setInventory(Map<Item, Integer> inventory) {
		this.inventory = new HashMap<>(inventory);
	}
	
	public Point getOrigin() {
		return new Point(this.origin);
	}
	
	public void setOrigin(Point origin) {
		this.origin = new Point(origin);
	}


}
