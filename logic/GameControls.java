package logic;

import javafx.scene.input.KeyCode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameControls {
    private Snake snake;
    private final Map<KeyCode, Snake.Direction> keyMappings;
    private Set<KeyCode> pressedKeys;

    public GameControls(Snake snake) {
        this.snake = snake;
        this.keyMappings = new HashMap<>();
        this.pressedKeys = new HashSet<>();
        initializeKeys();
    }

    public void setSnake(Snake newSnake) {
        this.snake = newSnake;
    }

    private void initializeKeys() { // possible to control snake with WASD or arrow keys
        keyMappings.put(KeyCode.W, Snake.Direction.UP);
        keyMappings.put(KeyCode.UP, Snake.Direction.UP);
        keyMappings.put(KeyCode.S, Snake.Direction.DOWN);
        keyMappings.put(KeyCode.DOWN, Snake.Direction.DOWN);
        keyMappings.put(KeyCode.A, Snake.Direction.LEFT);
        keyMappings.put(KeyCode.LEFT, Snake.Direction.LEFT);
        keyMappings.put(KeyCode.D, Snake.Direction.RIGHT);
        keyMappings.put(KeyCode.RIGHT, Snake.Direction.RIGHT);
    }

    public void handleKeyPress(KeyCode code) {
        
        if (!keyMappings.containsKey(code)) return;

        Snake.Direction newDirection = keyMappings.get(code);
        Snake.Direction currentDirection = snake.getDirection();

        if ((newDirection == Snake.Direction.UP && (currentDirection != Snake.Direction.DOWN)) ||
            (newDirection == Snake.Direction.DOWN && (currentDirection != Snake.Direction.UP)) ||
            (newDirection == Snake.Direction.LEFT && (currentDirection != Snake.Direction.RIGHT)) ||
            (newDirection == Snake.Direction.RIGHT && (currentDirection != Snake.Direction.LEFT))) {
            snake.setDirection(newDirection);
        }
    }
}