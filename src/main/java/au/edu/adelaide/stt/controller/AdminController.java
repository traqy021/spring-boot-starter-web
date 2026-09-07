package au.edu.adelaide.stt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import au.edu.adelaide.stt.model.UptimeResponse;
import au.edu.adelaide.stt.service.UptimeService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UptimeService uptimeService;

    public AdminController(UptimeService uptimeService) {
        this.uptimeService = uptimeService;
    }

    @GetMapping("/uptime")
    public UptimeResponse getServerUptime() {
        return uptimeService.getUptime();
    }
}