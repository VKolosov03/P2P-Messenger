import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = Main.class.getClass().getResource("frontend/Controller.java");
        if(url!=null) System.out.println("Okay");
        Parent root = FXMLLoader.load(getClass().getResource("/frontend/front.fxml"));
        primaryStage.setTitle("P2P Messenger");
        primaryStage.setScene(new Scene(root, 1000, 571.43));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(String[] args) {launch(args);}
}
