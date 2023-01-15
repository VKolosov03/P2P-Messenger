package com.example.peers;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

/**
 * Клас, що ініціалізує сервер на базі користувача
 */
public class ServerThread extends Thread{
    private ServerSocket serverSocket;
    private Set<ServerThreads> serverThreads = new HashSet<ServerThreads>();

    ServerThread(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.parseInt(port));
    }

    /**
     * Функція, що ініціалізує сервер
     */
    @Override
    public void run() {
        try {
            while(true){
                ServerThreads serverThreads2 = new ServerThreads(serverSocket.accept(), this);
                serverThreads.add(serverThreads2);
                serverThreads2.start();
            }
        } catch (IOException e) {}
    }

    /**
     * Функція, що робить запит з повідомленням до сервера
     * @param message
     */
    public void sendMessage(String message) {
        try{
            serverThreads.forEach(t->t.getPrintWriter().println(message));
        } catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Функція, що закриває порт сервера
     */
    public void closeConnection(){
        try {
            serverSocket.close();
        } catch (Exception ignored) {}
    }

    /**
     * Функція для виводу списку серверів
     * @return
     */
    public Set<ServerThreads> getServerThreads() {return serverThreads;}
}

