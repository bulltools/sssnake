package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIController {
    private GridPane grid;
    private Scene scene;
    private Button btn;
    private Label scoreLabel, gameStatus;
    private Stage primaryStage, modalStage;
    private Timeline powerupTimeline;
    private Snake snake;
    private GameControls gameControls;
    private int existingHighScore;

    final int size = 16;
    final int midPoint = (size / 2) - 1;
    final int cellSize = 35;

    public boolean gamePaused;

    public UIController(Stage primaryStage, Snake snake, GameControls gameControls, GridPane grid) {
        this.grid = grid;
        // grid.setPrefSize(576, 576);

        this.primaryStage = primaryStage;
        this.snake = snake;
        this.gameControls = gameControls;
        createUI();
    }

    private void createUI() {
        setupMenuButton();
        setupGameWindow();
    }

    private void setupMenuButton() {
        btn = new Button("Menu");
        String menuButtonStyle = "-fx-background-color: red; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-family: 'Arial'; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 15px; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;";

        btn.setStyle(menuButtonStyle);
        btn.setPrefSize(80, 40);
        btn.setOnAction(event -> showModal(this.primaryStage));
    }

    public Button getMenuButton() {
        return btn;
    }

    private void setupGameWindow() {
        // Create controlBox
        HBox controlBox = new HBox();
        controlBox.setPrefSize(576, 54);
        controlBox.setStyle("-fx-background-color: #424242;");

        controlBox.setAlignment(Pos.CENTER);
        // Create scoreLabel
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 19px; -fx-font-weight: bold;");
        scoreLabel.setPadding(new Insets(0, 380, 0, 10));
        gameStatus = new Label("");
        gameStatus.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        gameStatus.setAlignment(Pos.CENTER); // Center align the gameStatus label
        gameStatus.setPadding(new Insets(0, 0, 0, 10));

        HBox.setHgrow(scoreLabel, Priority.ALWAYS);

        // Add scoreLabel and menu button to control box
        controlBox.getChildren().addAll(scoreLabel, btn);

        // Add both to a VBox
        VBox root = new VBox(controlBox, grid);

        // Set the scene and stage
        scene = new Scene(root, 576, 630);
        primaryStage.setScene(scene);
        scene.getRoot().requestFocus();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Snake Game");
    }

    public Scene getScene() {
        return scene;
    }
    
    /*
     * Methods for manipulating the UI
     */

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    private void saveScoreToFile(int score) {
        File file = new File("highscore.txt");

        try {
            // Create the file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }

            // Read the existing high score from the file
            existingHighScore = readHighScoreFromFile();

            // Check if the current score is higherw
            if (score > existingHighScore) {
                try (FileWriter writer = new FileWriter(file)) {
                    // Update the high score in the file
                    writer.write(String.valueOf(score));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void displayGameOver(int score) {

        GridPane.setValignment(gameStatus, VPos.CENTER);
        GridPane.setHalignment(gameStatus, HPos.CENTER);

        grid.add(gameStatus, 5, 4, midPoint, midPoint);
        saveScoreToFile(score);

        existingHighScore = readHighScoreFromFile();
        if (score > existingHighScore) {
            System.out.println(existingHighScore);
            gameStatus.setText("High score!" + "\n" + score);
            gameStatus.setStyle("-fx-text-fill: BLUE; -fx-font-size: 44px; -fx-font-weight: bold;");
        } 
        gameStatus.setText("Game Over" + "\n" + "Score: " + score);
        
        gameStatus.setStyle("-fx-text-fill: RED; -fx-font-size: 44px; -fx-font-weight: bold;");
        gameStatus.setPadding(new Insets(0, 0, 0, 0));
        gameStatus.setAlignment(Pos.CENTER);
    }


    private int readHighScoreFromFile() {
        int highScore = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                // Parse the existing high score from the file
                highScore = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return highScore;
    }

    // Modal
    public void showModal(Stage primaryStage) {
        // New window (Stage)
        modalStage = new Stage();

        gamePaused = true;
        // timeline.pause();

        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);
        modalStage.setTitle("Modal Window");

        modalStage.initStyle(StageStyle.TRANSPARENT);

        // Contents for the modal
        Button restartGame = new Button("Restart Game");
        Button returnToGame = new Button("Return to Game");
        Button exitGame = new Button("Exit Game");

        // Setting styles and width for buttons
        String buttonStyle = "-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Arial'; -fx-font-weight: bold;";
        restartGame.setStyle(buttonStyle);
        returnToGame.setStyle(buttonStyle);
        exitGame.setStyle(buttonStyle);

        double buttonWidth = 250;
        double buttonHeight = 70;
        restartGame.setPrefSize(buttonWidth, buttonHeight);
        returnToGame.setPrefSize(buttonWidth, buttonHeight);
        exitGame.setPrefSize(buttonWidth, buttonHeight);

        // Set action events for the buttons
        restartGame.setOnAction(event -> {
            // Code to restart the game
            modalStage.close();
            // restartGame();

            powerupTimeline.stop();
            powerupTimeline.play();

        });

        returnToGame.setOnAction(event -> {
            // Code to return to the game
            modalStage.close();
            resumeGame();
        });

        exitGame.setOnAction(event -> {
            Platform.exit(); // Lukker programmet
        });

        // Arrange buttons vertically
        VBox modalPane = new VBox();
        modalPane.getChildren().addAll(returnToGame, restartGame, exitGame);
        modalPane.setAlignment(Pos.CENTER);
        modalPane.setStyle(
                "-fx-background-color: #f3f3f3;" +
                        "-fx-border-style: solid;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: blue;");
        VBox.setMargin(restartGame, new Insets(10, 10, 10, 10));
        VBox.setMargin(returnToGame, new Insets(0, 10, 00, 10));
        VBox.setMargin(exitGame, new Insets(0, 10, 10, 10));

        Scene modalScene = new Scene(modalPane, 300, 300);

        modalStage.setScene(modalScene);

        modalStage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - modalScene.getWidth() / 2);
        modalStage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - modalScene.getHeight() / 2);

        modalStage.showAndWait();
    }

    private void resumeGame() {
        // Resume the game
        gamePaused = false;
        // timeline.play();
    }

    // private void restartGame() {
    // // Stop the current timeline
    // timeline.stop();
    // // Reset game state
    // snakeLength = 2;
    // snakeSpeed = 120; // Reset snake speed
    // score = 0;
    // snakeMoving = true;
    // gamePaused = false;

    // // Reset snake and food
    // snake = new Snake(midPoint, midPoint);
    // snake.setDirection(Direction.RIGHT); // Set initial direction
    // food = new Food(size);
    // generateNewFood();

    // // Clear the grid
    // grid.getChildren().clear();

    // // Create a new GameControls instance or update the existing one
    // GameControls gameControls = new GameControls(snake);

    // // Reset score label
    // scoreLabel.setText("Score: " + score);

    // // Start a new timeline
    // timeline = new Timeline(new KeyFrame(Duration.millis(snakeSpeed), event -> {
    // if (snakeMoving) {
    // snake.move();
    // drawSnake(grid);
    // checkCollision();
    // }
    // }));
    // timeline.setCycleCount(Timeline.INDEFINITE);
    // timeline.play();

    // // Update key event handlers
    // setupKeyHandlers(scene, gameControls);

    // Draw the initial state of the game
    // drawSnake(grid);
    // drawFood(grid, cellSize);
    // }

    public GridPane getGrid() {
        return grid;
    }
}
