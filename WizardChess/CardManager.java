import greenfoot.*;
public class CardManager  {
    //drag point x, drag point y, drag active, left border(0:false, 1:true), spawn x, spawn y
    private static int cardThrows[][] = {
        {440,180,430,0,-100,1000},
        {350,100,400,0,-50,1000},
        {0,-1280,721,1,370,994},
        {258,-970,425,1,185,1000},
        {0,-400,1090,1,300,1000},
        {-300,-1100,650,1,500,1200},
        {420,-2600,215,1,280,1000},
        {200,-400,620,1,300,1150}
    };
    public static void readyThrowCard(){
        int random=Greenfoot.getRandomNumber(cardThrows.length);
        Game.throwCard(cardThrows[random][0], cardThrows[random][1], cardThrows[random][2], cardThrows[random][3], cardThrows[random][4], cardThrows[random][5]);
    }
}
