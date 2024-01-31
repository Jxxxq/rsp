package rsp2.attempt2;

import java.util.Map;

public class Main {

	public static void main(String[] args) {
		System.out.println(AppManager.getInstance().getCopyOfSectionPoints().toString());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Board b = new Board();
                b.setVisible(true);
            }
        });
		//Entity e = new Entity(true);
		//e.calculateFitness();
		printInv();
		
	}
	private static void printInv() {
	    Map<Item, Integer> inv = AppManager.getInstance().getCopyOfInventory();
	    for (Map.Entry<Item, Integer> entry : inv.entrySet()) {
	        Item item = entry.getKey();
	        Integer quantity = entry.getValue();
	        System.out.println(item.getName() + ": " + quantity); // Assuming Item has a "getName" method
	    }
	}

	
}
