import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * 一个极简 HTTP 服务器：
 *  - 监听 0.0.0.0:8081
 *  - 访问 http://<IP>:8081/ 时返回一行文本
 */
public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8081;

        // 绑定 0.0.0.0:8081（所有网卡）
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // 注册根路径处理器
        server.createContext("/", new RootHandler());

        // 使用默认线程池
        server.setExecutor(null);

        System.out.println("Server started at http://0.0.0.0:" + port);
        server.start();
    }

    // 处理根路径 /
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello, Jenkins CI with Docker! build " + System.currentTimeMillis();
            byte[] bytes = response.getBytes("UTF-8");

            // 设置响应头和响应体
            exchange.getResponseHeaders()
                    .add("Content-Type", "text/plain; charset=utf-8");

            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
