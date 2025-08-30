/**
 * Nii Oye Kpakpo
 * Section 3
 * 11/18/2024
 *
 *
 */

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends Rectangle {
    public static final int BRICK_WIDTH = 35;
    public static final int BRICK_HEIGHT = 20;
    private int pointValue;
    private Color color;

    // create brick at given location
    public Brick(int xLoc, int yLoc, int pointValue, Color color){
        super(BRICK_WIDTH, BRICK_HEIGHT);
        setX(xLoc);
        setY(yLoc);
        this.color = color;
        this.setFill(color);
        this.setPointValue(pointValue);

    }


    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }
}

