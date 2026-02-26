import java.util.*;


public class User {
    //TODO: rework attribute vector to be HashMap<String,Double>, where each string is an attribute and each double is its value
    private static UserAttributeMap userAttributes;
    private static ArrayList<String> preferredMakes;
    private static ArrayList<String> preferredBodyTypes;

    public User(UserAttributeMap userAttributes, ArrayList<String> preferredMakes,
                ArrayList<String> preferredBodyTypes) {
        this.userAttributes = userAttributes;
        this.preferredMakes = preferredMakes;
        this.preferredBodyTypes = preferredBodyTypes;
    }

    public User() {
        userAttributes = new UserAttributeMap();
        preferredMakes = new ArrayList<>();
        preferredBodyTypes = new ArrayList<>();
    }

    public void addAttribute(String name, Double value, Double importance) {
        userAttributes.addAttribute(name, value, importance);
    }

    public void addPreferredMake(String make) {
        preferredMakes.add(make);
    }

    public void addPreferredBodyType(String bodyType) {
        preferredBodyTypes.add(bodyType);
    }

    public static double match(Car car) {
        HashMap<String, Double> carAttributes = car.getCarAttributes();
        double distance = 0;
        if (!preferredMakes.contains(car.getMake())){
            distance += Math.pow(UserAttributeMap.ATTRIBUTE_MAX, 2);
        }
        if (!preferredBodyTypes.contains(car.getBodyType())) {
            distance += Math.pow(UserAttributeMap.ATTRIBUTE_MAX, 2);
        }
        for(String attribute : userAttributes.getAttributeNames()) {
            distance += Math.pow( (userAttributes.getValue(attribute) - carAttributes.get(attribute)) , 2)
                    * userAttributes.getImportance(attribute);
        }
        return Math.sqrt(distance);
    }

}