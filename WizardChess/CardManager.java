import greenfoot.*;
public class CardManager  {
    //drag point x, drag point y, drag active, left border(0:false, 1:true), spawn x, spawn y
    private static int cardThrows[][] = {
        {460,200,420,0,-100,1000},
        {350,100,400,0,-50,1000},
        {150,-200,910,1,600,1000}//,
    };
    public static void readyThrowCard(){
        int random=Greenfoot.getRandomNumber(cardThrows.length);
        Game.throwCard(cardThrows[random][0], cardThrows[random][1], cardThrows[random][2], cardThrows[random][3], cardThrows[random][4], cardThrows[random][5]);
    }
}
