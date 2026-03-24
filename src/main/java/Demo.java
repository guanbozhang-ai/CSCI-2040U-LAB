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
                    System.out.println("Please use the GUI to find your match.");
                    break;

                default:
                    System.out.println("Invalid selection!\n");
                    break;
            }
        }
    }
}
