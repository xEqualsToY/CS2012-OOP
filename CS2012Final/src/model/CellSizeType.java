package model;
/**
 * description: There are three types of enumeration objects of room size: 5*5 7*7 10*7
 */
public enum CellSizeType {
    TINY(5, 5), MID(7, 7), LARGE(10, 7);
    // Grid height
    private final int height;
    // Grid width
    private final int width;
    CellSizeType(int height, int width) {
        this.height = height;
        this.width = width;
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }
    @Override
    public String toString() {
        switch (this) {
            case MID:
                return "MID" + " (7 * 7)";
            case TINY:
                return "TINY" + " (5 * 5)";
            default:
                return "LARGE" + " (10 * 7)";
        }
    }
}
