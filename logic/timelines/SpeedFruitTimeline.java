package logic.timelines;

import logic.TimelineHandler;
import logic.UIController;
import javafx.animation.Timeline;
import logic.GameEngine;
import logic.Snake;

public class SpeedFruitTimeline extends TimelineHandler {
    private Timeline speedFruitTimeline;


    public SpeedFruitTimeline(Snake snake, UIController ui, int snakeSpeed, int size, int cellSize, GameEngine gameEngine) {
        super(snake, ui, snakeSpeed, size, cellSize, gameEngine);
        // Additional initialization for speed fruit timeline
    }

    // Additional methods specific to the speed fruit timeline
    public void handleSpeedFruit() {
        // Implementation for handling speed fruit
    }
}
