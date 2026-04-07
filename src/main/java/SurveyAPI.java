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


        try {
            User user = User.fromJson(userJson);

            int num;

            try {
                num = JSONHelper.extractInt(userJson, "count");
            } catch (Exception e) {
                num = 5;
            }
            if (num <= 0) num = 5;

            ArrayList<Car> cars = StockSingleton.getInstance().findBestMatch(num, user);

            if (cars == null || cars.isEmpty()) {
                return "[]";
            }

            int limit = Math.min(num, cars.size());

            StringBuilder carsJson = new StringBuilder("[");

            for (int i = 0; i < limit; i++) {
                if (cars.get(i) != null) {
                    carsJson.append(cars.get(i).toJson());
                    if (i < limit - 1) {
                        carsJson.append(",");
                    }
                }
            }

            carsJson.append("]");

            return carsJson.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }

    }

}
