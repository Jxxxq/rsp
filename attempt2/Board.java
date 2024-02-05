package me.capstone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Board extends JFrame implements MouseListener{
    private static final long serialVersionUID = 1L;
    private static final int BOARD_WIDTH = 1280;
    private static final int BOARD_HEIGHT = 880;
    private static final int MAX_GENERATIONS = 1000000;
    private static final int HORIZONTAL_SECTION_MARGIN = 160;
    private static final int VERTICAL_SECTION_MARGIN = 150;
    
    private int currentGeneration = 0;
    private ArrayList<Point> bestEntityPath;
    private Population population;

    //all of the buttons
    private JButton btnGenerateOneGen, btnLoopGenerations, btnStopGenerations,
                    btnCreateNewPopulation, btnGenerateNewInventory,
                    btnAddToInventory, btnRemoveFromInventory, btnSaveState,
                    btnLoadState;
    
    private JComboBox<String> itemComboBox;
    private JLabel statusMessageLabel;
    private JTextField quantityField;
    private Timer generationTimer;
    
    private Point p1,p2;
    
    public Board() {
        setTitle("Restock Program");
        this.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        
        this.setBounds(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        

        generateBoard();
        population = new Population(0.03f,500,1);
        processGeneration();
        Item[] items = Item.values();
        String[] itemNames = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            itemNames[i] = items[i].getName();
        }
        Arrays.sort(itemNames);

        int comboBoxWidth = 150;
        int textFieldWidth = 50;
        int buttonWidth = 180;
        int componentHeight = 30;
        int topMargin = 30;
        int rightMargin = 30;
        statusMessageLabel = new JLabel("");
        statusMessageLabel.setBounds(BOARD_WIDTH - rightMargin - comboBoxWidth - textFieldWidth - buttonWidth, topMargin + componentHeight * 2 + 20, 400, 30);
        this.add(statusMessageLabel);

        btnSaveState = new JButton("Save State");
        this.add(btnSaveState);
        btnSaveState.setBounds(10, BOARD_HEIGHT - 140, 200, 30);
        btnSaveState.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	saveState();
            }
        });
        btnLoadState = new JButton("Load State");
        btnLoadState.setBounds(220, BOARD_HEIGHT-140, 200, 30);
        this.add(btnLoadState);
        btnLoadState.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	loadState();
            }
        });
        itemComboBox = new JComboBox<>(itemNames);
        itemComboBox.setBounds(BOARD_WIDTH - rightMargin - comboBoxWidth, topMargin, comboBoxWidth, componentHeight);

        //this.addMouseListener(this);
        quantityField = new JTextField();
        quantityField.setBounds(BOARD_WIDTH - rightMargin - comboBoxWidth - textFieldWidth, topMargin, textFieldWidth, componentHeight);
        btnRemoveFromInventory = new JButton("Remove from Inventory");
        btnRemoveFromInventory.setBounds(BOARD_WIDTH - rightMargin - comboBoxWidth - textFieldWidth - buttonWidth, topMargin + componentHeight + 10, buttonWidth, componentHeight);
        this.add(btnRemoveFromInventory);
        btnRemoveFromInventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItemName = (String) itemComboBox.getSelectedItem();
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                    Item selectedItem = null;
                    for (Item item : Item.values()) {
                        if (item.getName().equals(selectedItemName)) {
                            selectedItem = item;
                            break;
                        }
                    }
                    if (selectedItem != null) {
                        AppManager.getInstance().removeItemFromInventory(selectedItem, quantity);
                        quantityField.setText("");
                        displayTemporaryMessage("Successfully removed " + selectedItemName + ", Amount: " + quantity);
                        repaint();
                    } else {
                        System.out.println("Item not found");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid quantity");
                }
            }
        });

        btnAddToInventory = new JButton("Add to Inventory");
        btnAddToInventory.setBounds(BOARD_WIDTH - rightMargin - comboBoxWidth - textFieldWidth - buttonWidth, topMargin, buttonWidth, componentHeight);
        this.add(itemComboBox);
        this.add(quantityField);
        this.add(btnAddToInventory);
        btnAddToInventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItemName = (String) itemComboBox.getSelectedItem();
                int quantity;
                try {
                	
                    quantity = Integer.parseInt(quantityField.getText());
                    Item selectedItem = null;
                    
                    for (Item item : Item.values()) {
                        if (item.getName().equals(selectedItemName)) {
                            selectedItem = item;
                            break;
                        }
                    }
                    if(quantity >= 500000) {
                    	System.out.println("Number too high");
                    	return;
                    }
                    if (selectedItem != null) {
                        AppManager.getInstance().addItemToInventory(selectedItem, quantity);
                        quantityField.setText("");
                        displayTemporaryMessage("Successfully added " + selectedItem.getName() + ", Amount: " + quantity);
                        repaint();
                    } else {
                        System.out.println("Item not found");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid quantity");
                }
            }
        });


        btnGenerateOneGen = new JButton("Generate One Generation");
        btnGenerateOneGen.setBounds(10, BOARD_HEIGHT - 100, 200, 30); // Adjust position and size as needed
        this.add(btnGenerateOneGen);
        btnGenerateOneGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processGeneration();
                //Update the path for the best entity
                ArrayList<Point> bestEntityPath = new ArrayList<>(population.getBestEntity().getGenSeq().values());
                setBestEntityPath(bestEntityPath);
                repaint();
                //System.out.println(population.getBestEntity().getGenSeq().toString());

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
                            //System.out.println(currentGeneration);
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
                currentGeneration = 0;
                repaint(); 
            }
        });

    }
    public void setBestEntityPath(ArrayList<Point> path) {
        this.bestEntityPath = path;
    }
    private void saveState() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            AppManager.getInstance().saveAppState(file.getAbsolutePath());
        }
    }
    private void loadState() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            AppManager.getInstance().loadAppState(file.getAbsolutePath());
            repaint();
            createNewPopulation();
        }
    }
    private void displayTemporaryMessage(String message) {
        statusMessageLabel.setText(message);
        Timer messageTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false); 
        messageTimer.start();
    }

    private void createNewPopulation() {
        if (generationTimer != null) {
            generationTimer.stop();
        }
        population = new Population(0.03f, 600, 1); 
        processGeneration(); 
        ArrayList<Point> bestEntityPath = new ArrayList<>(population.getBestEntity().getGenSeq().values());
        setBestEntityPath(bestEntityPath);
        currentGeneration = 1;
        repaint();
    }
    private void processGeneration() {
    	population.calcFitnessOfAllEntities();
    	population.poolSelection();
    	population.generate();
    	//System.out.println("Best fit: " + population.getBestEntity().getFitness());
    	ArrayList<Point> bestEntityPath = new ArrayList<>(population.getBestEntity().getGenSeq().values());
    	setBestEntityPath(bestEntityPath);
    	currentGeneration+=1;
    	System.out.println("Current Gen " + currentGeneration);
    }
    private void generateBoard() {
    	AppManager.getInstance().generateRandomInventory();
    	AppManager.getInstance().generateRandomSections(BOARD_WIDTH, BOARD_HEIGHT, HORIZONTAL_SECTION_MARGIN, VERTICAL_SECTION_MARGIN);
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
        g.setColor(Color.BLACK); 
        Font originalFont = g.getFont();
        Font infoFont = new Font(originalFont.getFontName(), Font.BOLD, 16); 
        g.setFont(infoFont); 

        String generationText = "Current Generation: " + currentGeneration;
        g.drawString(generationText, 19, BOARD_HEIGHT-120);

        g.setFont(originalFont);

        
        g.setColor(Color.RED);
        //uncomment to see section boundaries
        //g.drawRect(HORIZONTAL_SECTION_MARGIN, VERTICAL_SECTION_MARGIN, BOARD_WIDTH - 2 * HORIZONTAL_SECTION_MARGIN, BOARD_HEIGHT - 2 * VERTICAL_SECTION_MARGIN);
        Map<Point, Point> barrierPoints = AppManager.getInstance().getCopyOfBarrierPoints();
        g.setColor(Color.BLACK); 
        for (Map.Entry<Point, Point> entry : barrierPoints.entrySet()) {
            Point p1 = entry.getKey();
            Point p2 = entry.getValue();
            g.drawRect(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y),
                       Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
        }
        Point origin = AppManager.getInstance().getOrigin();
        g.setColor(Color.GREEN);
        g.fillOval(origin.x - 5, origin.y - 5, 10, 10);
        g.drawString("Origin", origin.x + 10, origin.y);
        Map<Section, Point> sectionPoints = AppManager.getInstance().getCopyOfSectionPoints();
        for (Map.Entry<Section, Point> entry : sectionPoints.entrySet()) {
            Point point = entry.getValue();
            Section section = entry.getKey();

            g.setColor(Color.RED);
            g.fillOval(point.x - 5, point.y - 5, 10, 10); 

            g.setColor(Color.BLACK);
            g.drawString(section.name(), point.x + 10, point.y);
        }

        if (bestEntityPath != null && bestEntityPath.size() > 0) {
            g.setColor(Color.BLUE); 

            //draw line from origin to first point and last point
            if (bestEntityPath.size() > 0) {
                Point firstPoint = bestEntityPath.get(0);
                Point lastPoint = bestEntityPath.get(bestEntityPath.size()-1);
                g.drawLine(origin.x, origin.y, firstPoint.x, firstPoint.y);
                drawNumber(g, 1, origin, firstPoint); 
                g.drawLine(origin.x, origin.y, lastPoint.x, lastPoint.y);
                drawNumber(g, bestEntityPath.size()+1, origin, lastPoint);
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
        g.drawString(pathOrderString, BOARD_WIDTH/2 - 350, 50); 
        displayInventory(g);
    }
    private String generatePathOrder() {
        if (bestEntityPath == null || bestEntityPath.isEmpty()) {
            return "No path available";
        }
        StringBuilder pathOrder = new StringBuilder("Best Path: ");
        
        Map<Section, Point> sectionPoints = AppManager.getInstance().getCopyOfSectionPoints();
        
        for (Point p : bestEntityPath) {
            sectionPoints.forEach((section, point) -> {
                if (point.equals(p)) {
                    pathOrder.append(section.name()).append(" -> "); 
                }
            });
        }
        pathOrder.setLength(pathOrder.length() - 4);
        return pathOrder.toString();
    }
    private void displayInventory(Graphics g) {
        Map<Item, Integer> inventory = AppManager.getInstance().getCopyOfInventory();
        int yPosition = 60; 

        Font originalFont = g.getFont();
        Font boldFont = new Font(originalFont.getFontName(), Font.BOLD, originalFont.getSize());
        g.setFont(boldFont);

        g.setColor(Color.BLACK);
        String title = "Inventory";
        g.drawString(title, 10, yPosition - 15);

        int titleWidth = g.getFontMetrics().stringWidth(title);
        int underlineYPosition = yPosition - 14; // Position the underline slightly below the text
        g.drawLine(10, underlineYPosition, 10 + titleWidth, underlineYPosition);

        g.setFont(originalFont);

        for (Map.Entry<Item, Integer> item : inventory.entrySet()) {
            String itemName = item.getKey().getName();
            Integer itemCount = item.getValue();
            g.drawString(itemName + ": " + itemCount, 10, yPosition); 
            yPosition += 15; 
        }
    }


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		p1 = e.getPoint();
	}

    @Override
    public void mouseReleased(MouseEvent e) {
        p2 = e.getPoint(); 
        AppManager.getInstance().addBarrier(p1,p2);
        repaint(); 

    }
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
 


}