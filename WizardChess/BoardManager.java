import java.util.*;
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
    public static String currentFEN(){
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
                else if(line.charAt(k)!='K')incoming[i][j++]=new Piece(line.charAt(k));//ignore K
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
