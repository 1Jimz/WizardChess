import greenfoot.*;
import java.util.*;
public class Piece extends SuperSmoothMover
{
    private char type;//p,n,b,r,q,k,K(wiz)
    private int HP, tH,tV,movePhase=0,sH,sV;
    private Queue<BoardManager.Move> q;
    private int dying=-1;
    private boolean fix=false, awaitingDeath=false;
    private GreenfootImage pieceImage;    
    
    public Piece(char type, int tH, int tV, int sH, int sV){
        this.type=type;
        this.tH=tH;
        this.tV=tV;
        this.sH=sH;
        this.sV=sV;
        q=new LinkedList<BoardManager.Move>();    
        switch(type){
            case'p':
                pieceImage = new GreenfootImage("basePawn1.png");
                HP=(int)(0.5*Game.getWave())+1;
                break;
            case'n':HP=1*Game.getWave()+1;pieceImage = new GreenfootImage("baseKnight.png");break;
            case'b':HP=1*Game.getWave()+1;pieceImage = new GreenfootImage("baseBishop.png");break;
            case'q':HP=(int)(1.5*Game.getWave())+2;pieceImage = new GreenfootImage("baseQueen.png");break;
            case'k':HP=2*Game.getWave()+1;pieceImage = new GreenfootImage("baseKing.png");break;
        }
        setImage(pieceImage);
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
        else if(movePhase==8&&(!Utility.inRangeInclusive(getX(),tH-(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1),tH+(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1))||!Utility.inRangeInclusive(getY(),tV-32-(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1),tV-32+(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1)))){
            double bearing=Utility.bearingDegreesAToB(getX(),getY(),tH,tV-32);
            fix=false;
            //System.out.println(spd);
            setLocation(getX()+Math.cos(Utility.degreesToRadians(bearing))*(Utility.distance(sH,sV,tH,tV)/25+1),getY()+Math.sin(Utility.degreesToRadians(bearing))*-(Utility.distance(sH,sV,tH,tV)/25+1));
            //System.out.println(getX()+" "+getY()+" "+tH+" "+tV+" "+!Utility.inRangeInclusive(getX(),tH-2,tH+2)+" "+!Utility.inRangeInclusive(getY(),(tV-spd*8)-2,(tV-spd*8)-3+2));
        }
        else if(!fix&&(Utility.inRangeInclusive(getX(),tH-(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1),tH+(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1))&&Utility.inRangeInclusive(getY(),tV-32-(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1),tV-32+(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1)))){
            fix=true;
            setLocation(tH,tV-32);
        }
        else if(movePhase<16){
            //System.out.println(tH+" "+tV+" "+getY()+" "+t+" "+"R");
            setLocation(getX(),getY()+4);
            movePhase++;
            if(awaitingDeath)dying=17;
        }
        else if(!q.isEmpty()){
            sV=tV;
            sH=tH;
            tV=Game.vPush+q.peek().getToR()*80;
            tH=Game.hPush+q.poll().getToC()*80;
            movePhase=0;
        }
    }
    public char getType(){
        return type;
    }
    public void attack(BoardManager.Move m){
        q.add(m);
        System.out.println(m);
        awaitingDeath=true;
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
        return dying>=0||awaitingDeath;
    }
    public void promote(){//only to queen because for this game best value(probably)
        if(type!='p')return;
        type='q';
        setImage(new GreenfootImage("Piece_q.png"));
        HP=(int)(1.5*Game.getWave())+2;
    }
}
