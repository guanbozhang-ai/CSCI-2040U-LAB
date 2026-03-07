import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * Handles saving and loading cars from a JSON file.
 *
 * File format (cars.json):
 * [
 *   {"make":"Toyota","model":"Corolla","attributes":[1.0,2.4,3.2]},
 *   {"make":"Honda","model":"Civic","attributes":[2.0,0.4,2.3]}
 * ]
 */
public class CarStorage {

    private final String filePath; // Path to cars.json file

    public CarStorage(String filePath) {
        this.filePath = filePath; // Save file path
    }

    // Load all cars from the JSON file
    public ArrayList<Car> load() {

        ArrayList<Car> cars = new ArrayList<>();
        File file = new File(filePath); // Create file object
        if (!file.exists()) return cars;

        try {

            String content = new String(Files.readAllBytes(file.toPath())).trim();

            // Remove outer [ and ]
            content = content.substring(1, content.length() - 1).trim();
            if (content.isEmpty()) return cars;

            // Find each { ... } JSON block manually
            int depth = 0;  // Track nesting level
            int start = 0;  // Track start of object

            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i); // Current character

                if (c == '{') { // If object starts
                    if (depth == 0) start = i; // Mark start
                    depth++; // Increase depth
                }

                else if (c == '}') { // If object ends
                    depth--; // Decrease depth

                    if (depth == 0) { // Full object found
                        String block = content.substring(start, i + 1); // Extract JSON
                        cars.add(Car.fromJson(block)); // Convert to Car and add
                    }
                }
            }

        } catch (IOException e) { // If file read fails
            System.err.println("Error reading " + filePath + ": " + e.getMessage());
        }

        return cars; // Return loaded cars
    }

    // Save list of cars to file overwrite old content
    public void save(ArrayList<Car> cars) {

        StringBuilder sb = new StringBuilder(); // Build JSON text

        sb.append("[\n"); // Start JSON array

        for (int i = 0; i < cars.size(); i++) {

            sb.append("  ").append(cars.get(i).toJson()); // Add car JSON

            if (i < cars.size() - 1) sb.append(","); // Add comma if not last

            sb.append("\n"); // New line
        }

        sb.append("]"); // Close JSON array

        // Write to file safely
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.print(sb.toString());

        } catch (IOException e) {

            System.err.println("Error writing " + filePath + ": " + e.getMessage());
        }
    }

    // Add one car (load → add → save)
    public void append(Car car) {
        ArrayList<Car> cars = load();
        cars.add(car);
        save(cars);
    }
}