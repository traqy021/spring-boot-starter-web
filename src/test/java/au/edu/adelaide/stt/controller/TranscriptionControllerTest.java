package au.edu.adelaide.stt.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import au.edu.adelaide.stt.model.TranscriptionResponse;
import au.edu.adelaide.stt.service.TranscriptionService;

@WebMvcTest(TranscriptionController.class)
class TranscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TranscriptionService
            transcriptionService;


    @Test
    void audioUploadReturnsTranscription()
            throws Exception {

        MockMultipartFile audioFile =
                new MockMultipartFile(
                    "file",
                    "recording.webm",
                    "audio/webm",
                    "fake-audio-data"
                        .getBytes()
                );


        when(
            transcriptionService
                .transcribe(any())
        ).thenReturn(
            new TranscriptionResponse(
                "Hello from the test."
            )
        );


        mockMvc.perform(
                multipart(
                    "/api/v1/transcribe"
                )
                .file(audioFile)
            )
            .andExpect(
                status().isOk()
            )
            .andExpect(
                jsonPath(
                    "$.text"
                ).value(
                    "Hello from the test."
                )
            );
    }
}