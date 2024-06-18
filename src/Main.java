import javax.swing.*;

public class Main {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {

         final JFrame frame = new JFrame("its snaking time");
        SnakeGame snakeGame = new SnakeGame(WIDTH, HEIGHT);

        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(snakeGame);
              frame.pack();
        snakeGame.startGame();


    }
}
