package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents an account having username, user's TDEE and 
// amount of calorie can be intake left through the day (in kcal)
public class Account implements Writable {
    private String username;    //account username
    private double tdee;        //the user TDEE
    private double left;        //the amount of calorie user can consume left
    private List<Food> consumedFoods = new ArrayList<>(); //remembers consumed foods

    //REQUIRES: initialLeft <= TDEE
    //EFFECTS: username is set to accountName
    //         if initialLeft >= 0 then intake of user is set to 
    //         initialIntake, otherwise intake is zero.
    public Account(String accountName, double max, double initialLeft) {
        username = accountName;
        tdee = max;
        if (initialLeft >= 0) {
            left = initialLeft;
        } else {
            left = 0;
        }
        consumedFoods = new ArrayList<>(); // create empty food list

    }
    
    public String getUsername() {
        return username;
    }

    public double getTdee() {
        return tdee;
    }

    public double getCaloLeft() {
        return left;
    }

    public List<Food> getConsumedFoods() {
        return consumedFoods;
    }

    public int numFoods() {
        return consumedFoods.size();
    }

    public double getRemainingCalories() {
        return left; 
    }

    //EFFECTS: add new consumed food to food list and add the log to collection of event
    public void addFood(Food foodName) {
        consumedFoods.add(foodName);
        EventLog.getInstance().logEvent(new Event("Added new food: " + foodName.getFoodName())); 
        //return an event log 

    }

    //REQURES: caloIn >= 0 && caloIn <= left
    //MODIFIES: this
    //EFFECTS: deduct the amount of calorie the user can have after
    //         consume food (caloIn) and update amount calories 
    //         the user have left
    public double consumeNewFood(double caloIn) {
        left -= caloIn;
        return left;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", username);
        json.put("TDEE", tdee);
        json.put("left", left);
        json.put("foods", foodsToJson());
        return json;
    }

    // EFFECTS: returns things in this account as a JSON array
    private JSONArray foodsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Food f : consumedFoods) {
            jsonArray.put(f.toJson());
        }

        return jsonArray;
    }
}
