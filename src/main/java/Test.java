import java.util.*;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarStock carStock = new CarStock("src/main/resources/cars.json");
        User user = new User();
        int carSelection = 1;

        while (carSelection != 0) {
            System.out.println("Add new cars to storage. Any number to continue, 0 to quit.");
            carSelection = scanner.nextInt();
            if(carSelection != 0) {
                System.out.print("Make: ");
                String make = scanner.next();

                System.out.print("Model: ");
                String model = scanner.next();

                System.out.print("Body Type: ");
                String bodyType = scanner.next();

                System.out.print("Horsepower: ");
                int horsepower = scanner.nextInt();

                System.out.print("Price: ");
                int price = scanner.nextInt();

                System.out.print("Mileage: ");
                int mileage = scanner.nextInt();

                System.out.print("Seating: ");
                int seating = scanner.nextInt();

                carStock.addCar(new Car(make, model, bodyType, horsepower, price, mileage, seating));
            }
        }

        carStock.printInventory();

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
