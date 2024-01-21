import greenfoot.*;
import java.util.*;
public class Piece extends SuperSmoothMover
{
    private char type;//p,n,b,r,q,k
    private int MaxHP, tH,tV,movePhase=0,sH,sV,saveR, saveC;
    private Queue<BoardManager.Move> q;
    private int dying=-1;
    private static int HP;
    private boolean fix=false, awaitingDeath=false;
    public Piece(char type, int tH, int tV){
        this.type=type;
        this.tH=tH;
        this.tV=tV;
        this.sH=tH;
        this.sV=tV-30;
        q=new LinkedList<BoardManager.Move>();    
        switch(type){
            case'p':setImage(new GreenfootImage("Piece_p_3.png"));MaxHP=(int)(0.5*Game.getWave())+1;break;
            case'n':setImage(new GreenfootImage("Piece_n_3.png"));MaxHP=1*Game.getWave()+1;break;
            case'b':setImage(new GreenfootImage("Piece_b_3.png"));MaxHP=1*Game.getWave()+1;break;
            case'r':setImage(new GreenfootImage("Piece_r_3.png"));MaxHP=(int)(1.25*Game.getWave())+2;break;
            case'q':setImage(new GreenfootImage("Piece_q_3.png"));MaxHP=(int)(1.5*Game.getWave())+2;break;
            case'k':setImage(new GreenfootImage("Piece_k_3.png"));MaxHP=2*Game.getWave()+1;break;
        }
        HP=MaxHP;
    }
    public void act()
    {
        if(dying>0){
            setLocation(getX(),getY()-5);
            setImage(Utility.customize(getImage(),dying--*15));
        }
        else if(dying==0) {
            BoardManager.getBoard()[(tV-Game.vPush)/80][(tH-Game.hPush)/80].empty();
            if(type=='k')Game.kingDying();
            getWorld().removeObject(this);
        }
        else if(movePhase<8){
            setLocation(getX(),getY()-4);
            movePhase++;
            if(movePhase==8)BoardManager.allowNextMove();
        }
        else if(movePhase==8&&(!Utility.inRangeInclusive(getX(),tH-(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1),tH+(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1))||!Utility.inRangeInclusive(getY(),tV-32-(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1),tV-32+(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1)))){
            double bearing=Utility.bearingDegreesAToB(getX(),getY(),tH,tV-32);
            fix=false;
            setLocation(getX()+Math.cos(Utility.degreesToRadians(bearing))*(Utility.distance(sH,sV,tH,tV)/25+1),getY()+Math.sin(Utility.degreesToRadians(bearing))*-(Utility.distance(sH,sV,tH,tV)/25+1));
        }
        else if(!fix&&(Utility.inRangeInclusive(getX(),tH-(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1),tH+(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1))&&Utility.inRangeInclusive(getY(),tV-32-(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1),tV-32+(int)Math.ceil(Utility.distance(sH,sV,tH,tV)/25+1)))){
            fix=true;
            setLocation(tH,tV-32);
        }
        else if(movePhase<16){
            if(movePhase==14&&(tV-Game.vPush)/80==7&&type=='p')promote();
            setLocation(getX(),getY()+4);
            movePhase++;
        }
        else if(!q.isEmpty()&&BoardManager.timeToMove(q.peek().getI())){
            sV=tV;
            sH=tH;
            tV=Game.vPush+q.peek().getToR()*80;
            tH=Game.hPush+q.poll().getToC()*80;
            movePhase=0;
        }
        else if((tV-Game.vPush)/80==Wizard.getR()&&(tH-Game.hPush)/80==Wizard.getC()){
            dying=17;
            Wizard.takeDmg(96);
        }
    }
    public char getType(){
        return type;
    }
    public boolean isKing(){
        return type == 'k';
    }
    public static int getHP(){
        return HP;
    }
    public void setHP(int health){
        HP = health;
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
        //System.out.println(type+" "+dying+" "+HP);
        setImage(new GreenfootImage("Piece_"+type+"_"+(int)(Math.round((HP/(double)MaxHP)*3))+".png"));
        playDmgEffect(-dmg);
        HP-=dmg;
        if(HP<=0)dying=17;
    }
    public void playDmgEffect(int dmg) {
        getWorld().addObject(new Message((Integer.signum(dmg)==-1?"-":"+")+Math.abs(dmg),(Integer.signum(dmg)==-1?Color.RED:Color.GREEN)), getX(),getY()-30);
    }
    public void kill(){
        dying=17;
    }
    public boolean isDying(){
        return dying>=0||awaitingDeath;
    }
    public void promote(){//only to queen because for this game best value(probably)
        System.out.println(tV+" "+tH+"promote");
        HP=MaxHP;
        if(type!='p')return;
        type='q';
        setImage(new GreenfootImage("Piece_q_3.png"));
        HP=(int)(1.5*Game.getWave())+2;
    }
}
