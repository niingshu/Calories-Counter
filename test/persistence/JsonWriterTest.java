package persistence;

import model.Food;
import model.Account;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Account newAcc = new Account("ning", 1200, 1200);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            Account newAcc = new Account("ning", 1200, 1200);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(newAcc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            newAcc = reader.read();
            assertEquals("ning", newAcc.getUsername());
            assertEquals(0, newAcc.getConsumedFoods().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Account newAcc = new Account("ning", 1200, 1200);
            newAcc.addFood(new Food("Apple", 52.1));
            newAcc.addFood(new Food("Beef", 250.5));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(newAcc);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            newAcc = reader.read();
            assertEquals("ning", newAcc.getUsername());
            List<Food> foods = newAcc.getConsumedFoods();
            assertEquals(2, foods.size());
            checkFood("Apple", 52.1, foods.get(0));
            checkFood("Beef", 250.5, foods.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
