package view;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.Entity;

/**
 CellView.java
 
 * description: Render the content of each room. When there is a creature, the picture is a creature,
 * and when it does not exist, it is a picture of the room.
 */
public class CellView extends StackPane implements View {
    // Objects in each room
    private final Entity cell;
    // image
    private Image image;
    // background
    private final ImageView bgView;
    // view
    private final ImageView view;
    // Show the room when there are no creatures or when it is not explored
    private static final String img = "/cell/house.png";

    /**
      cell Room object
      Construct the content that needs to be displayed in each room,
     * fix the size of the picture, and turn on automatic scaling
     */
    public CellView(Entity cell) {
        this.cell = cell;
        try {
        	image = new Image(CellView.class.getResourceAsStream(cell.getImg()));
        }catch(Exception e) {
        	System.out.println(cell.getImg());
        }
        
        bgView = new ImageView(image);
        // Long styles are 80
        bgView.setFitHeight(80);
        bgView.setFitWidth(80);
        bgView.setPreserveRatio(true);
        bgView.setSmooth(false);
        view = new ImageView();
        view.setFitHeight(80);
        view.setFitWidth(80);
        view.setPreserveRatio(true);
        view.setSmooth(false);
        this.getChildren().addAll(bgView, view);
    }

    /**
      Update the object of the view
     */
    @Override
    public void update() {
        if (cell.isVisible()) {
            view.setImage(new Image(cell.getImg()));
        } else {
            view.setImage(new Image(img));
        }
    }
}
