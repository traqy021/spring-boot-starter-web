package au.edu.adelaide.stt.model;

public record GlobalStatsResponse(
        long inputTokens,
        long outputTokens
) {
}