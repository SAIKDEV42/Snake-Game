import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int width;
    private final int height;
    private GamePoint food;
    private   int highScore;
     private int currentScore;
    private final Random random = new Random();

    private final int cellSize;
    private final static int FRAME_RATE = 20;
    private boolean gameOver = false;

    private Direction direction=Direction.RIGHT;
    private Direction newDirection=Direction.RIGHT;
    private boolean gameStarted = false;
    List<GamePoint> snake = new ArrayList<>();


    public SnakeGame(final int width, final int height) {
        super();
        setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;
        this.cellSize = width / (FRAME_RATE * 2);
        setBackground(Color.BLACK);

    }


    public void startGame() {
        resetGameData();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        HandleKeyEvent(e);
                    }
                });


        new Timer(1000 / FRAME_RATE, this).start();

    }


    private void resetGameData() {
        snake.clear();
        snake.add(new GamePoint(width / 2, height / 2));

        generateFood();
    }


    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (!gameStarted){
            printMessage(graphics,"press space-bar to start "   );

        }
        else {

           graphics.setColor(Color.CYAN);
            graphics.fillRect(food.x, food.y,cellSize,cellSize);
            Color snakeColour= Color.green;

            for (var point : snake) {
                graphics.setColor(snakeColour);
                graphics.fillRect(point.x, point.y, cellSize, cellSize);
                final int  newGreen= (int) (snakeColour.getGreen()*(0.95));
                snakeColour=new Color(0,newGreen,0);

            }
            if(gameOver){
                if (highScore< snake.size()){
                    highScore= snake.size();
                }
                printMessage(graphics,"Your score "+ snake.size()
                +"\nYour HighScore "+highScore+"\nPress SPACE to restart ");

            }
        }

    }

    private void printMessage(Graphics graphics,String message) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(graphics.getFont().deriveFont(30f));
        int currentHeight = height / 3;
        final var graphics2D = (Graphics2D) graphics;
        final var frc = graphics2D.getFontRenderContext();

        for (final var line : message.split("\n")) {
            final var layout = new TextLayout(line, graphics.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float) (width - bounds.getWidth()) / 2;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics.getFontMetrics().getHeight();
        }
    }


    private void HandleKeyEvent(KeyEvent keyCode) {
        if (!gameStarted){
            if (keyCode.getKeyCode() == KeyEvent.VK_SPACE) {
                gameStarted = true;
            }
        } else if (!gameOver) {
            switch (keyCode.getKeyCode()){

                case KeyEvent.VK_UP :
                    if (direction!=Direction.DOWN)newDirection=Direction.UP;
                    break;
                case KeyEvent.VK_DOWN : if (direction!=Direction.UP)newDirection=Direction.DOWN;
                    break;
                case KeyEvent.VK_RIGHT :if (direction!=Direction.LEFT)newDirection=Direction.RIGHT;
                    break;
                case KeyEvent.VK_LEFT :if(direction!=Direction.RIGHT) newDirection=Direction.LEFT;
                    break;

            }
        } else if(keyCode.getKeyCode()== KeyEvent.VK_SPACE) {
            gameStarted=false;
            gameOver=false;
            resetGameData();
        }
        }






    private void move() {
        final GamePoint currentHead = snake.getFirst();
         final GamePoint newHead = switch (direction){

             case Direction.UP -> new GamePoint(currentHead.x,currentHead.y-cellSize);
            case DOWN -> new GamePoint(currentHead.x,currentHead.y+cellSize);
            case LEFT -> new GamePoint(currentHead.x-cellSize,currentHead.y);
            case RIGHT -> new GamePoint(currentHead.x+cellSize,currentHead.y);


        };
        snake.addFirst(newHead);
        if (newHead.equals(food)){
            generateFood();

        }
       else if (iscollision()) {
            gameOver = true;
            snake.removeFirst();
        } else {
            snake.removeLast();
        }
        direction=newDirection;
    }
    private void generateFood() {
        do {
            food = new GamePoint(random.nextInt(width / cellSize) *cellSize ,
                    random.nextInt(height / cellSize)  *cellSize);
        } while (snake.contains(food));
    }

    private boolean iscollision() {

        final GamePoint head = snake.getFirst();
        final var invalidWidth = (head.x < 0) || (head.x >= width);
        final var invalidHeight = (head.y < 0) || (head.y >= height);

        if (invalidHeight || invalidWidth) {
            return true;
        }
        return snake.size() != new HashSet<>(snake).size();


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            move();
        }
        repaint();

    }







    private record GamePoint(int x, int y) {
    }

    private enum Direction{
        UP,DOWN,RIGHT,LEFT
    }

}
