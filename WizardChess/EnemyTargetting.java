import java.io.*;
import java.util.*;

/**
 * The `EnemyTargetting` class is responsible for handling enemy pieces' targeting of the king.
 * It interacts with the Stockfish chess engine to determine the best move for the enemy.
 * 
 * @author Jimmy Zhu, David Guo
 * @version 1.0 01/11/2024
 */
public class EnemyTargetting {
    
    private static String testFen = "2b1k3/2pp4/8/4pp2/7q/1K6/8/8 b - - 0 1";
    private static Process p;
    private static BufferedReader br;
    private static BufferedWriter bw;

    /**
     * Sets up the communication with the Stockfish chess engine.
     * 
     * @throws IOException if an I/O error occurs
     */
    public static void setup() throws IOException {
        p = Runtime.getRuntime().exec("stockfish/stockfish-windows-x86-64-avx2");
        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        // test();
    }

    /**
     * Initiates a test with the Stockfish engine (commented out in the provided code).
     * 
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public static void test() throws IOException, InterruptedException {
        // System.out.println(bestMove(testFen, 5, 20));
    }

    /**
     * Retrieves the best move for the enemy using the Stockfish engine.
     * 
     * @param fen the current FEN (Forsyth-Edwards Notation) of the chess position
     * @param depth the depth of the search in the game tree
     * @param processTime the maximum time allowed for the engine to calculate the move
     * @return the best move determined by Stockfish
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    public static BoardManager.Move bestMove(String fen, int depth, int processTime) throws IOException, InterruptedException {
        bw.write("ucinewgame\n");
        bw.write("position fen " + fen + "\n");
        bw.write("go depth " + depth + " movetime " + processTime + "\n");
        bw.flush();
        BoardManager.Move m = new BoardManager.Move(-1, -1, -1, -1, -99);
        Thread t = new Thread(new Runnable() {
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String latest = "";
                while (true) {
                    try {
                        latest = br.readLine();
                    } catch (IOException e) {
                    }
                    try {
                        if (latest.contains("bestmove")) {
                            m.change(8 - latest.charAt(10) + '0', latest.charAt(9) - 'a', 8 - latest.charAt(12) + '0',
                                    latest.charAt(11) - 'a');
                            return;
                        }
                    } catch (NullPointerException e) {
                        return;
                    }
                }
            }
        });
        t.start();
        t.join();
        return m;
    }

    /**
     * Generates a deque of potential moves for the enemy based on the current board position.
     * 
     * @return a deque of potential moves for the enemy
     */
    public static Deque<BoardManager.Move> ram() {
        Deque<BoardManager.Move> dq = new LinkedList<BoardManager.Move>();
        Tile[][] currentBoard = BoardManager.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (currentBoard[i][j].getOccupyingPiece() == null
                        || (currentBoard[i][j].getOccupyingPiece() != null
                                && currentBoard[i][j].getOccupyingPiece().isDying()))
                    continue;
                switch (currentBoard[i][j].getOccupyingPiece().getType()) {
                    case 'p':
                        if (Wizard.getR() == i + 1 && (Wizard.getC() == j + 1 || Wizard.getC() == j - 1))
                            dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                        break;
                    case 'n':
                        // Check all possible knight moves
                        if (Wizard.getR() == i + 2 && Wizard.getC() == j + 1)
                            dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                        else if (Wizard.getR() == i + 2 && Wizard.getC() == j - 1)
                            dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                        else if (Wizard.getR() == i + 1 && Wizard.getC() == j + 2)
                            dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                        else if (Wizard.getR() == i + 1 && Wizard.getC() == j - 2)
                            dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                        else if (Wizard.getR() == i - 1 && Wizard.getC() == j + 2)
                            dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                        else if (Wizard.getR() == i - 1 && Wizard.getC() == j - 2)
                            dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                        else if (Wizard.getR() == i - 2 && Wizard.getC() == j + 1)
                            dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                        else if (Wizard.getR() == i - 2 && Wizard.getC() == j - 1)
                            dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                        break;
                    case 'b':
                        // Check diagonals
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i + k][j + k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i + k && Wizard.getC() == j + k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i - k][j + k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i - k && Wizard.getC() == j + k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i + k][j - k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i + k && Wizard.getC() == j - k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i - k][j - k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i - k && Wizard.getC() == j - k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        break;
                    case 'r':
                        // Check straights
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i + k][j].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i + k && Wizard.getC() == j)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i - k][j].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i - k && Wizard.getC() == j)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i][j + k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i && Wizard.getC() == j + k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i][j - k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i && Wizard.getC() == j - k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        break;
                    case 'q':
                        // Check diagonals
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i + k][j + k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i + k && Wizard.getC() == j + k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i - k][j + k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i - k && Wizard.getC() == j + k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i + k][j - k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i + k && Wizard.getC() == j - k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i - k][j - k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i - k && Wizard.getC() == j - k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        // Check straights
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i + k][j].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i + k && Wizard.getC() == j)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i - k][j].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i - k && Wizard.getC() == j)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i][j + k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i && Wizard.getC() == j + k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        try {
                            for (int k = 1; k < 8; k++) {
                                if (currentBoard[i][j - k].getOccupyingPiece() != null)
                                    break;
                                if (Wizard.getR() == i && Wizard.getC() == j - k)
                                    dq.add(new BoardManager.Move(i, j, Wizard.getR(), Wizard.getC(), -99));
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                        break;
                    case 'k':
                        if(Wizard.getR()==i-1 &&Wizard.getC()==j-1)dq.add(new BoardManager.Move(i,j,Wizard.getR(),Wizard.getC(),-99));
                        else if(Wizard.getR()==i-1 &&Wizard.getC()==j)dq.add(new BoardManager.Move(i,j,Wizard.getR(),Wizard.getC(),-99));
                        else if(Wizard.getR()==i-1 &&Wizard.getC()==j+1)dq.add(new BoardManager.Move(i,j,Wizard.getR(),Wizard.getC(),-99));
                        else if(Wizard.getR()==i && Wizard.getC()==j-1)dq.add(new BoardManager.Move(i,j,Wizard.getR(),Wizard.getC(),-99));
                        else if(Wizard.getR()==i && Wizard.getC()==j+1)dq.add(new BoardManager.Move(i,j,Wizard.getR(),Wizard.getC(),-99));
                        else if(Wizard.getR()==i+1 && Wizard.getC()==j+1)dq.add(new BoardManager.Move(i,j,Wizard.getR(),Wizard.getC(),-99));
                        else if(Wizard.getR()==i+1 && Wizard.getC()==j)dq.add(new BoardManager.Move(i,j,Wizard.getR(),Wizard.getC(),-99));
                        else if(Wizard.getR()==i+1 && Wizard.getC()==j-1)dq.add(new BoardManager.Move(i,j,Wizard.getR(),Wizard.getC(),-99));
                        break;
                }
            }
        }
        //while(dq.size()>cap)dq.removeLast();
        return dq;
    }
}
