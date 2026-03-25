import org.w3c.dom.Attr;

import java.awt.color.ICC_ColorSpace;
import java.sql.SQLOutput;
import java.util.*;


public class User {
    private static UserAttributeMap userAttributes;
    private static ArrayList<String> preferredMakes;
    private static ArrayList<String> preferredBodyTypes;
    public static String preferredFuelType = null;
    public static String preferredTransmission = null;

    /**
     * Initializes a new User object using the survey methods in the User class
     */
    public User() {
        userSurvey();
        preferredMakesSurvey();
        preferredBodyTypesSurvey();
        preferredFuelTypeSurvey();
        preferredTransmissionSurvey();
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
     * @param economyValue
     * @param economyImportance
     * @param recencyValue
     * @param recencyImportance
     * @param preferredMakes
     * @param preferredBodyTypes
     * @param preferredFuelType
     */
    public User(int costValue, double costImportance,
                int sportinessValue, double sportinessImportance,
                int mileageValue, double mileageImportance,
                int seatingValue, double seatingImportance,
                double economyValue, double economyImportance,
                int recencyValue, double recencyImportance,
                ArrayList<String> preferredMakes, ArrayList<String> preferredBodyTypes,
                String preferredFuelType, String preferredTransmission) {
        userAttributes = new UserAttributeMap();
        addAttribute("cost", AttributeFormulas.cost(costValue), costImportance);
        addAttribute("sportiness", AttributeFormulas.sportiness(sportinessValue), sportinessImportance);
        addAttribute("mileage", AttributeFormulas.mileage(mileageValue), mileageImportance);
        addAttribute("seating", AttributeFormulas.seating(seatingValue),seatingImportance);
        addAttribute("economy", AttributeFormulas.economy(economyValue), economyImportance);
        addAttribute("recency", AttributeFormulas.recency(recencyValue), recencyImportance);

        this.preferredMakes = preferredMakes;
        this.preferredBodyTypes = preferredBodyTypes;
        if(Car.FUEL_TYPES.contains(preferredFuelType)) {
            this.preferredFuelType = preferredFuelType;
        }
        if(Car.TRANSMISSIONS.contains(preferredTransmission)) {
            this.preferredTransmission = preferredTransmission;
        }
    }

    /**
     * Returns a new User object made from a String in json format passed from a .js script
     *
     * @param json The json representing the User
     * @return The constructed User
     */
    public static User fromJson(String json) {
        int costValue = JSONHelper.extractInt(json, "costValue");
        double costImportance = JSONHelper.extractDouble(json, "costImportance");

        int sportinessValue = JSONHelper.extractInt(json, "sportinessValue");
        double sportinessImportance = JSONHelper.extractDouble(json, "sportinessImportance");

        int mileageValue = JSONHelper.extractInt(json, "mileageValue");
        double mileageImportance = JSONHelper.extractDouble(json, "mileageImportance");

        int seatingValue = JSONHelper.extractInt(json, "seatingValue");
        double seatingImportance = JSONHelper.extractDouble(json, "seatingImportance");

        double economyValue = JSONHelper.extractDouble(json, "economyValue");
        double economyImportance = JSONHelper.extractDouble(json, "economyImportance");

        int recencyValue = JSONHelper.extractInt(json, "recencyValue");
        double recencyImportance = JSONHelper.extractDouble(json, "recencyImportance");

        ArrayList<String> preferredMakes = JSONHelper.extractStringList(json, "preferredMakes");
        ArrayList<String> preferredBodyTypes = JSONHelper.extractStringList(json, "preferredBodyTypes");

        String preferredFuelType = JSONHelper.extractString(json, "preferredFuelType");
        String preferredTransmission = JSONHelper.extractString(json, "preferredTransmission");

        return new User(costValue, costImportance, sportinessValue, sportinessImportance,
                mileageValue, mileageImportance, seatingValue, seatingImportance,
                economyValue, economyImportance, recencyValue, recencyImportance,
                preferredMakes, preferredBodyTypes, preferredFuelType, preferredTransmission);
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
     * and a given Car, taking into account the User's preferred makes, body types, and fuel type
     * @param car The Car to be matched to the User
     * @return A double containing the Euclidean distance between the attribute vectors
     */
    public static double match(Car car) {
        //TODO: add importance value for preferred makes, body types, and fuel type
        //Below are placeholders for the importance value, just as a proof of concept
        double makeImportance = 1;
        double bodyTypeImportance = 1;
        double fuelTypeImportance = 1;
        double transmissionImportance = 1;

        HashMap<String, Double> carAttributes = car.getCarAttributes();
        double distance = 0;
        if ( (!preferredMakes.isEmpty()) && (!preferredMakes.contains(car.getMake())) ){
            distance += makeImportance;
        }
        if ( (!preferredBodyTypes.isEmpty()) && (!preferredBodyTypes.contains(car.getBodyType())) ) {
            distance += bodyTypeImportance;
        }
        if ( (preferredFuelType != null) && (!Objects.equals(preferredFuelType, car.getFuelType())) ) {
            distance += fuelTypeImportance;
        }
        if ( (preferredTransmission != null) && (!Objects.equals(preferredTransmission, car.getTransmission())) ) {
            distance += transmissionImportance;
        }

        for(String attribute : userAttributes.getAttributeNames()) {
            if (Objects.equals(attribute, "sportiness") || Objects.equals(attribute, "seating")) {
                distance += Math.pow((userAttributes.getValue(attribute) - carAttributes.get(attribute)), 2)
                        * userAttributes.getImportance(attribute);
            }
            else {
                if (carAttributes.get(attribute) > userAttributes.getValue(attribute)) {
                    distance += Math.pow((userAttributes.getValue(attribute) - carAttributes.get(attribute)), 2)
                            * userAttributes.getImportance(attribute);
                }
            }
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
        System.out.println("On a scale of 0-5, how important is it that your car be at or below this price?");
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
        System.out.println("On a scale of 0-5, how important is it that your car has this mileage or less?");
        currentImportance = scanner.nextDouble();
        userAttributes.addAttribute("mileage", currentValue, currentImportance);

        System.out.println("How many seats would you like your car to have?");
        currentInput = scanner.nextInt();
        currentValue = AttributeFormulas.seating(currentInput);
        System.out.println("On a scale of 0-5, how important is it that your car has this many seats?");
        currentImportance = scanner.nextDouble();
        userAttributes.addAttribute("seating", currentValue, currentImportance);

        System.out.println("What fuel economy, in L/100km, do you want your car to have?");
        double idealEconomy = scanner.nextDouble();
        currentValue = AttributeFormulas.economy(idealEconomy);
        System.out.println("On a scale of 0-5, how important is it that your car has a fuel economy this good?");
        currentImportance = scanner.nextDouble();
        userAttributes.addAttribute("economy", currentValue, currentImportance);

        System.out.println("What year would you like your car to be from?");
        currentInput = scanner.nextInt();
        currentValue = AttributeFormulas.recency(currentInput);
        System.out.println("On a scale of 0-5, how important is it that your car is from this year or later?");
        currentImportance = scanner.nextDouble();
        userAttributes.addAttribute("recency", currentValue, currentImportance);

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

    public void preferredFuelTypeSurvey() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you have a preferred fuel type? (0 to exit, anything else to continue)");
        String option = scanner.next();
        while(!Objects.equals(option, "0")) {
            System.out.println("Input preferred fuel type.");
            System.out.println("Options: " + Car.FUEL_TYPES);
            preferredFuelType = scanner.next();
            if(Car.FUEL_TYPES.contains(preferredFuelType)) {
                option = "0";
            }
            else {
                System.out.println("Invalid selection.");
            }
        }
    }

    public void preferredTransmissionSurvey() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you have a preferred transmission? (0 to exit, anything else to continue)");
        String option = scanner.next();
        while(!Objects.equals(option, "0")) {
            System.out.println("Input preferred transmission.");
            System.out.println("Options: " + Car.TRANSMISSIONS);
            preferredTransmission = scanner.next();
            if(Car.TRANSMISSIONS.contains(preferredTransmission)) {
                option = "0";
            }
            else {
                System.out.println("Invalid selection.");
            }
        }
    }
}