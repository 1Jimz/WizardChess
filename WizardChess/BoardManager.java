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
    public static void placeTile(Tile t, int r, int c){
        board[r][c]=t;
    }
}
