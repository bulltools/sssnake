package logic.timelines;

import logic.TimelineHandler;
import logic.UIController;
import logic.Snake;

public class MainTimeline extends TimelineHandler {
    public MainTimeline(Snake snake, UIController ui, int snakeSpeed, int size, int cellSize) {
        super(snake, ui, snakeSpeed, size, cellSize);
    }
    
    // Additional methods specific to the power-up timeline
    public void handlePowerup() {
        // Implementation for handling power-up
    }
}