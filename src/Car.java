import java.util.ArrayList;

public class Car {
    //TODO: rework attribute vector to be HashMap<String,Double>, where each string is an attribute and each double is its value
    private ArrayList<Double> carAttributes;
    private String make;
    private String model;

    public Car() {
        this.carAttributes = new ArrayList<Double>();
        this.make = null;
        this.model = null;
    }

    public Car(ArrayList<Double> carAttributes, String make, String model) {
        this.carAttributes = carAttributes;
        this.make = make;
        this.model = model;
    }

    public ArrayList<Double> getCarAttributes() {
        return carAttributes;
    }

    public String getMake() {
        return make;
    }

    public String toString() {
        return make + " " + model;
    }
}
