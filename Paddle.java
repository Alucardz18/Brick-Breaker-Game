/**
 * Nii Oye Kpakpo
 * Section 3
 * 11/18/2024
 *
 *
 */

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {
    public static final int BASE_Y = 540;
    public static final int PADDLE_WIDTH = 70;
    public static final int PADDLE_HEIGHT = 10;
    private int paHeight;
    private int paWidth;

    public Paddle(int paWidth, int paHeight){
        this.paHeight = paHeight;
        this.paWidth = paWidth;

        setWidth(PADDLE_WIDTH);
        setHeight(PADDLE_HEIGHT);

        setX((double) (paWidth - PADDLE_WIDTH) /2);
        setY(BASE_Y);
        this.setFill(Color.TURQUOISE);

    }

    public void move(double xLoc, double areaWidth){
        double newX = xLoc - PADDLE_WIDTH / 2;
        if (newX < 0) {
            newX = 0; // Keep the paddle on the left side
        } else if (newX + PADDLE_WIDTH > areaWidth) {
            newX = areaWidth - PADDLE_WIDTH; // Keep the paddle on the right side
        }

        // Update the paddle's position
        setX(newX);
    }

}

