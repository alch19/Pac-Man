import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameboard extends JPanel implements ActionListener{
    private Timer timer;
    private int pacManX;
    private int pacManY;
    private int pacManDX;
    private int pacManDY;
    private final int[][] maze = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
        {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public Gameboard() {
        initBoard();
        pacManX=40;
        pacManY=40;
        pacManDX=0;
        pacManDY=0;
    }

    private void initBoard() {
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(800,600));
        addKeyListener(new KAdapter());

        timer = new Timer(40, this);
        timer.start();
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
    }

    private boolean isValidMove(int x, int y) {
        int gridX=x/40;
        int gridY=y/40;

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
                    g.fillRect(x * 40, y * 40, 40, 40);
                } else if (maze[y][x] == 0) {
                    g.setColor(Color.YELLOW);
                    g.fillOval(x * 40 + 15, y * 40 + 15, 10, 10);
                }
            }
        }
    }

    private void drawPacMan(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(pacManX, pacManY, 40, 40);
    }


    private class KAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                pacManDX = -40;
                pacManDY = 0;
            } else if (key == KeyEvent.VK_RIGHT) {
                pacManDX = 40;
                pacManDY = 0;
            } else if (key == KeyEvent.VK_UP) {
                pacManDX = 0;
                pacManDY = -40;
            } else if (key == KeyEvent.VK_DOWN) {
                pacManDX = 0;
                pacManDY = 40;
            }
        }
    }
}
