/**
 * Nii Oye Kpakpo
 * Section 3
 * 11/18/2024
 *
 *zone for the PlayArea (center zone), the ScorePane and the StatusPane
 */

import edu.ncat.brickbreakerbackend.GameProfiles;
import edu.ncat.brickbreakerbackend.Level;
import edu.ncat.brickbreakerbackend.PlayerProfile;
import edu.ncat.brickbreakerbackend.BrickBreakerIO;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.event.EventHandler;

public class GameBoard extends BorderPane {

    private static PlayArea playArea;
    private GameProfiles profiles;
    private PlayerProfile playerProfile;
    private int currentLevel;
    private Level[] levels;
    private String profilesFilename;
    private PaddleHandler paddleHandler;
    private ScorePane scorePane;

    private boolean ballActive = false;
    private boolean gameOver = false;
    private GameOver gamePane;
    private BrickBreakerIO breakerIO;



    // Constructor for GameBoard
    public GameBoard(Level[] levels, GameProfiles profiles, String profilesFilename, PlayerProfile selectedProfile) {
        this.levels = levels;
        this.profiles = profiles;
        this.profilesFilename = profilesFilename;
        this.currentLevel = 0;
        this.playerProfile = selectedProfile;
        breakerIO = new BrickBreakerIO();

        // Initialize the score pane
        scorePane = new ScorePane(playerProfile);

        // Initialize the PlayArea
        playArea = new PlayArea(700, 700, levels[currentLevel], scorePane);

        // Set the PlayArea in the center of the BorderPane
        this.setCenter(playArea);
        this.setTop(scorePane);


        // Initialize the PaddleHandler and register it to listen for mouse movement
        paddleHandler = new PaddleHandler();
        this.setOnMouseMoved(paddleHandler);

        // Set up the mouse event to launch the ball
        setOnMouseClicked(event -> {
            if (!ballActive) {  // Only launch a new ball if the current one is inactive
                playArea.newBall();
                ballActive = true;

            }
        });

        // Create and start the ball animation loop
        BallAnimation ballAnimation = new BallAnimation();
        ballAnimation.start();
    }

    // AnimationTimer to handle the ball animation and collisions
    private class BallAnimation extends AnimationTimer {
        @Override
        public void handle(long now) {
            if (ballActive && !gameOver) {
                // Move the ball
                playArea.moveBall();

            }
        }
    }

    // Inner class PaddleHandler to handle mouse movement events
    private static class PaddleHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            double mouseX = event.getX();
            playArea.movePaddle(mouseX);
        }
    }

    // Handle  game over
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        if (gameOver) {
            if (playerProfile != null) {
                System.out.println("High score: " + playerProfile.getHighScore());
            }

            System.out.println("Score: " + scorePane.getScore());
            if(scorePane.getScore() > playerProfile.getHighScore()){
                playerProfile.setHighScore(scorePane.getScore());
                System.out.println("New HighScore: " + playerProfile.getHighScore());
            }
            breakerIO.saveGame(profiles, playerProfile, playerProfile.getHighScore(), currentLevel);
            // Show the GameOver Pane
            showGameOverScreen();
        }
    }

    // Show the GameOverPane with the final score and high score
    private void showGameOverScreen() {
        int finalScore = scorePane.getScore();
        // Create the GameOver Pane and display it
        gamePane = new GameOver(playerProfile, finalScore, currentLevel);
        // Add the GameOverPane to the center of the BorderPane
        this.setCenter(gamePane);
    }


    // next level
    public void nextLevel(){
        if(currentLevel < levels.length - 1){
            currentLevel++;
            System.out.println("Level " + (currentLevel) + " cleared. next level");
            playArea.createBricks(levels[currentLevel]);
            // Update the level label in the PlayArea
            playArea.updateLevelLabel(levels[currentLevel].getLevelNum() + 1);
            ballActive = false;
            playArea.setBallVisibility(false);

        } else{
            if (!gameOver) {  // Check if game over flag is false
                System.out.println("Game over");
                gameOver = true;  // Set the gameOver flag to true
                setGameOver(true);
            }
        }
    }

    public void moveToNextlevel(){
        if(playArea.checkHitAllBricks()){
            nextLevel();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

}

