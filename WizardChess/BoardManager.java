import java.util.*;
import java.io.*;
public class BoardManager  
{
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
            return "("+fromR+", "+fromC+")-->("+toR+", "+toC+")";
        }
    }
    private static Tile[][] board = new Tile[8][8];
    private static Piece[][] incoming = new Piece[8][8];
    public static void placeTile(Tile t, int r, int c){
        board[r][c]=t;
    }
    public static void makeMove(Move m){
        board[m.getFromR()][m.getFromC()].getOccupyingPiece().transverse(m);
        board[m.getToR()][m.getToC()].placePiece(board[m.getFromR()][m.getFromC()].getOccupyingPiece());
        board[m.getFromR()][m.getFromC()].placePiece(null);
    }
    public static void attackWizard(Move m){
        board[m.getFromR()][m.getFromC()].getOccupyingPiece().attack(m);
        //remember to oof piece
        //the take dmg part in piece
    }
    public static void enemyTurn(int cap, int depth, int processTime) throws InterruptedException, IOException {
        int movesTaken=0;
        while(movesTaken!=cap){
            Deque<Move> dq = EnemyTargetting.ram();
            if(dq.size()>cap-movesTaken)dq.removeLast();
            movesTaken+=dq.size();
            while(!dq.isEmpty())attackWizard(dq.poll());
            //
            if(movesTaken==cap)break;
            makeMove(EnemyTargetting.bestMove(currentFEN(), depth, processTime));
            movesTaken++;
        }
    }
    private static String currentFEN(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<8; i++){
            int cnt = 0;
            for(int j = 0; j<8; j++){
                if(board[i][j].getOccupyingPiece()==null)cnt++;
                else {
                    if(cnt!=0){
                        sb.append(cnt);
                        cnt=0;
                    }
                    sb.append(board[i][j].getOccupyingPiece().getType());
                }
            }
            if(cnt!=0)sb.append(cnt);
            if(i!=7)sb.append("/");
        }
        sb.append(" b - - 0 1");
        return sb.toString();
    }
    //2b1kq2/2pppp2/8/8/8/4K3/8/8 w - - 0 1
    /*
     double bearing=Utility.bearingDegreesAToB(getX(),getY(),target.getX(),target.getY());
                direction=Utility.direction(bearing);
                attemptedMove=true;
                setLocation(getX()+Math.cos(Utility.degreesToRadians(bearing))*super.speed,getY()+Math.sin(Utility.degreesToRadians(bearing))*-super.speed);
                animate(true);
     */
    /*
        public static int direction(double angle){
        //angle based on unconventional 0~359 bearing that starts at E and goes CCW. E=0, NE=1, N=2, NW=3, W=4, SW=5, S=6, SE=7
        if(angle<=22.5||angle>337.5)return 0;
        if(angle<=67.5&&angle>22.5)return 1;
        if(angle<=112.5&&angle>67.5)return 2;
        if(angle<=157.5&&angle>112.5)return 3;
        if(angle<=202.5&&angle>157.5)return 4;
        if(angle<=247.5&&angle>202.5)return 5;
        if(angle<=292.5&&angle>247.5)return 6;
        if(angle<=337.5&&angle>292.5)return 7;
        return -1;
    }
     */
    public static void spawnPieces(){
        for(int i = 0; i<8; i++)for(int j = 0; j<8; j++)if(incoming[i][j]!=null)board[i][j].placePiece(incoming[i][j]);
    }
    public static void createIncoming(){//String fen){
        String fen = "2b1kq2/2pppp2/8/8/8/4K3/8/8 b - - 0 1";//temp
        StringTokenizer st = new StringTokenizer(fen.replaceAll(" b - - 0 1",""),"/");
        for(int i = 0; i<8; i++){
            String line = st.nextToken();
            int j = 0;
            for(int k = 0, len=line.length(); k<len; k++){
                //System.out.println(j);
                //System.out.println(line);
                if(Character.isDigit(line.charAt(k)))j+=(line.charAt(k)-'0');
                else if(line.charAt(k)!='K')incoming[i][j++]=new Piece(line.charAt(k),Game.hPush+j*80,Game.vPush+i*80);//ignore K
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
