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

        List<FoodEntry> fEntries = testAccount.getConsumedFoods();

        Food apple = new Food(1, "Apple", 52.1);

        testAccount.addFood(apple, 30.0);
        assertEquals(1, fEntries.size()); //check the legth of list
        assertEquals("Apple", fEntries.get(0).getFood().getFoodName());

        Food banana = new Food(4, "Banana", 88.7);
        testAccount.addFood(banana, 42.5);
        assertEquals(2, fEntries.size());
        assertEquals("Banana", fEntries.get(1).getFood().getFoodName()); //see if add properly
    }
}
