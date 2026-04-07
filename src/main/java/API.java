import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;

public class API {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/match", (HttpExchange exchange) -> {

            addCORS(exchange);

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                sendTextResponse(exchange, 405, "Only POST allowed");
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes());
            System.out.println("Frontend JSON: " + body);

            String backendJson = convert(body);

            System.out.println("Converted JSON: " + backendJson);

            String result = SurveyAPI.match(backendJson);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, result.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(result.getBytes());
            }
        });

        server.createContext("/api/cars", (HttpExchange exchange) -> {

            addCORS(exchange);

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                sendTextResponse(exchange, 405, "Only GET allowed");
                return;
            }

            Map<String, String> queryParams = parseQuery(exchange.getRequestURI().getRawQuery());
            ArrayList<Predicate<Car>> filters = new ArrayList<>();

            String make = queryParams.getOrDefault("make", "").trim();
            if (!make.isEmpty()) {
                filters.add(CarFilter.byMake(make));
            }

            String bodyType = queryParams.getOrDefault("bodyType", "").trim();
            if (!bodyType.isEmpty()) {
                filters.add(CarFilter.byBodyType(bodyType));
            }

            String maxPrice = queryParams.getOrDefault("maxPrice", "").trim();
            if (!maxPrice.isEmpty()) {
                try {
                    filters.add(CarFilter.maxPrice(Integer.parseInt(maxPrice)));
                } catch (NumberFormatException exception) {
                    sendTextResponse(exchange, 400, "Invalid maxPrice");
                    return;
                }
            }

            CarFilterRunner runner = new CarFilterRunner("src/main/resources/static/cars.json");
            ArrayList<Car> cars = runner.filter(filters.toArray(new Predicate[0]));

            String response = toJsonArray(cars);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        server.start();
        System.out.println("✅ Server running at http://localhost:8080");
    }

    private static String convert(String json) {

        int price = safeInt(json, "price");
        double priceImp = clamp(JSONHelper.extractDouble(json, "priceImportance"));

        int hp = safeInt(json, "horsepower");
        double hpImp = clamp(JSONHelper.extractDouble(json, "powerImportance"));

        int mileage = safeInt(json, "mileage");
        double mileageImp = clamp(JSONHelper.extractDouble(json, "mileageImportance"));

        int seats = safeInt(json, "seats");
        double seatImp = clamp(JSONHelper.extractDouble(json, "seatImportance"));

        double economy = safeDouble(json, "economy");
        double economyImp = clamp(JSONHelper.extractDouble(json, "economyImportance"));

        int year = safeInt(json, "year");
        double yearImp = clamp(JSONHelper.extractDouble(json, "yearImportance"));

        ArrayList<String> makes = JSONHelper.extractStringList(json, "makes");
        String bodyType = JSONHelper.extractString(json, "bodyType");

        String fuel = JSONHelper.extractString(json, "fuelType");
        String transmission = JSONHelper.extractString(json, "transmission");

        return "{"
                + "\"costValue\":" + price + ","
                + "\"costImportance\":" + priceImp + ","

                + "\"sportinessValue\":" + hp + ","
                + "\"sportinessImportance\":" + hpImp + ","

                + "\"mileageValue\":" + mileage + ","
                + "\"mileageImportance\":" + mileageImp + ","

                + "\"seatingValue\":" + seats + ","
                + "\"seatingImportance\":" + seatImp + ","

                + "\"economyValue\":" + economy + ","
                + "\"economyImportance\":" + economyImp + ","

                + "\"recencyValue\":" + year + ","
                + "\"recencyImportance\":" + yearImp + ","

                + "\"preferredMakes\":" + listToJson(makes) + ","

                // ✅ 修复空 bodyType
                + "\"preferredBodyTypes\":" +
                (bodyType.isEmpty() ? "[]" : "[\"" + bodyType + "\"]") + ","

                + "\"preferredFuelType\":\"" + (fuel.isEmpty() ? "Gas" : fuel) + "\","
                + "\"preferredTransmission\":\"" + (transmission.isEmpty() ? "Automatic" : transmission) + "\""
                + "}";
    }

    private static int safeInt(String json, String key) {
        return Math.max(0, JSONHelper.extractInt(json, key));
    }

    private static double safeDouble(String json, String key) {
        return Math.max(0, JSONHelper.extractDouble(json, key));
    }

    private static double clamp(double val) {
        return Math.max(1, Math.min(5, val));
    }

    private static String listToJson(ArrayList<String> list) {
        if (list == null || list.isEmpty()) return "[]";

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("\"").append(list.get(i)).append("\"");
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;

        for (String pair : query.split("&")) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                map.put(key, value);
            }
        }
        return map;
    }

    private static String toJsonArray(ArrayList<Car> cars) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cars.size(); i++) {
            sb.append(cars.get(i).toJson()); // ⚠️ Car 必须实现 toJson()
            if (i < cars.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }


    private static void addCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }


    private static void sendTextResponse(HttpExchange exchange, int code, String msg) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/plain");
        exchange.sendResponseHeaders(code, msg.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(msg.getBytes());
        }
    }
}