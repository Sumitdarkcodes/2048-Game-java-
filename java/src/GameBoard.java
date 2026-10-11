import java.util.ArrayList;
import java.util.Random;

// this class holds all the game logic - no UI code here
public class GameBoard {

    int[][] board = new int[4][4];                      // 4x4 grid, 0 = empty cell
    int score = 0;                                      // current score
    int bestScore = 0;                                  // best score this session
    boolean won = false;                                // did player reach 2048?
    boolean gameOver = false;                           // no more moves possible?

    Random random = new Random();                       // for random tile spawns

    // constructor - start a fresh game
    public GameBoard() {
        reset();
    }

    // reset the board for a new game
    public void reset() {

        // empty all cells
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = 0;
            }
        }

        score = 0;
        won = false;
        gameOver = false;

        spawnTile();                                    // start with 2 tiles
        spawnTile();
    }

    // place a 2 or 4 in a random empty cell
    public void spawnTile() {

        // collect all empty cells
        ArrayList<int[]> empty = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    empty.add(new int[]{i, j});
                }
            }
        }

        // no space left
        if (empty.isEmpty()) {
            return;
        }

        // pick a random empty cell
        int[] cell = empty.get(random.nextInt(empty.size()));

        // 90% chance of 2, 10% chance of 4
        board[cell[0]][cell[1]] = random.nextInt(10) < 9 ? 2 : 4;
    }

    // move all tiles in the given direction
    // returns true if anything moved
    public boolean move(String direction) {
        int[][] before = copyBoard();                   // snapshot to compare later

        if (direction.equals("LEFT")) {
            for (int r = 0; r < 4; r++) {
                board[r] = mergeLine(board[r]);
            }
        } else if (direction.equals("RIGHT")) {
            for (int r = 0; r < 4; r++) {
                board[r] = reverse(mergeLine(reverse(board[r])));
            }
        } else if (direction.equals("UP")) {
            for (int c = 0; c < 4; c++) {
                int[] merged = mergeLine(getColumn(c));
                setColumn(c, merged);
            }
        } else if (direction.equals("DOWN")) {
            for (int c = 0; c < 4; c++) {
                int[] merged = reverse(mergeLine(reverse(getColumn(c))));
                setColumn(c, merged);
            }
        }

        boolean moved = !boardsEqual(before, board);    // did anything change?

        if (moved) {
            spawnTile();                                // add a new tile after move
            if (score > bestScore) {
                bestScore = score;                      // update best score
            }
            if (!canMove()) {
                gameOver = true;                        // no moves left
            }
        }

        return moved;
    }

    // process one line of 4 numbers:
    // step 1 - remove zeros
    // step 2 - merge equal neighbors
    // step 3 - pad with zeros at the end
    private int[] mergeLine(int[] line) {

        // step 1 - drop all zeros
        ArrayList<Integer> list = new ArrayList<>();
        for (int num : line) {
            if (num != 0) {
                list.add(num);
            }
        }

        // step 2 - merge adjacent equal numbers
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).equals(list.get(i + 1))) {
                int merged = list.get(i) * 2;
                list.set(i, merged);
                list.remove(i + 1);
                score += merged;                        // add merged value to score

                if (merged == 2048) {
                    won = true;                         // player reached 2048
                }
            }
        }

        // step 3 - pad with zeros
        int[] result = new int[4];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    // check if any move is possible
    public boolean canMove() {

        // empty cell exists - we can still move
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    return true;
                }
            }
        }

        // no empty cells - check if any neighbors can merge
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j < 3 && board[i][j] == board[i][j + 1]) return true;
                if (i < 3 && board[i][j] == board[i + 1][j]) return true;
            }
        }

        return false;
    }

    // helper - reverse a 4-element array
    private int[] reverse(int[] arr) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = arr[3 - i];
        }
        return result;
    }

    // helper - get one column as an array
    private int[] getColumn(int c) {
        int[] col = new int[4];
        for (int r = 0; r < 4; r++) {
            col[r] = board[r][c];
        }
        return col;
    }

    // helper - write an array back into a column
    private void setColumn(int c, int[] col) {
        for (int r = 0; r < 4; r++) {
            board[r][c] = col[r];
        }
    }

    // helper - deep copy of the board
    private int[][] copyBoard() {
        int[][] copy = new int[4][4];
        for (int i = 0; i < 4; i++) {
            copy[i] = board[i].clone();
        }
        return copy;
    }

    // helper - check if two boards are equal
    private boolean boardsEqual(int[][] a, int[][] b) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}