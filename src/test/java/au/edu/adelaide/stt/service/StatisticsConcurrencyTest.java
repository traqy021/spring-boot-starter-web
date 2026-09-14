package au.edu.adelaide.stt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;

import au.edu.adelaide.stt.model.GlobalStatsResponse;

class StatisticsConcurrencyTest {

    @Test
    void handlesConcurrentUpdates()
            throws InterruptedException {

        StatisticsService service =
                new StatisticsService();

        int threadCount = 250;

        CountDownLatch ready =
                new CountDownLatch(
                    threadCount
                );

        CountDownLatch start =
                new CountDownLatch(1);

        CountDownLatch finished =
                new CountDownLatch(
                    threadCount
                );

        List<Thread> threads =
                new ArrayList<>();


        for (
            int i = 0;
            i < threadCount;
            i++
        ) {

            Thread thread =
                    new Thread(() -> {

                        try {

                            ready.countDown();

                            start.await();

                            service.addUsage(
                                    10,
                                    2
                            );

                        }
                        catch (
                            InterruptedException e
                        ) {

                            Thread.currentThread()
                                .interrupt();
                        }
                        finally {

                            finished.countDown();
                        }

                    });

            threads.add(thread);

            thread.start();
        }


        ready.await();

        start.countDown();

        finished.await();


        GlobalStatsResponse result =
                service.getStats();


        assertEquals(
                2500,
                result.inputTokens()
        );

        assertEquals(
                500,
                result.outputTokens()
        );
    }
}