import java.util.*;


public class User {
    private static UserAttributeMap userAttributes;
    private static ArrayList<String> preferredMakes;
    private static ArrayList<String> preferredBodyTypes;

    /**
     * Initializes a new User object using the survey methods in the User class
     */
    public User() {
        userSurvey();
        preferredMakesSurvey();
        preferredBodyTypesSurvey();
    }

    /**
     * Manually create a User object given all required values.
     *
     * @param costValue
     * @param costImportance
     * @param sportinessValue
     * @param sportinessImportance
     * @param mileageValue
     * @param mileageImportance
     * @param seatingValue
     * @param seatingImportance
     * @param preferredMakes
     * @param preferredBodyTypes
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
        addAttribute("seating", AttributeFormulas.seating(seatingValue),seatingImportance);

        this.preferredMakes = preferredMakes;
        this.preferredBodyTypes = preferredBodyTypes;
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
     * and a given Car, taking into account the User's preferred makes and body types
     * @param car The Car to be matched to the User
     * @return A double containing the Euclidean distance between the attribute vectors
     */
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

    /**
     * Initializes the User's UserAttributeMap (attribute vector) using a placeholder survey
     */
    public void userSurvey() {
        userAttributes = new UserAttributeMap();
        int currentInput;
        double currentValue;
        double currentImportance;
        Scanner scanner = new Scanner(System.in);

        System.out.println("What is your ideal price point for your car?");
        currentInput = scanner.nextInt();
        currentValue = AttributeFormulas.cost(currentInput);
        System.out.println("On a scale of 0-5, how important is it that your car be near this price?");
        currentImportance = scanner.nextDouble();
        userAttributes.addAttribute("cost", currentValue, currentImportance);

        System.out.println("How much horsepower do you want your car to have?");
        currentInput = scanner.nextInt();
        currentValue = AttributeFormulas.sportiness(currentInput);
        System.out.println("On a scale of 0-5, how important is it that your car has about this much power?");
        currentImportance = scanner.nextDouble();
        userAttributes.addAttribute("sportiness", currentValue, currentImportance);

        System.out.println("How many kilometres driven do you want your car to have?");
        currentInput = scanner.nextInt();
        currentValue = AttributeFormulas.mileage(currentInput);
        System.out.println("On a scale of 0-5, how important is it that your car has about this mileage?");
        currentImportance = scanner.nextDouble();
        userAttributes.addAttribute("mileage", currentValue, currentImportance);

        System.out.println("How many seats would you like your car to have?");
        currentInput = scanner.nextInt();
        currentValue = AttributeFormulas.seating(currentInput);
        System.out.println("On a scale of 0-5, how important is it that your car has this many seats?");
        currentImportance = scanner.nextDouble();
        userAttributes.addAttribute("seating", currentValue, currentImportance);
    }

    /**
     * Allows a user to input their preferred makes
     */
    public void preferredMakesSurvey() {
        preferredMakes = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String preferredMake = null;

        while(!Objects.equals(preferredMake, "0")) {
            System.out.println("Input one of your preferred makes (0 to exit):");
            preferredMake = scanner.nextLine();
            if (!Objects.equals(preferredMake, "0")) {
                preferredMakes.add(preferredMake);
            }
        }
    }

    /**
     * Allows a user to input their preferred body types
     */
    public void preferredBodyTypesSurvey() {
        preferredBodyTypes = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String preferredBodyType = null;

        while(!Objects.equals(preferredBodyType, "0")) {
            System.out.println("Input one of your preferred body types (0 to exit):");
            preferredBodyType = scanner.nextLine();
            if (!Objects.equals(preferredBodyType, "0")) {
                preferredMakes.add(preferredBodyType);
            }
        }
    }
}