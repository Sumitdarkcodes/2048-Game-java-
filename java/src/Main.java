import javax.swing.JFrame;

// this is the entry point - just opens the window
public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("2048 Game");         // create window with title
        GameFrame gamePanel = new GameFrame();          // create the game panel

        frame.add(gamePanel);                           // add panel to window
        frame.pack();                                   // size window to fit panel
        frame.setLocationRelativeTo(null);              // center on screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // close app on X
        frame.setResizable(false);                      // fixed size
        frame.setVisible(true);                         // show the window

        gamePanel.requestFocusInWindow();               // focus for keyboard input
    }
}