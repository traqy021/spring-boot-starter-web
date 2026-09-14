package au.edu.adelaide.stt.service;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ShutdownService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    ShutdownService.class
            );

    private final ConfigurableApplicationContext
            applicationContext;

    private final AtomicBoolean
            shutdownStarted =
                    new AtomicBoolean(false);

    public ShutdownService(
            ConfigurableApplicationContext
                    applicationContext
    ) {
        this.applicationContext =
                applicationContext;
    }

    public boolean requestShutdown() {

        if (
            !shutdownStarted.compareAndSet(
                    false,
                    true
            )
        ) {

            LOGGER.warn(
                    "Rejected shutdown request because shutdown is already in progress"
            );

            return false;
        }

        LOGGER.info(
                "Graceful shutdown requested"
        );

        Thread shutdownThread =
                new Thread(() -> {

                    try {

                        Thread.sleep(300);

                    }
                    catch (
                        InterruptedException e
                    ) {

                        Thread.currentThread()
                                .interrupt();

                        LOGGER.warn(
                                "Graceful shutdown thread was interrupted"
                        );
                    }

                    LOGGER.info(
                            "Closing Spring application context"
                    );

                    applicationContext.close();
                });

        shutdownThread.setName(
                "graceful-shutdown"
        );

        shutdownThread.start();

        return true;
    }
}