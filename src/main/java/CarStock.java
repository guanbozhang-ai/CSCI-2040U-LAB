import java.util.ArrayList;
import java.util.List;

public class CarStock {
    private ArrayList<Car> cars;
    private final CarStorage storage;

    /**
     * Constructs a CarStock object given a file path
     * @param storageFilePath String containing the file path of .json file
     */
    public CarStock(String storageFilePath) {
        this.storage = new CarStorage(storageFilePath);
        this.cars = storage.load();
        System.out.println("Loaded " + cars.size() + " car(s) from " + storageFilePath);
    }

    /**
     * Adds a Car to the CarStock and saves it to the .json file
     * @param car The Car to be added
     */
    public void addCar(Car car) {
        cars.add(car);
        storage.save(cars);
    }

    /**
     * Returns the full list of cars (used by the GUI to build car cards).
     * @return ArrayList of all Car objects in stock
     */
    public ArrayList<Car> getCars() {
        return cars;
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
            if (distance < bestMatch) {
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
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("Current inventory:");
        for (Car car : cars) {
            System.out.println(car);
        }
    }
}
