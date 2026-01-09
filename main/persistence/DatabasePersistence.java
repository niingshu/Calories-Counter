package persistence;
/*
this file is resposible for 
opening database connections, executing sql, converting rows <-> java objects
*/
import model.Account;
import model.Food;

import java.sql.*;
import java.math.BigDecimal;
import java.util.List; 
import java.util.ArrayList;

public class DatabasePersistence {

    public List<Food> getAllFoods() throws SQLException {
        List<Food> foodList = new ArrayList<>();
        String query = "SELECT id, name, calories FROM foods ORDER BY name";
        String url = "jdbc:postgresql://localhost:5432/calories_counter"; 
        String user = "ningshu";
        String password = "password";

        //open connection with db
        try (Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String foodName = rs.getString("name");
                BigDecimal foodCal = rs.getBigDecimal("calories");
                double foodKal = foodCal.doubleValue();
                
                //create new food every row
                Food food = new Food(id, foodName, foodKal); 
                foodList.add(food); // Add the object to the list
            }
        } // Resources are closed automatically here

        return foodList;
    }



    public void logFood(int foodId, BigDecimal quantity) throws SQLException {
        String input = "INSERT INTO daily_log(food_id, quantity, eaten_at) VALUES (?, ?, CURRENT_DATE)";
        //java sends exactly one statement 
        //jdbc already knows where the statement ends
        
        String url = "jdbc:postgresql://localhost:5432/calories_counter";
        String username = "ningshu";
        String password = "password";
        
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = con.prepareStatement(input);
            
            ps.setInt(1, foodId);
            ps.setBigDecimal(2, quantity);
            
            ps.executeUpdate();
        }
    }

    public BigDecimal getCaloriesConsumedToday() { //because database use numeric(6,2)

    }
}
