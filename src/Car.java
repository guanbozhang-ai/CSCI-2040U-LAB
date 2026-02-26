import java.util.ArrayList;
import java.util.HashMap;

public class Car {
    //TODO: rework attribute vector to be HashMap<String,Double>, where each string is an attribute and each double is its value
    private HashMap<String, Double> carAttributes;
    private String make;
    private String model;
    private String bodyType;

    public Car() {
        this.carAttributes = new HashMap<>();
        this.make = null;
        this.model = null;
        this.bodyType = null;
    }

    public Car(HashMap<String, Double> carAttributes, String make, String model, String bodyType) {
        this.carAttributes = carAttributes;
        this.make = make;
        this.model = model;
        this.bodyType = bodyType;
    }

    public HashMap<String, Double> getCarAttributes() {
        return carAttributes;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
       return model;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public void setAttribute(String name, Double value) {
        carAttributes.putIfAbsent(name, value);
    }

    public String toString() {
        return make + " " + model;
    }
}
