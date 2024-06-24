import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class Gameboard extends JPanel implements ActionListener {
    private Timer timer;
    private int pacManX;
    private int pacManY;
    private int pacManDX;
    private int pacManDY;
    private int newDX;
    private int newDY;
    private int tile;
    private int[][] maze;
    private int cols = 20;
    private int rows = 20;
    private int frameWidth = 800;
    private int frameHeight = 600;
    private int pelletCounter = 0;
    private int totalPellets = 0;
    private int scoreHeight = 30;
    private boolean gameOver = false;
    private Rectangle exitButton;
    private Rectangle playAgainButton;
    private int[][] ghosts = new int[3][2];

    public Gameboard() {
        initBoard();
        pacManX = 40;
        pacManY = 40;
        pacManDX = 0;
        pacManDY = 0;
        newDX = 0;
        newDY = 0;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameOver && exitButton.contains(e.getPoint())) {
                    System.exit(0);
                } else if (playAgainButton.contains(e.getPoint())) {
                    restartGame();
                }
            }
        });
    }

    private void initBoard() {
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        addKeyListener(new KAdapter());

        timer = new Timer(100, this);
        timer.start();

        tile = (frameWidth - scoreHeight) / rows;
        maze = generateRandomMaze(rows, cols);
        spawnGhosts();
    }

        private int[][] generateRandomMaze(int rows, int cols) {
        int[][] newMaze = new int[rows][cols];
        Random rand = new Random();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                newMaze[y][x] = 1;
            }
        }

        int centerX = cols / 2;
        int centerY = rows / 2;

        for (int y = centerY - 1; y <= centerY + 1; y++) {
            for (int x = centerX - 1; x <= centerX + 1; x++) {
                if ((y==centerY || y==centerY+1 || y==centerY-1) && (x==centerX || x==centerX+1 || x==centerX-1)) {
                    newMaze[y][x] = 0;
                } else {
                    newMaze[y][x] = 1;
                }
            }
        }

        int startX = rand.nextInt((cols - 2) / 2) * 2 + 1;
        int startY = rand.nextInt((rows - 2) / 2) * 2 + 1;
        if (newMaze[startY][startX] == 1) {
            newMaze[startY][startX] = 0;
        } else {
            startX = 1;
            startY = 1;
            newMaze[startY][startX] = 0;
        }

        carveMaze(newMaze, startX, startY, rows, cols, rand, centerX, centerY);

        for (int y = 1; y < rows - 1; y += 2) {
            for (int x = 1; x < cols - 1; x += 2) {
                if (rand.nextInt(100) < 50) { //CHANCE
                    int direction = rand.nextInt(4);
                    switch (direction) {
                        case 0: // left
                            if (x > 1 && newMaze[y][x - 2] == 0) {
                                newMaze[y][x - 1] = 0;
                            }
                            break;
                        case 1: // right
                            if (x < cols - 2 && newMaze[y][x + 2] == 0) {
                                newMaze[y][x + 1] = 0;
                            }
                            break;
                        case 2: // up
                            if (y > 1 && newMaze[y - 2][x] == 0) {
                                newMaze[y - 1][x] = 0;
                            }
                            break;
                        case 3: // down
                            if (y < rows - 2 && newMaze[y + 2][x] == 0) {
                                newMaze[y + 1][x] = 0;
                            }
                            break;
                    }
                }
            }
        }

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (newMaze[y][x] == 0 && !(y >= centerY - 1 && y <= centerY + 1 && x >= centerX - 1 && x <= centerX + 1)) {
                    newMaze[y][x] = 2;
                    totalPellets++;
                }
            }
        }

        return newMaze;
    }

    private void carveMaze(int[][] maze, int x, int y, int rows, int cols, Random rand, int centerX, int centerY) {
        int[][] directions = {{0, -2}, {0, 2}, {-2, 0}, {2, 0}};
        Collections.shuffle(Arrays.asList(directions), rand);

        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if ((newX >= centerX - 1 && newX <= centerX + 1) && (newY >= centerY - 1 && newY <= centerY + 1)) {
                continue;
            }

            if (newX > 0 && newX < cols - 1 && newY > 0 && newY < rows - 1 && maze[newY][newX] == 1) {
                maze[newY][newX] = 0;
                maze[y + direction[1] / 2][x + direction[0] / 2] = 0;
                carveMaze(maze, newX, newY, rows, cols, rand, centerX, centerY);
            }
        }
    }

    private void spawnGhosts() {
        int centerX = cols / 2;
        int centerY = rows / 2;
        ghosts[0][0] = centerX - 1;
        ghosts[0][1] = centerY;
        ghosts[1][0] = centerX;
        ghosts[1][1] = centerY;
        ghosts[2][0] = centerX + 1;
        ghosts[2][1] = centerY;
    }

    private void moveGhosts() {
        //implement logic here
        for (int i = 0; i < ghosts.length; i++) {
            int direction = new Random().nextInt(4);
            int dx = 0, dy = 0;
            switch (direction) {
                case 0: dx = -1; break; // left
                case 1: dx = 1; break; // right
                case 2: dy = -1; break; // up
                case 3: dy = 1; break; // down
            }
            int newX = ghosts[i][0] + dx;
            int newY = ghosts[i][1] + dy;
            if (isValidMove(newX * tile, newY * tile)) {
                ghosts[i][0] = newX;
                ghosts[i][1] = newY;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePacMan();
        repaint();
    }

    private void movePacMan() {
        int newPacManX = pacManX + pacManDX;
        int newPacManY = pacManY + pacManDY;

        if (isValidMove(newPacManX, newPacManY)) {
            pacManX = newPacManX;
            pacManY = newPacManY;
        }
        if (isValidMove(pacManX + newDX, pacManY + newDY)) {
            pacManDX = newDX;
            pacManDY = newDY;
        }
        int gridX = pacManX / tile;
        int gridY = pacManY / tile;
        if (maze[gridY][gridX] == 2) {
            maze[gridY][gridX] = 0;
            pelletCounter++;
            if (pelletCounter == totalPellets) {
                endGame();
            }
        }
    }

    private boolean isValidMove(int x, int y) {
        int gridX = x / tile;
        int gridY = y / tile;

        if (maze[gridY][gridX] == 1) {
            return false;
        }
        return true;
    }

    private void endGame() {
        timer.stop();
        gameOver = true;
        repaint();
    }

    private void restartGame() {
        gameOver = false;
        pelletCounter = 0;
        pacManX = 40;
        pacManY = 40;
        pacManDX = 0;
        pacManDY = 0;
        newDX = 0;
        newDY = 0;
        maze = generateRandomMaze(rows, cols);
        spawnGhosts();
        timer.restart();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
        if (pelletCounter == totalPellets) {
            drawEndGameMessage(g);
        }
    }

    private void drawGame(Graphics g) {
        drawMaze(g);
        drawPacMan(g);
        drawGhosts(g);
        drawScore(g);
    }

    private void drawMaze(Graphics g) {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                if (maze[y][x] == 1) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x * tile, y * tile + scoreHeight, tile, tile);
                } else if (maze[y][x] == 2) {
                    g.setColor(Color.YELLOW);
                    g.fillOval(x * tile + tile / 4, y * tile + tile / 4 + scoreHeight, tile / 2, tile / 2);
                }
            }
        }
    }

    private void drawPacMan(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(pacManX, pacManY + scoreHeight, tile, tile);
    }

    private void drawGhosts(Graphics g) {
        g.setColor(Color.RED);
        for (int[] ghost : ghosts) {
            g.fillOval(ghost[0] * tile, ghost[1] * tile + scoreHeight, tile, tile);
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Pellets Collected: " + pelletCounter, 10, 20);
    }

    private void drawEndGameMessage(Graphics g) {
        String message = "Game Over!";
        Font font = new Font("MV Boli", Font.BOLD, 30);
        g.setFont(font);
        int messageWidth = g.getFontMetrics(font).stringWidth(message);
        int messageHeight = g.getFontMetrics(font).getHeight();
        int x = (frameWidth - messageWidth) / 2;
        int y = (frameHeight - messageHeight) / 2 - 60;

        g.setColor(Color.BLACK);
        g.fillRect(x - 20, y - 40, messageWidth + 40, messageHeight + 120);

        g.setColor(Color.WHITE);
        g.drawString(message, x, y);

        Font buttonFont = new Font("MV Boli", Font.PLAIN, 20);
        g.setFont(buttonFont);

        String exitText = "EXIT";
        int exitWidth = g.getFontMetrics(buttonFont).stringWidth(exitText);
        int exitX = (frameWidth - exitWidth) / 2;
        int exitY = y + 60;
        exitButton = new Rectangle(exitX - 10, exitY - 30, exitWidth + 20, 40);

        g.setColor(Color.RED);
        g.fillRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        g.setColor(Color.WHITE);
        g.drawString(exitText, exitX, exitY);

        String playAgainText = "PLAY AGAIN";
        int playAgainWidth = g.getFontMetrics(buttonFont).stringWidth(playAgainText);
        int playAgainX = (frameWidth - playAgainWidth) / 2;
        int playAgainY = exitY + 50;
        playAgainButton = new Rectangle(playAgainX - 10, playAgainY - 30, playAgainWidth + 20, 40);

        g.setColor(Color.GREEN);
        g.fillRect(playAgainButton.x, playAgainButton.y, playAgainButton.width, playAgainButton.height);
        g.setColor(Color.WHITE);
        g.drawString(playAgainText, playAgainX, playAgainY);

        g.setColor(Color.WHITE);
        g.drawRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        g.drawRect(playAgainButton.x, playAgainButton.y, playAgainButton.width, playAgainButton.height);
    }

    private class KAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (gameOver) {
                return;
            }
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                newDX = -tile;
                newDY = 0;
            } else if (key == KeyEvent.VK_RIGHT) {
                newDX = tile;
                newDY = 0;
            } else if (key == KeyEvent.VK_UP) {
                newDX = 0;
                newDY = -tile;
            } else if (key == KeyEvent.VK_DOWN) {
                newDX = 0;
                newDY = tile;
            }
        }
    }
}
