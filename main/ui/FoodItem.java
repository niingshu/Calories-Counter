package ui;

import model.Food;
import javax.swing.*;

public class FoodItem {
    private Food food; 
    private ImageIcon imageIcon;

    //construct a food item with the given food and display with the
    //same imageicon generated from imageicon java class 
    public FoodItem(Food insertFood, ImageIcon insertImageIcon) {
        this.food = insertFood;
        this.imageIcon = insertImageIcon;
    }
    //turn food and imageicon into 1 single object 
    //so its easier to handle

    public Food getFood() { //getter
        return food;
    }

    public ImageIcon getImageIcon() { //getter
        return imageIcon;
    }

}
