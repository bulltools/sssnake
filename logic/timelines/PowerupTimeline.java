package logic.timelines;

import logic.TimelineHandler;
import logic.UIController;
import logic.GameEngine;
import logic.Snake;

public class PowerupTimeline extends TimelineHandler {
    public PowerupTimeline(Snake snake, UIController ui, int snakeSpeed, int size, int cellSize, GameEngine gameEngine) {
        super(snake, ui, snakeSpeed, size, cellSize, gameEngine);
        // Additional initialization for power-up timeline
    }

    // Additional methods specific to the power-up timeline
    public void handlePowerup() {
        // Implementation for handling power-up
    }
}
