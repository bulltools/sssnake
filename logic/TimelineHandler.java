package logic;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class TimelineHandler {
    private Snake snake;
    private UIController ui;
    private int snakeSpeed;
    private int size;
    private int cellSize;
    private GameEngine gameEngine;

    private Timeline mainTimeline;
    private Timeline powerupTimeline;
    private Timeline speedFruitDelayTimeline;

    public TimelineHandler(Snake snake, UIController ui, int snakeSpeed, int size, int cellSize) {
        this.snake = snake;
        this.ui = ui;
        this.snakeSpeed = snakeSpeed;
        this.size = size;
        this.cellSize = cellSize;
        initializeTimeline();
    }

    // Setter method for GameEngine
    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    private void initializeTimeline() {
        // Initialize and configure the timelines
        mainTimeline = new Timeline(new KeyFrame(Duration.millis(snakeSpeed), event -> {
            mainTimeline.setCycleCount(Timeline.INDEFINITE);
            if (gameEngine.snakeMoving) {
                snake.move();
                gameEngine.drawSnake();
                gameEngine.handleCollision();
            }
        }));
        powerupTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            if (!gameEngine.isFoodOnPowerupOrSnake()) {
                gameEngine.goldenFruit.randomizePosition(size);
            }
        }));
        speedFruitDelayTimeline = new Timeline(new KeyFrame(Duration.seconds(20), event -> {
            if (!gameEngine.isFoodOnPowerupOrSnake()) {
                gameEngine.generateNewSpeedFruit();
            }
        }));
    }

    // Getter methods
    public Timeline getMainTimeline() {
        return mainTimeline;
    }

    public Timeline getPowerupTimeline() {
        return powerupTimeline;
    }

    public Timeline getSpeedFruitDelayTimeline() {
        return speedFruitDelayTimeline;
    }

    // Method for setting the CyclCount of the main timeline
    public void setCycleCount() {
        mainTimeline.setCycleCount(Timeline.INDEFINITE);
        powerupTimeline.setCycleCount(Timeline.INDEFINITE);
        speedFruitDelayTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    // Methods to start timelines
    public void startTimeline() {
        mainTimeline.play();
    }

    public void startPowerupTimeline() {
        powerupTimeline.play();
    }

    public void startSpeedFruitDelayTimeline() {
        speedFruitDelayTimeline.play();
    }

    // Methods to pause timelines
    public void pauseTimeline() {
        mainTimeline.pause();
    }

    public void pausePowerupTimeline() {
        powerupTimeline.pause();
    }

    public void pauseSpeedFruitDelayTimeline() {
        speedFruitDelayTimeline.pause();
    }

    // Method to stop the main timeline
    public void stopTimeline() {
        mainTimeline.stop();
    }

    public void stopPowerupTimeline() {
        powerupTimeline.stop();
    }

    public void stopSpeedFruitDelayTimeline() {
        speedFruitDelayTimeline.stop();
    }

}