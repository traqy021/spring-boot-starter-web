package au.edu.adelaide.stt.service;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import tools.jackson.databind.JsonNode;

import au.edu.adelaide.stt.model.TranscriptionResponse;

@Service
public class TranscriptionService {

    private final RestClient openAiRestClient;

    private final StatisticsService statisticsService;

    public TranscriptionService(
            RestClient openAiRestClient,
            StatisticsService statisticsService
    ) {

        this.openAiRestClient =
                openAiRestClient;

        this.statisticsService =
                statisticsService;
    }


    public TranscriptionResponse transcribe(
            MultipartFile file
    ) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Audio file is empty."
            );
        }

        byte[] audioBytes =
                file.getBytes();

        String originalFilename =
                file.getOriginalFilename();

        final String uploadFilename =
                (
                    originalFilename == null ||
                    originalFilename.isBlank()
                )
                ? "recording.webm"
                : originalFilename;

        ByteArrayResource audioResource =
                new ByteArrayResource(
                    audioBytes
                ) {

                    @Override
                    public String getFilename() {
                        return uploadFilename;
                    }

                };


        MultipartBodyBuilder bodyBuilder =
                new MultipartBodyBuilder();

        bodyBuilder.part(
                "model",
                "gpt-4o-mini-transcribe"
        );

        bodyBuilder
                .part(
                    "file",
                    audioResource
                )
                .contentType(
                    MediaType.APPLICATION_OCTET_STREAM
                );


        JsonNode response =
                openAiRestClient
                    .post()
                    .uri(
                        "/v1/audio/transcriptions"
                    )
                    .contentType(
                        MediaType.MULTIPART_FORM_DATA
                    )
                    .body(
                        bodyBuilder.build()
                    )
                    .retrieve()
                    .body(JsonNode.class);


        if (response == null) {

            throw new IllegalStateException(
                    "Cloud transcription returned no response."
            );
        }


        String text =
                response
                    .path("text")
                    .asString();


        JsonNode usage =
                response.path("usage");

        long inputTokens =
                usage
                    .path("input_tokens")
                    .asLong(0);

        long outputTokens =
                usage
                    .path("output_tokens")
                    .asLong(0);


        statisticsService.addUsage(
                inputTokens,
                outputTokens
        );


        return new TranscriptionResponse(
                text
        );
    }
}