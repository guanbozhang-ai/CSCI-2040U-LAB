import java.util.function.Predicate;

public class CarFilter {

    public static Predicate<Car> byMake(String make) {
        return car -> car.getMake().equalsIgnoreCase(make);
    }

    public static Predicate<Car> byBodyType(String bodyType) {
        return car -> car.getBodyType().equalsIgnoreCase(bodyType);
    }

    public static Predicate<Car> maxPrice(int max) {
        return car -> car.getPrice() <= max;
    }

    public static Predicate<Car> minPrice(int min) {
        return car -> car.getPrice() >= min;
    }

    public static Predicate<Car> maxMileage(int max) {
        return car -> car.getMileage() <= max;
    }

    public static Predicate<Car> minHorsepower(int min) {
        return car -> car.getHorsepower() >= min;
    }

    public static Predicate<Car> minSeating(int min) {
        return car -> car.getSeating() >= min;
    }
}