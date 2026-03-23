import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;

public class TestServer {

    public static void main(String[] args) throws Exception {

        // 创建服务器（端口8080）
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 创建接口 /api/match
        server.createContext("/api/match", (HttpExchange exchange) -> {

            // ====== 1. 允许跨域（必须）======
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // ====== 2. 处理浏览器预检请求 ======
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // ====== 3. 只允许 POST ======
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                String error = "Only POST allowed";
                exchange.sendResponseHeaders(405, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();
                return;
            }

            // ====== 4. 接收前端数据 ======
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes());

            System.out.println("===== 收到前端数据 =====");
            System.out.println(body);

            // ====== 5. 构造返回数据（模拟推荐结果）======
            String response = "{ " +
                    "\"make\":\"Toyota\", " +
                    "\"model\":\"Corolla\", " +
                    "\"price\":15000, " +
                    "\"horsepower\":150" +
                    " }";

            // ====== 6. 设置响应头 ======
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            // ====== 7. 返回数据 ======
            System.out.println("准备返回数据");
            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            System.out.println("返回数据");
        });

        // 启动服务器
        server.start();

        System.out.println("✅ Server running at http://localhost:8080");
        System.out.println("👉 API: POST http://localhost:8080/api/match");
    }
}