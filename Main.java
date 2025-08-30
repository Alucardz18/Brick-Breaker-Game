/**
 * Nii Oye Kpakpo
 * Section 3
 * 11/18/2024
 */

import edu.ncat.brickbreakerbackend.BrickBreakerIO;
import edu.ncat.brickbreakerbackend.GameProfiles;
import edu.ncat.brickbreakerbackend.Level;
import edu.ncat.brickbreakerbackend.PlayerProfile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    private GameProfiles profiles;
    private PlayerProfile playerProfile;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        profiles = new GameProfiles();


        // Load the levels from configuration file
        Level[] levels = BrickBreakerIO.readConfigFile("brickbreaker.txt");

        if (levels.length > 0) {
            // Display ProfilePane
            ProfilePane profilePane = new ProfilePane("brickbreakerprofiles.txt", "brickbreaker.txt", profiles);
            profilePane.setProfileSelectedListener(this::onProfileSelected);  // Listen for profile selection

            Scene profileScene = new Scene(profilePane, 400, 400);
            primaryStage.setTitle("Player Selection");
            primaryStage.setScene(profileScene);
            primaryStage.show();
        } else {
            System.out.println("No levels found in the configuration file.");
        }
    }

    // Handle profile selection
    private void onProfileSelected(PlayerProfile selectedProfile) {
        // Create a new stage to display the profile details

        Stage profileStage = new Stage();
        profileStage.setTitle("Player Profile: " + selectedProfile.getName());

        if (selectedProfile == null) {
            Text label = new Text();
            label.setText("No valid profile selected.");
            return; // Exit early if profile is null
        }

        // Create a TextArea to display the selected profile
        TextArea profileTextArea = new TextArea();
        profileTextArea.setEditable(false);  // Disable editing
        profileTextArea.appendText("Name: " + selectedProfile.getName() + "\n");
        profileTextArea.appendText("High Score: " + selectedProfile.getHighScore() + "\n");
        profileTextArea.appendText("Games Played: " + selectedProfile.getNumGamesPlayed() + "\n");

        // Set up the profile scene and show the profile window
        Scene profileScene = new Scene(profileTextArea, 300, 250);
        profileStage.setScene(profileScene);
        profileStage.show();

        // When profile details are closed, transition to the GameBoard
        profileStage.setOnCloseRequest(event -> openGameBoard(selectedProfile));
    }


    // Transition to the GameBoard after profile is viewed
    private void openGameBoard(PlayerProfile selectedProfile) {
        // Load the levels
        Level[] levels = BrickBreakerIO.readConfigFile("brickbreaker.txt");

        if (levels.length > 0) {
            // Initialize the GameBoard with the selected profile
            GameBoard gameBoard = new GameBoard(levels, profiles, "brickbreakerprofiles.txt", selectedProfile);
            Scene gameScene = new Scene(gameBoard, 700, 600);
            primaryStage.setTitle("Brick Breaker Game" );
            primaryStage.setScene(gameScene);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
