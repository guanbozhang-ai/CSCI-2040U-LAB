import java.util.ArrayList;

public class CarStock {

    private ArrayList<Car> cars;   // List of cars in memory
    private final CarStorage storage; // Handles saving/loading from file

    // Constructor: load cars from file
    public CarStock(String storageFilePath) {

        this.storage = new CarStorage(storageFilePath); // Create storage handler

        this.cars = storage.load(); // Load cars from file into memory

        System.out.println("Loaded " + cars.size() + " car(s) from " + storageFilePath); // Print how many loaded
    }

    // Add a car to memory and save immediately
    public void addCar(Car car) {
        cars.add(car);        // Add car to list
        storage.save(cars);   // Save updated list to file
    }

    // Find the car closest to user's preferences
    // Uses Euclidean distance (math distance formula)
    public Car findBestMatch(ArrayList<Double> userAttributes) {

        if (cars.isEmpty()) {
            System.out.println("No cars in stock.");
            return null;
        }

        Car bestCar = null;
        double bestScore = Double.MAX_VALUE;

        // Loop through every car
        for (Car car : cars) {

            // Calculate distance between user and car
            double distance = euclideanDistance(
                    userAttributes,
                    car.getCarAttributes()
            );

            // If this car is closer than previous best
            if (distance < bestScore) {
                bestScore = distance; // Update best distance
                bestCar = car;        // Update best car
            }
        }
        return bestCar; // Return closest car
    }

    // Calculate Euclidean distance between two lists
    // Only compare up to shortest list length
    private double euclideanDistance(ArrayList<Double> a, ArrayList<Double> b) {

        int len = Math.min(a.size(), b.size()); // Use shortest length

        double sum = 0; // Store sum of squared differences

        for (int i = 0; i < len; i++) {
            double diff = a.get(i) - b.get(i); // Difference
            sum += diff * diff; // Square it and add to sum
        }

        return Math.sqrt(sum);
    }

    // Return all cars
    public ArrayList<Car> getAllCars() {
        return cars;
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
            System.out.println(
                    "  " + car + " | attributes: " + car.getCarAttributes()
            );
        }
    }
}