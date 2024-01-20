import java.util.*;
import java.io.*;
import greenfoot.*;
public class BoardManager  
{
    private static int countdown=-1;
    private static boolean warned = false;
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
    public static class Move{
        private int fromR, fromC, toR, toC, i;
        public Move(int fromR, int fromC, int toR, int toC, int i){
            this.fromR=fromR;
            this.fromC=fromC;
            this.toR=toR;
            this.toC=toC;
            this.i=i;
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
        public int getI(){
            return i;
        }
        public void setI(int in){
            i=in;
        }
        public void change(int fromR, int fromC, int toR, int toC){
            this.fromR=fromR;
            this.fromC=fromC;
            this.toR=toR;
            this.toC=toC;
        }
        public String toString(){
            return "["+fromR+"]["+fromC+"]-->["+toR+"]["+toC+"]"+i;
        }
    }
    private static Tile[][] board = new Tile[8][8];
    private static Piece[][] incoming = new Piece[8][8];
    public static void printBoard(){
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++)System.out.print(board[i][j].getOccupyingPiece()==null?".":board[i][j].getOccupyingPiece().getType());
            System.out.println();
        }
    }
    public static Tile getTile(int r, int c){
        try{
            return board[r][c];
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }
    public static void placeTile(Tile t, int r, int c){
        board[r][c]=t;
    }
    //createIncoming("2b1kq2/2pppp2/8/8/8/4K3/8/8 b - - 0 1");
    //createIncoming("2brkn2/2pppp2/8/8/8/8/8/4K3 b - - 0 1");
    //createIncoming("b2qk1rb/1npppp2/8/8/8/8/8/4K3 b - - 0 1");
    //createIncoming("2bk1b2/4pppp/8/6K1/8/8/8/8 b - - 0 1");
    public static void enemyTurn(int cap, int depth, int processTime) throws InterruptedException, IOException {
        System.out.println("Running "+cap);
        countdown=cap;
        int movesTaken=0,increment=0;
        while(movesTaken!=cap){
            Deque<Move> dq = EnemyTargetting.ram();
            while(dq.size()>cap-movesTaken)dq.removeLast();
            movesTaken+=dq.size();
            Move m;
            while(!dq.isEmpty()){
                m=dq.poll();
                m.setI(countdown-increment++);
                board[m.getFromR()][m.getFromC()].getOccupyingPiece().addMove(m);
                board[m.getFromR()][m.getFromC()].empty();System.out.println("M"+m.getFromR()+" "+m.getFromC());
            }
            if(movesTaken==cap)break;
            m=EnemyTargetting.bestMove(currentFEN(), depth, processTime);
            m.setI(countdown-increment++);
            board[m.getFromR()][m.getFromC()].getOccupyingPiece().addMove(m);
            board[m.getToR()][m.getToC()].placePiece(board[m.getFromR()][m.getFromC()].getOccupyingPiece());
            board[m.getFromR()][m.getFromC()].empty();System.out.println("M"+m.getFromR()+" "+m.getFromC());
            movesTaken++;
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
        incoming=new Piece[8][8];
        countdown=0;
        warned = false;
        StringTokenizer st = new StringTokenizer(fen.replaceAll(" b - - 0 1",""),"/");
        for(int i = 0; i<8; i++){
            String line = st.nextToken();
            int j = 0;
            for(int k = 0, len=line.length(); k<len; k++){
                if(Character.isDigit(line.charAt(k)))j+=(line.charAt(k)-'0');
                else if(line.charAt(k)!='K'){
                    incoming[i][j]=new Piece(line.charAt(k),Game.hPush+j++*80,Game.vPush+i*80);
                    countdown++;
                }
            }
        }
    }
    public static void resetTiles() {
        for(int i = 0; i < 8; i++)for(int j = 0; j < 8; j++)if(board[i][j] != null)board[i][j].turnNormal();
    }
    public static void warn(){
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++)if(incoming[i][j]!=null)board[i][j].turnRed();
        }
        warned = true;
    }
    public static void unwarn(){
        resetTiles();
        warned = false;
    }
    public static Tile[][] getBoard(){
        return board;
    }
    public static boolean timeToMove(int i){
        return countdown==i;
    }
    public static void allowNextMove(){
        countdown--;
    }
    public static boolean enemiesDefeated() {
        if(warned) return false;
        for(Tile[] row : getBoard())for(Tile tile : row)if(tile.getOccupyingPiece() != null&&tile.getOccupyingPiece().isKing())return false;
        return true;
    }
    public static boolean isWarned() {
        return warned;
    }
    public static void wipe(){
        for(Tile[] ts : board)for(Tile t : ts)t.empty();
    }
    public static int getCountdown(){
        return countdown;
    }
}
