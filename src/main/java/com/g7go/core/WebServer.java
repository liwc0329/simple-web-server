package com.g7go.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Web
 *
 * @author Mr_Lee
 */
public class WebServer {
    private ServerSocket server;
    private ExecutorService threadPool;

    public WebServer() throws Exception {
        try {
            server = new ServerSocket(8088);
            threadPool = Executors.newFixedThreadPool(100);
        } catch (Exception e) {
            throw e;
        }
    }

    public void start() {
        try {
            while (true) {
                System.out.println("ȴ...");
                Socket socket = server.accept();
                System.out.println("һͻ!");
                ClientHandler handler
                        = new ClientHandler(socket);

                threadPool.execute(handler);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            WebServer server = new WebServer();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ʧ!");
        }
    }
}








