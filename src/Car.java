import java.util.ArrayList;

public class Car {

    private ArrayList<Double> carAttributes;
    private String make;
    private String model;

    // Default constructor
    public Car() {
        this.carAttributes = new ArrayList<>();
        this.make = null;
        this.model = null;
    }

    // Constructor with values
    public Car(ArrayList<Double> carAttributes, String make, String model) {
        this.carAttributes = carAttributes;
        this.make = make;
        this.model = model;
    }

    public ArrayList<Double> getCarAttributes() {return carAttributes;}
    public String getMake()  { return make; }
    public String getModel() { return model; }


    // Convert object to readable string
    @Override
    public String toString() {
        return make + " " + model; // Example: Toyota Corolla
    }

    // Convert object to JSON text (Serialization)
    // Example: {"make":"Toyota","model":"Corolla","attributes":[1.0,2.4,3.2]}

    public String toJson() {

        StringBuilder sb = new StringBuilder();

        sb.append("{");

        sb.append("\"make\":\"").append(make).append("\","); // Add make field

        sb.append("\"model\":\"").append(model).append("\","); // Add model field

        sb.append("\"attributes\":["); // Start attributes array

        // Loop through attributes
        for (int i = 0; i < carAttributes.size(); i++) {

            sb.append(carAttributes.get(i)); // Add number
            // Add comma if not last element
            if (i < carAttributes.size() - 1)
                sb.append(",");
        }

        sb.append("]}");

        return sb.toString(); // Return JSON string
    }

    // Convert JSON text back into Car object (Deserialization)
    public static Car fromJson(String json) {

        json = json.trim(); // Remove extra spaces

        String make  = extractString(json, "make");
        String model = extractString(json, "model");

        // Find position of attributes array
        int arrStart = json.indexOf('[') + 1;
        int arrEnd   = json.indexOf(']');

        // Split numbers by comma
        String[] parts = json.substring(arrStart, arrEnd).split(",");

        ArrayList<Double> attributes = new ArrayList<>(); // New list

        // Convert each text number to Double
        for (String part : parts) {
            part = part.trim(); // Remove spaces

            if (!part.isEmpty())
                attributes.add(Double.parseDouble(part)); // Convert to double
        }

        return new Car(attributes, make, model); // Create new Car object
    }

    // Helper method extract value of a key from JSON
    private static String extractString(String json, String key) {

        // Create pattern like: "make":"
        String search = "\"" + key + "\":\"";

        int start = json.indexOf(search) + search.length(); // Start position

        int end = json.indexOf("\"", start); // End at next quote

        return json.substring(start, end); // Return extracted text
    }
}