import greenfoot.*;
public class Wizard extends Actor
{
    private static int r,c,HP;
    public void act()
    {
        // Add your action code here.
    }
    public static int getR(){
        return r;
    }
    public static int getC(){
        return c;
    }
    public static void takeDmg(int dmg){
        HP-=dmg;//need to check for death
    }
}
