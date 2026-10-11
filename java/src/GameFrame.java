import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// this class handles the window, grid, keyboard, and painting
public class GameFrame extends JPanel {

    GameBoard game = new GameBoard();                   // our game logic

    // colors for each tile value
    Color[] tileColors = {
        new Color(205, 193, 180),                       // 0   (empty)
        new Color(238, 228, 218),                       // 2
        new Color(237, 224, 200),                       // 4
        new Color(242, 177, 121),                       // 8
        new Color(245, 149, 99),                        // 16
        new Color(246, 124, 95),                        // 32
        new Color(246, 94, 59),                         // 64
        new Color(237, 207, 114),                       // 128
        new Color(237, 204, 97),                        // 256
        new Color(237, 200, 80),                        // 512
        new Color(237, 197, 63),                        // 1024
        new Color(237, 194, 46)                         // 2048
    };

    // constructor - sets up the window
    public GameFrame() {
        setPreferredSize(new Dimension(500, 600));      // panel size
        setBackground(new Color(250, 248, 239));        // background color
        setFocusable(true);                             // needed for keyboard

        // listen for arrow keys
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_LEFT)  game.move("LEFT");
                if (key == KeyEvent.VK_RIGHT) game.move("RIGHT");
                if (key == KeyEvent.VK_UP)    game.move("UP");
                if (key == KeyEvent.VK_DOWN)  game.move("DOWN");

                if (key == KeyEvent.VK_R)     game.reset();   // press R to restart

                repaint();                              // redraw the board
            }
        });
    }

    // this runs whenever the panel needs to be drawn
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw title
        g2.setColor(new Color(119, 110, 101));
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.drawString("2048", 30, 50);

        // draw score box
        g2.setColor(new Color(187, 173, 160));
        g2.fillRoundRect(200, 20, 120, 50, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("SCORE", 220, 40);

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("" + game.score, 230, 62);

        // draw best score box
        g2.setColor(new Color(187, 173, 160));
        g2.fillRoundRect(340, 20, 120, 50, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("BEST", 365, 40);

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("" + game.bestScore, 375, 62);

        // draw the grid
        int gridX = 30;                                 // x position of grid
        int gridY = 100;                                // y position of grid
        int cellSize = 105;                             // each cell 105px
        int gap = 10;                                   // gap between cells

        // background of the grid
        g2.setColor(new Color(187, 173, 160));
        g2.fillRoundRect(gridX - gap, gridY - gap, 4 * cellSize + 5 * gap, 4 * cellSize + 5 * gap, 15, 15);

        // draw each cell
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int value = game.board[row][col];

                int x = gridX + col * (cellSize + gap);
                int y = gridY + row * (cellSize + gap);

                // pick a color based on value
                int index = value == 0 ? 0 : (int) (Math.log(value) / Math.log(2));
                if (index > 11) index = 11;             // cap at 2048+ color

                g2.setColor(tileColors[index]);
                g2.fillRoundRect(x, y, cellSize, cellSize, 10, 10);

                // draw the number
                if (value != 0) {
                    g2.setColor(value <= 4 ? new Color(119, 110, 101) : Color.WHITE);

                    // smaller font for bigger numbers
                    int fontSize = value < 100 ? 40 : value < 1000 ? 32 : 26;
                    g2.setFont(new Font("Arial", Font.BOLD, fontSize));

                    String text = "" + value;
                    FontMetrics fm = g2.getFontMetrics();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent();

                    g2.drawString(text, x + (cellSize - textWidth) / 2, y + (cellSize + textHeight) / 2 - 5);
                }
            }
        }

        // show WIN or GAME OVER overlay
        if (game.won || game.gameOver) {
            g2.setColor(new Color(255, 255, 255, 220));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(119, 110, 101));
            g2.setFont(new Font("Arial", Font.BOLD, 48));

            String msg = game.won ? "YOU WIN!" : "GAME OVER";
            FontMetrics fm = g2.getFontMetrics();
            int msgWidth = fm.stringWidth(msg);

            g2.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2 - 20);

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            String hint = "Press R to restart";
            int hintWidth = g2.getFontMetrics().stringWidth(hint);

            g2.drawString(hint, (getWidth() - hintWidth) / 2, getHeight() / 2 + 30);
        }
    }
}