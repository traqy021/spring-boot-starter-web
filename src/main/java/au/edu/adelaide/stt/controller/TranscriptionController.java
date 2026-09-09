package au.edu.adelaide.stt.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import au.edu.adelaide.stt.model.TranscriptionResponse;

@RestController
@RequestMapping("/api/v1")
public class TranscriptionController {

    @PostMapping(
        value = "/transcribe",
        consumes =
            MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public TranscriptionResponse transcribe(
            @RequestParam("file")
            MultipartFile file
    ) {

        return new TranscriptionResponse(
                "Backend received "
                + file.getSize()
                + " bytes."
        );
    }
}