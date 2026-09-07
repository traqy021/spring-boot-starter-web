package au.edu.adelaide.stt.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import au.edu.adelaide.stt.model.UptimeResponse;

@Service
public class UptimeService {

    private final Instant serverStart;

    public UptimeService() {
        this.serverStart = Instant.now();
    }

    public UptimeResponse getUptime() {

        Instant now = Instant.now();

        Duration uptime = Duration.between(serverStart, now);

        double uptimeSeconds =
                uptime.toNanos() / 1_000_000_000.0;

        return new UptimeResponse(
                serverStart,
                now,
                uptimeSeconds
        );
    }
}