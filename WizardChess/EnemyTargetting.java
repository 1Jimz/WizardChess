import java.lang.*;
import java.util.*;
import java.io.*;
public class EnemyTargetting  
{
    private static String testFen="2b1k3/2pp4/8/4pp2/7q/1K6/8/8 b - - 0 1";
    private static Process p;
    private static BufferedReader br;
    private static BufferedWriter bw;
    //private static BoardManager.Move m=new BoardManager.Move(-1,-1,-1,-1);
    public static void setup() throws IOException {
        p = Runtime.getRuntime().exec("stockfish/stockfish-windows-x86-64-avx2");
        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        //test();
    }
    public static void test() throws IOException,InterruptedException {
        System.out.println(bestMove(testFen,5,20));
        //System.out.println(bestMove);
        bestMove("8/4k3/8/8/8/4K3/8/8 b - - 0 1",5,20);
        //System.out.println(bestMove);
        bestMove("6pk/6pp/8/8/8/4K3/8/8 b - - 0 1",5,20);
        //System.out.println(bestMove);
    }
    public static BoardManager.Move bestMove(String fen, int depth, int processTime)throws IOException,InterruptedException {//
       bw.write("ucinewgame\n");
       bw.write("position fen "+fen+"\n");
       bw.write("go depth "+depth+" movetime "+processTime+"\n");
       bw.flush();
       BoardManager.Move m = new BoardManager.Move(-1,-1,-1,-1);
       Thread t = new Thread(new Runnable() {
           public void run() {
               BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
               String latest="";
               while(true){
                   try{
                       latest=br.readLine();
                   }catch (IOException e){}
                   //System.out.println(s+" "+"looped");
                   if(latest.contains("bestmove")){
                       //System.out.println(latest+" "+"ended");
                       m.change(8-latest.charAt(10)+'0',latest.charAt(9)-'a',8-latest.charAt(12)+'0',latest.charAt(11)-'a');
                       return;
                   }
               }
           }
       });
       t.start();
       t.join();
       //System.out.println(m);
       return m;
    }
    public static Deque<BoardManager.Move> ram(){
        Deque<BoardManager.Move> dq = new LinkedList<BoardManager.Move>();
        Tile[][] currentBoard = BoardManager.getBoard();
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                if(currentBoard[i][j].getOccupyingPiece()==null)continue;
                switch(currentBoard[i][j].getOccupyingPiece().getType()){
                    case 'p':
                        if(Wizard.getR()==i+1&&(Wizard.getC()==j+1||Wizard.getC()==j-1))dq.add(new BoardManager.Move(i,j,Wizard.getR(),Wizard.getC()));
                        break;
                    case 'n':
                        if(Wizard.getR()==i+2&&Wizard.getC()==j+1);
                        else if(Wizard.getR()==i+2&&Wizard.getC()==j-1);
                        else if(Wizard.getR()==i+1&&Wizard.getC()==j+2);
                        else if(Wizard.getR()==i+1 && Wizard.getC()==j-2);
                        else if(Wizard.getR()==i-1 && Wizard.getC()==j+2);
                        else if(Wizard.getR()==i-1 && Wizard.getC()==j-2);
                        else if(Wizard.getR()==i-2 && Wizard.getC()==j+1);
                        else if(Wizard.getR()==i-2 && Wizard.getC()==j-1);
                        break;
                    case 'b':
                        //for(int i = 0; i < 8; i++)
                        break;
                    case 'r':
                        break;
                    case 'q':
                        break;
                    case 'k':
                        break;
                }
            }
        }
        //while(dq.size()>cap)dq.removeLast();
        return dq;
    }
}
