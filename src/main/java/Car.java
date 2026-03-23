import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Car {
    public static final ArrayList<String> ENGINE_CONFIGURATIONS =
            new ArrayList<>(Arrays.asList("V", "Inline", "W", "Flat", "Boxer", "Radial", "Rotary"));
    public static final ArrayList<String> DRIVETRAIN_CONFIGURATIONS =
            new ArrayList<>(Arrays.asList("FF", "MF", "RF", "FR", "MR", "RR", "F4", "M4", "R4", "24"));
    public static final ArrayList<String> FUEL_TYPES =
            new ArrayList<>(Arrays.asList("Gas", "Diesel", "Hybrid", "Electric"));
    public static final ArrayList<String> TRANSMISSIONS =
            new ArrayList<>(Arrays.asList("Manual", "Automatic", "CVT", "Electric"));

    private HashMap<String, Double> carAttributes = new HashMap<>();

    private String make;
    private String model;
    private String bodyType;
    private String engineConfiguration;
    private String transmission;
    private String trim;
    private String drivetrainConfiguration;
    private String extColour;
    private String intColour;
    private String fuelType;
    private String imageURL;

    private int year;
    private int price;
    private int horsepower;
    private int cylinders;
    private int gears;
    private int mileage;
    private int seating;

    private double fuelEconomy;

    /**
     * Initializes a full Car object
     *
     * @param year int, year of production
     * @param make String, the make of the car
     * @param model String, the model of the car
     * @param price int, the price of the car
     * @param bodyType String, the body type of the car
     * @param trim String, the trim level
     * @param extColour String, exterior colour
     * @param intColour String, interior colour
     * @param fuelType String, the type of fuel the car uses
     * @param horsepower int, the horsepower of the car
     * @param mileage int, the amount of km the car has been driven
     * @param fuelEconomy double, the fuel economy in L/100km
     * @param engineConfiguration String, the layout of the engine (ex. Inline, Boxer, V)
     * @param drivetrainConfiguration String, the layout of the drivetrain (ex. FF, FR)
     * @param seating int, the amount of seats the car has
     * @param cylinders int, the number of cylinders in the engine
     * @param gears int, the number of gears in the transmission
     * @param transmission String, whether the transmission is manual, automatic, a CVT, or electric
     * @param imageURL String, a link to the cover image of the car
     */
    public Car(int year, String make, String model, int price, String bodyType, String trim,
               String extColour, String intColour, String fuelType, int horsepower, int mileage,
               double fuelEconomy, String engineConfiguration, String drivetrainConfiguration, int seating,
               int cylinders, int gears, String transmission, String imageURL) throws Exception {

        if(!ENGINE_CONFIGURATIONS.contains(engineConfiguration)) {
            throw(new Exception("Invalid engine configuration."));
        }
        if(!DRIVETRAIN_CONFIGURATIONS.contains(drivetrainConfiguration)) {
            throw(new Exception("Invalid drivetrain configuration."));
        }
        if(!FUEL_TYPES.contains(fuelType)) {
            throw(new Exception("Invalid fuel type."));
        }
        if(!TRANSMISSIONS.contains(transmission)) {
            throw(new Exception("Invalid transmission type."));
        }



        this.year = year;
        this.make = make;
        this.model = model;
        this.price = price;
        this.bodyType = bodyType;

        this.trim = trim;
        this.extColour = extColour;
        this.intColour = intColour;
        this.fuelType = fuelType;
        this.horsepower = horsepower;

        this.mileage = mileage;
        this.fuelEconomy = fuelEconomy;
        this.engineConfiguration = engineConfiguration;
        this.drivetrainConfiguration = drivetrainConfiguration;
        this.seating = seating;

        this.cylinders = cylinders;
        this.gears = gears;
        this.transmission = transmission;
        this.imageURL = imageURL;

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
        setAttribute("economy", AttributeFormulas.economy(fuelEconomy));
        setAttribute("recency", AttributeFormulas.recency(year));
    }


    public HashMap<String, Double> getCarAttributes() {
        return carAttributes;
    }

    public int getYear() { return year; }

    public String getMake() { return make; }

    public String getModel() { return model; }

    public int getPrice() { return price; }

    public String getBodyType() { return bodyType; }

    public String getTrim() { return trim; }

    public String getExtColour() { return extColour; }

    public String getIntColour() { return intColour; }

    public String getFuelType() { return fuelType; }

    public int getHorsepower() { return horsepower; }

    public int getMileage() { return mileage; }

    public double getFuelEconomy() { return fuelEconomy; }

    public String getEngineConfiguration() { return engineConfiguration; }

    public String getDrivetrainConfiguration() { return drivetrainConfiguration; }

    public int getSeating() { return seating; }

    public int getCylinders() { return cylinders; }

    public int getGears() { return gears; }

    public String getTransmission() { return transmission; }

    public String getImageURL() { return imageURL; }

    public void setAttribute(String name, Double value) {
        carAttributes.putIfAbsent(name, value);
    }

    public String toString() {
        return year + " " + make + " " + model;
    }

    /**
     * Converts a Car object to a String in .json format
     * @return A String representing the Car
     */
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"year\":").append(year).append(",");
        sb.append("\"make\":\"").append(make).append("\",");
        sb.append("\"model\":\"").append(model).append("\",");
        sb.append("\"price\":").append(price).append(",");
        sb.append("\"bodyType\":\"").append(bodyType).append("\",");

        sb.append("\"trim\":\"").append(trim).append("\",");
        sb.append("\"extColour\":\"").append(extColour).append("\",");
        sb.append("\"intColour\":\"").append(intColour).append("\",");
        sb.append("\"fuelType\":\"").append(fuelType).append("\",");
        sb.append("\"horsepower\":").append(horsepower).append(",");

        sb.append("\"mileage\":").append(mileage).append(",");
        sb.append("\"fuelEconomy\":").append(fuelEconomy).append(",");
        sb.append("\"engineConfiguration\":\"").append(engineConfiguration).append("\",");
        sb.append("\"drivetrainConfiguration\":\"").append(drivetrainConfiguration).append("\",");
        sb.append("\"seating\":").append(seating).append(",");

        sb.append("\"cylinders\":").append(cylinders).append(",");
        sb.append("\"gears\":").append(gears).append(",");
        sb.append("\"transmission\":\"").append(transmission).append("\",");
        sb.append("\"imageURL\":\"").append(imageURL);
        sb.append("\"}");
        return sb.toString(); // Return JSON string
    }

    /**
     * Converts a given String in .json format to a Car object
     * @param json A String in .json format representing a Car
     * @return The represented Car object
     */
    public static Car fromJson(String json) throws Exception {
        json = json.trim(); // Remove extra spaces

        int year = extractInt(json, "year");
        String make  = extractString(json, "make");
        String model = extractString(json, "model");
        int price  = extractInt(json, "price");
        String bodyType = extractString(json, "bodyType");

        String trim  = extractString(json, "trim");
        String extColour  = extractString(json, "extColour");
        String intColour  = extractString(json, "intColour");
        String fuelType  = extractString(json, "fuelType");
        int horsepower  = extractInt(json, "horsepower");

        int mileage  = extractInt(json, "mileage");
        double fuelEconomy  = extractDouble(json, "fuelEconomy");
        String engineConfiguration  = extractString(json, "engineConfiguration");
        String drivetrainConfiguration  = extractString(json, "drivetrainConfiguration");
        int seating  = extractInt(json, "seating");

        int cylinders  = extractInt(json, "cylinders");
        int gears  = extractInt(json, "gears");
        String transmission  = extractString(json, "transmission");
        String imageURL  = extractString(json, "imageURL");

        return new Car(year, make, model, price, bodyType,
                trim, extColour, intColour, fuelType, horsepower,
                mileage, fuelEconomy, engineConfiguration, drivetrainConfiguration, seating,
                cylinders, gears, transmission, imageURL); // Create new Car object
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

    private static double extractDouble(String json, String key) {
        // Create pattern like: "make":"
        String search = "\"" + key + "\":";

        int start = json.indexOf(search) + search.length(); // Start position

        int end = json.indexOf(",", start); // End at comma separator
        if(end == -1) {
            end = json.indexOf("}", start); // End at the end of json if no more elements
        }

        return Double.parseDouble(json.substring(start, end).trim()); // Return extracted text
    }

}