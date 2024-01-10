import java.util.*;
import java.io.*;
public class BoardManager  
{
    public static class Position{
        private int r,c;
        public Position(int r, int c){
            this.r=r;
            this.c=c;
        }
        public int getR(){
            return r;
        }
        public int getC(){
            return c;
        }
    }
    
    
    public static Tile getTile(int r, int c) {
        if (r >= 0 && r < board.length && c >= 0 && c < board[0].length) {
            return board[r][c];
        }
        return null;
    }
    // checks if spell was cast on the board (not outside boundaries)
    //private static boolean isOnBoard(int r, int c) {
    //    return r >= 0 && r < board.length && c >= 0 && c < board[0].length;
    //}
    public static void applySpell(Position spellOrigin) {
        int affectedR = spellOrigin.getR(), affectedC = spellOrigin.getC();
        //if (isOnBoard(affectedR, affectedC)) {
            Tile tile = board[affectedR][affectedC];
            if (tile != null) {
                Piece occupyingPiece = tile.getOccupyingPiece();
                if (occupyingPiece != null) {
                    occupyingPiece.takeDmg(100);
                    System.out.println("damage working");
                }
            }
        //}
    }
    public static class Move{
        private int fromR, fromC, toR, toC;
        public Move(int fromR, int fromC, int toR, int toC){
            this.fromR=fromR;
            this.fromC=fromC;
            this.toR=toR;
            this.toC=toC;
        }
        public int getFromR(){
            return fromR;
        }
        public int getFromC(){
            return fromC;
        }
        public int getToR(){
            return toR;
        }
        public int getToC(){
            return toC;
        }
        public void change(int fromR, int fromC, int toR, int toC){
            this.fromR=fromR;
            this.fromC=fromC;
            this.toR=toR;
            this.toC=toC;
        }
        public String toString(){
            return "["+fromR+"]["+fromC+"]-->["+toR+"]["+toC+"]";
        }
    }
    private static Tile[][] board = new Tile[8][8];
    private static Piece[][] incoming = new Piece[8][8];
    public static void placeTile(Tile t, int r, int c){
        board[r][c]=t;
    }
    public static void test1() throws IOException, InterruptedException {
        createIncoming("2b1kq2/2pppp2/8/8/8/4K3/8/8 b - - 0 1");
        spawnPieces();
    }
    public static void test2() throws IOException, InterruptedException {
        enemyTurn(6,5,50);
    }
    public static void makeMove(Move m){
        board[m.getFromR()][m.getFromC()].getOccupyingPiece().addMove(m);
        board[m.getToR()][m.getToC()].placePiece(board[m.getFromR()][m.getFromC()].getOccupyingPiece());
        board[m.getFromR()][m.getFromC()].empty();
    }
    public static void attackWizard(Move m){
        //Piece p = board[m.getFromR()][m.getFromC()].getOccupyingPiece();
        //board[m.getFromR()][m.getFromC()].empty();
        board[m.getFromR()][m.getFromC()].getOccupyingPiece().attack(m);
        //remember to oof piece
        //the take dmg part in piece
    }
    public static void enemyTurn(int cap, int depth, int processTime) throws InterruptedException, IOException {
        int movesTaken=0;
        while(movesTaken!=cap){
            Deque<Move> dq = EnemyTargetting.ram();
            System.out.println(dq.size()+" "+(cap-movesTaken));
            while(dq.size()>cap-movesTaken)dq.removeLast();
            movesTaken+=dq.size();
            while(!dq.isEmpty())attackWizard(dq.poll());
            //
            if(movesTaken==cap)break;
            makeMove(EnemyTargetting.bestMove(currentFEN(), depth, processTime));
            movesTaken++;
            for(int i = 0; i<8; i++)if(board[7][i].getOccupyingPiece()!=null&&board[7][i].getOccupyingPiece().getType()=='p')board[7][i].getOccupyingPiece().promote();
        }
    }
    public static String currentFEN(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<8; i++){
            int cnt = 0;
            for(int j = 0; j<8; j++){
                if(board[i][j].getOccupyingPiece()!=null){
                    if(cnt!=0){
                        sb.append(cnt);
                        cnt=0;
                    }
                    sb.append(board[i][j].getOccupyingPiece().getType());
                }
                else if(Wizard.getR()==i&&Wizard.getC()==j){
                    if(cnt!=0){
                        sb.append(cnt);
                        cnt=0;
                    }
                    sb.append("K");
                }
                else cnt++;
            }
            if(cnt!=0)sb.append(cnt);
            if(i!=7)sb.append("/");
        }
        sb.append(" b - - 0 1");
        return sb.toString();
    }
    public static void spawnPieces(){
        for(int i = 0; i<8; i++)for(int j = 0; j<8; j++)if(incoming[i][j]!=null)board[i][j].placePiece(incoming[i][j]);
    }
    public static void createIncoming(String fen){
        StringTokenizer st = new StringTokenizer(fen.replaceAll(" b - - 0 1",""),"/");
        for(int i = 0; i<8; i++){
            String line = st.nextToken();
            int j = 0;
            for(int k = 0, len=line.length(); k<len; k++){
                //System.out.println(j);
                //System.out.println(line);
                if(Character.isDigit(line.charAt(k)))j+=(line.charAt(k)-'0');
                else if(line.charAt(k)!='K')incoming[i][j]=new Piece(line.charAt(k),Game.hPush+j*80,Game.vPush+i*80,Game.hPush+j++*80,Game.vPush+i*80-30);
            }
        }
    }
    public static void warn(){
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++)if(incoming[i][j]!=null)board[i][j].turnRed();
        }
    }
    public static void unwarn(){
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++)if(incoming[i][j]!=null)board[i][j].turnNormal();
        }
    }
    public static Tile[][] getBoard(){
        return board;
    }
    //give wiz a turn before each round to get out of the way of the incoming pieces(if wiz is not out of the way wiz takes dmg from the piece ramming)
}
