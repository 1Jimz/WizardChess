import greenfoot.*;
public class Wizard extends Actor
{
    private static int r,c,HP,walkDirection=0,phase=0,frame=0,rate=0,w=80,h=120;
    private static boolean walking=false;
    public Wizard(){
        r=7;
        c=4;
    }
    public void act(){
        if(walking){
            if(++phase<=4)setLocation(getX(), getY()-10);
            else if(phase<=12){
                switch(walkDirection){
                    case 0:setLocation(getX(), getY()-10);break;
                    case 6:setLocation(getX()-10, getY());break;
                    case 4:setLocation(getX(), getY()+10);break;
                    case 2:setLocation(getX()+10, getY());break;
                }
            }
            else if(phase<=16)setLocation(getX(), getY()+10);
            else{
                walking=false;
                phase=0;
            }
        }
        else{
            //setImage(Utility.customize(w,h,new GreenfootImage("Wizard-"+walkDirection+"-"+frame+".png")));
            setImage(new GreenfootImage("Wizard-"+walkDirection+"-"+frame+".png"));
            if(rate==50)rate=0;
            if(rate==0)frame=0;
            else if(rate==20)frame=1;
            else if(rate==40)frame=2;
            rate++;
            System.out.println(rate+" "+frame);
            Tile[][] currentBoard = BoardManager.getBoard();
            if(r!=0&&currentBoard[r-1][c].getOccupyingPiece()==null&&Greenfoot.isKeyDown("W")){
                walking=true;
                r--;
                walkDirection=0;
                //setImage(Utility.customize(w,h,new GreenfootImage("Wizard-0-1.png")));
                setImage(new GreenfootImage("Wizard-0-1.png"));
            }
            else if(c!=0&&currentBoard[r][c-1].getOccupyingPiece()==null&&Greenfoot.isKeyDown("A")){
                walking=true;
                c--;
                walkDirection=6;
                //setImage(Utility.customize(w,h,new GreenfootImage("Wizard-6-1.png")));
                setImage(new GreenfootImage("Wizard-6-1.png"));
            }
            else if(r!=7&&currentBoard[r+1][c].getOccupyingPiece()==null&&Greenfoot.isKeyDown("S")){
                walking=true;
                r++;
                walkDirection=4;
                //setImage(Utility.customize(w,h,new GreenfootImage("Wizard-4-1.png")));
                setImage(new GreenfootImage("Wizard-4-1.png"));
            }
            else if(c!=7&&currentBoard[r][c+1].getOccupyingPiece()==null&&Greenfoot.isKeyDown("D")){
                walking=true;
                c++;
                walkDirection=2;
                //setImage(Utility.customize(w,h,new GreenfootImage("Wizard-2-1.png")));
                setImage(new GreenfootImage("Wizard-2-1.png"));
            }
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
