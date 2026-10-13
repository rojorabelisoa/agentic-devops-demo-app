package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/** Boots the full application on a random port and calls it over real HTTP. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @LocalServerPort
    private int port;

    private final HttpClient http = HttpClient.newHttpClient();

    private HttpResponse<String> get(String path) throws Exception {
        return http.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).build(),
                HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void greetingEndpointReturnsJson() throws Exception {
        HttpResponse<String> response = get("/api/greeting?name=Jenkins");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Hello, Jenkins!");
    }

    @Test
    void readinessProbeIsUp() throws Exception {
        HttpResponse<String> response = get("/actuator/health/readiness");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"status\":\"UP\"");
    }
}
