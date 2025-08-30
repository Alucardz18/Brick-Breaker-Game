/**
 * Nii Oye Kpakpo
 * Section 3
 * 11/26/2024
 *
 * display player scores
 */

import edu.ncat.brickbreakerbackend.PlayerProfile;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ScorePane extends HBox {
    private int score;
    private Label lblScore;
    private PlayerProfile playerProfile;

    public ScorePane(PlayerProfile playerProfile){
        this.playerProfile = new PlayerProfile();
        this.setScore(getScore());

        lblScore = new Label("Score: " + score );
        lblScore.setStyle("-fx-font-size: 15px;");
        this.getChildren().add(lblScore);
    }

    public void incrementScore(int pts){
        this.score += pts;
        lblScore.setText("Score: " + score);
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
