
package model;

/**
 
 * The player object, which stores the player bullet alive state
 */
public class Player extends AbstractEntity {
    // Number of bullets
    private int bullet;
    // Whether to survive
    private boolean alive;


    public Player(String img) {
        super(img);
        // Initial three bullets
        bullet = 3;
        alive = true;
    }

    /**
      Get the picture path of the current room
     */
    @Override
    public String getImg() {
        return img;
    }

    /**
     Player shooting
     */
    public boolean shoot() {
        if (bullet == 0)
            return false;
        bullet--;
        return true;
    }

    /**
     The bullet is incremented. When the bullet item is obtained, it will automatically increase by one.
     */
    public void bulletIncr() {
        bullet++;
    }

    /**
      boolean true means the player survives
     
     */
    public boolean isAlive() {
        return alive;
    }

    /**
      Mark the player status as dead
     */
    public void dead() {
        alive = false;
    }

    /**
     The number of bullets currently available to the player
     
     */
    public int getBullet() {
        return bullet;
    }

    /**
    boolean Returns whether the objects in the current room are visible
     true means visible
     */
    @Override
    public boolean isVisible() {
        return true;
    }


    /**
     Get the description information of the creature in the current room
     */
    @Override
    public String getDescription() {
        return "";
    }
}
