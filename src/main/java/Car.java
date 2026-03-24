import java.util.HashMap;

public class Car {
    public final String[] SPECIFICATIONS = {"make", "model", "bodyType"};
    private HashMap<String, Double> carAttributes = new HashMap<>();
    private String make;
    private String model;
    private String bodyType;
    private int horsepower;
    private int price;
    private int mileage;
    private int seating;
    private String imageUrl;

    public Car(String make, String model, String bodyType, int horsepower,
               int price, int mileage, int seating, String imageUrl) {
        this.make = make;
        this.model = model;
        this.bodyType = bodyType;
        this.horsepower = horsepower;
        this.price = price;
        this.mileage = mileage;
        this.seating = seating;
        this.imageUrl = imageUrl;
        setCarAttributes();
    }

    // Backwards-compatible constructor without imageUrl
    public Car(String make, String model, String bodyType, int horsepower,
               int price, int mileage, int seating) {
        this(make, model, bodyType, horsepower, price, mileage, seating, null);
    }

    private void setCarAttributes() {
        setAttribute("cost",      AttributeFormulas.cost(price));
        setAttribute("sportiness",AttributeFormulas.sportiness(horsepower));
        setAttribute("mileage",   AttributeFormulas.mileage(mileage));
        setAttribute("seating",   AttributeFormulas.seating(seating));
    }

    public HashMap<String, Double> getCarAttributes() { return carAttributes; }
    public String getMake()      { return make; }
    public String getModel()     { return model; }
    public String getBodyType()  { return bodyType; }
    public int getHorsepower()   { return horsepower; }
    public int getPrice()        { return price; }
    public int getMileage()      { return mileage; }
    public int getSeating()      { return seating; }
    public String getImageUrl()  { return imageUrl; }

    public void setMake(String make)          { this.make = make; }
    public void setModel(String model)        { this.model = model; }
    public void setBodyType(String bodyType)  { this.bodyType = bodyType; }
    public void setImageUrl(String imageUrl)  { this.imageUrl = imageUrl; }

    public void setAttribute(String name, Double value) {
        carAttributes.putIfAbsent(name, value);
    }

    public String toString() {
        return make + " " + model + ", body type: " + bodyType +
                ", $" + price + ", " + horsepower + " HP, " + mileage + " km";
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"make\":\"").append(make).append("\",");
        sb.append("\"model\":\"").append(model).append("\",");
        sb.append("\"bodyType\":\"").append(bodyType).append("\",");
        sb.append("\"horsepower\":").append(horsepower).append(",");
        sb.append("\"price\":").append(price).append(",");
        sb.append("\"mileage\":").append(mileage).append(",");
        sb.append("\"seating\":").append(seating).append(",");
        sb.append("\"imageUrl\":\"").append(imageUrl != null ? imageUrl : "").append("\"");
        sb.append("}");
        return sb.toString();
    }

    public static Car fromJson(String json) {
        json = json.trim();
        String make     = extractString(json, "make");
        String model    = extractString(json, "model");
        String bodyType = extractString(json, "bodyType");
        int horsepower  = extractInt(json, "horsepower");
        int price       = extractInt(json, "price");
        int mileage     = extractInt(json, "mileage");
        int seating     = extractInt(json, "seating");
        String imageUrl = extractStringOptional(json, "imageUrl");
        return new Car(make, model, bodyType, horsepower, price, mileage, seating, imageUrl);
    }

    private static String extractString(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search) + search.length();
        int end   = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private static String extractStringOptional(String json, String key) {
        String search = "\"" + key + "\":\"";
        int idx = json.indexOf(search);
        if (idx == -1) return null;
        int start = idx + search.length();
        int end   = json.indexOf("\"", start);
        String val = json.substring(start, end).trim();
        return val.isEmpty() ? null : val;
    }

    private static int extractInt(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search) + search.length();
        int end   = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Integer.parseInt(json.substring(start, end).trim());
    }
}
