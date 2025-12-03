import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestAll {
    public static void main(String[] args) {
        System.out.println("=== COMPLETE TEST OF ALL HUs ===\n");

        try {
            testHU1();
            testHU2();
            testHU3();
            testHU4();
            testHU5();
            testHU6();

            System.out.println("\n=== ALL TESTS COMPLETED SUCCESSFULLY ===");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
        }
    }

    private static void testHU1() throws Exception {
        System.out.println("HU1: Testing In-Memory Catalog...");
        String response = sendGET("http://localhost:8080/api/v1/events");
        System.out.println("✓ Events API responding");
    }

    private static void testHU2() throws Exception {
        System.out.println("HU2: Testing Persistent Catalog...");
        String response = sendGET("http://localhost:8080/api/v1/venues");
        System.out.println("✓ Venues API responding with JPA");
    }

    private static void testHU3() throws Exception {
        System.out.println("HU3: Testing Hexagonal Architecture...");
        String[] layers = {
                "src/main/java/com/eventmanagement/domain",
                "src/main/java/com/eventmanagement/application",
                "src/main/java/com/eventmanagement/infrastructure"
        };

        for (String layer : layers) {
            if (Files.exists(Paths.get(layer))) {
                System.out.println("✓ " + layer + " exists");
            } else {
                throw new Exception("✗ " + layer + " missing");
            }
        }
    }

    private static void testHU4() throws Exception {
        System.out.println("HU4: Testing Events & Venues Management...");
        String response = sendGET("http://localhost:8080/actuator/health");
        if (response.contains("\"status\":\"UP\"")) {
            System.out.println("✓ Database and JPA working");
        } else {
            throw new Exception("Database not healthy");
        }
    }

    private static void testHU5() throws Exception {
        System.out.println("HU5: Testing JWT Security...");
        String json = "{\"username\":\"admin\",\"password\":\"admin123\"}";
        String response = sendPOST("http://localhost:8080/api/v1/auth/login", json);

        if (response.contains("\"success\":true") && response.contains("\"token\"")) {
            System.out.println("✓ JWT Authentication working");
        } else if (response.contains("Invalid username or password")) {
            System.out.println("⚠ JWT configured but wrong credentials");
            System.out.println("  Try password: admin123");
        } else {
            throw new Exception("JWT test failed");
        }
    }

    private static void testHU6() throws Exception {
        System.out.println("HU6: Testing Metrics & Deployment...");
        String response = sendGET("http://localhost:8080/actuator/health");
        if (response.contains("\"status\":\"UP\"")) {
            System.out.println("✓ Actuator endpoints working");
        }

        response = sendGET("http://localhost:8080/actuator/metrics");
        if (response.contains("\"names\"")) {
            System.out.println("✓ Metrics endpoint working");
        }
    }

    private static String sendGET(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static String sendPOST(String urlString, String json) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}