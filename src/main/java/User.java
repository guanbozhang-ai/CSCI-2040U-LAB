import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class User {
    private UserAttributeMap userAttributes;
    private ArrayList<String> preferredMakes;
    private ArrayList<String> preferredBodyTypes;

    // full constructor
    public User(UserAttributeMap userAttributes, ArrayList<String> preferredMakes,
                ArrayList<String> preferredBodyTypes) {
        this.userAttributes = userAttributes;
        this.preferredMakes = preferredMakes;
        this.preferredBodyTypes = preferredBodyTypes;
    }

    // no-arg constructor
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

    public double match(Car car) {
        HashMap<String, Double> carAttributes = car.getCarAttributes();
        double distance = 0;
        if (!preferredMakes.isEmpty() && !preferredMakes.contains(car.getMake())) {
            distance += Math.pow(UserAttributeMap.ATTRIBUTE_MAX, 2);
        }
        if (!preferredBodyTypes.isEmpty() && !preferredBodyTypes.contains(car.getBodyType())) {
            distance += Math.pow(UserAttributeMap.ATTRIBUTE_MAX, 2);
        }
        for (String attribute : userAttributes.getAttributeNames()) {
            distance += Math.pow((userAttributes.getValue(attribute) -
                    carAttributes.getOrDefault(attribute, 0.0)), 2)
                    * userAttributes.getImportance(attribute);
        }
        return Math.sqrt(distance);
    }

    public static void main(String[] args) {
        CarStock carStock = new CarStock("classpath:cars.json");
        if (carStock.getAllCars().isEmpty()) {
            HashMap<String, Double> attrs1 = new HashMap<>();
            attrs1.put("cost", 2.0);
            attrs1.put("sportiness", 1.5);
            attrs1.put("mileage", 4.0);
            attrs1.put("seating", 3.0);
            carStock.addCar(new Car(attrs1, "Toyota", "Corolla", "Sedan"));

            HashMap<String, Double> attrs2 = new HashMap<>();
            attrs2.put("cost", 2.5);
            attrs2.put("sportiness", 2.5);
            attrs2.put("mileage", 3.0);
            attrs2.put("seating", 2.5);
            carStock.addCar(new Car(attrs2, "Honda", "Civic", "Hatchback"));

            HashMap<String, Double> attrs3 = new HashMap<>();
            attrs3.put("cost", 5.0);
            attrs3.put("sportiness", 5.0);
            attrs3.put("mileage", 1.0);
            attrs3.put("seating", 0.0);
            carStock.addCar(new Car(attrs3, "Lexus", "LFA", "Sports"));
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Car Recommendation System ===");
            System.out.println("1. Add Car");
            System.out.println("2. Check Car Inventory");
            System.out.println("3. Find Best Match");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    System.out.println("Enter make:");
                    String make = scanner.nextLine().trim();
                    System.out.println("Enter model:");
                    String model = scanner.nextLine().trim();
                    System.out.println("Enter body type:");
                    String bodyType = scanner.nextLine().trim();

                    HashMap<String, Double> attrs = new HashMap<>();
                    for (String attr : UserAttributeMap.ATTRIBUTE_NAMES) {
                        System.out.println("Enter value for " + attr + " (0-" + (int) UserAttributeMap.ATTRIBUTE_MAX + "):");
                        while (!scanner.hasNextDouble()) {
                            System.out.println("Please enter a valid number:");
                            scanner.nextLine();
                        }
                        attrs.put(attr, scanner.nextDouble());
                    }
                    scanner.nextLine();

                    carStock.addCar(new Car(attrs, make, model, bodyType));
                    System.out.println("Car added successfully!");
                    break;

                case "2":
                    carStock.printInventory();
                    break;

                case "3":
                    User user = new User();

                    for (String attr : UserAttributeMap.ATTRIBUTE_NAMES) {
                        System.out.print(attr + " preference [1=Very Low, 3=Medium, 5=Very High]: ");
                        while (!scanner.hasNextInt()) { scanner.nextLine(); }
                        double val = scanner.nextInt();

                        System.out.print(attr + " importance [1=Not Important, 3=Moderate, 5=Extremely Important]: ");
                        while (!scanner.hasNextInt()) { scanner.nextLine(); }
                        double imp = scanner.nextInt();
                        user.addAttribute(attr, val, imp);
                    }
                    scanner.nextLine();

                    System.out.println("Enter preferred makes (one per line, 0 to finish):");
                    while (true) {
                        String input = scanner.nextLine().trim();
                        if (input.equals("0")) break;
                        user.addPreferredMake(input);
                    }

                    System.out.println("Enter preferred body types (one per line, 0 to finish):");
                    while (true) {
                        String input = scanner.nextLine().trim();
                        if (input.equals("0")) break;
                        user.addPreferredBodyType(input);
                    }

                    Car best = carStock.findBestMatch(user);
                    System.out.println("\nBest match for this user: " + best);
                    break;

                case "4":
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1-4.");
            }
        }

        scanner.close();
    }
}