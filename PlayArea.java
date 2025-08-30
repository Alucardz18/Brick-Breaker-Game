/**
 * Nii Oye Kpakpo
 * Section 3
 * 11/18/2024
 *
 * display your bricks, paddle and ball.
 * It is where the game action occurs
 */

import edu.ncat.brickbreakerbackend.BrickRow;
import edu.ncat.brickbreakerbackend.Level;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;

import java.util.Random;

public class PlayArea extends Pane {

    // Constants
    private static final int BASE_Y = 345; // Y-coordinate for the bottom of the play area where the paddle is fixed

    // Instance variables
    private int paHeight; // Height of the play area
    private int paWidth;  // Width of the play area
    private Brick[][] bricks; // 2D array of bricks

    private Paddle paddle; // Paddle object
    private Ball ball;
    private ScorePane scorePane;
    private Level level;

    private int missCounter = 0;
    private final int maxMisses = 3;
    private Label livesLabel;
    private Label levelLabel;


    // Constructor
    public PlayArea(int paHeight, int paWidth, Level level, ScorePane scorePane) {
        this.paHeight = paHeight;
        this.paWidth = paWidth;
        this.scorePane = scorePane;

        // Create the PlayArea (Pane size)
        this.setPrefSize(paWidth, paHeight);

        // Initialize the lives label
        livesLabel = new Label("Lives: " + (maxMisses - missCounter)); // Set initial lives
        livesLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: black;");
        livesLabel.setLayoutX(paWidth - 100);
        livesLabel.setLayoutY(-20);
        this.getChildren().add(livesLabel);

        // Create and position the level label
        levelLabel = new Label("Level: " + (level.getLevelNum() + 1));  // Show the level number
        levelLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        levelLabel.setLayoutX(paWidth / 2 - 50);  // Position it center of the top
        levelLabel.setLayoutY(-20);  // Position it near the top
        this.getChildren().add(levelLabel);  // Add the label to the play area

        // Create bricks and add them to the play area
        createBricks(level);

        // Create the paddle and add it to the play area
        setPaddle(new Paddle(paWidth, paHeight));
        this.getChildren().add(getPaddle());
    }

    /**
     * This method creates and populates the 2D array of bricks based on the level.
     * It adds the bricks to the PlayArea.
     * @param level the current level that contains brick configuration.
     */
    public void createBricks(Level level) {
        try{
        // Get the number of brick rows from the level
        int numRows = level.getNumBrickRows();

        // Initialize the 2D brick array
        bricks = new Brick[numRows][];

        // Loop through each row to create bricks
        for (int i = 0; i < numRows; i++) {
            BrickRow brickRow = level.getBrickRow(i); // Get the brick row for the current level

            // Initialize the array for the current brick row
            int numBricks = brickRow.getBrickMaskLength();
            bricks[i] = new Brick[numBricks];

            // Loop through each brick in the row and create the Brick objects
            for (int j = 0; j < numBricks; j++) {
                // Check if the brick is visible
                if (brickRow.getBrickMaskValue(j)) {
                    // Create a new brick with its color and point value
                    Brick brick = new Brick(j * 35, BASE_Y - (i * 20), brickRow.getPointValue(), brickRow.getColor());
                    bricks[i][j] = brick;
                    this.getChildren().add(brick); // Add the brick to the play area
                }
            }
        }
        }catch (NullPointerException e){
            System.out.println("Null");
        }
    }


    /**
     *
     * This method is called by the GameBoard to update the paddle's position based on mouse movement.
     * @param xLoc The x-coordinate of the paddle.
     */
    public void movePaddle(double xLoc) {
        getPaddle().move(xLoc, paWidth);
    }

    // Method to handle collisions with walls, paddle, or bricks
    public void handleCollisions() {
        checkWallCollisions();
        checkPaddleCollision();
        checkBrickCollision();

        if(checkHitAllBricks()){
            GameBoard gameBoard = (GameBoard)getScene().getRoot();
            gameBoard.moveToNextlevel();

            createBricks(level);
            newBall();
        }
    }

    // Method to move the ball
    public void moveBall() {
        if (getBall() != null ) {
            getBall().move();
            handleCollisions();  // Check for any collisions
        }
    }

    // Method to create a new ball, position it above the paddle, and give it a random direction
    public void newBall() {
        Random rand = new Random();
        double randomAngle = 180 + rand.nextDouble() * 100;  // Random angle

        // If a ball already exists, remove it before creating a new one
        if (getBall() != null) {
            getChildren().remove(getBall());
        }

        // Create the new ball positioned above the paddle
        setBall(new Ball( paWidth, paHeight, randomAngle));
        getBall().setCenterX(getPaddle().getX() + getPaddle().getWidth() / 2);  // Align ball with the paddle
        getBall().setCenterY(getPaddle().getY() - getBall().getRadius());  // Position ball just above paddle

        getChildren().add(ball);  // Add the ball to the PlayArea
        setBallVisibility(true);  // Make the ball visible

    }

    // Collisions with the walls
    private void checkWallCollisions() {
        if (ball.getCenterY() + ball.getRadius() >= this.getHeight()) {
            ball.setCenterY(this.getHeight() - ball.getRadius() - 1);
            ball.setDirection(360 - ball.getDirection());
        }

        if (ball.getCenterY() - ball.getRadius() <= 0) {
            ball.setCenterY(ball.getRadius() + 1);
            ball.setDirection(360 - ball.getDirection());
        }

        if (ball.getCenterX() + ball.getRadius() >= this.getWidth()) {
            ball.setCenterX(this.getWidth() - ball.getRadius() - 1);
            ball.setDirection(180 - ball.getDirection());
        }

        if (ball.getCenterX() - ball.getRadius() <= 0) {
            ball.setCenterX(ball.getRadius() + 1);
            ball.setDirection(180 - ball.getDirection());
        }
    }

    // Check if the ball collides with the paddle
    private void checkPaddleCollision() {
        // Check if the ball is coming down towards the paddle and not past it yet
        if (ball.getBottomEdge() >= paddle.getY()  && ball.getBottomEdge() <= paddle.getY() + paddle.getHeight() &&
                ball.getRightEdge() > paddle.getX() &&
                ball.getLeftEdge() < paddle.getX() + paddle.getWidth()) {

            // Handle the collision with the paddle
            handlePaddleCollision();

        } else if (ball.getTopEdge() > paddle.getY() + paddle.getHeight()) {
            // Ball is dead
            handleBallDead();
        }
    }

    // Handle the ball collision with the paddle
    private void handlePaddleCollision() {
        ball.setDirection(360 - ball.getDirection());

        double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
        double ballCenter = ball.getCenterX();

        double diff = (ballCenter - paddleCenter) / (paddle.getWidth() / 2); // where on the paddle the ball hit

        // Adjust the ball's direction based on where it hit the paddle
        ball.setDirection(ball.getDirection() + diff * 45);
    }

    // Handle when the ball is dead
    private void handleBallDead() {
        // Check if the game is over through GameBoard
        GameBoard gameBoard = (GameBoard)getScene().getRoot(); // GameBoard instance

        if (!gameBoard.isGameOver()) {  // Only count the miss if the game is not over
            missCounter++;  // Increment the miss counter
            if (missCounter >= maxMisses) {
                endGame();  // End the game after misses
            } else {
                setBallVisibility(false);
                newBall(); // Create a new ball
            }

            // Update the lives label
            livesLabel.setText("Lives: " + (maxMisses - missCounter));
        }
    }

    private void endGame() {
        GameBoard gameBoard = (GameBoard)getScene().getRoot();
        gameBoard.setGameOver(true);
        System.out.println("Game Over! You missed " + missCounter + " times.");
        ball.setVisible(false);
        missCounter = 0;

    }

    private void checkBrickCollision(){
        for (Brick[] value : bricks) {
            for (Brick brick : value) {
                if (brick != null && brick.isVisible() && ball.intersects(brick.getBoundsInLocal())) {
                    ball.setDirection(360 - ball.getDirection()); // Reflect the ball
                    brick.setVisible(false);  // Hide the brick
                    scorePane.incrementScore(brick.getPointValue()); // increase score
                    break;
                }
            }
        }
    }

    public boolean checkHitAllBricks(){
        for(Brick[] bricks1 : bricks){
            for(Brick brick : bricks1){
                if(brick != null && brick.isVisible()){
                    return false; // bricks still exist
                }
            }
        }
        return true; //all bricks cleared
    }

    // Method to set the visibility of the ball
    public void setBallVisibility(boolean visibility) {
        if (getBall() != null) {
            getBall().setVisible(visibility);
        }
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    // update level label
    public void updateLevelLabel(int levelNum) {
        levelLabel.setText("Level: " + levelNum);
    }
}


