import javax.swing.JFrame;

public class Game extends JFrame{
    public Game() {
        initUI();
    }

    public void initUI() {
        add(new Gameboard());
        setTitle("Pac-Man");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,800);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setVisible(true);
    }
}