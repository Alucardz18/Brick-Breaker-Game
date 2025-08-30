/**
 * Nii Oye Kpakpo
 *
 * display Game Over screen that exists game
 */

import edu.ncat.brickbreakerbackend.PlayerProfile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameOver extends VBox {

    private PlayerProfile playerProfile;
    private int finalScore;
    private int highestLevel;

    public GameOver(PlayerProfile playerProfile, int finalScore, int highestLevel) {
        this.playerProfile = playerProfile;
        this.finalScore = finalScore;
        this.highestLevel = highestLevel;

        // Title
        Text title = new Text("Game Over");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        // Display player name, score, and highest level
        Text playerName = new Text("Player: " + playerProfile.getName());
        Text scoreText = new Text("Final Score: " + finalScore);
        Text highScoreText = new Text("High Score: " + playerProfile.getHighScore());
        Text levelText = new Text("Level: " + (1+highestLevel));

        // Create exit button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> closeGame());

        // Layout for the Game Over screen
        VBox layout = new VBox(25, title, playerName, scoreText, highScoreText, levelText, exitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefHeight(700);
        layout.setPrefWidth(700);
        layout.setStyle("-fx-background-color: gray; -fx-padding: 20px;");

        // Add layout to this pane
        this.getChildren().add(layout);
    }

    // Close the game
    private void closeGame() {
        System.out.println("Closing the game...");
        System.exit(0);  // Exit the game
    }
}

