package rsp2.attempt2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AppManager {

	private static AppManager instance = null;
	private Map<Item, Integer> inventory;
	private Map<Section, Point> sectionPoints;
	private Point origin;
	
    private AppManager() {
        this.inventory = new EnumMap<>(Item.class);
        this.sectionPoints = new HashMap<>(); // Initialize the Section-Point map
        this.origin = new Point(0, 0);
    }
	
	public static AppManager getInstance() {
		if(instance == null) {
			instance = new AppManager();
		}
		return instance;
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
	
	
	
	
}
