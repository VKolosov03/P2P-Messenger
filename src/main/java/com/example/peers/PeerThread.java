package com.example.peers;

import com.example.demo.MessengerController;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Клас, що реалізує клієнтські сокети
 */
public class PeerThread extends Thread{
    private BufferedReader bufferedReader;
    private final MessengerController messengerController;
    PeerThread(Socket socket, MessengerController controller) throws IOException {
        bufferedReader =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
        messengerController=controller;
    }

    /**
     * Функція, що одержує та обробляє повідомлення від клієнта-користувача сервер-користувачу і направляє їх в конструктор класу вікна(месенджер-чату)
     */
    @Override
    public void run() {
        boolean flag =true;
        while(flag){
            try{
                JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
                if(jsonObject.containsKey("username")) {
                    messengerController.createOthersMessage(jsonObject.getString("username"), jsonObject.getString("message"), jsonObject.getString("port"), jsonObject.getString("avatar"));
                }
            } catch (Exception e) {
                flag = false;
                interrupt();
            }
        }
    }
}

