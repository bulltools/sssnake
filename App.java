import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

import logic.GameControls;
import logic.GameEngine;
import logic.UIController;
import logic.Snake;
import logic.TimelineHandler;

public class App extends Application {
    // Game entities
    private Snake snake;
    private UIController ui;
    private TimelineHandler timelineHandler;
    private GameEngine gameEngine;
    private GameControls gameControls;

    public void start(Stage primaryStage) {
        // Initializing game entities
        GridPane grid = new GridPane();
        snake = new Snake();
        gameControls = new GameControls(snake);
        ui = new UIController(primaryStage, snake, gameControls, grid);
        gameEngine = new GameEngine(snake, ui, grid);
        timelineHandler = new TimelineHandler(snake, ui);
        // Set references between GameEngine and TimelineHandler
        ui.setDependencies(timelineHandler, gameEngine);
        gameEngine.setDependencies(timelineHandler, ui);
        timelineHandler.setDependencies(gameEngine, ui);
        // Setup the UI
        ui.setupKeyHandlers(gameControls);
        ui.getMenuButton();
        ui.getScene();
        // Start the game
        primaryStage.show();
        timelineHandler.setandStartTimelines();
    }
    public static void main(String[] args) {
        launch(args);
    }
}