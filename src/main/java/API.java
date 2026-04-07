import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;

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
                send(exchange, 405, "Only POST allowed");
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes());
            System.out.println("Frontend JSON: " + body);

            String backendJson = convert(body);

            System.out.println("Converted JSON: " + backendJson);

            String result = SurveyAPI.match(backendJson);
            System.out.println("RESULT: " + result);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, result.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Server running at http://localhost:8080/api/match");
    }


    // ==========================
    // 🔥 核心转换（适配前端）
    // ==========================
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

        // 🚨 修复 User.java bug：bodyType 被当 make
        // 👉 我们手动放进正确字段
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

                // 🔥 手动修复 bodyType（关键）
                + "\"preferredBodyTypes\":[\"" + bodyType + "\"],"

                + "\"preferredFuelType\":\"" + (fuel.isEmpty() ? "Gas" : fuel) + "\","
                + "\"preferredTransmission\":\"" + (transmission.isEmpty() ? "Automatic" : transmission) + "\""
                + "}";
    }


    // ==========================
    // 🛠 工具方法
    // ==========================
    private static int safeInt(String json, String key) {
        int val = JSONHelper.extractInt(json, key);
        return Math.max(1, val);
    }

    private static double safeDouble(String json, String key) {
        double val = JSONHelper.extractDouble(json, key);
        return Math.max(1, val);
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

    private static void addCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    private static void send(HttpExchange exchange, int code, String msg) throws IOException {
        exchange.sendResponseHeaders(code, msg.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(msg.getBytes());
        os.close();
    }
}