import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CarStock {
    private ArrayList<Car> cars;

    public CarStock () {
        cars = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public Car findBestMatch(User user) {
        double bestMatch = Double.MAX_VALUE;
        Car bestCar = null;
        for (Car car : cars) {
            double distance = user.match(car);
            if(distance < bestMatch) {
                bestMatch = distance;
                bestCar = car;
            }
        }
        return bestCar;
    }
}
