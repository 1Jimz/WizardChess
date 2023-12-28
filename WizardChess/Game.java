import greenfoot.*;
import java.io.*;
public class Game extends World
{
    public final static int hPush=510,vPush=90;//feel free to change 510 and 90 to move around the board
    public Game() throws IOException,InterruptedException{    
        super(1200, 740, 1, false);
        System.out.println("_____________________________________________________________");
        EnemyTargetting.setup();
        //each time size 80
        for(int i = 0; i<8; i++)for(int j = 0; j<8; j++)addObject(new Tile(i,j),hPush+j*80,vPush+i*80);
        addObject(new Wizard(),hPush+4*80,vPush+7*80);//need to add to board also
    }
}
