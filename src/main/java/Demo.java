import java.util.*;

public class Demo {
    public static void printMenu() {
        System.out.println("Car Dealership Test Menu");
        System.out.println("1. Add a car");
        System.out.println("2. List all cars in stock");
        System.out.println("3. Take survey to match user to a car");
        System.out.println("0. Exit");
    }

    public static void addCar(Scanner scanner, CarStock carStock) {
        System.out.println("Year: ");
        int year = scanner.nextInt();

        System.out.print("Make: ");
        String make = scanner.next();

        System.out.print("Model: ");
        String model = scanner.next();

        System.out.print("Price: ");
        int price = scanner.nextInt();

        System.out.print("Body Type: ");
        String bodyType = scanner.next();
        scanner.nextLine();

        System.out.print("Trim: ");
        String trim = scanner.nextLine();

        System.out.print("Exterior Colour: ");
        String extColour = scanner.nextLine();

        System.out.print("Interior Colour: ");
        String intColour = scanner.nextLine();

        System.out.print("Fuel Type: ");
        String fuelType = scanner.next();

        System.out.print("Horsepower: ");
        int horsepower = scanner.nextInt();

        System.out.print("Mileage: ");
        int mileage = scanner.nextInt();

        System.out.print("Fuel Economy: ");
        double fuelEconomy = scanner.nextDouble();

        System.out.print("Engine Configuration: ");
        String engineConfiguration = scanner.next();

        System.out.print("Drivetrain Configuration: ");
        String drivetrainConfiguration = scanner.next();

        System.out.print("Seating: ");
        int seating = scanner.nextInt();

        System.out.print("Cylinders: ");
        int cylinders = scanner.nextInt();

        System.out.print("Gears: ");
        int gears = scanner.nextInt();

        System.out.print("Transmission: ");
        String transmission = scanner.next();

        System.out.print("Image Link: ");
        String imageURL = scanner.next();

        carStock.addCar(new Car(year, make, model, price, bodyType,
                trim, extColour, intColour, fuelType, horsepower,
                mileage, fuelEconomy, engineConfiguration, drivetrainConfiguration, seating,
                cylinders, gears, transmission, imageURL));
    }

    public static void main(String[] args) {
        int option = 100;
        Scanner scanner = new Scanner(System.in);
        CarStock carStock = new CarStock("src/main/resources/static/cars.json");

        while(option != 0) {
            printMenu();
            option = scanner.nextInt();

            switch(option) {
                case 0:
                    break;

                case 1:
                    addCar(scanner, carStock);
                    break;

                case 2:
                    carStock.printInventory();
                    break;

                case 3:
                    User user = new User();
                    System.out.println("Best match for this user: " + carStock.findBestMatch(user));
                    break;

                default:
                    System.out.println("Invalid selection!\n");
                    break;
            }
        }
    }
}
