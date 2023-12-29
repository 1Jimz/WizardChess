import greenfoot.*;
import java.util.List;
//original code from https://www.greenfoot.org/scenarios/925. Modified by Jimmy Zhu at 12/19/23.
public class Card extends SuperSmoothMover{
    private List springs;
    private double x1,y1,x2=0,y2=0,r1,r2,a,b,c,d,l,ang,dl[]=new double[6],x[]={-100,100,100,-100},y[]={-160,-160,160,160},xprev[]=new double[4],yprev[]=new double[4],xv[]={0,0,0,0},yv[]={0,0,0,0};
    private boolean drag=false,stick=false,leftBorder;
    private int type=1,point1,point2,mx,my,pointer=0,p=0,points[]=new int[4],conect1[]={0,1,2,3,0,1},conect2[]={1,2,3,0,2,3},active;
    public Card(int mx, int my, int active,boolean leftBorder){
        this.mx=mx;//800
        this.my=my;//200
        this.active=active;
        this.leftBorder=leftBorder;
        for(int f=0;f<6;f++)dl[f] = Math.sqrt(Math.pow((x[conect1[f]] - x[conect2[f]]),2)+Math.pow((y[conect1[f]] - y[conect2[f]]),2));
        setImage(new GreenfootImage("Testcardfront2.png"));
    }
    public void addedToWorld(World w){
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
        
    }    
    public boolean pointIn(double a,double b){
        int[] testPoints = {0,1,2,3,0};
        boolean in=true;
        double x1,x0,y1,y0;
        for(int i=0;i<x.length;i++){
            x0 = x[testPoints[i]];
            x1 = x[testPoints[i+1]];
            y0 = y[testPoints[i]];
            y1 = y[testPoints[i+1]];
            in = in && ((b-y0)*(x1-x0)-(a-x0)*(y1-y0)>=0);
        }
        return in;
    }
    public void rebound(Card s){
        double time=0;
        boolean in = true;  
        Utility.Vector[] normals = new Utility.Vector[pointer];
        for(int i=0;i<pointer;i++){
            normals[i] = s.normal(xprev[points[i]],yprev[points[i]]);
            if(normals[i] == null){
                for(int j=0;j<4;j++){
                    x[j] = xprev[j];
                    y[j] = yprev[j];
                    s.x[j] = s.xprev[j];
                    s.y[j] = s.yprev[j]; 
                }
                return;
            } 
            normals[i] = normals[i].getUnitVector();
        }
        double[] biasA = new double[pointer],biasB = new double[pointer];
        double dist;
        for(int i=0;i<pointer;i++){
            dist = Math.sqrt(Math.pow(s.x[s.point1]-s.x[s.point2],2)+Math.pow(s.y[s.point1]-s.y[s.point2],2));
            biasA[i]=Math.sqrt(Math.pow(s.x[s.point1]-x[points[i]],2)+Math.pow(s.y[s.point1]-y[points[i]],2));
            biasB[i]=Math.sqrt(Math.pow(s.x[s.point2]-x[points[i]],2)+Math.pow(s.y[s.point2]-y[points[i]],2));
            biasA[i]=(dist-biasA[i])/dist;
            biasB[i]=(dist-biasB[i])/dist;
            if(biasB[i]>.95||biasB[i]<.05){
                for(int f=0;f<4;f++){
                    double vx = getX()-s.getX();
                    double vy = getY()-s.getY();
                    xv[f] = .0001*vx;
                    yv[f] = .0001*vy;
                    s.xv[f] = -.0001*vx;
                    s.yv[f] = -.0001*vy;
                }
                return;
            }
        }
        while(in){
            in = false;
            for(int i=0;i<pointer;i++){
                if(!stick){
                    x[points[i]] += normals[i].xPart()/50.0;
                    y[points[i]] += normals[i].yPart()/50.0;
                    xv[points[i]] += normals[i].xPart()/100.0;
                    yv[points[i]] += normals[i].yPart()/100.0;
                }
                s.nudge(-normals[i].xPart()/50.0 , -normals[i].yPart()/50.0, biasA[i], biasB[i]);
            }
        }   
    }  
    public Utility.Vector normal(double a, double b){
        int[] testPoints = {0,1,2,3,0};
        double x1,x0,y1,y0;
        Utility.Vector n = null;
        int tries=0;
        for(int i=0;i<x.length;i++){
            x0 = xprev[testPoints[i]];
            x1 = xprev[testPoints[i+1]];
            y0 = yprev[testPoints[i]];
            y1 = yprev[testPoints[i+1]];
            if((b-y0)*(x1-x0)-(a-x0)*(y1-y0)<=0){
                point1 = testPoints[i];
                point2 = testPoints[i+1];
                n = new Utility.Vector(y1-y0,x0-x1);
                tries++;
            }    
        }
        if(n == null)System.out.println("error!!!!");
        if(tries>1)System.out.println("overload!!");
        return n;
    }
    public void step(double time, int dir){
        for(int i=0;i<4;i++){
            x[i] += dir*time*xv[i];
            y[i] += dir*time*yv[i];
            if(!stick)yv[i] += dir*time*.01;
        }
    }
    public void nudge(double a, double b,double c, double d){
        xv[point1] += a*c;
        xv[point2] += a*d;
        yv[point1] += b*c;
        yv[point2] += b*d;
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
