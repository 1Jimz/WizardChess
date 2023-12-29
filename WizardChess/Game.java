import greenfoot.*;
import java.io.*;
import java.util.*;
public class Game extends World
{
    public final static int hPush=550,vPush=90;//maybe need change into private and use getter.
    private static boolean throwingCard=false, pickCard=false,leftBorder;
    private static int wave=1,throwX, throwY, throwActive, startX, startY;
    //Thing that happens if two pieces step on the same tile at once during their movement. This is completely normal. Not a bug.
    public Game() throws IOException,InterruptedException{    
        super(1200, 740, 1, false);
        System.out.println("_____________________________________________________________");
        EnemyTargetting.setup();
        //each time size 80
        for(int i = 0; i<8; i++)for(int j = 0; j<8; j++)addObject(new Tile(i,j),hPush+j*80,vPush+i*80);
        addObject(new Wizard(),hPush+4*80,vPush+7*80-25);//need to add to board also
        //addObject(new Card(800,200,1000,false),300,475);
        //addObject(new Card(460,200,420,false),-100,1000);
        //addObject(new Card(350,100,400,false),-50,1000);
        //addObject(new Card(150,-200,910,true),600,1000);
    }
    public static int getWave(){
        return wave;
    }
    public static void throwCard(int tX, int tY, int tA, int lB, int sX, int sY){
        throwX=tX;
        throwY=tY;
        throwActive=tA;
        leftBorder=lB!=0;
        startX=sX;
        startY=sY;
        throwingCard=true;
        System.out.println("ASDASD");
    }
    public void act(){
        if(pickCard){
            addObject(new Hand(),-120,510);
            pickCard=false;
        }
        if(throwingCard){
            addObject(new Card(throwX,throwY,throwActive,leftBorder),startX,startY);
            throwingCard=false;
        }
        List<Card> cards = getObjects(Card.class);
        for(int i=0;i<130;i++)for(Card c : cards)c.simulate(1);
    }
    public static void grabCardAnimation(){
        pickCard=true;
    }
}
