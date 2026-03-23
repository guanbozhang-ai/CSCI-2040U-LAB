import java.util.HashMap;
import java.util.Set;

public class UserAttributeMap {
    public static final double ATTRIBUTE_MAX = 5;
    //TODO: add fuel economy
    public static final String[] ATTRIBUTE_NAMES = {"cost", "sportiness", "mileage", "seating", "economy", "recency"};
    private HashMap<String, HashMap<String, Double>> attributes;

    UserAttributeMap(HashMap<String, HashMap<String, Double>> attributes) {
        this.attributes = attributes;
    }

    UserAttributeMap() {
        attributes = new HashMap<>();
    }

    public void addAttribute(String name, Double value, Double importance) {
        HashMap<String, Double> attribute = new HashMap<>();
        attribute.put("value", value);
        attribute.put("importance", importance);
        attributes.putIfAbsent(name, attribute);
    }

    public Set<String> getAttributeNames () {
        return attributes.keySet();
    }

    public Double getValue(String attribute) {
        return attributes.get(attribute).get("value");
    }

    public Double getImportance(String attribute) {
        return attributes.get(attribute).get("importance");
    }
}
