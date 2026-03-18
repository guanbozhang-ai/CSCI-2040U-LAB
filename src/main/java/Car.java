import java.util.HashMap;

public class Car {
    //TODO: make a list (initialize on runtime) of all possible makes
    //TODO: make a list (initialize on runtime) of all possible bodyTypes in Car class

    public final String[] SPECIFICATIONS = {"make", "model", "bodyType"};
    private HashMap<String, Double> carAttributes = new HashMap<>();
    private String make;
    private String model;
    private String bodyType;
    private int horsepower;
    private int price;
    private int mileage;
    private int seating;

    /**
     * Initializes a full Car object
     * @param make String, the make of the car
     * @param model String, the model of the car
     * @param bodyType String, the body type of the car
     * @param horsepower int, the horsepower of the car
     * @param price int, the price of the car
     * @param mileage int, the amount of km the car has been driven
     * @param seating int, the amount of seats the car has
     */
    public Car(String make, String model, String bodyType, int horsepower,
               int price, int mileage, int seating) {
        this.make = make;
        this.model = model;
        this.bodyType = bodyType;
        this.horsepower = horsepower;
        this.price = price;
        this.mileage = mileage;
        this.seating = seating;
        setCarAttributes();
    }

    /**
     * Initializes the attribute vector of the car. Runs on construction
     */
    private void setCarAttributes() {
        setAttribute("cost", AttributeFormulas.cost(price));
        setAttribute("sportiness", AttributeFormulas.sportiness(horsepower));
        setAttribute("mileage", AttributeFormulas.mileage(mileage));
        setAttribute("seating", AttributeFormulas.seating(seating));
    }


    public HashMap<String, Double> getCarAttributes() {
        return carAttributes;
    }


    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getBodyType() {
        return bodyType;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public int getPrice() {
        return price;
    }

    public int getMileage() {
        return mileage;
    }

    public int getSeating() {
        return seating;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public void setAttribute(String name, Double value) {
        carAttributes.putIfAbsent(name, value);
    }

    public String toString() {
        return make + " " + model + ", body type: " + bodyType +
                ", $" + price + ", " + horsepower + " HP, " + mileage + " km";
    }

    /**
     * Converts a Car object to a String in .json format
     * @return A String representing the Car
     */
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"make\":\"").append(make).append("\",");
        sb.append("\"model\":\"").append(model).append("\",");
        sb.append("\"bodyType\":\"").append(bodyType).append("\",");
        sb.append("\"horsepower\":").append(horsepower).append(",");
        sb.append("\"price\":").append(price).append(",");
        sb.append("\"mileage\":").append(mileage).append(",");
        sb.append("\"seating\":").append(seating);
        sb.append("}");
        return sb.toString(); // Return JSON string
    }

    /**
     * Converts a given String in .json format to a Car object
     * @param json A String in .json format representing a Car
     * @return The represented Car object
     */
    public static Car fromJson(String json) {
        json = json.trim(); // Remove extra spaces

        String make  = extractString(json, "make");
        String model = extractString(json, "model");
        String bodyType = extractString(json, "bodyType");
        int horsepower  = extractInt(json, "horsepower");
        int price  = extractInt(json, "price");
        int mileage  = extractInt(json, "mileage");
        int seating  = extractInt(json, "seating");

        return new Car(make, model, bodyType, horsepower, price, mileage, seating); // Create new Car object
    }

    /**
     * Extracts the value associated with a key in a given .json format String
     * @param json String containing the .json information
     * @param key The String key
     * @return The String value associated with the given key
     */
    private static String extractString(String json, String key) {

        // Find "key": then skip to opening quote (handles optional space after colon)
        String search = "\"" + key + "\":";

        int colonEnd = json.indexOf(search) + search.length(); // Position after the colon
        int start = json.indexOf("\"", colonEnd) + 1;          // Skip to char after opening quote
        int end = json.indexOf("\"", start);                    // End at closing quote

        return json.substring(start, end).trim();
    }

    private static int extractInt(String json, String key) {
        // Create pattern like: "make":"
        String search = "\"" + key + "\":";

        int start = json.indexOf(search) + search.length(); // Start position

        int end = json.indexOf(",", start); // End at comma separator
        if(end == -1) {
            end = json.indexOf("}", start); // End at the end of json if no more elements
        }

        return Integer.parseInt(json.substring(start, end).trim()); // Return extracted text
    }

}