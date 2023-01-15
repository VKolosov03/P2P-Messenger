package com.example.demo;

import com.example.peers.PeerManager;
import com.example.peers.ServerThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Клас для керування першим вікном додатку(вікном вводу даних)
 */
public class LogInController {
    private int photoIndex;
    private ServerThread serverThread;
    @FXML
    private Circle circle;

    @FXML
    private Pane errorMessage;

    @FXML
    private Text errorText;

    @FXML
    private TextField portInput;

    @FXML
    private TextField usernameInput;

    /**
     * Функція, що за індексом фото відтворює фото
     * @param index
     */
    public void showPhoto(int index) {
        try {
            this.photoIndex = index;
            Image im = new Image(Constants.photoArray[this.photoIndex]);
            circle.setFill(new ImagePattern(im));
        } catch(Exception e) {System.out.println(e.getMessage());}
    }

    /**
     * Функція, що реагує на натискання лівої стрілочки та змінює індекс фото
     * @param event
     */
    @FXML
    void clickLeft(ActionEvent event) {
        showPhoto(photoIndex<=1 ? 6 : photoIndex-1);
    }

    /**
     * Функція, що реагує на натискання правої стрілочки та змінює індекс фото
     * @param event
     */
    @FXML
    void clickRight(ActionEvent event) {
        showPhoto(photoIndex==6 ? 1 : photoIndex+1);
    }

    /**
     * Функція для підтвердження даних та передачі у інше вікно при натисканні клавіши
     * @param event
     * @throws Exception
     */
    @FXML
    void onLogInClick(ActionEvent event) throws Exception {
        portInput.setStyle("-fx-position-shape: absolute; -fx-pref-width: 165.71; -fx-pref-height: 21.43; -fx-background-color: #D9D9D9; -fx-background-radius: 15; -fx-font-family: 'Lato'; -fx-font-style: italic; -fx-font-weight: 500; -fx-alignment: center; -fx-text-inner-color: #6F7A86; -fx-prompt-text-fill: #6F7A86;");
        usernameInput.setStyle("-fx-position-shape: absolute; -fx-pref-width: 165.71; -fx-pref-height: 21.43; -fx-background-color: #D9D9D9; -fx-background-radius: 15; -fx-font-family: 'Lato'; -fx-font-style: italic; -fx-font-weight: 500; -fx-alignment: center; -fx-text-inner-color: #6F7A86; -fx-prompt-text-fill: #6F7A86;");
        if(!checkMistakes()) return;

        FXMLLoader fxmlLoader = new FXMLLoader(LogInController.class.getResource("messenger-view.fxml"));
        MessengerController messengerController = new MessengerController();
        fxmlLoader.setController(messengerController);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 571.43);
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setTitle("P2P Messenger");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        messengerController.change(usernameInput.getText(),portInput.getText(), photoIndex, serverThread);
    }

    /**
     * Функція для перевірки всіх помилок при введенні даних
     * @return
     * @throws Exception
     */
    public boolean checkMistakes() throws Exception {
        errorMessage.setOpacity(1);
        if(portInput.getText().equals("") && usernameInput.getText().equals("")) {
            errorText.setText("Empty lines, please, try again");
            portInput.setStyle("-fx-position-shape: absolute; -fx-pref-width: 165.71; -fx-pref-height: 21.43; -fx-background-color: #D9D9D9; -fx-background-radius: 15; -fx-font-family: 'Lato'; -fx-font-style: italic; -fx-font-weight: 500; -fx-alignment: center; -fx-text-inner-color: #6F7A86; -fx-prompt-text-fill: red;");
            usernameInput.setStyle("-fx-position-shape: absolute; -fx-pref-width: 165.71; -fx-pref-height: 21.43; -fx-background-color: #D9D9D9; -fx-background-radius: 15; -fx-font-family: 'Lato'; -fx-font-style: italic; -fx-font-weight: 500; -fx-alignment: center; -fx-text-inner-color: #6F7A86; -fx-prompt-text-fill: red;");
            return false;
        }
        else if(portInput.getText().equals("") || usernameInput.getText().equals("")) {
            if(portInput.getText().equals("")) {
                errorText.setText("Empty line port, please, try again");
                portInput.setStyle("-fx-position-shape: absolute; -fx-pref-width: 165.71; -fx-pref-height: 21.43; -fx-background-color: #D9D9D9; -fx-background-radius: 15; -fx-font-family: 'Lato'; -fx-font-style: italic; -fx-font-weight: 500; -fx-alignment: center; -fx-text-inner-color: #6F7A86; -fx-prompt-text-fill: red;");
            }
            else{
                errorText.setText("Empty line username, please, try again");
                usernameInput.setStyle("-fx-position-shape: absolute; -fx-pref-width: 165.71; -fx-pref-height: 21.43; -fx-background-color: #D9D9D9; -fx-background-radius: 15; -fx-font-family: 'Lato'; -fx-font-style: italic; -fx-font-weight: 500; -fx-alignment: center; -fx-text-inner-color: #6F7A86; -fx-prompt-text-fill: red;");
            }
            return false;
        }
        for(int i=0; i<portInput.getText().length(); i++){
            if(!Character.isDigit(portInput.getText().charAt(i))) {
                return makePortError();
            }
        }
        if(usernameInput.getText().length()>13) {
            errorText.setText("Maximum 13 digits in the username, try again");
            return makeUsernameError();
        }
        if(portInput.getText().length()>4) {
            errorText.setText("Maximum 4 digits in the port, try again");
            return makePortError();
        }
        serverThread = PeerManager.logIn(portInput.getText());
        if (serverThread==null) return makePortError();
        return true;
    }

    /**
     * Функція, що висвічує поле, що описує помилку при введенні порта
     * @return
     */
    public boolean makePortError() {
        portInput.setText("");
        portInput.setStyle("-fx-position-shape: absolute; -fx-pref-width: 165.71; -fx-pref-height: 21.43; -fx-background-color: #D9D9D9; -fx-background-radius: 15; -fx-font-family: 'Lato'; -fx-font-style: italic; -fx-font-weight: 500; -fx-alignment: center; -fx-text-inner-color: #6F7A86; -fx-prompt-text-fill: red;");
        return false;
    }

    /**
     * Функція, що висвічує поле, що описує помилку при введенні юзернейму
     * @return
     */
    public boolean makeUsernameError() {
        usernameInput.setText("");
        usernameInput.setStyle("-fx-position-shape: absolute; -fx-pref-width: 165.71; -fx-pref-height: 21.43; -fx-background-color: #D9D9D9; -fx-background-radius: 15; -fx-font-family: 'Lato'; -fx-font-style: italic; -fx-font-weight: 500; -fx-alignment: center; -fx-text-inner-color: #6F7A86; -fx-prompt-text-fill: red;");
        return false;
    }

}
