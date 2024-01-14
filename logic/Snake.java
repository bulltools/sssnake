package logic;

import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.Constants;

import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> body;
    private Direction direction;
    Timeline timeline;
    
    private int snakeLength = 2;
    private int snakeSpeed = 120;
    Point head;
    int snakeX;
    int snakeY;
    boolean snakeMoving = true;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static class Point {
        public int x;
        public int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(Constants.MIDPOINT, Constants.MIDPOINT + 1)); // Initial position of the snake's tail
        body.add(new Point(Constants.MIDPOINT, Constants.MIDPOINT));
        this.direction = Direction.RIGHT; // initial direction
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSnakeSize(int size) {
        snakeLength = size;
    }

        public void move() {
        Point oldHead = body.getFirst();
        Point newHead = null;
        int gridSize = 16;

        switch (direction) {
            case UP:
                newHead = new Point(oldHead.x, (oldHead.y - 1 + gridSize) % gridSize);
                break;
            case DOWN:
                newHead = new Point(oldHead.x, (oldHead.y + 1) % gridSize);
                break;
            case LEFT:
                newHead = new Point((oldHead.x - 1 + gridSize) % gridSize, oldHead.y);
                break;
            case RIGHT:
                newHead = new Point((oldHead.x + 1) % gridSize, oldHead.y);
                break;
        }

        body.addFirst(newHead);

        while (body.size() > snakeLength) {
            body.removeLast();
        }
    }

    public void draw(GridPane root, int cellSize) {
        for (int i = 0; i < body.size(); i++) {
            Point point = body.get(i);
    
            Rectangle rect = new Rectangle(cellSize, cellSize);
    
            if (i == 0) {
                // Color the head differently
                rect.setFill(Color.BLUE); // You can use any color for the head
            } else {
                rect.setFill(Color.WHITE);
            }
    
            root.add(rect, point.x, point.y);
        }
    }

    public boolean checkCollision(int size) {
        head = body.getFirst();
        snakeX = head.x;
        snakeY = head.y;

        // Check if snake collides with itself
        for (int i = 1; i < body.size(); i++) {
            if (head.x == body.get(i).x && head.y == body.get(i).y) {
                return true;
            }
        }
        return false;
    }

    // Method to stop the snake
    public void stop() {
        snakeMoving = false;
        if (timeline != null) {
            timeline.stop();
        }
    }

    // Method to start the snake
    public void start() {
        snakeMoving = true;
        if (timeline != null) {
            timeline.play();
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public Point getHeadPosition() {
        return body.getFirst();
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    public int getSnakeSpeed() {
        return snakeSpeed;
    }

    public int increaseSnakeLength(int i) {
        return snakeLength += i;
    }

    public int increaseSnakeSpeed(int i) {
        return snakeSpeed -= i;
    }
}