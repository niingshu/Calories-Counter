package persistence;

import model.Account;
import model.Food;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Referenced from the JsonSerialization Demo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads account from JSON data stored in file
// ACCOUNT CLASS
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads account from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Account read() throws IOException { // reads from account 
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccount(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses account from JSON object and returns it
    private Account parseAccount(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double max = jsonObject.getDouble("TDEE");
        double initLeft = jsonObject.getDouble("left");
        Account newAcc = new Account(name, max, initLeft);
        addFoods(newAcc, jsonObject);
        return newAcc; //return an account 
    }

    // MODIFIES: account
    // EFFECTS: parses foods from JSON object and adds them to account
    private void addFoods(Account newAcc, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("foods");
        for (Object json : jsonArray) {
            JSONObject nextFood = (JSONObject) json;
            addFood(newAcc, nextFood);
        }
    }

    // MODIFIES: account
    // EFFECTS: parses foods from JSON object and adds it to account
    private void addFood(Account newAcc, JSONObject jsonObject) {
        String name = jsonObject.getString("food");
        double caloPer100 = jsonObject.getDouble("calories per 100");
        Food newFood = new Food(name, caloPer100);
        newAcc.addFood(newFood);
    }
}
