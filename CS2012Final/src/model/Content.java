package model;
/**
 Objects of objects in the room, such as neutral creatures, bosses, etc.
 */
public class Content extends AbstractEntity{
    // A description of the objects in the room
    private final String desc;

    public Content(String img, String d) {
        super(img);
        desc = d;
        // Object type
    }

    @Override
    public String getDescription() {
        return desc;
    }
}
