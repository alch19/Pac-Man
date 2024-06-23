import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.util.Timer;
import java.awt.Dimension;

public class Gameboard extends JPanel implements ActionListener{
    private Timer timer;

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
        repaint();
    }

    private void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }

    private void drawGame(Graphics g) {
        
    }

    private class KAdapter() {

    }
}
