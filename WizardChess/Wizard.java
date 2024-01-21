import greenfoot.*;
public class Wizard extends SuperSmoothMover{
    private static int r,c,HP,walkDirection=0,direction=0,phase=0,frame=0,rate=0,h=Game.hPush+4*80,v=Game.vPush+7*80-25, range;//,w=80,h=120;
    private static boolean walking=false, damaged=false;
    private static double degrees=0;
    private EnergyBar energyBar;
    private HPBar hpBar;
    public Wizard(){
        h=Game.hPush+4*80;
        v=Game.vPush+7*80-25;
        degrees=0;
        direction=0;
        r=7;
        c=4;
        HP=100;
        Game.grabCardAnimation();
    }
    public void act(){
        h=getX();
        v=getY();
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(mouse!=null)degrees=Utility.bearingDegreesAToB(h,v,mouse.getX(),mouse.getY());
        if(walking){
            if(++phase<=3)setLocation(getX(), getY()-10);
            else if(phase<=11){
                switch(walkDirection){
                    case 0:setLocation(getX(), getY()-10);break;
                    case 6:setLocation(getX()-10, getY());break;
                    case 4:setLocation(getX(), getY()+10);break;
                    case 2:setLocation(getX()+10, getY());break;
                }
            }
            else if(phase<=14)setLocation(getX(), getY()+10);
            else{
                walking=false;
                phase=0;
            }
        }
        else{
            if(mouse!=null&&Game.isSpellActivated())direction=Utility.direction(Utility.bearingDegreesAToB(getX(),getY(),mouse.getX(),mouse.getY()));
            setImage(new GreenfootImage("Wizard-"+direction+"-"+frame+".png"));
            if(rate==50)rate=0;
            if(rate==0)frame=0;
            else if(rate==20)frame=1;
            else if(rate==40)frame=2;
            rate++;
            Tile[][] currentBoard = BoardManager.getBoard();
            if(Game.wizardTurn()&&r!=0&&currentBoard[r-1][c].getOccupyingPiece()==null&&Greenfoot.isKeyDown("W")){
                walking=true;
                r--;
                walkDirection=0;
                if(!Game.isSpellActivated()){
                    setImage(new GreenfootImage("Wizard-0-1.png"));
                    direction=0;
                }
                decreaseE();
            }
            else if(Game.wizardTurn()&&c!=0&&currentBoard[r][c-1].getOccupyingPiece()==null&&Greenfoot.isKeyDown("A")){
                walking=true;
                c--;
                walkDirection=6;
                if(!Game.isSpellActivated()){
                    setImage(new GreenfootImage("Wizard-6-1.png"));
                    direction=6;
                }
                decreaseE();
            }
            else if(Game.wizardTurn()&&r!=7&&currentBoard[r+1][c].getOccupyingPiece()==null&&Greenfoot.isKeyDown("S")){
                walking=true;
                r++;
                walkDirection=4;
                if(!Game.isSpellActivated()){
                    setImage(new GreenfootImage("Wizard-4-1.png"));
                    direction=4;
                }
                decreaseE();
            }
            else if(Game.wizardTurn()&&c!=7&&currentBoard[r][c+1].getOccupyingPiece()==null&&Greenfoot.isKeyDown("D")){
                walking=true;
                c++;
                walkDirection=2;
                if(!Game.isSpellActivated()){
                    setImage(new GreenfootImage("Wizard-2-1.png"));
                    direction=2;
                }
                decreaseE();
            }
        }
        if(damaged){
            decreaseHP();
            damaged=false;
        }
    }
    public static int getR(){
        return r;
    }
    public static int getC(){
        return c;
    }
    public static void takeDmg(int dmg){
        SoundManager.playSound("Crunch");
        HP-=dmg;//need to check for death
        damaged=true;
    }
    public static int getHP(){
        return HP;
    }
    public static void heal(int h){
        HP+=h;
    }
    public static int getH(){
        return h;
    }
    public static int getV(){
        return v;
    }
    public static double getDegrees(){
        return degrees;
    }
    public void setEnergyBar(EnergyBar energyBar) {
        this.energyBar = energyBar;
    }
    public void setHPBar(HPBar hpBar) {
        this.hpBar = hpBar;
    }
    private void decreaseE() {
        if(energyBar!=null)energyBar.setE(energyBar.getE()-1);
    }
    private void decreaseHP() {
        if(hpBar!=null)hpBar.setHP(hpBar.getHP()-5);
    }
    public static void highlightRange(int range) {
        BoardManager.resetTiles();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Tile t = BoardManager.getTile(i, j);
                if (Utility.distance(Game.hPush+c*80,Game.vPush+r*80,t.getX(),t.getY())<range)t.turnBlue();
            }
        }
    }   
}
