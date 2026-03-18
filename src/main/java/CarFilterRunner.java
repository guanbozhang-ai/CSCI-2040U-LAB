import java.util.ArrayList;
import java.util.function.Predicate;

public class CarFilterRunner {
    private final CarStorage storage;

    public CarFilterRunner(String filePath) {
        this.storage = new CarStorage(filePath);
    }

    /**
     * Returns all cars from the JSON file that match every provided filter.
     * Pass any number of CarFilter predicates — they are AND-ed together.
     */
    @SafeVarargs
    public final ArrayList<Car> filter(Predicate<Car>... filters) {
        ArrayList<Car> cars = storage.load();

        // merged filters together
        Predicate<Car> combined = car -> true;   // start with "match all"
        for (Predicate<Car> f : filters) {
            combined = combined.and(f);
        }
        //Applying the Rules
        ArrayList<Car> results = new ArrayList<>();
        for (Car car : cars) {
            if (combined.test(car)) results.add(car);
        }
        return results;
    }
}