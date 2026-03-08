import java.util.ArrayList;

import java.util.ArrayList;

public class CarStock {

    private ArrayList<Car> cars;   // List of cars in memory
    private final CarStorage storage; // Handles saving/loading from file

    // Constructor: load cars from file
    public CarStock(String storageFilePath) {
        this.storage = new CarStorage(storageFilePath); // Create storage handler
        this.cars = storage.load(); // Load cars from file into memory
        System.out.println("Loaded " + cars.size() + " car(s) from " + storageFilePath);
    }

    // No-arg constructor for tests or simple use (does not persist)
    public CarStock() {
        this.storage = null;
        this.cars = new ArrayList<>();
    }

    // Add a car to memory and save immediately if storage exists
    public void addCar(Car car) {
        cars.add(car);
        if (storage != null) {
            storage.save(cars);
        }
    }

    // Find the closest car to the user using User.match
    public Car findBestMatch(User user) {
        if (cars.isEmpty()) {
            System.out.println("No cars in stock.");
            return null;
        }
        Car bestCar = null;
        double bestScore = Double.MAX_VALUE;
        for (Car car : cars) {
            double distance = user.match(car);
            if (distance < bestScore) {
                bestScore = distance;
                bestCar = car;
            }
        }
        return bestCar;
    }

    public ArrayList<Car> getAllCars() {
        return cars;
    }

    public void printInventory() {
        if (cars.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("Current inventory:");
        for (Car car : cars) {
            System.out.println("  " + car + " | attributes: " + car.getCarAttributes());
        }
    }
}