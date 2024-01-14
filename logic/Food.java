    package logic;

    import java.awt.Point;
    import java.util.Random;

    public class Food {
        private Point position;

        public Food(int gridSize) {
            initialize(gridSize);
        }

        private void initialize(int gridSize) {
            Random random = new Random();
            int x = random.nextInt(gridSize);
            int y = random.nextInt(gridSize);
            position = new Point(x, y);
        }

        public Point getPosition() {
            return position;
        }

        public void randomizePosition(int gridSize) {
            initialize(gridSize);
        }
    }