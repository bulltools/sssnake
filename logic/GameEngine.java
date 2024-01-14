package logic;

import javafx.animation.KeyFrame;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameEngine {
    private Snake snake;
    private Food food;
    private UIController ui;
    private GridPane grid;

    java.awt.Point foodPosition;
    java.awt.Point goldenfruitposition;
    java.awt.Point speedFruitPosition;

    public GoldenFruitPowerup goldenFruit;
    public SpeedBoostPowerup speedFruit;

    Ellipse goldenfruitEllipse;
    Ellipse speedFruitEllipse;

    private boolean powerupVisible = true;
    private boolean isSpeedFruitEaten = false;
    private boolean isFoodOnPowerupOrSnake;

    public int score = 0;
    public int snakeLength = 2;
    public int snakeSpeed = 120;
    public boolean snakeMoving = true;

    // TODO: Move to separate class as constants
    final int size = 16;
    final int midPoint = (size / 2) - 1;
    final int cellSize = 35;

    public GameEngine(Snake snake, UIController ui, GridPane grid) {
        this.snake = snake;
        this.ui = ui;
        this.grid = grid;
        initializeFood();
        setTimelineHandler(timelineHandler);
    }

    private TimelineHandler timelineHandler;

    // Setter method for TimelineHandler
    public void setTimelineHandler(TimelineHandler timelineHandler) {
        this.timelineHandler = timelineHandler;
    }

    public void initializeFood() {
        food = new Food(size);
        goldenFruit = new GoldenFruitPowerup(size);
        speedFruit = new SpeedBoostPowerup(size);
    }

    public void drawSnake() {
        // Clear the previous state of the snake
        grid.getChildren().clear();

        // Draw the background grid
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setFill(Color.rgb(59, 196, 96));
                rect.setStroke(Color.rgb(44, 44, 44));
                rect.setEffect(new DropShadow(5, Color.BLACK));
                grid.add(rect, col, row);
            }
        }

        // Draw the food
        drawFood(grid);

        drawGoldenFruit(grid);

        drawSpeedFruit(grid);

        // Draw the snake with the updated state
        snake.draw(grid, cellSize);
        eatFood();
    }

    public void drawFood(GridPane grid) {
        Ellipse foodEllipse = new Ellipse(cellSize / 2.0, cellSize / 2.0);
        foodEllipse.setFill(Color.RED);

        // Get the position of the food from the Food object
        foodPosition = food.getPosition();
        java.awt.Point goldenFruitposition = goldenFruit.getPosition();

        // Add the food ellipse to the root at the appropriate position
        grid.add(foodEllipse, foodPosition.x, foodPosition.y);
    }

    private void generateNewGoldenfruit() {
        goldenFruit.randomizePosition(size);
        goldenfruitposition = goldenFruit.getPosition();
        drawGoldenFruit(ui.getGrid());
        increaseSnakeLength(3);
        snake.setSnakeSize(snakeLength);
    }

    public boolean isFoodOnPowerupOrSnake() {
        isFoodOnPowerupOrSnake = false;
        Snake.Point headPosition = snake.getHeadPosition();
        if (goldenfruitposition.x == foodPosition.x && goldenfruitposition.y == foodPosition.y
                || goldenfruitposition.equals(new java.awt.Point(headPosition.x, headPosition.y))) {
            isFoodOnPowerupOrSnake = true;
        } else {
            isFoodOnPowerupOrSnake = false;
        }

        for (Snake.Point bodyPart : snake.getBody()) {
            if (goldenfruitposition.equals(new java.awt.Point(bodyPart.x, bodyPart.y))) {
                isFoodOnPowerupOrSnake = true;
            }
        }
        return isFoodOnPowerupOrSnake;
    }

    private void drawGoldenFruit(GridPane grid) {
        goldenfruitEllipse = new Ellipse(cellSize / 2.0, cellSize / 2.0);
        goldenfruitEllipse.setFill(Color.YELLOW);

        // Set opacity based on powerup visibility
        goldenfruitEllipse.setOpacity(powerupVisible ? 1.0 : 0.0);

        // Get the position of the powerups from the Powerups object
        goldenfruitposition = goldenFruit.getPosition();

        // Add the powerups ellipse to the root at the appropriate position
        grid.add(goldenfruitEllipse, goldenfruitposition.x, goldenfruitposition.y);
    }

    private void drawSpeedFruit(GridPane grid) {
        if (!isSpeedFruitEaten) {
            speedFruitEllipse = new Ellipse(cellSize / 2.0, cellSize / 2.0);
            speedFruitEllipse.setFill(Color.BLACK);

            // Set opacity based on powerup visibility
            speedFruitEllipse.setOpacity(powerupVisible ? 1.0 : 0.0);

            // Get the position of the powerups from the Powerups object
            speedFruitPosition = speedFruit.getPosition();

            // Add the powerups ellipse to the root at the appropriate position
            grid.add(speedFruitEllipse, speedFruitPosition.x, speedFruitPosition.y);
        }
    }

    private void eatFood() {
        Snake.Point headPosition = snake.getHeadPosition();

        // Normal food
        if (foodPosition.x == headPosition.x && foodPosition.y == headPosition.y) {
            System.out.println("Food eaten");
            score++;
            ui.updateScore(score);
            snakeLength++;
            generateNewFood();
            increaseSnakeSpeed();

        }
        // Golden fruit
        if (goldenfruitposition.getX() == headPosition.x && goldenfruitposition.getY() == headPosition.y) {
            System.out.println("Golden fruit eaten");
            score += 5;
            timelineHandler.stopPowerupTimeline();
            ui.updateScore(score);
            if (!isFoodOnPowerupOrSnake) {
                System.out.println("Generated!");
                grid.getChildren().remove(speedFruitEllipse);
                generateNewGoldenfruit();
            }

            timelineHandler.pausePowerupTimeline();
        }

        if (speedFruitPosition.x == headPosition.x && speedFruitPosition.y == headPosition.y) {
            timelineHandler.stopTimeline();
            timelineHandler.getMainTimeline().getKeyFrames().set(0, new KeyFrame(Duration.millis(50), event -> {
                if (snakeMoving && !ui.gamePaused) {
                    snake.move();
                    drawSnake();
                    handleCollision();
                }
            }));
            timelineHandler.startTimeline();
            timelineHandler.stopPowerupTimeline();

            ui.getGrid().getChildren().remove(speedFruitEllipse);
            isSpeedFruitEaten = true;
            timelineHandler.stopSpeedFruitDelayTimeline();
            timelineHandler.getSpeedFruitDelayTimeline().getKeyFrames().set(0,
                    new KeyFrame(Duration.seconds(10), event -> {
                        if (!isFoodOnPowerupOrSnake()) {
                            generateNewSpeedFruit();
                            isSpeedFruitEaten = false;
                        }
                    }));

            // Play the delay timeline
            timelineHandler.startSpeedFruitDelayTimeline();
            timelineHandler.startPowerupTimeline();
        }
    }

    public void handleCollision() {
        // You can add more collision checks here if needed
        if (snake.checkCollision(size - 1)) {
            snakeMoving = false;
            ui.displayGameOver(this.getScore());
        }
    }

    public void generateNewSpeedFruit() {
        speedFruit.randomizePosition(size);
        speedFruitPosition = speedFruit.getPosition();
        drawSpeedFruit(grid);

        timelineHandler.stopTimeline();
        timelineHandler.getMainTimeline().getKeyFrames().set(0, new KeyFrame(Duration.millis(snakeSpeed), event -> {
            if (snakeMoving && !ui.gamePaused) {
                snake.move();
                drawSnake();
                handleCollision();
            }
        }));
        timelineHandler.startTimeline();
    };

    private void generateNewFood() {
        do {
            food.randomizePosition(size);
            foodPosition = food.getPosition();
        } while (isFoodOnSnake());

        drawFood(ui.getGrid());
        snake.setSnakeSize(snakeLength);
    }

    private boolean isFoodOnSnake() {
        Snake.Point headPosition = snake.getHeadPosition();

        if (foodPosition.equals(new java.awt.Point(headPosition.x, headPosition.y))) {
            return true;
        }

        for (Snake.Point bodyPart : snake.getBody()) {
            if (foodPosition.equals(new java.awt.Point(bodyPart.x, bodyPart.y))) {
                return true;
            }
        }
        return false;
    }

    public void increaseSnakeSpeed() {

        if (score % 5 == 0 && snakeSpeed >= 0) {
            snakeSpeed -= 5;
            timelineHandler.stopTimeline();
            timelineHandler.getMainTimeline().getKeyFrames().set(0, new KeyFrame(Duration.millis(snakeSpeed), event -> {
                if (snakeMoving && !ui.gamePaused) {
                    snake.move();
                    drawSnake();
                    handleCollision();
                }
            }));
            timelineHandler.startTimeline();
        }
    }

    public int getScore() {
        return score;
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    public int increaseSnakeLength(int i) {
        return snakeLength += i;
    }
}
