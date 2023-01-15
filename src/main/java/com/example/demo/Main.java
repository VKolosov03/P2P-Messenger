package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Клас для створення вікон додатку
 */
public class Main extends Application {
    /**
     * Створення та відкривання вікна програми
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        if(!checkConnection()){
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("error.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 150);
            primaryStage.setTitle("Error");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        LogInController logInController = new LogInController();
        fxmlLoader.setController(logInController);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 571.43);
        primaryStage.setTitle("P2P Messenger");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        logInController.showPhoto(0);
    }

    /**
     * Функція, що перевіряє чи має комп'ютер інтерет з'єднання
     * @return
     */
    public boolean checkConnection() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection connection = url.openConnection();
            connection.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {return false;}
    }
    public static void main(String[] args) {launch(args);}
}

