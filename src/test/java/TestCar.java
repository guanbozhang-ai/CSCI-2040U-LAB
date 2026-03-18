//TODO: might want to update to match new Car structure

//import org.junit.jupiter.api.Test;
//import java.util.HashMap;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class TestCar {
//    public Car makeTestCar() {
//        return new Car("Honda", "Civic", "Hatchback",
//                150, 15000, 30000, 5);
//    }
//
//    @Test
//    public void testCarConstructor() {
//        Car car = makeTestCar();
//        assertEquals("Honda", car.getMake());
//        assertEquals("Civic", car.getModel());
//        assertEquals("Hatchback", car.getBodyType());
//        assertEquals(150, car.getHorsepower());
//        assertEquals(15000, car.getPrice());
//        assertEquals(30000, car.getMileage());
//        assertEquals(5, car.getSeating());
//    }
//
//    @Test
//    public void testCarAttributes() {
//        Car car = makeTestCar();
//        HashMap<String, Double> expectedAttributes = new HashMap<>();
//        expectedAttributes.put("cost", 0.75);
//        expectedAttributes.put("sportiness", 1.50);
//        expectedAttributes.put("mileage", 1.50);
//        expectedAttributes.put("seating", 3.125);
//        assertEquals(expectedAttributes, car.getCarAttributes());
//    }
//
//    @Test
//    public void testToJson() {
//        Car car = makeTestCar();
//        String expectedJson = "{\"make\":\"Honda\"," +
//                "\"model\":\"Civic\"," +
//                "\"bodyType\":\"Hatchback\"," +
//                "\"horsepower\":150," +
//                "\"price\":15000," +
//                "\"mileage\":30000," +
//                "\"seating\":5}";
//        assertEquals(expectedJson, car.toJson());
//    }
//
//    @Test
//    public void testFromJson() {
//        Car car = makeTestCar();
//        String json = "{\"make\":\"Honda\"," +
//                "\"model\":\"Civic\"," +
//                "\"bodyType\":\"Hatchback\"," +
//                "\"horsepower\":150," +
//                "\"price\":15000," +
//                "\"mileage\":30000," +
//                "\"seating\":5}";
//        Car jsonCar = Car.fromJson(json);
//        assertEquals(car.getMake(), jsonCar.getMake());
//        assertEquals(car.getModel(), jsonCar.getModel());
//        assertEquals(car.getBodyType(), jsonCar.getBodyType());
//        assertEquals(car.getPrice(), jsonCar.getPrice());
//        assertEquals(car.getHorsepower(), jsonCar.getHorsepower());
//        assertEquals(car.getSeating(), jsonCar.getSeating());
//        assertEquals(car.getMileage(), jsonCar.getMileage());
//    }
//
//}
