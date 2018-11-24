package server;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KKServer {

    KKServer(int portNumber) {
        boolean listening = true;
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            System.out.println("Server started on localhost:" + portNumber);

            while(listening) {
                pool.execute(new KKThread(serverSocket.accept()));
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
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