import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;

public class TestUser {
    public User makeTestUser() {
        ArrayList<String> preferredMakes = new ArrayList<>();
        preferredMakes.add("Honda");
        ArrayList<String> preferredBodyTypes = new ArrayList<>();
        preferredBodyTypes.add("Sedan");

        return new User(20000, 4, 200, 2,
                0, 5, 5, 2,
                4, 5, 2025, 2,
                preferredMakes, preferredBodyTypes,
                "Electric", "Automatic");
    }

    public UserAttributeMap makeTestUserAttributeMap() {
        UserAttributeMap attributeMap = new UserAttributeMap();
        attributeMap.addAttribute("cost", 1.0, 4.0);
        attributeMap.addAttribute("sportiness", 2.0, 2.0);
        attributeMap.addAttribute("mileage", 0.0, 5.0);
        attributeMap.addAttribute("seating", 3.125, 2.0);
        attributeMap.addAttribute("economy", 1.0, 5.0);
        attributeMap.addAttribute("recency", 0.0, 2.0);

        return attributeMap;
    }

    public Car makeTestCar() throws Exception {
        return new Car(1, 2024, "Honda", "Civic", 15000,
                "Hatchback", "Type R", "Red", "Grey",
                "Gas", 150, 30000, 8.0,
                "Inline", "FF", 5, 4,
                6, "Automatic", "N/A");
    }

    @Test
    public void testUserConstructor() {
        User user = makeTestUser();
        UserAttributeMap attributeMap = makeTestUserAttributeMap();
        assertEquals(attributeMap.getValue("cost"),
                user.getUserAttributes().getValue("cost"));
        assertEquals(attributeMap.getValue("sportiness"),
                user.getUserAttributes().getValue("sportiness"));
        assertEquals(attributeMap.getValue("mileage"),
                user.getUserAttributes().getValue("mileage"));
        assertEquals(attributeMap.getValue("seating"),
                user.getUserAttributes().getValue("seating"));
        assertEquals(attributeMap.getValue("economy"),
                user.getUserAttributes().getValue("economy"));
        assertEquals(attributeMap.getValue("recency"),
                user.getUserAttributes().getValue("recency"));
    }

    @Test
    public void testMatching() throws Exception {
        //distance: sqrt(0.0 (cost) + 0.5 (sportiness)
        // + 11.25 (mileage) + 0 (seating) + 5.0 (economy) + 2/9 (recency)
        // 0 (make) + 1 (body type)
        // + 1 (fuel type) + 0 (transmission) = 18.75 + 2/9
        double expectedDistance = Math.sqrt(18.75 + (2.0/9.0));
        Car car = makeTestCar();
        User user = makeTestUser();
        assertEquals(expectedDistance, user.match(car));
    }
}
