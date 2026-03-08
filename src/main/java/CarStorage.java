import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Handles saving and loading cars from a JSON file.
 * Supports both resource files (classpath:) and file system paths.
 *
 * File format (cars.json):
 * [
 *   {"make":"Toyota","model":"Corolla","attributes":[1.0,2.4,3.2]},
 *   {"make":"Honda","model":"Civic","attributes":[2.0,0.4,2.3]}
 * ]
 */
public class CarStorage {

    private final String filePath; // Path to cars.json file

    /**
     * Constructor: initializes storage with file path.
     * Supports two path types:
     * - "classpath:cars.json" - loads from resources folder
     * - "cars.json" or any path - loads from file system
     */
    public CarStorage(String filePath) {
        this.filePath = filePath; // Save file path
    }

    // Load all cars from the JSON file
    public ArrayList<Car> load() {

        ArrayList<Car> cars = new ArrayList<>();

        try {
            String content;

            // Check if path is a resource (classpath:)
            if (filePath.startsWith("classpath:")) {
                content = loadFromResource();
            } else {
                content = loadFromFile();
            }

            if (content == null || content.isEmpty()) {
                return cars;
            }

            // Parse JSON content
            cars = parseJsonContent(content);

        } catch (IOException e) { // If file read fails
            System.err.println("Error reading " + filePath + ": " + e.getMessage());
        }

        return cars; // Return loaded cars
    }

    /**
     * Load content from resources folder using ClassLoader
     */
    private String loadFromResource() throws IOException {
        String resourceName = filePath.substring("classpath:".length());

        try (InputStream is = ClassLoader.getSystemResourceAsStream(resourceName)) {
            if (is == null) {
                return null; // Resource not found
            }
            return new String(is.readAllBytes()).trim();
        }
    }

    /**
     * Load content from file system
     */
    private String loadFromFile() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        return new String(Files.readAllBytes(file.toPath())).trim();
    }

    /**
     * Parse JSON content and extract Car objects
     */
    private ArrayList<Car> parseJsonContent(String content) {
        ArrayList<Car> cars = new ArrayList<>();

        // Remove outer [ and ]
        content = content.substring(1, content.length() - 1).trim();
        if (content.isEmpty()) {
            return cars;
        }

        // Find each { ... } JSON block manually
        int depth = 0;  // Track nesting level
        int start = 0;  // Track start of object

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i); // Current character

            if (c == '{') { // If object starts
                if (depth == 0) start = i; // Mark start
                depth++; // Increase depth
            } else if (c == '}') { // If object ends
                depth--; // Decrease depth

                if (depth == 0) { // Full object found
                    String block = content.substring(start, i + 1); // Extract JSON
                    cars.add(Car.fromJson(block)); // Convert to Car and add
                }
            }
        }

        return cars;
    }

    // Save list of cars to file (overwrite old content)
    public void save(ArrayList<Car> cars) {

        StringBuilder sb = new StringBuilder(); // Build JSON text

        sb.append("[\n"); // Start JSON array

        for (int i = 0; i < cars.size(); i++) {

            sb.append("  ").append(cars.get(i).toJson()); // Add car JSON

            if (i < cars.size() - 1) sb.append(","); // Add comma if not last

            sb.append("\n"); // New line
        }

        sb.append("]"); // Close JSON array

        // Extract filename for saving
        String saveFilePath = filePath;
        if (filePath.startsWith("classpath:")) {
            saveFilePath = filePath.substring("classpath:".length());
        }

        // Write to file safely
        try (PrintWriter pw = new PrintWriter(new FileWriter(saveFilePath))) {

            pw.print(sb.toString());
            System.out.println("Cars saved to " + saveFilePath);

        } catch (IOException e) {

            System.err.println("Error writing " + saveFilePath + ": " + e.getMessage());
        }
    }

    // Add one car (load → add → save)
    public void append(Car car) {
        ArrayList<Car> cars = load();
        cars.add(car);
        save(cars);
    }
}