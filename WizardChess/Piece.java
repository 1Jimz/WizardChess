import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
public class Piece extends SuperSmoothMover
{
    private char type;//p,n,b,r,q,k,K(wiz)
    private int HP, tH,tV, spd=3;
    public Piece(char type, int tH, int tV){
        this.type=type;
        this.tH=tH;
        this.tV=tV;
    }
    public void act()
    {
        if(getX()!=tH||getY()!=tV){
            double bearing=Utility.bearingDegreesAToB(getX(),getY(),tH,tV);
            setLocation(getX()+Math.cos(Utility.degreesToRadians(bearing))*spd,getY()+Math.sin(Utility.degreesToRadians(bearing))*-spd);
        }
    }
    public char getType(){
        return type;
    }
    public void transverse(BoardManager.Move m){
        tV=Game.vPush+m.getToR()*80;
        tH=Game.hPush+m.getToC()*80;
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
