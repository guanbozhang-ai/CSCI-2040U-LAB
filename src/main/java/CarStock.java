import java.util.ArrayList;

public class CarStock {
    private ArrayList<Car> cars;
    private final CarStorage storage;

    // Constructor: load cars from file
    public CarStock(String storageFilePath) {
        this.storage = new CarStorage(storageFilePath); // Create storage handler
        this.cars = storage.load(); // Load cars from file into memory
        System.out.println("Loaded " + cars.size() + " car(s) from " + storageFilePath); // Print how many loaded
    }

    public void addCar(Car car) {
        cars.add(car);
        storage.save(cars);
    }

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

    // Print all cars in inventory
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
