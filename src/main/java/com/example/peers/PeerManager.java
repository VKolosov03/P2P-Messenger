package com.example.peers;

import com.example.demo.MessengerController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Клас, для створення зв'язків між користувачами
 */
public class PeerManager {

    /**
     * Функція для передачі даних користувача в клас, що створює сокет сервера
     * @param port
     * @return
     */
    public static ServerThread logIn(String port){
        try {
            ServerThread serverThread = new ServerThread(port);
            serverThread.start();
            return serverThread;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Функція для створення клієнтського сокету з портами, що користувач додавав
     * @param port
     * @param controller
     * @return
     * @throws IOException
     */
    public static boolean addFriendPort(String port, MessengerController controller) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), Integer.parseInt(port));
            (new PeerThread(socket, controller)).start();
            return true;
        } catch (Exception e) {
            if (socket != null) socket.close();
            return false;
        }
    }
}

