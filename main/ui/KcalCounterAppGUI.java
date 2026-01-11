package ui;

import model.Food;
import model.Account;
import model.EventLog;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class KcalCounterAppGUI extends JFrame {
    private static final String STORE = "data/account.json"; // remember this to the data
    // account.json

    private CardLayout cardLayout; // allow multi-screen UI
    // popping from menu (home) to food choices to history and back
    private JPanel cardPanel; // contaier panel that holds the displays
    private Account account;
    private JsonReader jsonReader; // read input from user
    private JsonWriter jsonWriter; // allow user to type

    // store the group of options (from choice options)
    private Map<String, FoodItem> fruits = new LinkedHashMap<>(); // hashmap with orders
    private Map<String, FoodItem> vegetables = new LinkedHashMap<>(); // hashmap with orders
    private Map<String, FoodItem> meat = new LinkedHashMap<>(); // hashmap with orders
    private Map<String, FoodItem> dairy = new LinkedHashMap<>(); // hashmap with orders
    private Map<String, FoodItem> protein = new LinkedHashMap<>(); // hashmap with orders

    // construct the gui
    public KcalCounterAppGUI() {
        setTitle("Calories Counter App"); // set the title for window
        setDefaultCloseOperation(EXIT_ON_CLOSE); // close the window -> exist
        setSize(900, 700); // size of the gui window

        jsonReader = new JsonReader(STORE); // taken from the source .data/account/json
        jsonWriter = new JsonWriter(STORE); // taken from the source .data/account/json

        initAccount(); // showing the initializing account window

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loadFoodItems();

        cardPanel.add(createMenu(), "Menu"); // add the menu display shown as main
        cardPanel.add(createCategoryPanel(fruits, "Fruits"), "Fruits"); // add the fruits menu
        cardPanel.add(createCategoryPanel(vegetables, "Vegetables"), "Vegetables"); // add the vegetables
        cardPanel.add(createCategoryPanel(meat, "Meat"), "Meat"); // add the meat menu
        cardPanel.add(createCategoryPanel(dairy, "Dairy"), "Dairy"); // add the dairy menu
        cardPanel.add(createCategoryPanel(protein, "Protein"), "Protein"); // add the dairy menu

        add(cardPanel);
        setVisible(true); // visibility of the window is true
        
    }

    // initializing the account
    // prompting the user for username, TDEE and remaining calories in day
    private void initAccount() {
        String username = JOptionPane.showInputDialog(this, "Enter your username:");
        // this window (the first one)
        double tdee = getValidDouble("Enter your TDEE (in kcal):", 0);
        double remain = getValidDouble("Enter your remain calories of the day (in kcal): ", 0);
        account = new Account(username, tdee, remain);
    }

    // see if the input of json is valid and catch exception
    private Double getValidDouble(String question, double min) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, question);
            // popped up in the new (this), different from the last (this)
            if (input == null) {
                // user pressed cancel -> stop
                return null;
            }
            try {
                double value = Double.parseDouble(input); // test the input
                if (value >= min) {
                    return value;
                }
            } catch (Exception e) { // catch exception from double.parse double
                JOptionPane.showMessageDialog(this, "Error\t\nPlease enter a number: ");
            }
        }
    }

    @SuppressWarnings("methodlength")
    // create the window to display menu options
    private JPanel createMenu() {
        // create a panel with 4 rows, 2 columns, 10 horizontal gap, 10 vertical gap
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        // set a border around the panel window
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // creates a button labeled fruits
        JButton fruitButton = new JButton("Fruits");
        // when button clicked (e ->: lambda expression) -> call the cardlayout to show
        // card (panel) inside containder cardPanel
        fruitButton.addActionListener(e -> cardLayout.show(cardPanel, "Fruits"));

        // vegetables
        JButton vegButton = new JButton("Vegetables");
        vegButton.addActionListener(e -> cardLayout.show(cardPanel, "Vegetables"));

        // meat
        JButton meatButton = new JButton("Meat");
        meatButton.addActionListener(e -> cardLayout.show(cardPanel, "Meat"));

        // dairy
        JButton dairyButton = new JButton("Dairy");
        dairyButton.addActionListener(e -> cardLayout.show(cardPanel, "Dairy"));

        // protein
        JButton proButton = new JButton("Protein");
        proButton.addActionListener(e -> cardLayout.show(cardPanel, "Protein"));

        // history
        JButton hisButton = new JButton("History");
        hisButton.addActionListener(e -> showHistory()); // call to the show history method

        // save
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveAccDB()); // call to the save account json med

        // load
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadAccDB()); // load acc

        // quit
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0)); // exits

        panel.add(fruitButton);
        panel.add(vegButton);
        panel.add(meatButton);
        panel.add(dairyButton);
        panel.add(proButton);
        panel.add(hisButton);
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(quitButton);

        return panel;
    }

    public JPanel createCategoryPanel(Map<String, FoodItem> foodMap, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        // create a new panel window Border Layout -> divided into 5 regions
        // north (top) south (bot) west (left) east (right)

        JLabel label = new JLabel("Select food choices: " + title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.NORTH); // add the label to the north of the panel

        JPanel grid = new JPanel(new GridLayout(0, 3, 15, 15));
        for (FoodItem i : foodMap.values()) { // for each of the food item inside, create a button
            JButton button = new JButton(i.getFood().getFoodName(), i.getImageIcon());
            button.setHorizontalTextPosition(SwingConstants.CENTER); // set the text
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            // when any item of food is clicked -> run handleFoodSelection
            button.addActionListener(e -> handleFoodSelection(i));
            grid.add(button); // add the button display to the grid (jpanel)
        }

        JScrollPane scroll = new JScrollPane(grid);
        panel.add(scroll, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        // attaches the code that will run when button is clicked
        // call back to the menu
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Menu"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;

    }

    // handle the click of any food item
    private void handleFoodSelection(FoodItem item) {
        Double grams = getValidDouble("Enter amount of consumed food (in grams): ", 0);
        if (grams == null) {
            return; // break out of the entering window
        }
        double kcal = item.getFood().getCaloriesPerAmount(grams);
        account.consumeNewFood(kcal);
        account.addFood(item.getFood());

        JOptionPane.showMessageDialog(this,
                account.getUsername() + " consumed: " + kcal + "\nRemaining: " + account.getCaloLeft());
    }

    // display history
    private void showHistory() {
        List<Food> foods = account.getConsumedFoods(); // return list of food
        if (foods.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    account.getUsername() + " has not consumed anything");
        } else {
            StringBuilder sb = new StringBuilder(account.getUsername() + " consumed:\n");
            for (Food f : foods) {
                sb.append(" - " + f.getFoodName() + "\n"); // " - foodname "
            }
            JOptionPane.showMessageDialog(this, sb.toString()); // display whole thing
        }
    }

    //
    private void saveAccDB() {

    }

    // save to account data
    private void saveAccJson() {
        try {
            jsonWriter.open(); // open the file data imput
            jsonWriter.write(account);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "Saved to " + STORE); // saved input to data
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error: File not found");
        }
    }

    //
    private void loadAccDB() {
        
    }

    // load the saved account data
    private void loadAccJson() {
        try {
            account = jsonReader.read(); // read from the data memo
            JOptionPane.showMessageDialog(this, "Loaded data for " + account.getUsername());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading saved data from account");
        }
    }

    @SuppressWarnings("methodlength")
    // load food items
    private void loadFoodItems() {
        fruits.put("Apple",
                new FoodItem(new Food(1, "Apple", 52.1),
                        Images.getScaledIcon("apple", 100, 100)));
        fruits.put("Banana",
                new FoodItem(new Food(4, "Banana", 88.7),
                        Images.getScaledIcon("banana", 100, 100)));
        vegetables.put("Broccoli",
                new FoodItem(new Food(5, "Broccoli", 33.7),
                        Images.getScaledIcon("broccoli", 100, 100)));
        vegetables.put("Carrots",
                new FoodItem(new Food(6, "Carrots", 41.3),
                        Images.getScaledIcon("carrot", 100, 100)));
        meat.put("Beef",
                new FoodItem(new Food(7, "Beef", 250.5),
                        Images.getScaledIcon("beef", 100, 100)));
        meat.put("Chicken",
                new FoodItem(new Food(8, "Chicken (Breast)", 164.9),
                        Images.getScaledIcon("chicken", 100, 100)));
        meat.put("Fish",
                new FoodItem(new Food(9, "Fish (Salmon)", 208.2),
                        Images.getScaledIcon("salmon", 100, 100)));
        dairy.put("Milk",
                new FoodItem(new Food(10, "Milk (1% Fat)", 42),
                        Images.getScaledIcon("milk", 100, 100)));
        dairy.put("Yogurt (Greek)",
                new FoodItem(new Food(11, "Yogurt (Greek)", 100),
                        Images.getScaledIcon("yogurt", 100, 100)));
        dairy.put("Cheese (Cottage)",
                new FoodItem(new Food(12, "Cheese (Cottage)", 98.5),
                        Images.getScaledIcon("cheese", 100, 100)));
        protein.put("Egg",
                new FoodItem(new Food(13, "Egg (Boiled)", 155.1),
                        Images.getScaledIcon("egg", 100, 100)));
    }

    public static void main(String[] args) {
        new KcalCounterAppGUI();
    }

}