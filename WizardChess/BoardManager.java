import java.util.*;
import java.io.*;
import greenfoot.*;

/**
 * The `BoardManager` class manages the game board, pieces, and turn progression.
 * It includes methods for handling piece movements, creating and updating the game board,
 * and managing the countdown for enemy turns.
 * 
 * @author Jimmy Zhu, Mekaeel Malik
 * @version January 21st, 2023
 */
public class BoardManager  
{
    // Static variables to keep track of countdown, abnormal end, and warning status
    private static int countdown = -1, abnormalEnd = -1;
    private static boolean warned = false;

    /**
     * Inner class `Position` represents a position on the game board with row and column indices.
     */
    public static class Position {
        private int r, c;

        public Position(int r, int c) {
            this.r = r;
            this.c = c;
        }

        public int getR() {
            return r;
        }

        public int getC() {
            return c;
        }
    }

    /**
     * Inner class `Move` represents a move from one position to another, including the move index.
     */
    public static class Move {
        private int fromR, fromC, toR, toC, i;

        public Move(int fromR, int fromC, int toR, int toC, int i) {
            this.fromR = fromR;
            this.fromC = fromC;
            this.toR = toR;
            this.toC = toC;
            this.i = i;
        }

        public int getFromR() {
            return fromR;
        }

        public int getFromC() {
            return fromC;
        }

        public int getToR() {
            return toR;
        }

        public int getToC() {
            return toC;
        }

        public int getI() {
            return i;
        }

        public void setI(int in) {
            i = in;
        }

        public void change(int fromR, int fromC, int toR, int toC) {
            this.fromR = fromR;
            this.fromC = fromC;
            this.toR = toR;
            this.toC = toC;
        }

        public String toString() {
            return "[" + fromR + "][" + fromC + "]-->[" + toR + "][" + toC + "]" + i;
        }

        public boolean reversedMove(Move m) {
            return fromR == m.getToR() && fromC == m.getToC() && toR == m.getFromR() && toC == m.getFromC();
        }
    }

    // 2D array to represent the game board and incoming pieces
    private static Tile[][] board = new Tile[8][8];
    private static Piece[][] incoming = new Piece[8][8];

    /**
     * Print the current state of the game board to the console.
     */
    public static void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                System.out.print(board[i][j].getOccupyingPiece() == null ? "." : board[i][j].getOccupyingPiece().getType());
            System.out.println();
        }
    }

    /**
     * Get the tile at the specified row and column indices.
     * @param r Row index
     * @param c Column index
     * @return Tile at the specified position, or null if out of bounds
     */
    public static Tile getTile(int r, int c) {
        try {
            return board[r][c];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Place a tile at the specified position on the game board.
     * @param t Tile to place
     * @param r Row index
     * @param c Column index
     */
    public static void placeTile(Tile t, int r, int c) {
        board[r][c] = t;
    }

    // Examples of FEN strings for initializing the game state
    // createIncoming("2b1kq2/2pppp2/8/8/8/4K3/8/8 b - - 0 1");
    // createIncoming("2brkn2/2pppp2/8/8/8/8/8/4K3 b - - 0 1");
    // createIncoming("b2qk1rb/1npppp2/8/8/8/8/8/4K3 b - - 0 1");
    // createIncoming("2bk1b2/4pppp/8/6K1/8/8/8/8 b - - 0 1");

    /**
     * Perform the enemy's turn with a given move limit, search depth, and time limit for processing.
     * @param cap Maximum number of moves to take
     * @param depth Search depth for move evaluation
     * @param processTime Time limit for processing
     * @throws InterruptedException Thrown when thread is interrupted
     * @throws IOException Thrown for IO-related issues
     */
    public static void enemyTurn(int cap, int depth, int processTime) throws InterruptedException, IOException {
        countdown = cap;
        int movesTaken = 0, increment = 0;
        Move pre = new Move(-2, -2, -2, -2, -2);

        while (movesTaken != cap) {
            Deque<Move> dq = EnemyTargetting.ram();
            while (dq.size() > cap - movesTaken)
                dq.removeLast();
            movesTaken += dq.size();
            Move m;

            while (!dq.isEmpty()) {
                m = dq.poll();
                m.setI(countdown - increment++);
                board[m.getFromR()][m.getFromC()].getOccupyingPiece().addMove(m);
                board[m.getFromR()][m.getFromC()].empty();
            }

            if (movesTaken == cap)
                break;

            m = EnemyTargetting.bestMove(currentFEN(), depth, processTime);

            if (m.reversedMove(pre)) {
                abnormalEnd = countdown - increment;
                incoming = new Piece[8][8];
                incoming[Wizard.getR()][Wizard.getC()] = new Piece('p', Game.hPush + Wizard.getC() * 80, Game.vPush + Wizard.getR() * 80);
                warn();
                break;
            }

            m.setI(countdown - increment++);
            pre = m;

            try {
                board[m.getFromR()][m.getFromC()].getOccupyingPiece().addMove(m);
            } catch (ArrayIndexOutOfBoundsException e) {
                abnormalEnd = countdown - increment + 1;
                return;
            }

            board[m.getToR()][m.getToC()].placePiece(board[m.getFromR()][m.getFromC()].getOccupyingPiece());
            board[m.getFromR()][m.getFromC()].empty();
            movesTaken++;
        }
    }

    /**
     * Generate the current FEN (Forsyth-Edwards Notation) string representing the game state.
     * @return FEN string
     */
    public static String currentFEN() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int cnt = 0;
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getOccupyingPiece() != null) {
                    if (cnt != 0) {
                        sb.append(cnt);
                        cnt = 0;
                    }
                    sb.append(board[i][j].getOccupyingPiece().getType());
                } else if (Wizard.getR() == i && Wizard.getC() == j) {
                    if (cnt != 0) {
                        sb.append(cnt);
                        cnt = 0;
                    }
                    sb.append("K");
                } else
                    cnt++;
            }

            if (cnt != 0)
                sb.append(cnt);

            if (i != 7)
                sb.append("/");
        }

        sb.append(" b - - 0 1");
        return sb.toString();
    }

    /**
     * Get the current HP (Hit Points) of all pieces on the board as a comma-separated string.
     * @return Comma-separated string of HP values
     */
    public static String getPiecesHP() {
        String piecesHP = "";
        for (Tile[] row : getBoard()) {
            for (Tile tile : row) {
                if (tile.getOccupyingPiece() != null) {
                    piecesHP += tile.getOccupyingPiece().getHP();
                    piecesHP += ",";
                }
            }
        }

        return piecesHP;
    }

    /**
     * Set the HP (Hit Points) of pieces on the board based on a comma-separated string of HP values.
     * @param pieceHP Comma-separated string of HP values
     */
    public static void setPiecesHP(String pieceHP) {
        String[] piecesHPArray = pieceHP.split(",");
        int i = 0;
        for (Tile[] row : getBoard()) {
            for (Tile tile : row) {
                if (tile.getOccupyingPiece() != null) {
                    tile.getOccupyingPiece().setHP(Integer.parseInt(piecesHPArray[i]));
                    i++;
                }
            }
        }
    }

    /**
     * Spawn pieces on the board based on the `incoming` array.
     */
    public static void spawnPieces() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (incoming[i][j] != null)
                    board[i][j].placePiece(incoming[i][j]);
    }

    /**
     * Create the initial state of the `incoming` array based on the provided FEN string.
     * @param fen Forsyth-Edwards Notation (FEN) string
     */
    public static void createIncoming(String fen) {
        incoming = new Piece[8][8];
        countdown = 0;
        warned = false;
        StringTokenizer st = new StringTokenizer(fen.replaceAll(" b - - 0 1",""), "/");
        for (int i = 0; i < 8; i++) {
            String line = st.nextToken();
            int j = 0;
            for (int k = 0, len = line.length(); k < len; k++) {
                if (Character.isDigit(line.charAt(k)))j += (line.charAt(k) - '0');
                else if (line.charAt(k) != 'K') {
                    incoming[i][j] = new Piece(line.charAt(k), Game.hPush + j++ * 80, Game.vPush + i * 80);
                    countdown++;
                }
            }
        }
    }

    /**
     * Reset the visual state of tiles to normal (no highlighting).
     */
    public static void resetTiles() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null)
                    board[i][j].turnNormal();
    }

    /**
     * Empty all tiles on the board.
     */
    public static void wipe() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    board[i][j].empty();
                }
            }
        }
    }

    /**
     * Highlight tiles based on the `incoming` array to warn the player.
     */
    public static void warn() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                if (incoming[i][j] != null)
                    board[i][j].turnRed();
        }
        warned = true;
    }

    /**
     * Reset the visual state of tiles to normal (no highlighting) after a warning.
     */
    public static void unwarn() {
        resetTiles();
        warned = false;
    }

    /**
     * Get the current game board.
     * @return 2D array of tiles representing the game board
     */
    public static Tile[][] getBoard() {
        return board;
    }

    /**
     * Check if it's time for a specific move based on the countdown.
     * @param i Move index
     * @return True if it's time for the move, false otherwise
     */
    public static boolean timeToMove(int i) {
        return countdown == i;
    }
    
    /**
     * Decrement the countdown, check for abnormal end conditions, and reset countdown if needed.
     * The method is responsible for managing the countdown and abnormal end scenarios.
     */
    public static void allowNextMove() {
        // Uncomment the line below for debugging purposes
        // System.out.println("BVBBfwafwawffawfBBB");
        
        if (--countdown == abnormalEnd) {
            // Uncomment the line below for debugging purposes
            // System.out.println("BVBBBBB");
            countdown = -1;
            abnormalEnd = -1;
        }
        // Uncomment the line below for debugging purposes
        // System.out.println("fwaafwwffa" + countdown + " " + abnormalEnd);
    }
    
    /**
     * Check if all enemies are defeated on the game board.
     * @return True if all enemies are defeated, false otherwise.
     */
    public static boolean enemiesDefeated() {
        if (warned) return false;
        for (Tile[] row : getBoard())
            for (Tile tile : row)
                if (tile.getOccupyingPiece() != null && tile.getOccupyingPiece().isKing())
                    return false;
        return true;
    }
    
    /**
     * Check if the warning status is active.
     * @return True if warned, false otherwise.
     */
    public static boolean isWarned() {
        return warned;
    }
    
    /**
     * Get the current countdown value.
     * @return The countdown value.
     */
    public static int getCountdown() {
        return countdown;
    }

}
