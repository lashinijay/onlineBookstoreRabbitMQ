import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/books", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override

        public void handle(HttpExchange t) throws IOException {

            String uri = t.getRequestURI().getQuery();
            String method = t.getRequestMethod();

            InputStreamReader rdr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
            Stream<String> query = new BufferedReader(rdr).lines();
            StringBuilder sb = new StringBuilder();
            query.forEach(s -> sb.append(s).append("\n"));
            String body = sb.toString();

            Publisher.publisher(method, uri, body);

            String response = "";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

