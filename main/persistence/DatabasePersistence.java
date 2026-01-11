package persistence;
/*
this file is resposible for 
opening database connections, executing sql, converting rows <-> java objects
*/
import model.Account;
import model.Food;
import model.FoodEntry;

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
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String foodName = rs.getString("name");
                BigDecimal foodCal = rs.getBigDecimal("calories");
                double foodKal = foodCal.doubleValue();
                
                //create new food every row
                Food food = new Food(id, foodName, foodKal); 
                foodList.add(food); // Add the object to the list
            }
        } catch (Exception e) {
            System.err.println("food");
        }

        return foodList;
    }


    public void getLogs(Account acc) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/calories_counter"; 
        String user = "ningshu";
        String password = "password";
        String query = "SELECT d.food_id, d.quantity, f.name, f.calories " + 
                        "FROM daily_log d " + 
                        "JOIN foods f ON d.food_id = f.id"; 

        try (Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) { //while there is food (execute from the result table)
                //i want to take the food and use acc.addFood(Food)
                int foodId = rs.getInt("food_id"); 
                String foodName = rs.getString("name");
                BigDecimal foodCalories = rs.getBigDecimal("calories");
                BigDecimal quantity = rs.getBigDecimal("quantity");
                double calories = foodCalories.doubleValue();
                double quan = quantity.doubleValue();

                Food newFood = new Food(foodId, foodName, calories); 
                acc.addFood(newFood, quan);

                System.err.println(foodId + "is added l");
                System.err.println(quantity + "is added l");
            }
        } catch (Exception e) {
            System.err.println("log");
        }
    }

    public Account getAccount() throws SQLException {
        String query = "SELECT username, tdee, calories_left FROM account ORDER BY username"; 
        String url = "jdbc:postgresql://localhost:5432/calories_counter"; 
        String user = "ningshu";
        String password = "password";

        Account acc = new Account("", 0, 0);

        try (Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String name = rs.getString("username");
                int tdee = rs.getInt("tdee");
                BigDecimal calLeft = rs.getBigDecimal("calories_left");
                double left = calLeft.doubleValue();
                
                //create new food every row
                acc = new Account(name, tdee, left); 
            }
        } catch (Exception e) {
            System.err.println("acc");
        }

        return acc;
    }

    public void eatFoods(List<FoodEntry> foods) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/calories_counter";
        String username = "ningshu";
        String password = "password";
        
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            con.prepareStatement("TRUNCATE TABLE daily_log").executeUpdate();
            
            for (FoodEntry food: foods) {
                BigDecimal quantity = BigDecimal.valueOf(food.getQuantity());
                eatFood(food.getFood().getId(), quantity);
            }

        } catch (Exception e) {
            System.err.println("eat food");
        }
    }

    public void eatFood(int foodId, BigDecimal quantity) throws SQLException {
        // String input = "TRUNCATE TABLE daily_log " +
        //                 "INSERT INTO daily_log(food_id, quantity, eaten_at) VALUES (?, ?, CURRENT_DATE)";
        //java sends exactly one statement 
        //jdbc already knows where the statement ends
        
        String url = "jdbc:postgresql://localhost:5432/calories_counter";
        String username = "ningshu";
        String password = "password";
        
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO daily_log(food_id, quantity, eaten_at) VALUES (?, ?, CURRENT_DATE)");
            
            ps.setInt(1, foodId);
            ps.setBigDecimal(2, quantity);
            
            ps.executeUpdate();

            System.err.println(foodId + "is added s");
            System.err.println(quantity + "is added s");
        } catch (Exception e) {
            System.err.println("eat food");
        }
    }

    public void updateUser(Account acc) throws SQLException {
        String input = "TRUNCATE TABLE account " +
                        "INSERT INTO account(username, tdee, calories_left) VALUES (?, ?, ?)";
        //java sends exactly one statement 
        //jdbc already knows where the statement ends
        
        String url = "jdbc:postgresql://localhost:5432/calories_counter";
        String username = "ningshu";
        String password = "password";

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            con.prepareStatement("TRUNCATE TABLE account").executeUpdate();
            PreparedStatement ps =
                                con.prepareStatement("INSERT INTO account(username, tdee, calories_left) VALUES (?, ?, ?)");
            
            ps.setString(1, acc.getUsername());
            BigDecimal tdee = BigDecimal.valueOf(acc.getTdee());
            ps.setBigDecimal(2, tdee);
            BigDecimal left = BigDecimal.valueOf(acc.getCaloLeft());
            ps.setBigDecimal(3, left);
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("user");
        }
    }

    public BigDecimal getCaloriesConsumedToday() throws SQLException { //because database use numeric(6,2)
        String url = "jdbc:postgresql://localhost:5432/calories_counter";
        String username = "ningshu";
        String password = "password";

        String input = "SELECT SUM(f.calories * d.quantity) AS calories_consumed " +  
                        "FROM daily_log d " + 
                        "JOIN foods f ON d.food_id = f.id " + 
                        "WHERE d.eaten_at = CURRENT_DATE ";

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = con.prepareStatement(input);
            ResultSet res = ps.executeQuery();

            if (res.next()) {
                BigDecimal result =  res.getBigDecimal("calories_consumed"); 
                return result != null ? result: BigDecimal.ZERO;
            }
        } 

        return BigDecimal.ZERO;
    }
}
