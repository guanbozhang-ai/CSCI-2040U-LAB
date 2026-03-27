import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class TestServer {

    public static void main(String[] args) throws Exception {

        // 创建服务器（端口8080）
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 创建接口 /api/match
        server.createContext("/api/match", (HttpExchange exchange) -> {

            addCorsHeaders(exchange, "POST, OPTIONS");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                sendTextResponse(exchange, 405, "Only POST allowed");
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes());

            System.out.println("===== 收到前端数据 =====");
            System.out.println(body);

            exchange.getResponseHeaders().add("Content-Type", "application/json");

            String response = SurveyAPI.match(body);

            System.out.println("准备返回数据");
            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            System.out.println("返回数据");
        });

        server.createContext("/api/cars", (HttpExchange exchange) -> {
            addCorsHeaders(exchange, "GET, OPTIONS");

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

        // 启动服务器
        server.start();

        System.out.println("✅ Server running at http://localhost:8080");
        System.out.println("👉 API: POST http://localhost:8080/api/match");
        System.out.println("👉 API: GET  http://localhost:8080/api/cars");
    }

    private static void addCorsHeaders(HttpExchange exchange, String methods) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", methods);
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    private static void sendTextResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        exchange.sendResponseHeaders(statusCode, body.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        }
    }

    private static Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> queryParams = new HashMap<>();
        if (rawQuery == null || rawQuery.isEmpty()) {
            return queryParams;
        }

        for (String entry : rawQuery.split("&")) {
            if (entry.isEmpty()) {
                continue;
            }

            String[] keyValue = entry.split("=", 2);
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = keyValue.length > 1
                    ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                    : "";
            queryParams.put(key, value);
        }
        return queryParams;
    }

    private static String toJsonArray(ArrayList<Car> cars) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < cars.size(); i++) {
            json.append(cars.get(i).toJson());
            if (i < cars.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
}
