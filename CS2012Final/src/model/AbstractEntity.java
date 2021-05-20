
package model;

/**
 The entity abstract class, which implements the public properties and methods of the entity class
 */
public abstract class AbstractEntity implements Entity {
    //image
    protected String img;
    // Is it visible
    protected boolean visible;

    /**
     Initialize the room image
     */
    public AbstractEntity(String img) {
        this.img = img;
        // Not visible by default
        visible = false;
    }

    /**
    Get the picture path of the current room
     */
    @Override
    public String getImg() {
        return img;
    }

    /**
     Set the current holiday picture
     */
    public void setImg(String img) {
        this.img = img;
    }


    /**
    true means visible
     */
    @Override
    public boolean isVisible() {
        return visible;
    }


    /**
     Set whether objects in the room are visible
     */
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
