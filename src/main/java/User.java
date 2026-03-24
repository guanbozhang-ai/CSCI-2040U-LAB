import java.util.ArrayList;

public class User {
    private static UserAttributeMap userAttributes;
    private static ArrayList<String> preferredMakes;
    private static ArrayList<String> preferredBodyTypes;

    /**
     * Creates a User from GUI-collected input.
     *
     * @param costValue            The user's ideal price
     * @param costImportance       Importance of price (0–5)
     * @param sportinessValue      The user's ideal horsepower
     * @param sportinessImportance Importance of horsepower (0–5)
     * @param mileageValue         The user's ideal mileage (km)
     * @param mileageImportance    Importance of mileage (0–5)
     * @param seatingValue         The user's ideal seat count
     * @param seatingImportance    Importance of seating (0–5)
     * @param preferredMakes       List of acceptable car makes
     * @param preferredBodyTypes   List of acceptable body types
     */
    public User(int costValue, double costImportance,
                int sportinessValue, double sportinessImportance,
                int mileageValue, double mileageImportance,
                int seatingValue, double seatingImportance,
                ArrayList<String> preferredMakes, ArrayList<String> preferredBodyTypes) {
        userAttributes = new UserAttributeMap();
        addAttribute("cost", AttributeFormulas.cost(costValue), costImportance);
        addAttribute("sportiness", AttributeFormulas.sportiness(sportinessValue), sportinessImportance);
        addAttribute("mileage", AttributeFormulas.mileage(mileageValue), mileageImportance);
        addAttribute("seating", AttributeFormulas.seating(seatingValue), seatingImportance);

        User.preferredMakes = preferredMakes;
        User.preferredBodyTypes = preferredBodyTypes;
    }

    public UserAttributeMap getUserAttributes() {
        return userAttributes;
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

    /**
     * Determines the Euclidean distance between the attribute vectors of the User
     * and a given Car, taking into account the User's preferred makes and body types.
     *
     * @param car The Car to be matched to the User
     * @return A double containing the Euclidean distance between the attribute vectors
     */
    public static double match(Car car) {
        java.util.HashMap<String, Double> carAttributes = car.getCarAttributes();
        double distance = 0;
        if (!preferredMakes.contains(car.getMake())) {
            distance += Math.pow(UserAttributeMap.ATTRIBUTE_MAX, 2);
        }
        if (!preferredBodyTypes.contains(car.getBodyType())) {
            distance += Math.pow(UserAttributeMap.ATTRIBUTE_MAX, 2);
        }
        for (String attribute : userAttributes.getAttributeNames()) {
            distance += Math.pow(
                    (userAttributes.getValue(attribute) - carAttributes.get(attribute)), 2)
                    * userAttributes.getImportance(attribute);
        }
        return Math.sqrt(distance);
    }
}
