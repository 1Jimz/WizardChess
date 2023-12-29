import greenfoot.*;
import java.io.*;
public class Game extends World
{
    public final static int hPush=550,vPush=90;//maybe need change into private and use getter.
    private static int wave=1;
    public Game() throws IOException,InterruptedException{    
        super(1200, 740, 1, false);
        System.out.println("_____________________________________________________________");
        EnemyTargetting.setup();
        //each time size 80
        for(int i = 0; i<8; i++)for(int j = 0; j<8; j++)addObject(new Tile(i,j),hPush+j*80,vPush+i*80);
        addObject(new Wizard(),hPush+4*80,vPush+7*80-25);//need to add to board also
    }
    public static int getWave(){
        return wave;
    }
}
