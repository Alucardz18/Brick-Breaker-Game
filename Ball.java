/**
 * Nii Oye Kpakpo
 *
 *Handles the user interface for selecting or creating player profiles.
 */
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends Circle {

    // Ball properties
    private int speed;  // Speed of the ball
    private double direction;

    // Constructor: initialize the ball
    public Ball(int paWidth, int paHeight, double dir) {
        super(paWidth / 2, paHeight / 2, 10); // Set ball in the center of the play area, with a radius of 7
        setFill(Color.SEAGREEN); // Set color of the ball

        this.setSpeed(3);
        this.setDirection(dir); // Set the initial direction in degrees
    }


    // Move the ball based on its speed and direction
    public void move() {
        double radians = Math.toRadians(getDirection()); // Convert the direction to radians for JavaFX trigonometry
        double dx = getSpeed() * Math.cos(radians); // Change in x direction
        double dy = getSpeed() * Math.sin(radians); // Change in y direction

        // Update the ball's position
        setCenterX(getCenterX() + dx);
        setCenterY(getCenterY() + dy);
    }



    // Get the top edge of the ball
    public int getTopEdge() {
        return (int) (getCenterY() - getRadius());
    }

    // Get the bottom edge of the ball
    public int getBottomEdge() {
        return (int) (getCenterY() + getRadius());
    }

    // Get the left edge of the ball
    public int getLeftEdge() {
        return (int) (getCenterX() - getRadius());
    }

    // Get the right edge of the ball
    public int getRightEdge() {
        return (int) (getCenterX() + getRadius());
    }


    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }
}





