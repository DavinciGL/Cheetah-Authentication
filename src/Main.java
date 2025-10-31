import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Main {
    // Configurable redirect target
    private static final String REDIRECT_URL = "https://example.com/protected";

    public static void main(String[] args) throws IOException {
        auth Auth = new auth();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Serve login page
        server.createContext("/", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            boolean failed = query != null && query.contains("error=true");

            String html = """
                <html>
                <head><title>Cheetah Network Authentication</title></head>
                <body>
                    <h2>Cheetah Network Authentication</h2>
                    <form method="POST" action="/login">
                        Username: <input type="text" name="username"><br><br>
                        Password: <input type="password" name="password"><br><br>
                        <input type="submit" value="Login">
                    </form>
                """ + (failed ? "<p style='color:red;'>Login Failed! You have not been Authenticated.</p>" : "") + """
                </body>
                </html>
                """;

            exchange.sendResponseHeaders(200, html.getBytes().length);
            exchange.getResponseBody().write(html.getBytes());
            exchange.close();
        });

        // Handle login logic
        server.createContext("/login", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
                StringBuilder formData = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) formData.append(line);

                String[] pairs = formData.toString().split("&");
                String username = "", password = "";
                for (String pair : pairs) {
                    String[] kv = pair.split("=");
                    if (kv.length == 2) {
                        String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                        String value = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                        if (key.equals("username")) username = value;
                        if (key.equals("password")) password = value;
                    }
                }

                if (Auth.validate(username, password)) {
                    exchange.getResponseHeaders().add("Location", "/protected");
                    exchange.sendResponseHeaders(302, -1);
                } else {
                    exchange.getResponseHeaders().add("Location", "/?error=true");
                    exchange.sendResponseHeaders(302, -1);
                }
                exchange.close();
            }
        });

        // Protected page with redirect message
        server.createContext("/protected", exchange -> {
            String html = "<html>\n" +
                    "<head>\n" +
                    "    <meta http-equiv=\"refresh\" content=\"3;url=" + REDIRECT_URL + "\">\n" +
                    "    <title>Authenticated</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h2>You have been authenticated: Redirecting you...</h2>\n" +
                    "</body>\n" +
                    "</html>";

            exchange.sendResponseHeaders(200, html.getBytes().length);
            exchange.getResponseBody().write(html.getBytes());
            exchange.close();
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Server running at http://localhost:8080/");
    }
}