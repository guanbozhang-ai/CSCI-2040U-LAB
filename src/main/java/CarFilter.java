import java.util.function.Predicate;

public class CarFilter {

    public static Predicate<Car> maxYear(int max) {
        return car -> car.getYear() <= max;
    }

    public static Predicate<Car> minYear(int min) {
        return car -> car.getYear() >= min;
    }

    public static Predicate<Car> byMake(String make) {
        return car -> car.getMake().equalsIgnoreCase(make);
    }

    public static Predicate<Car> maxPrice(int max) {
        return car -> car.getPrice() <= max;
    }

    public static Predicate<Car> minPrice(int min) {
        return car -> car.getPrice() >= min;
    }

    public static Predicate<Car> byBodyType(String bodyType) {
        return car -> car.getBodyType().equalsIgnoreCase(bodyType);
    }

    public static Predicate<Car> byFuelType(String fuelType) {
        return car -> car.getFuelType().equalsIgnoreCase(fuelType);
    }

    public static Predicate<Car> minHorsepower(int min) {
        return car -> car.getHorsepower() >= min;
    }

    public static Predicate<Car> maxMileage(int max) {
        return car -> car.getMileage() <= max;
    }

    public static Predicate<Car> minFuelEconomy(double min) {
        return car -> car.getFuelEconomy() >= min;
    }

    public static Predicate<Car> byDrivetrainConfiguration(String drivetrainConfiguration) {
        return car -> car.getDrivetrainConfiguration().equalsIgnoreCase(drivetrainConfiguration);
    }

    public static Predicate<Car> minSeating(int min) {
        return car -> car.getSeating() >= min;
    }

    public static Predicate<Car> maxSeating(int max) {
        return car -> car.getSeating() <= max;
    }

    public static Predicate<Car> minCylinders(int min) {
        return car -> car.getCylinders() >= min;
    }

    public static Predicate<Car> minGears(int min) {
        return car -> car.getGears() >= min;
    }

    public static Predicate<Car> byTransmission(String transmission) {
        return car -> car.getTransmission().equalsIgnoreCase(transmission);
    }

}