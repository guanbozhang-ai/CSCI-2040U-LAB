//TODO: might want to update to match new Car structure

//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import java.util.ArrayList;
//
//public class TestUser {
//    public User makeTestUser() {
//        ArrayList<String> preferredMakes = new ArrayList<>();
//        preferredMakes.add("Honda");
//        ArrayList<String> preferredBodyTypes = new ArrayList<>();
//        preferredBodyTypes.add("Sedan");
//
//        return new User(20000, 4, 200, 2,
//                0, 5, 5, 2,
//                preferredMakes, preferredBodyTypes);
//    }
//
//    public UserAttributeMap makeTestUserAttributeMap() {
//        UserAttributeMap attributeMap = new UserAttributeMap();
//        attributeMap.addAttribute("cost", 1.0, 4.0);
//        attributeMap.addAttribute("sportiness", 2.0, 2.0);
//        attributeMap.addAttribute("mileage", 0.0, 5.0);
//        attributeMap.addAttribute("seating", 3.125, 2.0);
//
//        return attributeMap;
//    }
//
//    public Car makeTestCar() {
//        return new Car("Honda", "Civic", "Hatchback",
//                150, 15000, 30000, 5);
//    }
//
//    @Test
//    public void testUserConstructor() {
//        User user = makeTestUser();
//        UserAttributeMap attributeMap = makeTestUserAttributeMap();
//        assertEquals(attributeMap.getValue("cost"),
//                user.getUserAttributes().getValue("cost"));
//        assertEquals(attributeMap.getValue("sportiness"),
//                user.getUserAttributes().getValue("sportiness"));
//        assertEquals(attributeMap.getValue("mileage"),
//                user.getUserAttributes().getValue("mileage"));
//        assertEquals(attributeMap.getValue("seating"),
//                user.getUserAttributes().getValue("seating"));
//    }
//
//    @Test
//    public void testMatching() {
//        //distance: sqrt(0.25 (cost) + 0.5 (sportiness)
//        // + 11.25 (mileage) + 0 (seating) + 0 (make) + 25 (body type) )
//        double expectedDistance = Math.sqrt(37);
//        Car car = makeTestCar();
//        User user = makeTestUser();
//        assertEquals(expectedDistance, user.match(car));
//    }
//}
