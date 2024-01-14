import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.GameControls;
import logic.GameEngine;
import logic.UIController;
import logic.Snake;
import logic.TimelineHandler;

public class App extends Application {
    // Grid configuration
    GridPane grid = new GridPane();
    final int size = 16;
    final int midPoint = (size / 2) - 1;
    final int cellSize = 35;
    Scene scene;

    // Snake configuration
    int snakeLength = 2;
    int snakeSpeed = 120;
    Timeline timeline;
    int score = 0;
    private Snake snake;

    public static final Color COLOR = Color.RED;
    private UIController ui;
    private GameEngine gameEngine;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        snake = new Snake(midPoint, midPoint);
        GameControls gameControls = new GameControls(snake);
        // Initialize UI controller
        ui = new UIController(primaryStage, snake, gameControls, grid);
        scene = ui.getScene();
        gameEngine = new GameEngine(snake, ui, grid);
        TimelineHandler timeline = new TimelineHandler(snake, ui, snakeSpeed, size, cellSize);

        // Set the references
        gameEngine.setTimelineHandler(timeline);
        timeline.setGameEngine(gameEngine);
        
        // Initialize controls
        setupKeyHandlers(gameControls);
        ui.getScene().setOnKeyPressed(event -> gameControls.handleKeyPress(event.getCode()));

        // Set up the stage and menu
        primaryStage.show();
        ui.getMenuButton();

        // Set and start timelines
        timeline.setCycleCount();
        timeline.startTimeline();
        timeline.startPowerupTimeline();
        timeline.startSpeedFruitDelayTimeline();
    }

    // Method to handle key events
    private void setupKeyHandlers(GameControls gameControls) {
        this.scene.setOnKeyPressed(event -> {
            gameControls.handleKeyPress(event.getCode());
        });
    }

}