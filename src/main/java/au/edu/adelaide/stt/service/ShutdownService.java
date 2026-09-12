package au.edu.adelaide.stt.service;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ShutdownService {

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
            !shutdownStarted
                .compareAndSet(false, true)
        ) {

            return false;
        }


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
                    }

                    applicationContext.close();

                });


        shutdownThread.setName(
                "graceful-shutdown"
        );

        shutdownThread.start();

        return true;
    }
}