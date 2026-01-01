package ui;

import javax.swing.*;
import java.awt.*;

public class Images {

    //load and scale images and return an imageicon
    public static ImageIcon getScaledIcon(String foodName, int width, int length) {
        String getImage = "/images/" + foodName + ".jpg"; // Fixed path here
        java.net.URL imgURL = Images.class.getResource(getImage);
    
        if (imgURL != null) {
            ImageIcon image = new ImageIcon(imgURL);
            Image scaled = image.getImage().getScaledInstance(width, length, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } else {
            System.err.println("Image not found: " + getImage);
            return null;
        }
    }


}
