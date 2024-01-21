import greenfoot.*;
import java.util.List;
//original code from https://www.greenfoot.org/scenarios/925. Modified(Modified maybe understatement. So much so that I might not need credit, it's more like inspired kinda) by Jimmy Zhu at 12/19/23.
public class Card extends SuperSmoothMover{
    private List springs;
    private double x1,y1,x2=0,y2=0,r1,r2,a,b,c,d,l,ang,dl[]=new double[6],x[]={-100,100,100,-100},y[]={-160,-160,160,160},xprev[]=new double[4],yprev[]=new double[4],xv[]={0,0,0,0},yv[]={0,0,0,0};
    private boolean drag=false,stick=false,leftBorder, disposing=false;
    private int point1,point2,mx,my,pointer=0,p=0,points[]=new int[4],conect1[]={0,1,2,3,0,1},conect2[]={1,2,3,0,2,3},active,type,whirl=0;
    public Card(int mx, int my, int active,boolean leftBorder){
        this.mx=mx;//800
        this.my=my;//200
        this.active=active;
        this.leftBorder=leftBorder;
        type=Greenfoot.getRandomNumber(5);
        for(int f=0;f<6;f++)dl[f] = Math.sqrt(Math.pow((x[conect1[f]] - x[conect2[f]]),2)+Math.pow((y[conect1[f]] - y[conect2[f]]),2));
        switch(type){
            case 0:setImage(new GreenfootImage("portalCard.png"));break;
            case 1:setImage(new GreenfootImage("explosionCard.png"));break;
            case 2:setImage(new GreenfootImage("bubbleCard.png"));break;
            case 3:setImage(new GreenfootImage("slashCard.png"));break;
            case 4:setImage(new GreenfootImage("healCard.png"));break;
        }
        //setImage(new GreenfootImage("Testcardfront2.png"));
    }
    public void addedToWorld(World w){
        //getWorld().addObject(new CardHitbox(mx,my,active,leftBorder,this),getX(),getY());
        for(int i=0;i<x.length;i++){
            x[i] += getX();
            y[i] += getY();
        }
        springs = getWorld().getObjects(Card.class);
        for(int i=0;i<springs.size();i++)((Card)springs.get(i)).getWorld().getObjects(Card.class);
        for(int i=0;i<4;i++){
            xprev[i] = x[i];
            yprev[i] = y[i];
        }
    }
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(whirl==0&&mouse!=null&&Greenfoot.mouseClicked(this))whirl++;
        if(whirl>0&&whirl<35&&mouse!=null){
            whirl++;
            getImage().scale(getImage().getWidth()-5,getImage().getHeight()-5);
            getImage().rotate(whirl);
            getImage().setTransparency(getImage().getTransparency()-5);
            setLocation(mouse.getX()+(Greenfoot.getRandomNumber(2)==0?-Greenfoot.getRandomNumber(7):Greenfoot.getRandomNumber(7)),mouse.getY()+(Greenfoot.getRandomNumber(2)==0?-Greenfoot.getRandomNumber(7):Greenfoot.getRandomNumber(7)));
        }
        else if(whirl==35&&mouse!=null){
            whirl++;
            Game.activateSpell();
            getWorld().addObject(new Spell(type), mouse.getX(), mouse.getY());
            getWorld().addObject(new Wand(),-200,-200);
        }
        else if(whirl>35)getWorld().removeObject(this);
        else{
            for(int i=0;i<130;i++)simulate(1);
            if(disposing&&getY()>900){
                Game.grabCardAnimation();
                getWorld().removeObject(this);
            }
            else if(Greenfoot.isKeyDown("G"))dispose();//also need to make sure have enough ep
        }
    }
    public void whirlUp(){
        whirl++;
    }
    public void dispose(){
        SoundManager.playSound("High Whoosh");
        active=1000;
        mx=Greenfoot.getRandomNumber(201)+200;
        my=Greenfoot.getRandomNumber(201)+1100;
        leftBorder=false;
        disposing=true;
    }
    public void simulate(int k){
        drag=true;
        if(active==0)drag=false;
        else active--;
        if(stick)return;
        for(int i=0;i<4;i++){
            xprev[i] = x[i];
            yprev[i] = y[i];
        }
        if(drag){
            yv[0] += (my - y[0])/1500000.0;
            xv[0] += (mx - x[0])/1500000.0;
        }
        for(int m=0;m<k;m++){
            for(int f=0;f<6;f++){
                l = Math.sqrt(Math.pow((x[conect1[f]] - x[conect2[f]]),2)+Math.pow((y[conect1[f]] - y[conect2[f]]),2));
                l = dl[f]-l;
                l = l/10;
                ang = Math.atan2(y[conect1[f]]-y[conect2[f]],x[conect1[f]]-x[conect2[f]]);
                xv[conect1[f]] = xv[conect1[f]] + l*Math.cos(ang);
                xv[conect2[f]] = xv[conect2[f]] - l*Math.cos(ang);
                yv[conect1[f]] = yv[conect1[f]] + l*Math.sin(ang);
                yv[conect2[f]] = yv[conect2[f]] - l*Math.sin(ang);
            }
            for(int f=0;f<4;f++){
                xv[f] = .9999*xv[f];
                yv[f] = .9999*yv[f];
                x[f] = x[f] + xv[f];
                y[f] = y[f] + yv[f];
                if(y[f]<0){
                    yv[f] = 0;
                    y[f]=0;
                    xv[f]=.9*xv[f];
                }
                if(leftBorder&&x[f]<0){
                    yv[f]=.9*yv[f];
                    x[f]=0;
                    xv[f]=0;
                }
                if(x[f]>1200){
                    yv[f]=.9*yv[f];
                    x[f]=1200;
                    xv[f]=0;
                }
            } 
        }
        x1 = (x[0]+x[1]+x[2]+x[3])/4.0;
        y1 = (y[0]+y[1]+y[2]+y[3])/4.0;
        r1 = 180*Math.atan2(y[0]-y[1],x[0]-x[1])/Math.PI;
        setLocation((int)x2,(int)y2);
        setRotation((int)r2);
        if(Math.abs(x1-x2)>1.5 || Math.abs(y1-y2)>1.5 || Math.abs(r1-r2)>1.5){
            r2 = r1;
            x2 = x1;
            y2 = y1;
        }
    }
}
