import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class User {

    // Each User has their own list of preference values
    private ArrayList<Double> userAttributes;
    // Constructor: create a User with given attributes
    public User(ArrayList<Double> userAttributes) {
        this.userAttributes = userAttributes; // Save user preferences
    }
    // Getter: return the user's attributes
    public ArrayList<Double> getUserAttributes() {
        return userAttributes; // Give back the list
    }

    public static void main(String[] args) {

        // Create / load car inventory from file cars.json
        CarStock carStock = new CarStock("cars.json");

        // If there are no cars saved, add default cars
        if (carStock.getAllCars().isEmpty()) {

            // Add Toyota Corolla with 3 attributes
            carStock.addCar(new Car(
                    new ArrayList<>(Arrays.asList(1.0, 2.4, 3.2)),
                    "Toyota",
                    "Corolla"
            ));

            // Add Honda Civic
            carStock.addCar(new Car(
                    new ArrayList<>(Arrays.asList(2.0, 0.4, 2.3)),
                    "Honda",
                    "Civic"
            ));

            // Add Lexus LFA
            carStock.addCar(new Car(
                    new ArrayList<>(Arrays.asList(0.2, 0.2, 0.9)),
                    "Lexus",
                    "LFA"
            ));
        }

        carStock.printInventory();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nWould you like to add a new car? (y/n)");

        if (scanner.next().equalsIgnoreCase("y")) {

            System.out.print("Make: ");
            String make = scanner.next();

            System.out.print("Model: ");
            String model = scanner.next();

            ArrayList<Double> attrs = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                System.out.println("Attribute " + (i + 1) + ": ");
                attrs.add(scanner.nextDouble());
            }

            carStock.addCar(new Car(attrs, make, model));
            System.out.println("Car saved to cars.json.");
        }

        System.out.println("\nEnter your preferences (3 values):");

        ArrayList<Double> userAttributes = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            System.out.println("Preference " + (i + 1) + ": ");
            userAttributes.add(scanner.nextDouble());
        }

        User user = new User(userAttributes);

        // Find the best matching car
        Car best = carStock.findBestMatch(user.getUserAttributes());

        System.out.println("\nBest match: " + best);
    }
}