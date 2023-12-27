import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
public class Piece extends Actor
{
    private char type;//p,n,b,r,q,k,K(wiz)
    private int HP;
    public Piece(char type){
        this.type=type;
    }
    public void act()
    {
        // Add your action code here.
    }
    public char getType(){
        return type;
    }
    public void transverse(BoardManager.Move m){
        /*
         double bearing=Utility.bearingDegreesAToB(getX(),getY(),target.getX(),target.getY());
                direction=Utility.direction(bearing);
                attemptedMove=true;
                setLocation(getX()+Math.cos(Utility.degreesToRadians(bearing))*super.speed,getY()+Math.sin(Utility.degreesToRadians(bearing))*-super.speed);
                animate(true);
         */
        //note to myself check out ghost targetting to add here
    }
    public void attack(BoardManager.Move m){
        //note to myself check out ghost targetting to add here(maybe just use transverse)
        
        //oof
        Wizard.takeDmg(HP);
    }
    public int getHP(){
        return HP;
    }
}
