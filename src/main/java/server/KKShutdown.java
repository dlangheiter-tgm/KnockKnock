package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KKShutdown implements Runnable {
    private Shutdown shutdown;

    KKShutdown(Shutdown shutdown) {
        this.shutdown = shutdown;
    }

    @Override
    public void run() {
        try (BufferedReader stdIn =
                     new BufferedReader(new InputStreamReader(System.in))) {
            while(!stdIn.readLine().equalsIgnoreCase("!stop")) {
                System.out.println("Unrecognised command.");
            }
            shutdown.shutdown();
        } catch (IOException e) {
            System.err.println("Could not read stdIn");
        }
    }
}
