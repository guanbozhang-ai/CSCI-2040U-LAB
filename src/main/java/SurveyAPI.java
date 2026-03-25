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
        Car car = StockSingleton.getInstance().findBestMatch(user);
        return car.toJson();
    }
}
