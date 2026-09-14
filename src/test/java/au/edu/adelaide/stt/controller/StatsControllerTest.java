package au.edu.adelaide.stt.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import au.edu.adelaide.stt.model.GlobalStatsResponse;
import au.edu.adelaide.stt.service.StatisticsService;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatisticsService statisticsService;


    @Test
    void globalStatsReturnsTokenTotals()
            throws Exception {

        when(
            statisticsService.getStats()
        ).thenReturn(
            new GlobalStatsResponse(
                18432,
                4096
            )
        );


        mockMvc.perform(
                get(
                    "/api/v1/global/stats"
                )
            )
            .andExpect(
                status().isOk()
            )
            .andExpect(
                jsonPath(
                    "$.inputTokens"
                ).value(18432)
            )
            .andExpect(
                jsonPath(
                    "$.outputTokens"
                ).value(4096)
            );
    }
}