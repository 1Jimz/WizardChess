import greenfoot.*;
import java.io.*;
public class Board extends World
{
    public Board() throws IOException,InterruptedException{    
        super(1200, 740, 1, false);
        System.out.println("_____________________________________________________________");
        EnemyTargetting.setup();
        //each time size 80
        for(int i = 0; i<8; i++)for(int j = 0; j<8; j++)addObject(new Tile(i,j),500+i*80,100+j*80);//feel free to change 500 and 100 to move around the board
    }
}
