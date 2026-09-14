package au.edu.adelaide.stt.concurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest(
    webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ConcurrentHttpRequestTest {

    @LocalServerPort
    private int port;


    @TestConfiguration
    static class TestConfig {

        @Bean
        BlockingTestController
                blockingTestController() {

            return new BlockingTestController();
        }
    }


    @RestController
    static class BlockingTestController {

        @GetMapping(
            "/test/blocking-request"
        )
        public String blockingRequest()
                throws InterruptedException {

            Thread.sleep(200);

            return "ok";
        }
    }


    @Test
    void handles250SimultaneousBlockingHttpRequests()
            throws Exception {

        int requestCount = 250;

        ExecutorService executor =
                Executors.newFixedThreadPool(
                    requestCount
                );

        HttpClient client =
                HttpClient.newBuilder()
                    .executor(executor)
                    .build();


        CountDownLatch ready =
                new CountDownLatch(
                    requestCount
                );

        CountDownLatch start =
                new CountDownLatch(1);


        List<Future<Integer>> futures =
                new ArrayList<>();


        for (
            int i = 0;
            i < requestCount;
            i++
        ) {

            futures.add(
                executor.submit(() -> {

                    ready.countDown();

                    start.await();


                    HttpRequest request =
                            HttpRequest
                                .newBuilder()
                                .uri(
                                    URI.create(
                                        "http://localhost:"
                                        + port
                                        + "/test/blocking-request"
                                    )
                                )
                                .GET()
                                .build();


                    HttpResponse<String> response =
                            client.send(
                                request,
                                HttpResponse
                                    .BodyHandlers
                                    .ofString()
                            );


                    return response
                            .statusCode();
                })
            );
        }


        ready.await();

        long startTime =
                System.nanoTime();

        start.countDown();


        for (
            Future<Integer> future
                : futures
        ) {

            assertEquals(
                200,
                future.get()
            );
        }


        double elapsedSeconds =
                (
                    System.nanoTime()
                    - startTime
                )
                / 1_000_000_000.0;


        System.out.println(
            "250 blocking HTTP requests completed in "
            + elapsedSeconds
            + " seconds."
        );


        executor.shutdown();
    }
}