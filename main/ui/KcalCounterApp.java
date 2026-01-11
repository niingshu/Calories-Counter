package ui; 

import model.Account;
import model.Event;
import model.EventLog;
import model.Food;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner; //user input keyboard

public class KcalCounterApp {
    private static final String JSON_STORE = "./data/account.json";
    private Account account;
    private Scanner input;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    @SuppressWarnings("methodlength")
    private final Food apple = new Food(1, "Apple", 52.1); 
    private final Food banana = new Food(4, "Banana", 88.7); 
    private final Food broccoli = new Food(5, "Broccoli", 33.7); 
    private final Food carrots = new Food(6, "Carrots", 41.3); 
    private final Food beef = new Food(7, "Beef", 250.5); 
    private final Food chicken = new Food(8, "Chicken (Breast)", 164.9); 
    private final Food fish = new Food(9, "Fish (Salmon)", 208.2); 
    private final Food milk = new Food(10, "Milk (1% Fat)", 42.0); 
    private final Food yogurt = new Food(11, "Yogurt (Greek)", 100.0); 
    private final Food cheese = new Food(12, "Cheese (Cottage)", 98.5); 
    private final Food egg = new Food(13, "Egg (Boiled)", 155.1); 



    //EFFECTS: runs the Calorie Counter application
    public KcalCounterApp() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runCounter();
        runShutdownHook();
    }

    //MODIFIES: this 
    //EFFECTS: process user choice of food
    private void runCounter() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayChoices();
            command = input.next();
            command = command.toLowerCase();

            //Ask user to choose type of food (first time)
            if (command.equals("quit")) {
                keepGoing = false;
            } else if (command.equals("history")) {
                displayHistory();
            } else {
                processChoices(command);

                //Ask user to choose specific food in type (final)
                System.out.println("Enter the code for specific food you consumed: ");
                String finalChoice = input.next().toLowerCase();
                processFinalChoice(finalChoice);
            }
        } 
    }

    //MODIFIES: this 
    //EFFECTS: process user consumed amount of chose food 
    private double processAmount() {
        System.out.println("Enter amount of consumed food (in grams): ");

        // if input is not double, then loop back 
        while (!input.hasNextDouble()) { //input is keyboard input from user
            System.out.println("Error");
            System.out.println("Please input a number: ");
            input.next();
        }
        //input is a double
        double grams = input.nextDouble();

        //input is double but negative
        while (grams < 0) {
            System.out.println("Error");
            System.out.println("Please input again a non-negative number: ");
            grams = input.nextDouble();
        }
        
        return grams;
    }

    @SuppressWarnings("methodlength")
    //MODIFIES: this 
    //EFFECTS: process user types of food 
    private void processChoices(String command) {
        if (command.equals("fruits")) {
            displayFruitsChoices();
        } else if (command.equals("vegetables")) {
            displayVegetablesChoices();
        } else if (command.equals("meat")) {
            displayMeatChoices();
        } else if (command.equals("dairy")) {
            displayDairyChoices();
        } else if (command.equals("protein")) {
            displayProteinChoices();
        } else if (command.equals("save")) {
            saveAccount();
        } else if (command.equals("load")) {
            loadAccount();
        } else if (command.equals("history")) {
            displayHistory();
        } else {
            System.out.println("Selection not valid.");
        }
    }

    @SuppressWarnings("methodlength")
    //MODIFIES: this
    //EFFECTS: process user final choice of consumed food
    private void processFinalChoice(String choice) {
        if (choice.equals("a")) {
            processFood(apple);
        } else if (choice.equals("ba")) {
            processFood(banana);
        } else if (choice.equals("br")) {
            processFood(broccoli);
        } else if (choice.equals("ca")) {
            processFood(carrots);
        } else if (choice.equals("be")) {
            processFood(beef);
        } else if (choice.equals("chi")) {
            processFood(chicken);
        } else if (choice.equals("f")) {
            processFood(fish);
        } else if (choice.equals("m")) {
            processFood(milk);
        } else if (choice.equals("y")) {
            processFood(yogurt);
        } else if (choice.equals("che")) {
            processFood(cheese);
        } else if (choice.equals("e")) {
            processFood(egg);
        } else {
            System.out.println("Selection not valid.");
        }
    }

    //EFFECTS: updates the intake calories from food and update 
    //         amount of calories remaining for the day and add food to
    //         list consumed food 
    private void processFood(Food food) {
        double grams;
        double calories; 
        double remaining = account.getRemainingCalories();

        while (true) {
            grams = processAmount();
            calories = food.getCaloriesPerAmount(grams);

            if (calories > remaining) {
                System.out.println("Error: You only have "
                    + String.format("%.2f", remaining)
                    + " kcal left today.");
                System.out.println("Please enter a smaller amount.");
            } else { 
                break; //valid amount
            }
        }
        printCaloriesConsumed(calories);
        double newRemain = account.consumeNewFood(calories);
        printCaloriesRemain(newRemain);
        account.addFood(food);
    }

    //EFFECTS: display choices of fruits food 
    private void displayFruitsChoices() {
        System.out.println("\nSelect Fruit choices:");
        System.out.println("\ta -> Apple");
        System.out.println("\tba -> Banana");
    }

    //EFFECTS: display choices of vegetables
    private void displayVegetablesChoices() {
        System.out.println("\nSelect Vegetables choices:");
        System.out.println("\tbr -> Broccoli");
        System.out.println("\tca -> Carrots");
    }

    //EFFECTS: display choices of meat  
    private void displayMeatChoices() {
        System.out.println("\nSelect Meat choices:");
        System.out.println("\tbe -> Beef");
        System.out.println("\tchi -> Chicken (Breast)");
        System.out.println("\tf -> Fish (Salmon)");
    }

    //EFFECTS: display choices of dairy food 
    private void displayDairyChoices() {
        System.out.println("\nSelect Dairy choices:");
        System.out.println("\tm -> Milk (1% Fat)");
        System.out.println("\ty -> Yogurt (Greek)");
        System.out.println("\tche -> Cheese (Cottage)");
    }

    //EFFECTS: display choices of protein food 
    private void displayProteinChoices() {
        System.out.println("\nSelect Protein choices:");
        System.out.println("\te -> Egg (Boiled)");
    }

    //EFFECTS: display cosumed food history
    private void displayHistory() {
        System.out.println("List of consumed food so far:");
        List<Food> foods = account.getConsumedFoods();

        if (foods.isEmpty()) {
            System.out.println(account.getUsername() + " has not consumed anything");
        } else { //print every get(num) in list -> for loop 
            System.out.println(account.getUsername() + " consumed: ");
            for (Food food: foods) { //for every element in food that is string
                System.out.println("\t " + food.getFoodName());
            }
        }  
    }


    //MODIFIES: this 
    //EFFECTS: display choices of food 
    private void displayChoices() {
        System.out.println("\nSelect food choices:");
        System.out.println("\tfruits");
        System.out.println("\tvegetables");
        System.out.println("\tmeat");
        System.out.println("\tprotein");
        System.out.println("\tdairy");
        System.out.println("\thistory");
        System.out.println("\tsave");
        System.out.println("\tload");
        System.out.println("\tquit");
    }

    @SuppressWarnings("methodlength")
    //EFFECTS: prompt user to initialize their account and information 
    private void init() {
        input = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = input.next(); //user enter username

        System.out.println("Enter your TDEE (in kcal): ");
        //check if entered double 

        while (!input.hasNextDouble()) {
            System.out.println("Error");
            System.out.println("Please enter a number: ");
            input.next(); //user input (string) but then get checked back 
                          // at start of loop to see if it is double
        }
        double tdee = input.nextDouble();

        System.out.println("Enter your remain calories of the day (in kcal): ");
        //check if entered double and if its >= 0
        while (!input.hasNextDouble()) { //loop
            System.out.println("Error");
            System.out.println("Please enter a number: ");
            input.next();
        } //check if input is double 

        double remain = input.nextDouble(); //rememberes the input remaining calories

        while (remain <= 0) { //loop
            System.out.println("Error");
            System.out.println("Please enter a non-negative number: ");
            while (!input.hasNextDouble()) { //loop
                System.out.println("Error");
                System.out.println("Please enter a number: ");
                input.next();
            } //check if input is double 

            remain = input.nextDouble(); //finialize the existed 
        } //check if input is > 0 and see if the re-enters is both double and > 0

        account = new Account(username, tdee, remain);
        

    }

    //EFFECTS: prints total calories user can consume left, 
    // throws IllegalArgumentException if remains < 0
    private void printCaloriesRemain(double remains) {
        System.out.printf("Total remaining calories of the day: %.2f kcal\n", remains);
    }

    //EFFECTS: prints total calories user consumed from food 
    private void printCaloriesConsumed(double consumed) {
        System.out.printf("Total consumed calories from meal: %.2f kcal\n", consumed);
    }

    // EFFECTS: saves the workroom to file
    private void saveAccount() {
        try {
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
            System.out.println("Saved " + account.getUsername() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadAccount() {
        try {
            account = jsonReader.read();
            System.out.println("Loaded " + account.getUsername() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //EFFECTS: print out all the logged events 
    private void runShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            StringBuilder sb = new StringBuilder();

            for (Event e: EventLog.getInstance()) {
                sb.append(e.toString() + "\n");
            }

            System.out.println(sb);
            
        }));
    }


    

}

