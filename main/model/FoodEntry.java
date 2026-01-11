package model;

public class FoodEntry {
    private Food food; 
    private double quantity; 

    public FoodEntry(Food food, double quantity) {
        this.food  = food;
        this.quantity = quantity;
    }

    public Food getFood() {
        return food; 
    }

    public double getQuantity() {
        return quantity;
    }
}
