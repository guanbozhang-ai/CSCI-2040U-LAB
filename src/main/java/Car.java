import java.util.HashMap;
import java.util.Map;

public class Car {

    private HashMap<String, Double> carAttributes;
    private String make;
    private String model;
    private String bodyType;

    // Default constructor
    public Car() {
        this.carAttributes = new HashMap<>();
        this.make = null;
        this.model = null;
        this.bodyType = null;
    }

    // Constructor with values
    public Car(HashMap<String, Double> carAttributes, String make, String model, String bodyType) {
        this.carAttributes = carAttributes;
        this.make = make;
        this.model = model;
        this.bodyType = bodyType;
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

    @Override
    public String toString() {
        return make + " " + model;
    }

    /**
     * Serialize to JSON with structure:
     * {"make":"Toyota","model":"Corolla","bodyType":"Sedan",
     *  "attributes":{"cost":2.0,"sportiness":1.5}}
     */
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"make\":\"").append(make).append("\",");
        sb.append("\"model\":\"").append(model).append("\",");
        sb.append("\"bodyType\":\"").append(bodyType).append("\",");
        sb.append("\"attributes\":{");

        int i = 0;
        for (Map.Entry<String, Double> entry : carAttributes.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue());
            if (++i < carAttributes.size()) {
                sb.append(",");
            }
        }
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

    public static Car fromJson(String json) {
        json = json.trim();

        String make = extractString(json, "make");
        String model = extractString(json, "model");
        String bodyType = extractString(json, "bodyType");

        // extract attributes object
        int attrStart = json.indexOf("\"attributes\":{") + "\"attributes\":{".length();
        int attrEnd = json.indexOf("}", attrStart);
        String attrsText = json.substring(attrStart, attrEnd).trim();

        HashMap<String, Double> attributes = new HashMap<>();
        if (!attrsText.isEmpty()) {
            String[] pairs = attrsText.split(",");
            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length == 2) {
                    String key = kv[0].replaceAll("\"", "").trim();
                    Double val = Double.parseDouble(kv[1].trim());
                    attributes.put(key, val);
                }
            }
        }

        return new Car(attributes, make, model, bodyType);
    }

    private static String extractString(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search) + search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}