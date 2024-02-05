package me.capstone;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AppManager {

	private static AppManager instance = null;
	private Map<Item, Integer> inventory;
	private Map<Section, Point> sectionPoints;
	private Map<Point, Point> barriers;
	private Point origin;
	
    private AppManager() {
        this.inventory = new EnumMap<>(Item.class);
        this.sectionPoints = new HashMap<>(); 
        this.barriers = new HashMap<>();
        this.origin = new Point(0, 0);
    }
	
	public static AppManager getInstance() {
		if(instance == null) {
			instance = new AppManager();
		}
		return instance;
	}
	
	public void addBarrier(Point p1, Point p2) {
		barriers.put(new Point(p1), new Point(p2));
	}
	
	public void addItemToInventory(Item item, Integer quantity) {
	    if (inventory.containsKey(item)) {
	        inventory.put(item, inventory.get(item) + quantity);
	    } else {
	        inventory.put(item, quantity);
	    }
	}
	public void removeItemFromInventory(Item item, Integer quantity) {
	    if (inventory.containsKey(item)) {
	        int currentQuantity = inventory.get(item);
	        int newQuantity = currentQuantity - quantity;

	        if (newQuantity > 0) {
	            inventory.put(item, newQuantity);
	        } 
	        else {
	            inventory.remove(item);
	        }
	    } else {
	        System.out.println("Item not present in inventory");
	    }
	}

	
	public void generateRandomInventory() {
		inventory.clear();
		Random r = new Random();
		List<Item> itemList = new ArrayList<>(List.of(Item.values()));
		Collections.shuffle(itemList);
		for(int i = 0; i < 30; i++) {
			Item item = itemList.get(i);
			int quantity = r.nextInt(50) + 1;
			inventory.put(item, quantity);
		}
	}
	
	public void generateRandomSections(int guiWidth, int guiHeight, int horiMargin, int vertMargin) {
	    Random r = new Random();
	    sectionPoints.clear(); 

	    int ox = horiMargin + r.nextInt(guiWidth - 2 * horiMargin);
	    int oy = vertMargin + r.nextInt(guiHeight - 2 * vertMargin);
	    updateOrigin(ox, oy);

	    for (Section section : Section.values()) {
	        int x = horiMargin + r.nextInt(guiWidth - 2 * horiMargin);
	        int y = vertMargin + r.nextInt(guiHeight - 2 * vertMargin);
	        Point point = new Point(x, y);

	        sectionPoints.put(section, point);
	    }
	}
	
	public Map<Item, Integer> getCopyOfInventory() {
	    return new HashMap<>(inventory);
	} 

    public void updateOrigin(int x, int y) {
        this.origin = new Point(x, y);
    }

    public Map<Section, Point> getCopyOfSectionPoints() {
        return new HashMap<>(sectionPoints);
    }

    public Point getOrigin() {
        return new Point(origin);
    }
    
    public Map<Point, Point> getCopyOfBarrierPoints(){
    	return new HashMap<>(barriers);
    }

    public void saveAppState(String fileName) {
    	ObjectMapper mapper = new ObjectMapper();
    	AppState appState = new AppState();
    	
    	appState.setInventory(inventory);
    	appState.setOrigin(origin);
    	appState.setSectionPoints(sectionPoints);
    	try {
    		mapper.writeValue(new File(fileName), appState);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void loadAppState(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AppState appState = mapper.readValue(new File(filename), AppState.class);
            

            this.sectionPoints = appState.getSectionPoints();
            this.inventory = appState.getInventory();
            this.origin = appState.getOrigin();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	
}