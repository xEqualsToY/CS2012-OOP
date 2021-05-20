
package view;


import controller.Controller;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.CellSizeType;
import model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 GameView.java
 
 * Game main view class, defines the game room grid,
 * displays prompt labels and reads the room size selected by the user
 */
public class GameView extends HBox implements View {
    // controller
    private final Controller controller;
    // The view that needs to be updated
    private final List<View> views;
    // Room grid
    private TilePane cellPane;
    // Grid height
    private int height;
    // Grid width
    private int width;
    // Optional size list
    private final ChoiceBox<CellSizeType> choiceBox;
    // Information provided by the room around the player
    private final Label tips;
    // Display the current number of bullets remaining in the player
    private final Label bulletLabel;
    // Font size of the text
    private static final Font font = Font.font(20);

    /**
     controller The controller trying to render
     *Initialize the most basic view elements and let the player choose the required grid size
     */
    public GameView(Controller controller) {
        this.controller = controller;
        views = new ArrayList<>();
        choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(CellSizeType.values()));
        choiceBox.setOnAction(this::selectEvent);
        controller.setView(this);
        bulletLabel = new Label();
        this.getChildren().add(choiceBox);
        tips = new Label();
        tips.setFont(font);
        bulletLabel.setFont(font);
    }

    /**
     Choose debug mode or normal mode
     */
    private void modeSelect() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choose game mode");
        alert.setHeaderText("Do you need to turn on debug mode?");
        alert.setContentText("Do you need to turn on debug mode?");
        alert.showAndWait();
        controller.setMode(alert.getResult() == ButtonType.OK);
    }

    /**
     event Event handler for size selection
     Read the grid size selected by the user
     * Initialize the view layout, and then call the controller to initialize the grid
     */
    private void selectEvent(ActionEvent event) {
        getChildren().clear();
        CellSizeType item = choiceBox.getSelectionModel().getSelectedItem();
        modeSelect();
        controller.setSize(item);
        height = item.getHeight();
        width = item.getWidth();
        getChildren().remove(cellPane);
        VBox box = new VBox();
        box.getChildren().addAll(choiceBox);
        cellPane = new TilePane();
        cellPane.setPrefRows(height);
        cellPane.setPrefColumns(width);
        cellPane.setHgap(5);
        cellPane.setVgap(5);
        for (Entity[] cell : controller.getCells()) {
            for (Entity c : cell) {
                CellView e = new CellView(c);
                views.add(e);
                cellPane.getChildren().add(e);
            }
        }
        box.getChildren().addAll(cellPane, bulletLabel);
        this.getChildren().addAll(box);
        getChildren().add(tips);
        controller.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.W, false, false, false, false));
    }

    /**
   
     If the player dies or runs out of bullets, the game ends and prompts the user
     */
    private void overAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME OVER");
        alert.setHeaderText("You ran out of bullets or fell into a trap");
        alert.setContentText("Game over!");
        alert.showAndWait();
    }

    /**
     The player kills the monster and wins
     */
    private void win() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations");
        alert.setHeaderText("You have defeated the boss and successfully passed the level");
        alert.setContentText("Congratulations!");
        alert.showAndWait();
    }

    /**
     *boolean
     Determine if the game is over
     */
    private boolean checkGame() {
        boolean exit = false;
        if (controller.isOver()) {
            overAlert();
            exit = true;
        } else if (controller.isWin()) {
            win();
            exit = true;
        }
        return exit;
    }

    /**
     * Update the object of the view
     */
    @Override
    public void update() {

        views.clear();
        cellPane.getChildren().clear();
        for (Entity[] cell : controller.getCells()) {
            for (Entity c : cell) {
                CellView e = new CellView(c);
                views.add(e);
                cellPane.getChildren().add(e);
            }
        }
        views.forEach(View::update);
        tips.setText(controller.getTips());
        bulletLabel.setText(String.format("Bullets: %d", controller.getPlayer().getBullet()));
        if (checkGame()) {
            for (Entity[] cell : controller.getCells()) {
                for (Entity c : cell) {
                    c.setVisible(true);
                }
            }
            views.forEach(View::update);
        }
    }
}
