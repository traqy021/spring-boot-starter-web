package au.edu.adelaide.stt.service;

import java.util.concurrent.atomic.LongAdder;

import org.springframework.stereotype.Service;

import au.edu.adelaide.stt.model.GlobalStatsResponse;

@Service
public class StatisticsService {

    private final LongAdder inputTokens =
            new LongAdder();

    private final LongAdder outputTokens =
            new LongAdder();

    public void addUsage(
            long input,
            long output
    ) {

        inputTokens.add(input);
        outputTokens.add(output);
    }

    public GlobalStatsResponse getStats() {

        return new GlobalStatsResponse(
                inputTokens.sum(),
                outputTokens.sum()
        );
    }
}