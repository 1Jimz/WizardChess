import greenfoot.*;
import java.util.*;
public class Piece extends SuperSmoothMover
{
    private char type;//p,n,b,r,q,k,K(wiz)
    private int HP, tH,tV, spd=8,movePhase=0;
    private Queue<BoardManager.Move> q;
    private int dying=-1;
    public Piece(char type, int tH, int tV){
        this.type=type;
        this.tH=tH;
        this.tV=tV;
        q=new LinkedList<BoardManager.Move>();    
        switch(type){
            case'p':setImage(new GreenfootImage("Piece_p.png"));HP=(int)(0.5*Game.getWave())+1;break;
            case'n':setImage(new GreenfootImage("Piece_n.png"));HP=1*Game.getWave()+1;break;
            case'b':setImage(new GreenfootImage("Piece_b.png"));HP=1*Game.getWave()+1;break;
            case'q':setImage(new GreenfootImage("Piece_q.png"));HP=(int)(1.5*Game.getWave())+2;break;
            case'k':setImage(new GreenfootImage("Piece_k.png"));HP=2*Game.getWave()+1;break;
        }
    }
    public void act()
    {
        if(dying>0){
            setLocation(getX(),getY()-5);
            setImage(Utility.customize(getImage(),dying--*15));
        }
        else if(dying==0)getWorld().removeObject(this);
        else if(movePhase<8){
            setLocation(getX(),getY()-4);
            movePhase++;
            //System.out.println(t+" "+"e");
        }
        else if(movePhase==8&&(!Utility.inRangeInclusive(getX(),tH-2,tH+2)||!Utility.inRangeInclusive(getY(),tV-34,tV-30))){
            double bearing=Utility.bearingDegreesAToB(getX(),getY(),tH,tV-32);
            setLocation(getX()+Math.cos(Utility.degreesToRadians(bearing))*spd,getY()+Math.sin(Utility.degreesToRadians(bearing))*-spd);
            //System.out.println(getX()+" "+getY()+" "+tH+" "+tV+" "+!Utility.inRangeInclusive(getX(),tH-2,tH+2)+" "+!Utility.inRangeInclusive(getY(),(tV-spd*8)-2,(tV-spd*8)-3+2));
        }
        else if(movePhase<16){
            //System.out.println(tH+" "+tV+" "+getY()+" "+t+" "+"R");
            setLocation(getX(),getY()+4);
            movePhase++;
        }
        else if(!q.isEmpty()){
            tV=Game.vPush+q.peek().getToR()*80;
            tH=Game.hPush+q.poll().getToC()*80;
            movePhase=0;
        }
    }
    public char getType(){
        return type;
    }
    public void attack(BoardManager.Move m){
        //note to myself check out ghost targetting to add here(maybe just use transverse)
        
        //oof
        dying=17;
        Wizard.takeDmg(HP);
    }
    public int getHP(){
        return HP;
    }
    public void addMove(BoardManager.Move m){
        q.add(m);
    }
    public int getTargetH(){
        return tH;
    }
    public int getTargetV(){
        return tV;
    }
    public void takeDmg(int dmg){
        HP-=dmg;
        if(HP<=0)dying=17;
    }
    public boolean isDying(){
        return dying>=0;
    }
}
