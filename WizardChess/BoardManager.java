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

        /**
         * The `Position` constructor initializes a new Position object with the specified row and column coordinates.
         *
         * @param r the row coordinate of the position
         * @param c the column coordinate of the position
         */
        public Position(int r, int c) {
            // Initialize the Position object with the specified row and column coordinates
            this.r = r;
            this.c = c;
        }

        /**
         * Gets the row coordinate of the position.
         *
         * @return the row coordinate
         */
        public int getR() {
            return r;
        }

        /**
         * Gets the column coordinate of the position.
         *
         * @return the column coordinate
         */
        public int getC() {
            return c;
        }

    }

    /**
     * Inner class `Move` represents a move from one position to another, including the move index.
     */
    public static class Move {
        private int fromR, fromC, toR, toC, i;

        /**
         * The `Move` constructor initializes a new Move object with the specified parameters.
         *
         * @param fromR the source row coordinate of the move
         * @param fromC the source column coordinate of the move
         * @param toR   the destination row coordinate of the move
         * @param toC   the destination column coordinate of the move
         * @param i     an additional parameter associated with the move
         */
        public Move(int fromR, int fromC, int toR, int toC, int i) {
            // Initialize the Move object with the specified parameters
            this.fromR = fromR;
            this.fromC = fromC;
            this.toR = toR;
            this.toC = toC;
            this.i = i;
        }

        /**
         * Gets the source row coordinate.
         *
         * @return the source row coordinate
         */
        public int getFromR() {
            return fromR;
        }

        /**
         * Gets the source column coordinate.
         *
         * @return the source column coordinate
         */
        public int getFromC() {
            return fromC;
        }

        /**
         * Gets the destination row coordinate.
         *
         * @return the destination row coordinate
         */
        public int getToR() {
            return toR;
        }

        /**
         * Gets the destination column coordinate.
         *
         * @return the destination column coordinate
         */
        public int getToC() {
            return toC;
        }

        /**
         * Gets the additional parameter 'i'.
         *
         * @return the additional parameter 'i'
         */
        public int getI() {
            return i;
        }

        /**
         * Sets the additional parameter 'i'.
         *
         * @param in the value to set for 'i'
         */
        public void setI(int in) {
            i = in;
        }

        /**
         * Changes the move to the specified coordinates.
         *
         * @param fromR the source row coordinate
         * @param fromC the source column coordinate
         * @param toR   the destination row coordinate
         * @param toC   the destination column coordinate
         */
        public void change(int fromR, int fromC, int toR, int toC) {
            this.fromR = fromR;
            this.fromC = fromC;
            this.toR = toR;
            this.toC = toC;
        }

        /**
         * Converts the move to a string representation.
         *
         * @return a string representation of the move
         */
        public String toString() {
            return "[" + fromR + "][" + fromC + "]-->[" + toR + "][" + toC + "]" + i;
        }

        /**
         * Checks if the move is the reverse of another move.
         *
         * @param m the other move to compare with
         * @return true if the moves are reverse of each other, false otherwise
         */
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
    //public static void printBoard() {
        //for (int i = 0; i < 8; i++) {
            //for (int j = 0; j < 8; j++)
                //prints out the type of the piece if the corresponding location has a piece on it
                //System.out.print(board[i][j].getOccupyingPiece() == null ? "." : board[i][j].getOccupyingPiece().getType());
            //System.out.println();
        //}
    //}

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
        // the amount of moves the pieces are allowed to make
        int movesTaken = 0, increment = 0;
        Move pre = new Move(-2, -2, -2, -2, -2);
        // an instance of the move class is created with placeholder values

        // stops any futher moves from being taken if the cap is reached
        while (movesTaken != cap) {
            // moves are taken from the ram function within enemy targetting
            Deque<Move> dq = EnemyTargetting.ram();
            
            while (dq.size() > cap - movesTaken)
                dq.removeLast();
                // the size of dq is adjusted to not exceed the remaining allowed moves
            
            movesTaken += dq.size();
            // the number of moves taken is updated
            Move m;

            // this loop processes the moves obtained from the dq deque
            while (!dq.isEmpty()) {
                m = dq.poll();
                m.setI(countdown - increment++);
                // the move is set with an incremental value and added to the corresponding piece's moves

                board[m.getFromR()][m.getFromC()].getOccupyingPiece().addMove(m);
                // move is added to the piece through it's reference from the board
                board[m.getFromR()][m.getFromC()].empty();
                // the reference to the piece on the source square is removed
            }

            // if the amount of moves taken is the max, the function stops here to save time
            if (movesTaken == cap)
                break;
                
            //if king is dying then end here and make sure the enemy turn also ends
            if(Game.isKingGoingToDie()){
                abnormalEnd = countdown - increment;
                break;
            }
            // uses the enemy targetting class to get the current best move from stockfish
            m = EnemyTargetting.bestMove(currentFEN(), depth, processTime);
            // checks the moves given to the function by stockfish to avoid repeated moves
            if (m.getFromR()==-5||m.reversedMove(pre)) {
                abnormalEnd = countdown - increment;
                //System.out.println("ASDAFA"+abnormalEnd);
                incoming = new Piece[8][8];
                incoming[Wizard.getR()][Wizard.getC()] = new Piece('p', Game.getHPush() + Wizard.getC() * 80, Game.getVPush() + Wizard.getR() * 80);//pawn is now incoming. Here to encourage player to move.
                warn();
                break;
            }

            m.setI(countdown - increment++);
            pre = m;
            // the move is set with an incremental value and becomes the new pre

            try {
                // retrieves the piece occupying the source square from the game board and calls the addMove method on the occupying piece
                board[m.getFromR()][m.getFromC()].getOccupyingPiece().addMove(m);
            } catch (ArrayIndexOutOfBoundsException e) {
                // catches any ArrayIndexOutOfBoundsException that might occur during the attempt to retrieve the piece from the game board
                abnormalEnd = countdown - increment + 1;
                // ends the function immediately
                return;
            }

            board[m.getToR()][m.getToC()].placePiece(board[m.getFromR()][m.getFromC()].getOccupyingPiece());
            board[m.getFromR()][m.getFromC()].empty();
            // places the occupying piece on the destination square of the move (m) and empties the source square on the game board.
            
            movesTaken++;
            // increments the moves taken counter
        }
        countdown++;
        allowNextMove();
        //System.out.println(abnormalEnd+" "+countdown);
        //if no move this allows abnormalEnd to take effect
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
        // creates a placeholder value for all of the pieces hp
        String piecesHP = "";
        for (Tile[] row : getBoard()) {
            for (Tile tile : row) {
                // individually pulls every tile within the board to check if there is a piece on it
                
                if (tile.getOccupyingPiece() != null) {
                    // adds the pieces hp as well as a comma so the string can be split later on
                    piecesHP += tile.getOccupyingPiece().getHP();
                    piecesHP += ",";
                }
            }
        }

        // returns the string now loaded up with the hp of every piece on the board
        return piecesHP;
    }

    /**
     * Set the HP (Hit Points) of pieces on the board based on a comma-separated string of HP values.
     * @param pieceHP Comma-separated string of HP values
     */
    public static void setPiecesHP(String pieceHP) {
        // decombines the string inputted into an array of strings of piece hps
        String[] piecesHPArray = pieceHP.split(",");
        int i = 0;
        for (Tile[] row : getBoard()) {
            for (Tile tile : row) {
                // individually pulls every tile within the board to check if there is a piece on it

                if (tile.getOccupyingPiece() != null) {
                    // sets the hp of the piece to the string value converted into an int
                    tile.getOccupyingPiece().setHP(Integer.parseInt(piecesHPArray[i]));
                    
                    // moves onto the next piece hp stored within the array
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
                    //places the piece on the tile if the location is stored within incoming
                    board[i][j].placePiece(incoming[i][j]);
    }

    /**
     * Create the initial state of the `incoming` array based on the provided FEN string.
     * @param fen Forsyth-Edwards Notation (FEN) string
     */
    public static void createIncoming(String fen) {
        // sets the already defined array incoming to an array of pieces matching the size of the board
        incoming = new Piece[8][8];
        countdown = 0;
        
        // unwarns the player
        warned = false;
        
        // uses StringTokenizer to take out the useless parts of the FEN and keep the pieces that we can read
        StringTokenizer st = new StringTokenizer(fen.replaceAll(" b - - 0 1",""), "/");
        for (int i = 0; i < 8; i++) {
            // gets the next token from within the array of Strings
            String line = st.nextToken();
            // placeholder value for the column the piece will be stored in
            int j = 0;
            
            // this basically interprets the FEN which is a normally unreadable jumble
            // of letters and numbers into the positions on the board and which pieces 
            // should be spawned at which location
            for (int k = 0, len = line.length(); k < len; k++) {
                if (Character.isDigit(line.charAt(k)))j += (line.charAt(k) - '0');
                else if (line.charAt(k) != 'K') {
                    incoming[i][j] = new Piece(line.charAt(k), Game.getHPush() + j++ * 80, Game.getVPush() + i * 80);
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
        //when countdown reaches abnormalEnd the countdown will immediately jump to -1 which ends the current enemy turn
        if (--countdown == abnormalEnd) {
            countdown = -1;
            abnormalEnd = -1;
        }
    }

    /**
     * Check if all enemies are defeated on the game board.
     * @return True if all enemies are defeated, false otherwise.
     */
    public static boolean enemiesDefeated() {
        // if the player is currently being warned and is therefore in between levels
        // we do not want to tell them the level has ended so therefore return false
        if (warned) return false;
        for (Tile[] row : getBoard())
            for (Tile tile : row)
                // basically checking each tile on the board and seeing if the king is
                // located on the tile, if so the king must be alive so return false
                if (tile.getOccupyingPiece() != null && tile.getOccupyingPiece().isKing())
                    return false;
        // by elimnation we know the king is not on the board so therefore the level
        // is completed so return true
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