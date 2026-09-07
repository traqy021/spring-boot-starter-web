package au.edu.adelaide.stt;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class UptimeController {

    private final Instant serverStart = Instant.now();

    @GetMapping("/uptime")
    public Map<String, Object> getUptime() {
        Instant now = Instant.now();

        double uptimeSeconds =
                Duration.between(serverStart, now).toMillis() / 1000.0;

        Map<String, Object> response = new HashMap<>();
        response.put("utcServerStart", serverStart.toString());
        response.put("utcNow", now.toString());
        response.put("serverUptimeSeconds", uptimeSeconds);

        return response;
    }
}