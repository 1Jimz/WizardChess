import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
public class Piece extends SuperSmoothMover
{
    private char type;//p,n,b,r,q,k,K(wiz)
    private int HP, tH,tV;
    public Piece(char type, int tH, int tV){
        this.type=type;
        this.tH=tH;
        this.tV=tV;
    }
    public void act()
    {
        //will stop as need to setup tX and tY
        if(getX()!=tH||getY()!=tV){
            double bearing=Utility.bearingDegreesAToB(getX(),getY(),tH,tV);
            setLocation(getX()+Math.cos(Utility.degreesToRadians(bearing))*1,getY()+Math.sin(Utility.degreesToRadians(bearing))*-1);
        }
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
    public int getTargetH(){
        return tH;
    }
    public int getTargetV(){
        return tV;
    }
}
