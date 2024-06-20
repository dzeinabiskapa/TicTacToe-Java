import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class Main extends JComponent {

    protected static JLabel label;
    public static final int CELL_SIZE = 100;
    private static final char[][] board = new char[3][3];
    private static boolean xTurn = true;
    private static String xPlayerName;
    private static String oPlayerName;

    Main() {
        resetBoard();
    }

    // initial state for board
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
        xTurn = true;
        if (label != null) {
            label.setText(xPlayerName + "'s Turn (X)");
        }
    }

    private void whoPlays() {
        xPlayerName = JOptionPane.showInputDialog("Xs player:");
        oPlayerName = JOptionPane.showInputDialog("Os player:");
        
        // Default names
        if (xPlayerName == null || xPlayerName.isEmpty()) xPlayerName = "X Player";
        if (oPlayerName == null || oPlayerName.isEmpty()) oPlayerName = "O Player";
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g2.draw(new Rectangle2D.Float(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'X') {
                    Shape cross = createCross(i, j);
                    Shape cross2 = createCross2(i, j);
                    g2.setPaint(Color.BLUE);
                    g2.draw(cross);
                    g2.draw(cross2);
                } else if (board[i][j] == 'O') {
                    Shape circle = createCircle(i, j);
                    g2.setPaint(Color.RED);
                    g2.draw(circle);
                }
            }
        }
    }

    private Shape createCross(int row, int col) {
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;
        return new Line2D.Float(x + 20, y + 20, x + CELL_SIZE - 20, y + CELL_SIZE - 20);
    }

    private Shape createCross2(int row, int col) {
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;
        return new Line2D.Float(x + 20, y + CELL_SIZE - 20, x + CELL_SIZE - 20, y + 20);
    }

    private Shape createCircle(int row, int col) {
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;
        return new Ellipse2D.Float(x + 20, y + 20, CELL_SIZE - 40, CELL_SIZE - 40);
    }

    private boolean checkWin(char player) {
        // rows and columns win condition
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        // diagonals win condition
        if ((board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
            (board[0][2] == player && board[1][1] == player && board[2][0] == player)) {
            return true;
        }
        return false;
    }

    private void checkGameEnd() {
        if (checkWin('X')) {
            label.setText(xPlayerName + " won!");
            // play again prompt
            if (JOptionPane.showConfirmDialog(null, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                resetBoard();
                repaint();
                // change names prompt
                if (JOptionPane.showConfirmDialog(null, "Change players?", "Change Players", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    whoPlays();
                }
            } else {
                System.exit(0);
            }
        } else if (checkWin('O')) {
            label.setText(oPlayerName + " won!");
            // play again prompt
            if (JOptionPane.showConfirmDialog(null, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                resetBoard();
                repaint();
                // change names prompt
                if (JOptionPane.showConfirmDialog(null, "Change players?", "Change Players", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    whoPlays();
                }
            } else {
                System.exit(0);
            }
        } else {
            // if draw
            boolean draw = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        draw = false;
                        break;
                    }
                }
            }
            if (draw) {
                label.setText("It's a draw!");
                // play again prompt
                if (JOptionPane.showConfirmDialog(null, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    resetBoard();
                    repaint();
                    // change names prompt
                    if (JOptionPane.showConfirmDialog(null, "Change players?", "Change Players", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        whoPlays();
                    }
                } else {
                    System.exit(0);
                }
            }
        }
    }

    private static void createAndShowGUI() {
        JFrame jFrame = new JFrame("Tic-tac-toe");
        jFrame.setSize(330, 350);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Main area = new Main();
        area.whoPlays();
        area.addMouseListener(new EventAdapter());
        label = new JLabel(area.xPlayerName + "'s Turn (X)");
        label.setHorizontalAlignment(JLabel.CENTER);

        jFrame.add(label, BorderLayout.NORTH);
        jFrame.add(area, BorderLayout.CENTER);
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        createAndShowGUI();
    }

    static class EventAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = e.getY() / Main.CELL_SIZE;
            int col = e.getX() / Main.CELL_SIZE;

            // ignore click if cell is occupied or game is over
            if (Main.board[row][col] != ' ' || Main.label.getText().endsWith("wins!") || Main.label.getText().endsWith("draw!")) {
                return;
            }

            // x and o placing
            if (Main.xTurn) {
                Main.board[row][col] = 'X';
                Main.label.setText(Main.oPlayerName + "'s Turn (O)");
            } else {
                Main.board[row][col] = 'O';
                Main.label.setText(Main.xPlayerName + "'s Turn (X)");
            }

            Main.xTurn = !Main.xTurn;
            ((Main) e.getSource()).repaint();

            ((Main) e.getSource()).checkGameEnd();
        }
    }
}