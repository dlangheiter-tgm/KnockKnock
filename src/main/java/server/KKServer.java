package server;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class KKServer implements Shutdown{
    private ExecutorService pool;
    private boolean listening = true;
    private int portNumber;

    KKServer(int portNumber) {
        this.portNumber = portNumber;
        this.pool = Executors.newFixedThreadPool(10);

        new Thread(new KKShutdown(this)).start();

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            System.out.println("Server started on localhost:" + portNumber);

            while(this.listening) {
                this.pool.execute(new KKThread(serverSocket.accept()));
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (RejectedExecutionException jee) {
            // Probably shutdown event. Ignore
        }
    }

    @Override
    public void shutdown() {
        System.out.println("Server shutdown");
        this.listening = false;
        this.pool.shutdown();
        try {
            new Socket("localhost", this.portNumber).close();
        } catch (IOException e) {
            // Ignore since we want to close it
        }
        this.pool.shutdownNow();
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        new KKServer(portNumber);
    }
}