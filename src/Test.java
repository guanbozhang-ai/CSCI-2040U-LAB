import java.util.*;

public class Test {
    public static void main(String[] args) {
        HashMap<String, Double> attributes1 = new HashMap<>();
        HashMap<String, Double> attributes2 = new HashMap<>();
        HashMap<String, Double> attributes3 = new HashMap<>();

        attributes1.put("cost", 2.0);
        attributes1.put("sportiness", 1.5);
        attributes1.put("mileage", 4.0);
        attributes1.put("seating", 3.0);

        attributes2.put("cost", 2.5);
        attributes2.put("sportiness", 2.5);
        attributes2.put("mileage", 3.0);
        attributes2.put("seating", 2.5);

        attributes3.put("cost", 5.0);
        attributes3.put("sportiness", 5.0);
        attributes3.put("mileage", 1.0);
        attributes3.put("seating", 0.0);

        Car car1 = new Car(attributes1, "Toyota", "Corolla", "Sedan");
        Car car2 = new Car(attributes2, "Honda", "Civic", "Hatchback");
        Car car3 = new Car(attributes3, "Lexus", "LFA", "Sports");
        CarStock carStock = new CarStock();
        carStock.addCar(car1);
        carStock.addCar(car2);
        carStock.addCar(car3);

        Scanner scanner = new Scanner(System.in);
        User user = new User();

        for(String attribute : UserAttributeMap.ATTRIBUTE_NAMES) {
            System.out.println("Input preferred value for attribute " + attribute + ": ");
            double attributeValue = scanner.nextDouble();
            System.out.println("Input importance of matching attribute " + attribute + ": ");
            double attributeImportance = scanner.nextDouble();
            user.addAttribute(attribute, attributeValue, attributeImportance);
        }

        String input = null;
        while(!Objects.equals(input, "0")){
            System.out.println("Input preferred make (0 to quit):");
            input = scanner.next();
            user.addPreferredMake(input);
            //TODO: make a list (initialize on runtime) of all possible makes in Car class
        }
        input = null;
        while(!Objects.equals(input, "0")){
            System.out.println("Input preferred body type (0 to quit):");
            input = scanner.next();
            user.addPreferredMake(input);
            //TODO: make a list (initialize on runtime) of all possible bodyTypes in Car class
        }

        System.out.println("Best match for this user: " + carStock.findBestMatch(user));
    }
}
