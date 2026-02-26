import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class User {
    public static final double ATTRIBUTE_MAX = 5;
    //TODO: rework attribute vector to be HashMap<String,Double>, where each string is an attribute and each double is its value
    private static ArrayList<Double> userAttributes;
    private static ArrayList<String> preferredMakes;

    public User(ArrayList<Double> userAttributes, ArrayList<String> preferredMakes) {
        this.userAttributes = userAttributes;
        this.preferredMakes = preferredMakes;
    }

    public static double match(Car car) {
        ArrayList<Double> carAttributes = car.getCarAttributes();
        double distance = 0;
        if (!preferredMakes.contains(car.getMake())){
            distance += Math.pow(ATTRIBUTE_MAX, 2);
        }
        for(int i = 0; i < userAttributes.size(); i++) {
            distance += Math.pow( (userAttributes.get(i) - carAttributes.get(i)) , 2);

        }
        return Math.sqrt(distance);
    }

    //TODO: create a new class to actually run the program, this main function is just for testing purposes
    public static void main(String[] args) {
        ArrayList<Double> attributes1 = new ArrayList<>(Arrays.asList(1.0,2.4,3.2));
        ArrayList<Double> attributes2 = new ArrayList<>(Arrays.asList(2.0,0.4,2.3));
        ArrayList<Double> attributes3 = new ArrayList<>(Arrays.asList(0.2,0.2,0.9));
        Car car1 = new Car(attributes1, "Toyota", "Corolla");
        Car car2 = new Car(attributes2, "Honda", "Civic");
        Car car3 = new Car(attributes3, "Lexus", "LFA");
        CarStock carStock = new CarStock();
        carStock.addCar(car1);
        carStock.addCar(car2);
        carStock.addCar(car3);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> userAttributes = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            System.out.println("Input value for attribute " + (i+1) + ":");
            double attributeInput = scanner.nextDouble();
            userAttributes.add(attributeInput);
        }

        ArrayList<String> preferredMakes = new ArrayList<>();
        String makeInput = null;
        while(!Objects.equals(makeInput, "0")){
            System.out.println("Input preferred make (0 to quit):");
            makeInput = scanner.next();
            preferredMakes.add(makeInput);
            //TODO: make a list of all possible makes in Car class
        }
        User user = new User(userAttributes, preferredMakes);

        System.out.println("Best match for this user: " + carStock.findBestMatch(user));
    }
}