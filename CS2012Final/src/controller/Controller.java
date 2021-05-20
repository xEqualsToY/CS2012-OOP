package controller;

import javafx.event.EventHandler;

import javafx.scene.input.KeyEvent;
import model.CellSizeType;
import model.Entity;
import model.Player;
import view.View;

/**
 * Controller.java
 *
 * description:  Game controller interface, control the process of the game, print prompt information, etc.
 */
public interface Controller extends EventHandler<KeyEvent> {
    /**
     * @return model.Entity[][] Get all room information and render the content of each room
     *Get all room objects
     */
    Entity[][] getCells();

    /**
    
    There are three types of maps: 55 107 77
     */
    void setSize(CellSizeType type);

    /**
   view The main window view of the game
   Game view rendering engine
     */
    void setView(View view);

    /**
    java.lang.String Get prompt information provided by surrounding rooms
     Get prompt information for display on the window
     */
    String getTips();

    /**
    
      Get the player object
     */
    Player getPlayer();

    /**
     boolean Determine if the game is over
      Running out of bullets, being eaten by the boss,
     * or stepping on a trap will cause the player to die and the game will end
     */
    boolean isOver();

    /**
    boolean Have you won
     You will win when you kill the boss with bullets, The bullet can't pass through the room
     */
    boolean isWin();

    /**
    When true, it means it is in debug mode. At this time, all creatures in the room will be displayed.
     */
    void setMode(boolean mode);
}
