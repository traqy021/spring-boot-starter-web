package au.edu.adelaide.stt.model;

import java.time.Instant;

public record UptimeResponse(
        Instant utcServerStart,
        Instant utcNow,
        double serverUptimeSeconds
) {
}