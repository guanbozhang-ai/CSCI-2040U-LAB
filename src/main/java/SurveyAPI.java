import java.util.ArrayList;

public class SurveyAPI {
    public static class StockSingleton {
        private static CarStock stock = null;

        public static CarStock getInstance() {
            if (stock == null) {
                stock = new CarStock("src/main/resources/static/cars.json");
            }
            return stock;
        }
    }
    public static String match(String userJson) {
        User user = User.fromJson(userJson);
        int num = JSONHelper.extractInt(userJson, "count");
        ArrayList<Car> cars = StockSingleton.getInstance().findBestMatch(num, user);
        StringBuilder carsJson = new StringBuilder("{");

        for(int i = 0; i < num; i++) {
            carsJson.append(cars.get(i).toJson());
            if(i < num - 1) {
                carsJson.append(",");
            }
        }

        return carsJson.toString();
    }
}
