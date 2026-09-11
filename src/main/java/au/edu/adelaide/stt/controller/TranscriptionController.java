package au.edu.adelaide.stt.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import au.edu.adelaide.stt.model.TranscriptionResponse;
import au.edu.adelaide.stt.service.TranscriptionService;

@RestController
@RequestMapping("/api/v1")
public class TranscriptionController {

    private final TranscriptionService
            transcriptionService;

    public TranscriptionController(
            TranscriptionService
                transcriptionService
    ) {

        this.transcriptionService =
                transcriptionService;
    }


    @PostMapping(
        value = "/transcribe",
        consumes =
            MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public TranscriptionResponse transcribe(
            @RequestParam("file")
            MultipartFile file
    ) throws IOException {

        return transcriptionService
                .transcribe(file);
    }
}