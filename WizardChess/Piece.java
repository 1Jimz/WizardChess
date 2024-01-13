import greenfoot.*;
import java.util.*;
public class Piece extends SuperSmoothMover
{
    private char type;//p,n,b,r,q,k,K(wiz)
    private int HP, tH,tV,movePhase=0,sH,sV,saveR, saveC;
    private Queue<BoardManager.Move> q;
    private int dying=-1;
    private boolean fix=false, awaitingDeath=false;
    public Piece(char type, int tH, int tV, int sH, int sV){
        this.type=type;
        this.tH=tH;
        this.tV=tV;
        this.sH=sH;
        this.sV=sV;
        q=new LinkedList<BoardManager.Move>();    
        switch(type){
            case'p':setImage(new GreenfootImage("Piece_p.png"));HP=(int)(0.5*Game.getWave())+1;break;
            case'n':setImage(new GreenfootImage("Piece_n.png"));HP=1*Game.getWave()+1;break;
            case'b':setImage(new GreenfootImage("Piece_b.png"));HP=1*Game.getWave()+1;break;
            case'r':setImage(new GreenfootImage("Piece_r.png"));HP=(int)(1.25*Game.getWave())+2;break;
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
            if(movePhase==8)BoardManager.allowNextMove();
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
            //if(movePhase==8)BoardManager.allowNextMove();
            //System.out.println(tH+" "+tV+" "+getY()+" "+t+" "+"R");
            setLocation(getX(),getY()+4);
            movePhase++;
            if(q.isEmpty()&&awaitingDeath)dying=17;
            //if(awaitingDeath)dying=17;
        }
        else if(!q.isEmpty()&&BoardManager.timeToMove(q.peek().getI())){
            System.out.println(q.peek().getI());
            sV=tV;
            sH=tH;
            tV=Game.vPush+q.peek().getToR()*80;
            saveR=q.peek().getToR();
            saveC=q.peek().getToR();
            tH=Game.hPush+q.poll().getToC()*80;
            movePhase=0;
        }
    }
    public char getType(){
        return type;
    }
    public void attack(BoardManager.Move m){
        q.add(m);
        System.out.println("eee "+m);
        awaitingDeath=true;
        //BoardManager.getTile(saveR,saveC).removePiece();
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
        System.out.println(tV+" "+tH+"promote");
        if(type!='p')return;
        type='q';
        setImage(new GreenfootImage("Piece_q.png"));
        HP=(int)(1.5*Game.getWave())+2;
    }
}
