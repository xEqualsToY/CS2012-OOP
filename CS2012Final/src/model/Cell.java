
package model;

/**
 * Room object, each room has a storage object, or it can be null
 */
public class Cell extends AbstractEntity {
    // Other entity objects
    private Content content;

    
    public Cell(String img) {
        super(img);
    }

    /**
 Get the objects in the room
     */
    public Content getContent() {
        return content;
    }

    /**
      Set the objects in the room
     Set the objects in the room
     */
    public void setContent(Content content) {
        this.setImg(content.getImg());
        this.content = content;
    }

    /**
     Get the description information of the creature in the current room
     */
    @Override
    public String getDescription() {
        return content == null ? "" : content.getDescription();
    }
}
