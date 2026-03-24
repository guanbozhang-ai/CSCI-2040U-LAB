import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCar {
    public Car makeTestCar() throws Exception {
        return new Car(1, 2024, "Honda", "Civic", 15000,
                "Hatchback", "Type R", "Red", "Grey",
                "Gas", 150, 30000, 8.0,
                "Inline", "FF", 5, 4,
                6, "Automatic", "N/A");
    }

    @Test
    public void testCarConstructor() throws Exception {
        Car car = makeTestCar();
        assertEquals(1, car.getId());
        assertEquals(2024, car.getYear());
        assertEquals("Honda", car.getMake());
        assertEquals("Civic", car.getModel());
        assertEquals(15000, car.getPrice());
        assertEquals("Hatchback", car.getBodyType());

        assertEquals("Type R", car.getTrim());
        assertEquals("Red", car.getExtColour());
        assertEquals("Grey", car.getIntColour());
        assertEquals("Gas", car.getFuelType());
        assertEquals(150, car.getHorsepower());

        assertEquals(30000, car.getMileage());
        assertEquals(8.0, car.getFuelEconomy());
        assertEquals("Inline", car.getEngineConfiguration());
        assertEquals("FF", car.getDrivetrainConfiguration());
        assertEquals(5, car.getSeating());

        assertEquals(4, car.getCylinders());
        assertEquals(6, car.getGears());
        assertEquals("Automatic", car.getTransmission());
        assertEquals("N/A", car.getImageURL());
    }

    @Test
    public void testCarAttributes() throws Exception {
        Car car = makeTestCar();
        HashMap<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("cost", 0.75);
        expectedAttributes.put("sportiness", 1.50);
        expectedAttributes.put("mileage", 1.50);
        expectedAttributes.put("seating", 3.125);
        expectedAttributes.put("economy", 2.0);
        expectedAttributes.put("recency", 1.0 / 3.0);
        assertEquals(expectedAttributes, car.getCarAttributes());
    }

    @Test
    public void testToJson() throws Exception {
        Car car = makeTestCar();
        String expectedJson = "{\"id\":1," +
                "\"year\":2024," +
                "\"make\":\"Honda\"," +
                "\"model\":\"Civic\"," +
                "\"price\":15000," +
                "\"bodyType\":\"Hatchback\"," +
                "\"trim\":\"Type R\"," +
                "\"extColour\":\"Red\"," +
                "\"intColour\":\"Grey\"," +
                "\"fuelType\":\"Gas\"," +
                "\"horsepower\":150," +
                "\"mileage\":30000," +
                "\"fuelEconomy\":8.0," +
                "\"engineConfiguration\":\"Inline\"," +
                "\"drivetrainConfiguration\":\"FF\"," +
                "\"seating\":5," +
                "\"cylinders\":4," +
                "\"gears\":6," +
                "\"transmission\":\"Automatic\"," +
                "\"imageURL\":\"N/A\"}";
        assertEquals(expectedJson, car.toJson());
    }

    @Test
    public void testFromJson() throws Exception {
        Car car = makeTestCar();
        String json = "{\"id\":1," +
                "\"year\":2024," +
                "\"make\":\"Honda\"," +
                "\"model\":\"Civic\"," +
                "\"price\":15000," +
                "\"bodyType\":\"Hatchback\"," +
                "\"trim\":\"Type R\"," +
                "\"extColour\":\"Red\"," +
                "\"intColour\":\"Grey\"," +
                "\"fuelType\":\"Gas\"," +
                "\"horsepower\":150," +
                "\"mileage\":30000," +
                "\"fuelEconomy\":8.0," +
                "\"engineConfiguration\":\"Inline\"," +
                "\"drivetrainConfiguration\":\"FF\"," +
                "\"seating\":5," +
                "\"cylinders\":4," +
                "\"gears\":6," +
                "\"transmission\":\"Automatic\"," +
                "\"imageURL\":\"N/A\"}";
        Car jsonCar = Car.fromJson(json);
        assertEquals(car.getMake(), jsonCar.getMake());
        assertEquals(car.getModel(), jsonCar.getModel());
        assertEquals(car.getBodyType(), jsonCar.getBodyType());
        assertEquals(car.getPrice(), jsonCar.getPrice());
        assertEquals(car.getHorsepower(), jsonCar.getHorsepower());
        assertEquals(car.getSeating(), jsonCar.getSeating());
        assertEquals(car.getMileage(), jsonCar.getMileage());
    }

}
