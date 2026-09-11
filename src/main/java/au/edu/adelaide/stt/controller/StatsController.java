package au.edu.adelaide.stt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import au.edu.adelaide.stt.model.GlobalStatsResponse;
import au.edu.adelaide.stt.service.StatisticsService;

@RestController
@RequestMapping("/api/v1/global")
public class StatsController {

    private final StatisticsService
            statisticsService;

    public StatsController(
            StatisticsService
                statisticsService
    ) {

        this.statisticsService =
                statisticsService;
    }


    @GetMapping("/stats")
    public GlobalStatsResponse getStats() {

        return statisticsService
                .getStats();
    }
}