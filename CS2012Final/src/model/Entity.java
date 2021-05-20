package model;

/**
 Entity interface, which defines the behaviors that all physical objects need to have
 */
public interface Entity {
    /**
    Get the picture path of the current room
     */
    String getImg();

    /**
    Set the current holiday picture
     */
    void setImg(String img);

    /**
    Set whether objects in the room are visible
     */
    void setVisible(boolean visible);

    /**
     true means visible
     */
    boolean isVisible();

    /**
      Get the description information of the creature in the current room
     */
    String getDescription();
}
