import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameboard extends JPanel implements ActionListener{
    private Timer timer;
    private int pacManX;
    private int pacManY;
    private int pacManDX;
    private int pacManDY;
    private int newDX;
    private int newDY;
    private int tile;
    private int[][] maze;
    private int cols=20;
    private int rows=20;
    private int frameWidth=800;
    private int frameHeight=600;

    public Gameboard() {
        initBoard();
        pacManX=40;
        pacManY=40;
        pacManDX=0;
        pacManDY=0;
        newDX=0;
        newDY=0;
    }

    private void initBoard() {
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(frameWidth,frameHeight));
        addKeyListener(new KAdapter());

        timer = new Timer(100, this);
        timer.start();

        tile=frameWidth/rows;
        maze=generateRandomMaze(rows,cols);
    }

    /*private int[][] generateRandomMaze(int rows, int cols) {
        Random rand=new Random();
        int[][] newMaze=new int[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (y == 0 || y == rows - 1 || x == 0 || x == cols - 1) {
                    newMaze[y][x] = 1;
                } else {
                    newMaze[y][x] = rand.nextInt(2);
                }
            }
        }
        newMaze[1][1] = 0;
        return newMaze;
    }*/

    private int[][] generateRandomMaze(int rows, int cols) {
        int[][] newMaze = new int[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                newMaze[y][x] = 1;
            }
        }

        Stack<int[]> stack = new Stack<>();
        int startX = 1;
        int startY = 1;
        stack.push(new int[]{startX, startY});
        newMaze[startY][startX] = 0;

        int[][] directions = {{0, -2}, {0, 2}, {-2, 0}, {2, 0}};
        Random rand = new Random();

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            Collections.shuffle(Arrays.asList(directions), rand);

            for (int[] direction : directions) {
                int newX = current[0] + direction[0];
                int newY = current[1] + direction[1];

                if (newX > 0 && newX < cols - 1 && newY > 0 && newY < rows - 1 && newMaze[newY][newX] == 1) {
                    newMaze[newY][newX] = 0;
                    newMaze[current[1] + direction[1] / 2][current[0] + direction[0] / 2] = 0;
                    stack.push(new int[]{newX, newY});
                }
            }
        }

        return newMaze;
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
    }

    private boolean isValidMove(int x, int y) {
        int gridX=x/tile;
        int gridY=y/tile;

        if (maze[gridY][gridX] == 1) {
            return false;
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }

    private void drawGame(Graphics g) {
        drawMaze(g);
        drawPacMan(g);
    }

    private void drawMaze(Graphics g) {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                if (maze[y][x] == 1) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x * tile, y * tile, tile, tile);
                } else if (maze[y][x] == 0) {
                    g.setColor(Color.YELLOW);
                    g.fillOval(x * tile + tile / 4, y * tile + tile / 4, tile / 2, tile / 2);
                }
            }
        }
    }

    private void drawPacMan(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(pacManX, pacManY, tile, tile);
    }


    private class KAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
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
