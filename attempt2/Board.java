package rsp2.attempt2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Board extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int BOARD_WIDTH = 1280;
	private static final int BOARD_HEIGHT = 880;
	private static final int MAX_GENERATIONS = 1000000; 
	
	private int currentGeneration = 0;
    private ArrayList<Point> bestEntityPath;

    private JButton btnGenerateOneGen;
    private JButton btnLoopGenerations;
    private JButton btnStopGenerations;
    private JButton btnCreateNewPopulation;
    private JButton btnGenerateNewInventory; 
    
    private Timer generationTimer;

	private Population population;
    public Board() {
        this.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        
        this.setBounds(0, 0, BOARD_WIDTH, BOARD_HEIGHT);


        generateBoard();
        population = new Population(0.03f,300,1);
        processGeneration();
    	
        btnGenerateOneGen = new JButton("Generate One Generation");
        btnGenerateOneGen.setBounds(10, BOARD_HEIGHT - 100, 200, 30); // Adjust position and size as needed
        this.add(btnGenerateOneGen);
        btnGenerateOneGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processGeneration();
                // Update the path for the best entity
                ArrayList<Point> bestEntityPath = new ArrayList<>(population.getBestEntity().getGenSeq().values());
                setBestEntityPath(bestEntityPath);
                repaint();
                System.out.println(population.getBestEntity().getGenSeq().toString());

            }
        });
        btnLoopGenerations = new JButton("Loop Generations");
        btnLoopGenerations.setBounds(220, BOARD_HEIGHT - 100, 200, 30);
        this.add(btnLoopGenerations);
        btnLoopGenerations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (generationTimer != null) {
                    generationTimer.stop();
                }
                generationTimer = new Timer(0, new ActionListener() { 
                	@Override
                    public void actionPerformed(ActionEvent e) {
                        if (currentGeneration < MAX_GENERATIONS) {
                            processGeneration();
                            repaint();
                            currentGeneration++;
                            System.out.println(currentGeneration);
                        } else {
                            generationTimer.stop();
                        }
                    }
                });
                generationTimer.start();
            }
        });
        btnStopGenerations = new JButton("Stop Looping Generations");
        btnStopGenerations.setBounds(430, BOARD_HEIGHT - 100, 220, 30); 
        this.add(btnStopGenerations);
        btnStopGenerations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (generationTimer != null) {
                    generationTimer.stop();
                    System.out.println("Generation loop stopped");
                }
            }
        });
        btnCreateNewPopulation = new JButton("Create New Population");
        btnCreateNewPopulation.setBounds(660, BOARD_HEIGHT - 100, 220, 30); 
        this.add(btnCreateNewPopulation);
        btnCreateNewPopulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewPopulation();
                System.out.println("New population created");
            }
        });
        btnGenerateNewInventory = new JButton("Generate New Inventory"); 
        btnGenerateNewInventory.setBounds(890, BOARD_HEIGHT - 100, 220, 30); 
        this.add(btnGenerateNewInventory);

        btnGenerateNewInventory.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                AppManager.getInstance().generateRandomInventory(); 
                repaint(); 
            }
        });

    }
    public void setBestEntityPath(ArrayList<Point> path) {
        this.bestEntityPath = path;
    }
    private void createNewPopulation() {
        if (generationTimer != null) {
            generationTimer.stop();
        }
        population = new Population(0.03f, 600, 1); 
        processGeneration(); 
        ArrayList<Point> bestEntityPath = new ArrayList<>(population.getBestEntity().getGenSeq().values());
        setBestEntityPath(bestEntityPath);
        repaint();
    }
    private void processGeneration() {
    	population.calcFitnessOfAllEntities();
    	population.poolSelection();
    	population.generate();
    	System.out.println("Best fit: " + population.getBestEntity().getFitness());
    	ArrayList<Point> bestEntityPath = new ArrayList<>(population.getBestEntity().getGenSeq().values());
    	setBestEntityPath(bestEntityPath);
    	currentGeneration+=1;
    	System.out.println("Current Gen " + currentGeneration);

    }
    private void generateBoard() {
    	AppManager.getInstance().generateRandomInventory();
    	AppManager.getInstance().generateRandomSections(BOARD_WIDTH, BOARD_HEIGHT, 300, 100);
    	this.repaint();
    }
    private void drawNumber(Graphics g, int number, Point start, Point end) {
        int midX = (start.x + end.x) / 2;
        int midY = (start.y + end.y) / 2;
        g.setColor(Color.BLUE); 
        g.drawString(Integer.toString(number), midX, midY);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Point origin = AppManager.getInstance().getOrigin();
        g.setColor(Color.GREEN);
        g.fillOval(origin.x - 5, origin.y - 5, 10, 10);
        g.drawString("Origin", origin.x + 10, origin.y);

        // Existing code to draw section points
        Map<Section, Point> sectionPoints = AppManager.getInstance().getCopyOfSectionPoints();
        for (Map.Entry<Section, Point> entry : sectionPoints.entrySet()) {
            Point point = entry.getValue();
            Section section = entry.getKey();

            // Draw a dot at each point
            g.setColor(Color.RED);
            g.fillOval(point.x - 5, point.y - 5, 10, 10); // Adjust as needed

            // Draw the section name as a label
            g.setColor(Color.BLACK);
            g.drawString(section.name(), point.x + 10, point.y);
        }

        if (bestEntityPath != null && bestEntityPath.size() > 0) {
            g.setColor(Color.BLUE); // Set path color

            //draw line from origin to first point and last point
            if (bestEntityPath.size() > 0) {
                Point firstPoint = bestEntityPath.get(0);
                Point lastPoint = bestEntityPath.get(bestEntityPath.size()-1);
                g.drawLine(origin.x, origin.y, firstPoint.x, firstPoint.y);
                drawNumber(g, 1, origin, firstPoint); // Number for the first segment
                g.drawLine(origin.x, origin.y, lastPoint.x, lastPoint.y);
                drawNumber(g, 9, origin, lastPoint);
            }
            //draw path in seq
            for (int i = 0; i < bestEntityPath.size() - 1; i++) {
                Point start = bestEntityPath.get(i);
                Point end = bestEntityPath.get(i + 1);
                g.drawLine(start.x, start.y, end.x, end.y);
                drawNumber(g, i + 2, start, end);             }
        }
        g.setColor(Color.BLACK); 
        String pathOrderString = generatePathOrder();
        g.drawString(pathOrderString, 10, BOARD_HEIGHT - 80); 
        displayInventory(g);
    }
    private String generatePathOrder() {
        if (bestEntityPath == null || bestEntityPath.isEmpty()) {
            return "No path available";
        }
        StringBuilder pathOrder = new StringBuilder("Path Order: ");
        
        Map<Section, Point> sectionPoints = AppManager.getInstance().getCopyOfSectionPoints();
        
        // Iterate over the bestEntityPath and append section names or IDs
        for (Point p : bestEntityPath) {
            sectionPoints.forEach((section, point) -> {
                if (point.equals(p)) {
                    pathOrder.append(section.name()).append(" -> "); // Append the section name or ID
                }
            });
        }
        pathOrder.setLength(pathOrder.length() - 4); // Remove the last arrow
        return pathOrder.toString();
    }
    private void displayInventory(Graphics g) {
        // Example method call - adjust based on your actual method to get inventory items
        Map<Item, Integer> inventory = AppManager.getInstance().getCopyOfInventory();
        int yPosition = 40; // Starting Y position for inventory items
        g.setColor(Color.BLACK); // Text color for inventory items

        for (Map.Entry<Item, Integer> item : inventory.entrySet()) {
            String itemName = item.getKey().getName();
            Integer itemCount = item.getValue();
            g.drawString(itemName + ": " + itemCount, 10, yPosition); // Adjust X position as needed
            yPosition += 15; // Increment Y position for the next item
        }
    }
 


}
