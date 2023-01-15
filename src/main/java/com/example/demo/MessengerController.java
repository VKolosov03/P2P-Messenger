package com.example.demo;

import com.example.peers.PeerManager;
import com.example.peers.ServerThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.json.Json;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Клас для керування другим вікном додатку(месенджер)
 */
public class MessengerController {
    private int photoIndex, portIndex, messageIndex, ownMessageIndex;
    private ServerThread serverThread;
    private ArrayList<String> portsArray;

    @FXML
    private TextField portText;
    @FXML
    private Text errorText;
    @FXML
    private Button portButton;
    @FXML
    private Rectangle avatar;

    @FXML
    private AnchorPane portsInfo;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField messageText;

    @FXML
    private Text port;

    @FXML
    private Text username;
    @FXML
    private AnchorPane messagePane;

    /**
     * Функція, що заміняє собою конструктор класу та ініціалізує параметри
     * @param nickname
     * @param portNumber
     * @param photoIndex
     * @param serverThread
     */
    public void change(String nickname, String portNumber, int photoIndex, ServerThread serverThread){
        this.serverThread=serverThread;
        username.setText(nickname);
        port.setText(portNumber);
        this.photoIndex =photoIndex;
        Image im = new Image(Constants.photoArray[this.photoIndex]);
        avatar.setFill(new ImagePattern(im));
        portIndex=0;
        messageIndex=0;
        ownMessageIndex=0;
        portsArray = new ArrayList<>();
        portsArray.add(port.getText());
        messagePane.setDisable(true);
        createLimit();
    }

    /**
     * Функція, що зчитує поле вводу, додає та підтверджує правильність введення порта користувачем
     * @param event
     * @throws IOException
     */
    @FXML
    void addPort(ActionEvent event) throws IOException {
        if(!portCheck()) {
            portText.setText("");
            return;
        }
        portsArray.add(portText.getText());
        Text friendsPort = new Text(portText.getText());
        friendsPort.setFont(new Font("Lato", 12));
        friendsPort.setStyle("-fx-font-style: normal; -fx-font-weight: 700;");
        friendsPort.setX(30);
        friendsPort.setY(80+(20*portIndex));
        friendsPort.setFill(Color.web("#B5B5B5"));
        portsInfo.getChildren().add(friendsPort);
        errorText.setLayoutY(errorText.getLayoutY()+20);
        portText.setLayoutY(portText.getLayoutY()+20);
        portButton.setLayoutY(portButton.getLayoutY()+20);
        portIndex++;
        portText.setText("");
    }

    /**
     * Функція, що перевіряє правильність введення порта друга користувачем
     * @return
     * @throws IOException
     */
    public boolean portCheck() throws IOException {
        errorText.setOpacity(1);
        if(portIndex==7) {
            errorText.setFill(Color.web("#B5B5B5"));
            errorText.setText("No more ports");
            return false;
        }
        if(portText.getText().equals("") || portsArray.contains(portText.getText())) {
            return false;
        }
        for(int i=0; i<portText.getText().length(); i++){
            if(!Character.isDigit(portText.getText().charAt(i))) {
                return false;
            }
        }
        if(portText.getText().length()>4) {
            return false;
        }
        if (!PeerManager.addFriendPort(portText.getText(), this)) {
            return false;
        }
        errorText.setOpacity(0);
        return true;
    }

    /**
     * Функція, що зчитує поле з повідомленням при натисканні кнопку та надсилає повідомлення користувачам
     * @param event
     */
    @FXML
    void sendMessage(ActionEvent event) {
        messageText.setPromptText("Message");
        if(messageText.getText().equals("")) return;
        if(messageText.getText().length()>120) {
            messageText.setText("");
            messageText.setPromptText("No more than 120 characters please");
            return;
        }
        StringWriter stringWriter = new StringWriter();
        Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                .add("username", username.getText())
                .add("message", messageText.getText())
                .add("port", port.getText())
                .add("avatar", String.valueOf(photoIndex))
                .build());
        serverThread.sendMessage(stringWriter.toString());
        createYourMessage();
        messageText.setText("");
    }

    /**
     * Функція, що при натисканні на клавішу Enter при вводі викликає ф-цію sendMessage
     * @param event
     */
    @FXML
    void sendWithEnter(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)) sendMessage(new ActionEvent());
    }

    /**
     * Функція, що відтворює повідомлення користувача на екрані користувача
     */
    public void createYourMessage() {
        Rectangle photo = new Rectangle();
        Text message = new Text(messageText.getText());
        Text user = new Text("Me");
        Text data = new Text(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm \n dd-MM-yyyy")));
        if(30+(70*((int)(messageIndex/4)+ownMessageIndex)+70)>messagePane.getHeight()){
            messagePane.setPrefHeight(messagePane.getHeight()+70);
        }

        photo.setHeight(22);
        photo.setWidth(22);
        Image im = new Image(Constants.photoArray[photoIndex]);
        photo.setFill(new ImagePattern(im));
        photo.setStyle("-fx-background-radius: 3px;");
        photo.setLayoutY(30+(70*((int)(messageIndex/4)+ownMessageIndex)));
        photo.setLayoutX(600);
        messagePane.getChildren().add(photo);

        user.setFont(new Font("Lato", 10));
        user.setStyle("-fx-font-style: normal; -fx-font-weight: 700;");
        user.setFill(Color.BLACK);
        user.setLayoutY(45+(70*((int)(messageIndex/4)+ownMessageIndex)));
        user.setLayoutX(575);
        messagePane.getChildren().add(user);

        message.setFont(new Font("Lato", 10));
        message.setStyle("-fx-font-style: normal; -fx-font-weight: 400;");
        message.setFill(Color.BLACK);
        message.setLayoutY(62+(70*((int)(messageIndex/4)+ownMessageIndex)));
        message.setLayoutX(325);
        message.setWrappingWidth(260);
        message.setTextAlignment(TextAlignment.RIGHT);
        messagePane.getChildren().add(message);

        data.setFont(new Font("Lato", 8));
        data.setStyle("-fx-font-style: normal; -fx-font-weight: 400;");
        data.setFill(Color.GREY);
        data.setLayoutY(62+(70*((int)(messageIndex/4)+ownMessageIndex)));
        data.setLayoutX(590);
        data.setTextAlignment(TextAlignment.CENTER);
        messagePane.getChildren().add(data);

        ownMessageIndex++;
    }

    /**
     * Функція, що відтворює отримане повідомлення на екран користувача
     * @param username
     * @param message
     * @param port
     * @param avatar
     */
    public void createOthersMessage(String username, String message, String port, String avatar) throws IOException {
        if(messageIndex>=200) logOut(new ActionEvent());
        if(30+(70*((int)(messageIndex/4)+ownMessageIndex)+70)>messagePane.getHeight()){
            messagePane.setPrefHeight(messagePane.getHeight()+70);
        }

        Image im = new Image(Constants.photoArray[Integer.parseInt(avatar)]);
        ((Rectangle) messagePane.getChildren().get(messageIndex)).setFill(new ImagePattern(im));
        ((Rectangle) messagePane.getChildren().get(messageIndex)).setLayoutY(30+(70*((int)(messageIndex/4)+ownMessageIndex)));
        ((Rectangle) messagePane.getChildren().get(messageIndex)).setOpacity(1);

        ((Text)messagePane.getChildren().get(messageIndex+1)).setText(username+" #"+port);
        ((Text)messagePane.getChildren().get(messageIndex+1)).setLayoutY(45+(70*((int)(messageIndex/4)+ownMessageIndex)));
        ((Text) messagePane.getChildren().get(messageIndex+1)).setOpacity(1);

        ((Text)messagePane.getChildren().get(messageIndex+2)).setText(message);
        ((Text)messagePane.getChildren().get(messageIndex+2)).setLayoutY(62+(70*((int)(messageIndex/4)+ownMessageIndex)));
        ((Text) messagePane.getChildren().get(messageIndex+2)).setOpacity(1);

        ((Text)messagePane.getChildren().get(messageIndex+3)).setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm \n dd-MM-yyyy")));
        ((Text)messagePane.getChildren().get(messageIndex+3)).setLayoutY(62+(70*((int)(messageIndex/4)+ownMessageIndex)));
        ((Text) messagePane.getChildren().get(messageIndex+3)).setOpacity(1);

        messageIndex=messageIndex+4;
    }

    /**
     * Функція, що створює елементи, що будуть відтворювати отримані повідомлення
     */
    public void createLimit() {
        for(int i=0; i<50; i++) {
            Rectangle photo = new Rectangle();
            Text message = new Text(messageText.getText());
            Text user = new Text();
            Text data = new Text();

            photo.setHeight(22);
            photo.setWidth(22);
            photo.setStyle("-fx-background-radius: 3px;");
            photo.setLayoutX(30);
            photo.setOpacity(0);
            messagePane.getChildren().add(photo);

            user.setFont(new Font("Lato", 10));
            user.setStyle("-fx-font-style: normal; -fx-font-weight: 700;");
            user.setFill(Color.BLACK);
            user.setLayoutX(60);
            photo.setOpacity(0);
            messagePane.getChildren().add(user);

            message.setFont(new Font("Lato", 10));
            message.setStyle("-fx-font-style: normal; -fx-font-weight: 400;");
            message.setFill(Color.BLACK);
            message.setLayoutX(66);
            message.setWrappingWidth(260);
            message.setTextAlignment(TextAlignment.LEFT);
            photo.setOpacity(0);
            messagePane.getChildren().add(message);

            data.setFont(new Font("Lato", 8));
            data.setStyle("-fx-font-style: normal; -fx-font-weight: 400;");
            data.setFill(Color.GREY);
            data.setLayoutX(20);
            data.setTextAlignment(TextAlignment.CENTER);
            photo.setOpacity(0);
            messagePane.getChildren().add(data);
        }
    }

    /**
     * Функція, що повертає користувача на перший екран при натисканні на кнопку повернення
     * @param event
     * @throws IOException
     */
    @FXML
    void logOut(ActionEvent event) throws IOException {
        serverThread.closeConnection();
        FXMLLoader fxmlLoader = new FXMLLoader(MessengerController.class.getResource("hello-view.fxml"));
        LogInController logInController = new LogInController();
        fxmlLoader.setController(logInController);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 571.43);
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setTitle("P2P Messenger");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        logInController.showPhoto(0);
    }
}
