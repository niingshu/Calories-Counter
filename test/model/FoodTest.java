package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {
    private Food testFood;

    @BeforeEach
    void runBefore() {
        testFood = new Food(1, "Apple", 52.1);
    }

    @Test
    void testConstructor() {
        assertEquals("Apple", testFood.getFoodName());
        assertEquals(52.1, testFood.getCalories());
    }

    @Test
    void testgetCaloriesPerAmount() {
        assertEquals(26.05, testFood.getCaloriesPerAmount(50.0));
    }
}
