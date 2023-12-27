import java.lang.*;
import java.util.*;
import java.io.*;
public class EnemyTargetting  
{
    private static String testFen="2b1k3/2pp4/8/4pp2/7q/1K6/8/8 b - - 0 1";
    private static Process p;
    private static BufferedReader br;
    private static BufferedWriter bw;
    private static BoardManager.Move bestMove=new BoardManager.Move(-1,-1,-1,-1);
    public static void setup() throws IOException {
        p = Runtime.getRuntime().exec("stockfish/stockfish-windows-x86-64-avx2");
        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
    }
    public static void test() throws IOException,InterruptedException {
        updateBestMove(testFen,100);
        System.out.println(bestMove);
    }
    //values might be off if spam click Greenfoot Reset button while this is called immediately upon each reset
    private static void updateBestMove(String fen, int processTime)throws IOException,InterruptedException {
        bw.write("ucinewgame\n");
        bw.write("position fen "+fen+"\n");
        bw.write("go movetime "+processTime+"\n");
        bw.flush();
        new Thread(new Runnable() {
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s=null, latest=null;
                try{
                    while((s=br.readLine())!=null)latest=s;
                    bestMove.change(8-latest.charAt(9)+'a',latest.charAt(10)-'0',8-latest.charAt(11)+'a',latest.charAt(12)-'0');
                }catch (IOException e) {} 
                finally {
                    try{
                        br.close();
                    }catch(IOException e){}
                }
            }
        }).start();
    }
}
