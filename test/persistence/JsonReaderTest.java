package persistence;

import model.Food;
import model.Account;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Account newAcc = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyAccount() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAccount.json");
        try {
            Account newAcc = reader.read();
            assertEquals("ning", newAcc.getUsername());
            assertEquals(0, newAcc.numFoods());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralAccount() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAccount.json");
        try {
            Account newAcc = reader.read();
            assertEquals("ning", newAcc.getUsername());
            List<Food> foods = newAcc.getConsumedFoods();
            assertEquals(2, foods.size());
            checkFood("Apple", 52.1, foods.get(0));
            checkFood("Beef", 250.5, foods.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
