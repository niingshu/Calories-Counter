package model;

import persistence.Writable;
import org.json.JSONObject;

//Represent the food (name) with its calories per 100g
public class Food implements Writable {
    private String food;        //type of food
    private double calories;    //calories of food per 100g

    //EFFECTS: remembers the name of the food and the
    //         calories it has per 100g 
    public Food(String food, double calories) {
        this.food = food;
        this.calories = calories;
    }

    public String getFoodName() {
        return food;
    }

    public double getCalories() {
        return calories;
    }

    //REQUIRES: grams >= 0
    //EFFECTS: calculate the actual intake calories 
    //         for the amount of food consumed
    public double getCaloriesPerAmount(double grams) {
        return (calories / 100) * grams;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("food", food);
        json.put("calories per 100", calories);
        return json;
    }
}
