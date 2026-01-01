package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AccountTest {
    private Account testAccount;
    
    @BeforeEach
    void runBefore() {
        testAccount = new Account("Vee", 1356.5, 1356.5);
    }

    @Test
    void testConstructor() {
        assertEquals("Vee", testAccount.getUsername());
        assertEquals(1356.5, testAccount.getTdee());
        assertEquals(1356.5, testAccount.getCaloLeft());
        assertTrue(testAccount.getTdee() >= testAccount.getCaloLeft());
    }

    @Test
    void testConstructorNegIntake() {
        testAccount = new Account("Grace", 1245.6, -3.15);
        assertEquals("Grace", testAccount.getUsername());
        assertEquals(1245.6, testAccount.getTdee());
        assertEquals(0, testAccount.getCaloLeft());
        assertTrue(testAccount.getTdee() >= testAccount.getCaloLeft());

    }

    @Test
    void testConsumeNewFood() {
        testAccount.consumeNewFood(35.6);
        assertEquals(1320.9, testAccount.getCaloLeft());
    }

    @Test
    void testConsumeMultipleNewFood() {
        testAccount.consumeNewFood(710.0);
        testAccount.consumeNewFood(160.2);
        assertEquals(486.3, testAccount.getCaloLeft());
    }

    @Test
    void testAddFood() {
        assertTrue(testAccount.getConsumedFoods().isEmpty()); //null at start

        List<Food> foods = testAccount.getConsumedFoods();

        Food apple = new Food("Apple", 52.1);

        testAccount.addFood(apple);
        assertEquals(1, foods.size()); //check the legth of list
        assertEquals("Apple", foods.get(0).getFoodName());

        Food banana = new Food("Banana", 88.7);
        testAccount.addFood(banana);
        assertEquals(2, foods.size());
        assertEquals("Banana", foods.get(1).getFoodName()); //see if add properly
    }
}
