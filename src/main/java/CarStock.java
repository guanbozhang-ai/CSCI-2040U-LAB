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
     * and User to determine which 5 Cars are the best match for a given User.
     * @param user The User to be matched to a car
     * @return The 5 Cars closest to the User's needs in the CarStock
     */
    public ArrayList<Car> findBestMatch(int num, User user) {
        ArrayList<Double> bestMatches = new ArrayList<>();
        ArrayList<Car> bestCars = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            bestMatches.add(Double.MAX_VALUE);
            bestCars.add(null);
        }

        for (Car car : cars) {
            double distance = user.match(car);
            if(distance < bestMatches.get(num - 1)) {

                for(int i = 0; i < num; i++) {
                    if(distance < bestMatches.get(i)) {
                        bestMatches.add(i, distance);
                        bestMatches.removeLast();
                        bestCars.add(i, car);
                        bestCars.removeLast();
                        break;
                    }
                }

            }
        }

        return bestCars;
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
