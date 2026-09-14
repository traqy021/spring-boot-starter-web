package au.edu.adelaide.stt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import au.edu.adelaide.stt.model.GlobalStatsResponse;

class StatisticsServiceTest {

    @Test
    void addsTokenUsageCorrectly() {

        StatisticsService service =
                new StatisticsService();

        service.addUsage(100, 20);

        service.addUsage(50, 10);

        GlobalStatsResponse result =
                service.getStats();

        assertEquals(
                150,
                result.inputTokens()
        );

        assertEquals(
                30,
                result.outputTokens()
        );
    }
}