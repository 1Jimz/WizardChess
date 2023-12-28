import greenfoot.*;
public class Wizard extends Actor
{
    private static int r,c,HP,walkType=-1,phase=0;
    private static boolean walking=false;
    public Wizard(){
        r=7;
        c=4;
    }
    public void act(){
        if(walking){
            if(++phase<=4)setLocation(getX(), getY()-10);
            else if(phase<=12){
                switch(walkType){
                    case 0:setLocation(getX(), getY()-10);break;
                    case 1:setLocation(getX()-10, getY());break;
                    case 2:setLocation(getX(), getY()+10);break;
                    case 3:setLocation(getX()+10, getY());break;
                }
            }
            else if(phase<=16)setLocation(getX(), getY()+10);
            else{
                walking=false;
                phase=0;
            }
        }
        else{
            Tile[][] currentBoard = BoardManager.getBoard();
            if(r!=0&&currentBoard[r-1][c].getOccupyingPiece()==null&&Greenfoot.isKeyDown("W")){
                walking=true;
                r--;
                walkType=0;
            }
            else if(c!=0&&currentBoard[r][c-1].getOccupyingPiece()==null&&Greenfoot.isKeyDown("A")){
                walking=true;
                c--;
                walkType=1;
            }
            else if(r!=7&&currentBoard[r+1][c].getOccupyingPiece()==null&&Greenfoot.isKeyDown("S")){
                walking=true;
                r++;
                walkType=2;
            }
            else if(c!=7&&currentBoard[r][c+1].getOccupyingPiece()==null&&Greenfoot.isKeyDown("D")){
                walking=true;
                c++;
                walkType=3;
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
