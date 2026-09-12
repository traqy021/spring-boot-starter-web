package au.edu.adelaide.stt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import au.edu.adelaide.stt.model.ShutdownResponse;
import au.edu.adelaide.stt.model.UptimeResponse;
import au.edu.adelaide.stt.service.ShutdownService;
import au.edu.adelaide.stt.service.UptimeService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UptimeService
            uptimeService;

    private final ShutdownService
            shutdownService;


    public AdminController(
            UptimeService uptimeService,
            ShutdownService shutdownService
    ) {

        this.uptimeService =
                uptimeService;

        this.shutdownService =
                shutdownService;
    }


    @GetMapping("/uptime")
    public UptimeResponse getServerUptime() {

        return uptimeService.getUptime();
    }


    @PostMapping("/shutdown")
    public ResponseEntity<ShutdownResponse>
            shutdownServer() {

        boolean accepted =
                shutdownService
                    .requestShutdown();


        if (!accepted) {

            return ResponseEntity
                    .status(
                        HttpStatus.CONFLICT
                    )
                    .body(
                        new ShutdownResponse(
                            "Graceful shutdown is already in progress."
                        )
                    );
        }


        return ResponseEntity
                .status(
                    HttpStatus.ACCEPTED
                )
                .body(
                    new ShutdownResponse(
                        "Graceful shutdown requested."
                    )
                );
    }
}