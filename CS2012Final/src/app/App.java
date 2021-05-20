package app;

import controller.GameController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import view.GameView;
/**
 * 
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Adventure King");

        GameController controller = new GameController();
        GameView parent = new GameView(controller);
        Scene scene = new Scene(parent,1600,980);
        scene.addEventHandler(KeyEvent.KEY_PRESSED,controller);
        stage.setScene(scene);
        stage.show();
    }
}