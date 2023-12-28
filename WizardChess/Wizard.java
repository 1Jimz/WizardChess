import greenfoot.*;
public class Wizard extends Actor
{
    private static int r,c,HP;
    private static boolean walk=true;
    public Wizard(){
        r=7;
        c=4;
    }
    public void act()
    {
        if(!Greenfoot.isKeyDown("W")&&!Greenfoot.isKeyDown("A")&&!Greenfoot.isKeyDown("S")&&!Greenfoot.isKeyDown("D"))walk=true;
        if(r!=0&&walk&&Greenfoot.isKeyDown("W")){
            walk=false;
            r--;
            setLocation(getX(), getY()-80);
        }
        else if(c!=0&&walk&&Greenfoot.isKeyDown("A")){
            walk=false;
            c--;
            setLocation(getX()-80, getY());
        }
        else if(r!=7&&walk&&Greenfoot.isKeyDown("S")){
            walk=false;
            r++;
            setLocation(getX(), getY()+80);
        }
        else if(c!=7&&walk&&Greenfoot.isKeyDown("D")){
            walk=false;
            c++;
            setLocation(getX()+80, getY());
        }
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
