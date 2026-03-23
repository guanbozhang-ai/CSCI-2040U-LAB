import java.util.ArrayList;
import java.util.HashSet;

public class CarStock {
    public HashSet<String> MAKES = new HashSet<>();
    public HashSet<String> BODY_TYPES = new HashSet<>();

    private ArrayList<Car> cars;
    private final CarStorage storage;
    //TODO: make a list (initialize on runtime) of all possible bodyTypes in Car class
    //TODO: make a list (initialize on runtime) of all possible makes

    /**
     * Constructs a CarStock object given a file path
     * @param storageFilePath String containing the file path of .json file
     */
    public CarStock(String storageFilePath) {
        this.storage = new CarStorage(storageFilePath); // Create storage handler
        this.cars = storage.load(); // Load cars from file into memory

        readMakes();
        readBodyTypes();

        System.out.println("Loaded " + cars.size() + " car(s) from " + storageFilePath); // Print how many loaded
    }

    /**
     * Initializes list of all makes in the current stock
     */
    private void readMakes() {
        for (Car car : cars) {
            MAKES.add(car.getMake());
        }
    }

    /**
     * Initializes list of all body types in the current stock
     */
    private void readBodyTypes() {
        for (Car car : cars) {
            BODY_TYPES.add(car.getBodyType());
        }
    }

    /**
     * Adds a Car to the CarStock and saves it to the .json file
     * @param car The Car to be added
     */
    public void addCar(Car car) {
        cars.add(car);
        storage.save(cars);

        MAKES.add(car.getMake());
        BODY_TYPES.add(car.getBodyType());
    }

    /**
     * Uses the Euclidean distance between the attribute vectors of a Car
     * and User to determine which Car is the best match for a given User.
     * @param user The User to be matched to a car
     * @return The Car closest to the User's needs in the CarStock
     */
    public Car findBestMatch(User user) {
        double bestMatch = Double.MAX_VALUE;
        Car bestCar = null;
        for (Car car : cars) {
            double distance = user.match(car);
            if(distance < bestMatch) {
                bestMatch = distance;
                bestCar = car;
            }
        }
        return bestCar;
    }

    /**
     * Prints all cars in inventory
     */
    public void printInventory() {
        if (cars.isEmpty()) {
            System.out.println("Inventory is empty."); // Show message
            return;
        }

        System.out.println("Current inventory:");
        // Print each car
        for (Car car : cars) {
            System.out.println(car);
        }
    }
}
