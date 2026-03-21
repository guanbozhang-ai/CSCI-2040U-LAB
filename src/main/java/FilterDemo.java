import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Predicate;

public class FilterDemo {
    /*
    NOTE: DOES NOT MATCH ALL CRITERIA OF NEW FILTER
    I figured we can probably just worry about adding that functionality with our frontend
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarFilterRunner runner = new CarFilterRunner("src/main/resources/static/cars.json");

        System.out.println("=== Car Inventory Filter ===");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = scanner.nextInt();

            ArrayList<Predicate<Car>> filters = new ArrayList<>();

            switch (choice) {
                case 0:
                    running = false;
                    break;

                case 1:
                    filters = buildFilters(scanner);
                    ArrayList<Car> results = runner.filter(filters.toArray(new Predicate[0]));
                    printResults(results);
                    break;

                default:
                    System.out.println("Invalid option.\n");
                    break;
            }
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Filter inventory");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private static ArrayList<Predicate<Car>> buildFilters(Scanner scanner) {
        ArrayList<Predicate<Car>> filters = new ArrayList<>();

        System.out.println("\n-- Leave blank (press Enter) to skip any filter --");
        scanner.nextLine(); // flush buffer

        // Make
        System.out.print("Filter by make (e.g. Toyota): ");
        String make = scanner.nextLine().trim();
        if (!make.isEmpty()) filters.add(CarFilter.byMake(make));

        // Body type
        System.out.print("Filter by body type (e.g. SUV): ");
        String bodyType = scanner.nextLine().trim();
        if (!bodyType.isEmpty()) filters.add(CarFilter.byBodyType(bodyType));

        // Max price
        System.out.print("Max price (e.g. 40000): ");
        String maxPrice = scanner.nextLine().trim();
        if (!maxPrice.isEmpty()) filters.add(CarFilter.maxPrice(Integer.parseInt(maxPrice)));

        // Min price
        System.out.print("Min price (e.g. 10000): ");
        String minPrice = scanner.nextLine().trim();
        if (!minPrice.isEmpty()) filters.add(CarFilter.minPrice(Integer.parseInt(minPrice)));

        // Max mileage
        System.out.print("Max mileage in km (e.g. 80000): ");
        String maxMileage = scanner.nextLine().trim();
        if (!maxMileage.isEmpty()) filters.add(CarFilter.maxMileage(Integer.parseInt(maxMileage)));

        // Min horsepower
        System.out.print("Min horsepower (e.g. 150): ");
        String minHp = scanner.nextLine().trim();
        if (!minHp.isEmpty()) filters.add(CarFilter.minHorsepower(Integer.parseInt(minHp)));

        // Min seating
        System.out.print("Min seats (e.g. 5): ");
        String minSeats = scanner.nextLine().trim();
        if (!minSeats.isEmpty()) filters.add(CarFilter.minSeating(Integer.parseInt(minSeats)));

        return filters;
    }

    private static void printResults(ArrayList<Car> results) {
        System.out.println("\n-- Results (" + results.size() + " car(s) found) --");
        if (results.isEmpty()) {
            System.out.println("No cars matched your filters.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }
}

