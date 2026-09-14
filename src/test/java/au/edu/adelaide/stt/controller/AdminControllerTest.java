package au.edu.adelaide.stt.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import au.edu.adelaide.stt.exception.GlobalExceptionHandler;
import au.edu.adelaide.stt.model.UptimeResponse;
import au.edu.adelaide.stt.service.ShutdownService;
import au.edu.adelaide.stt.service.UptimeService;

@WebMvcTest(AdminController.class)
@Import(GlobalExceptionHandler.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UptimeService uptimeService;

    @MockitoBean
    private ShutdownService shutdownService;


    @Test
    void uptimeReturnsExpectedResponse()
            throws Exception {

        Instant start =
                Instant.parse(
                    "2026-09-13T10:00:00Z"
                );

        Instant now =
                Instant.parse(
                    "2026-09-13T10:02:30Z"
                );

        when(uptimeService.getUptime())
                .thenReturn(
                    new UptimeResponse(
                        start,
                        now,
                        150.0
                    )
                );


        mockMvc.perform(
                get(
                    "/api/v1/admin/uptime"
                )
            )
            .andExpect(
                status().isOk()
            )
            .andExpect(
                jsonPath(
                    "$.utcServerStart"
                ).value(
                    "2026-09-13T10:00:00Z"
                )
            )
            .andExpect(
                jsonPath(
                    "$.utcNow"
                ).value(
                    "2026-09-13T10:02:30Z"
                )
            )
            .andExpect(
                jsonPath(
                    "$.serverUptimeSeconds"
                ).value(150.0)
            );
    }


    @Test
    void shutdownReturns202WhenAccepted()
            throws Exception {

        when(
            shutdownService.requestShutdown()
        ).thenReturn(true);


        mockMvc.perform(
                post(
                    "/api/v1/admin/shutdown"
                )
            )
            .andExpect(
                status().isAccepted()
            )
            .andExpect(
                jsonPath(
                    "$.message"
                ).value(
                    "Graceful shutdown requested."
                )
            );
    }


    @Test
    void shutdownReturns409WhenAlreadyInProgress()
            throws Exception {

        when(
            shutdownService.requestShutdown()
        ).thenReturn(false);


        mockMvc.perform(
                post(
                    "/api/v1/admin/shutdown"
                )
            )
            .andExpect(
                status().isConflict()
            )
            .andExpect(
                jsonPath(
                    "$.status"
                ).value(409)
            )
            .andExpect(
                jsonPath(
                    "$.error"
                ).value("Conflict")
            )
            .andExpect(
                jsonPath(
                    "$.message"
                ).exists()
            )
            .andExpect(
                jsonPath(
                    "$.path"
                ).value(
                    "/api/v1/admin/shutdown"
                )
            );
    }
}