/**
 * Nii Oye Kpakpo
 * Section 3
 * 11/11/2024
 *
 *Handles the user interface for selecting or creating player profiles.
 */


import edu.ncat.brickbreakerbackend.BrickBreakerIO;
import edu.ncat.brickbreakerbackend.GameProfiles;
import edu.ncat.brickbreakerbackend.Level;
import edu.ncat.brickbreakerbackend.PlayerProfile;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import static edu.ncat.brickbreakerbackend.BrickBreakerIO.readProfiles;


public class ProfilePane extends VBox {
    private String profileFileName;
    private String configFileName;
    private CheckBox checknewPlay;  // New player checkbox
    private CheckBox checkoldPlay;  // Registered player checkbox
    private TextField playerName;   // Area for name input
    private Button btnCreate;       // Create new player button
    private Button btnSearch;       // Search for existing player
    private Label label;            // Display messages
    private ComboBox<String> registerdPlay;  // List of registered players
    private VBox newPlayers;
    private VBox existingPlayers;
    private GameProfiles profiles;
    private PlayerProfile playerProfile;
    private Level[] levels;


    public ProfilePane(String profileFileName, String configFileName, GameProfiles profiles){
        this.profileFileName = profileFileName;
        this.configFileName = configFileName;

        // Initialize the profiles object
        this.profiles = profiles;
        readProfiles(profiles, profileFileName);

        checkoldPlay = new CheckBox("Current Player");
        checknewPlay = new CheckBox("Register New player");
        playerName = new TextField();
        playerName.setPromptText("Enter your player name");
        btnSearch = new Button("Search");
        btnCreate = new Button("Create");
        label = new Label();
        registerdPlay = new ComboBox<>();

        // Add the appropriate features to their respective VBox
        newPlayers = new VBox(10, checknewPlay, playerName, btnCreate);
        existingPlayers = new VBox(10, checkoldPlay, registerdPlay, btnSearch);

        // Create an invisible spacer
        Region spacer = new Region();
        spacer.setPrefHeight(20);

        // Background color
        setBackground(new Background(new BackgroundFill(
                new LinearGradient(1, 1, 0, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.DARKBLUE), new Stop(1, Color.LIGHTBLUE)),
                null, null)));

        // Fonts
        label.setFont(Font.font("Georgia", 15));
        btnSearch.setFont(Font.font("Agency FB", 15));
        btnCreate.setFont(Font.font("Agency FB", 15));
        checknewPlay.setFont(Font.font("Aptos", 15));
        checkoldPlay.setFont(Font.font("Aptos", 15));
        registerdPlay.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px;");


        // Add everything to the main VBox
        getChildren().addAll(newPlayers, spacer, existingPlayers, label);

        // Default visibility
        checknewPlay.setSelected(false);
        checkoldPlay.setSelected(false);
        registerdPlay.setDisable(false);

        // Listeners and Actions to perform
        existingCreatedPlayers();
        newCreatedPlayers();
        checkListen();
        mouseHover();

        // event handlers
        btnCreate.setOnAction(actionEvent -> createBtn());
        btnSearch.setOnAction(actionEvent -> searchBtn());

        // Padding
        setPadding(new Insets(5));
        checknewPlay.setPadding(new Insets(0,0,0,10));
        checkoldPlay.setPadding(new Insets(0,0,0,10));
        newPlayers.setSpacing(10);
        existingPlayers.setSpacing(10);
    }

    // listener for new player checkbox
    private void newCreatedPlayers(){
        // Activate and deactivate when selected
        if(checknewPlay.isSelected()){
            playerName.setDisable(false);
            btnCreate.setDisable(false);

            // Disable the content of the "Current Player" VBox
            checkoldPlay.setDisable(false);  // Keep the checkbox enabled
            registerdPlay.setDisable(true);
            btnSearch.setDisable(true);
        }
        else{
            playerName.setDisable(true);
            btnCreate.setDisable(true);

            // Enable the content of the "Current Player" VBox
            checkoldPlay.setDisable(false);
        }
    }

    // listener for registered player checkbox
    private void existingCreatedPlayers() {
        // Activate and deactivate when selected
        if (checkoldPlay.isSelected()) {
            // Enable the content of the "Current Player" VBox
            playerName.setDisable(false);
            registerdPlay.setDisable(false);
            btnSearch.setDisable(false);

            registerdPlay.getItems().clear();
            for(PlayerProfile profile : profiles.getProfiles()){
                registerdPlay.getItems().add(profile.getName());
            }

            // Disable the content of the "Register New player" VBox
            checknewPlay.setDisable(false);  // Keep the checkbox enabled
            playerName.setDisable(true);
            btnCreate.setDisable(true);
        } else {
            // Disable the content of the "Current Player" VBox
            playerName.setDisable(true);
            registerdPlay.setDisable(true);
            btnSearch.setDisable(true);

            // Enable the content of the "Register New player" VBox
            checknewPlay.setDisable(false);  // Keep the checkbox enabled
        }
    }

    //create button
    private void createBtn() {
        String newPlayerName = playerName.getText().trim();
        if (newPlayerName.isEmpty()) {
            label.setText("* Player name required");
        } else {
            // Check if player already exists
            for (PlayerProfile profile : profiles.getProfiles()) {
                if (profile.getName().equals(newPlayerName)) {
                    label.setText(newPlayerName + "'s Player name already exists! \nTry again");
                    return;
                }
            }

            // Add new name to player profile
            PlayerProfile newPlayers = new PlayerProfile(newPlayerName);
            profiles.addPlayerProfile(newPlayers);
            // Check if player was successfully added
            if (profiles.getProfiles().contains(newPlayers)) {
                label.setText("Successfully created " + newPlayerName);
            }

            if (listener != null) {
                listener.onProfileSelected(newPlayers);
            }
            // Write the updated profiles to the file
            BrickBreakerIO.writeProfiles(profiles, profileFileName);
        }
    }

    // search button
    private void searchBtn(){
        String existPlayName = "";

        // If "Current Player" checkbox is selected, use ComboBox selection
        if (checkoldPlay.isSelected()) {
            existPlayName = registerdPlay.getValue();  // Get selected player from ComboBox
        } else {
            existPlayName = playerName.getText().trim();  // Otherwise, use the text field
        }

        if(existPlayName.isEmpty()){
            label.setText("Please choose player name ");
        }
        else {
            boolean found = false;
            for (PlayerProfile profile : profiles.getProfiles()){
                if (profile.getName().equals(existPlayName)){
                    label.setText("Selected Player: " + existPlayName);
                    found = true;
                    profiles.setSelectedProfile(profile);

                    if (listener != null) {
                        listener.onProfileSelected(profile);
                    }
                    break;
                }
            }
            if(!found){
                label.setText("Player doesn't exist");
            }
        }
    }

    // Mouse hover effect
    private void mouseHover(){
        newPlayers.setOnMouseEntered(event -> {
            newPlayers.setBackground(new Background(new BackgroundFill(
                    new LinearGradient(1, 1, 0, 0, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.DARKBLUE), new Stop(1, Color.LIGHTBLUE)),
                    new CornerRadii(10), null)));
        });

        newPlayers.setOnMouseExited(event -> {
            if (!checknewPlay.isSelected()) {  // Only reset to transparent if not selected
                newPlayers.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
            }
        });

        existingPlayers.setOnMouseEntered(event -> {
            existingPlayers.setBackground(new Background(new BackgroundFill(
                    new LinearGradient(1, 1, 0, 0, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.DARKBLUE), new Stop(1, Color.LIGHTBLUE)),
                    new CornerRadii(10), null)));
        });

        existingPlayers.setOnMouseExited(event -> {
            if (!checkoldPlay.isSelected()) {  // Only reset to transparent if not selected
                existingPlayers.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
            }
        });
    }

    // listeners for checkboxes
    private void checkListen(){
        checknewPlay.setOnAction(actionEvent -> {
            if (checknewPlay.isSelected()) {
                // Deselect the other checkbox
                checkoldPlay.setSelected(false);
            }
            newCreatedPlayers();
        });
        checkoldPlay.setOnAction(actionEvent -> {
            if (checkoldPlay.isSelected()) {
                // Deselect the other checkbox
                checknewPlay.setSelected(false);

            }
            existingCreatedPlayers();
        });
    }

    // Declare a listener for profile selection
    public interface ProfileSelectedListener {
        void onProfileSelected(PlayerProfile selectedProfile);
    }

    private ProfileSelectedListener listener;

    // Setter for the listener
    public void setProfileSelectedListener(ProfileSelectedListener listener) {
        this.listener = listener;
    }


}


