import greenfoot.*;
public class Spell extends SuperSmoothMover{
    private int type=0,frame=0,rate=0,frameCount,adjustH,adjustV,w,h;
    private String picName;
    private static int bX,bY, range;
    private boolean placed = false;// tracks if spell was placed

    public Spell(int type){
        this.type=type;
        switch(type){
            case 0:
                setup(6,"MagicFire",-6,-56,70,145);
                range = 150;
                Game.getWizard().setRange(range);
                break;
            case 1:
                setup(6,"MagicFire",-6,-56,70,145);
                range = 150;
                Game.getWizard().setRange(range);
                break;
        }
    }
    public void act(){//remember to deactivate spell after
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(!placed && mouse!=null){
            setLocation(mouse.getX()+adjustH,mouse.getY()+adjustV);
        }
        if(!placed && mouse!=null&&Greenfoot.mouseClicked(null)){
            clicked(mouse, Game.getWizard());
        }

        if(rate==5){
            rate=0;
            if(++frame==frameCount)frame=0;
            setImage(Utility.customize(w,h,new GreenfootImage(picName+"_"+frame+".png")));
        }
        else rate++;
    }
    private void clicked(MouseInfo mouse, Wizard w){
        bX = (mouse.getX()-Game.hPush+40)/80;
        bY = (mouse.getY()-Game.vPush+40)/80;
        
        //System.out.println((mouse.getX()-Game.hPush+40)/80+" "+(mouse.getY()-Game.vPush+40)/80);
       // System.out.println(bX + " " + bY);
        //setLocation(mouse.getX(),mouse.getY());
        
        playSpell();
    }
    private void playSpell(){
        Tile t = BoardManager.getBoard(bX, bY);
        if(t!=null&&t.isBlue()){
            //System.out.println("working");
            placed = true;
            setLocation(Game.hPush+bX*80-10,Game.vPush+bY*80-40);

            if(BoardManager.getBoard(bX,bY)!=null){
                BoardManager.getBoard(bX,bY).turnGreen();
                //System.out.println("green");
            }
            
            if(BoardManager.getBoard(bY,bX).getOccupyingPiece()!=null){
                BoardManager.getBoard(bY,bX).getOccupyingPiece().takeDmg(10);
                //System.out.println("damage taken");
            }
            Game.deactivateSpell();
        }
    }
    public static int getRange(){
        return range;
    }
    private void setup(int frameCount, String picName, int adjustH, int adjustV, int w, int h){
        this.frameCount=frameCount;
        this.picName=picName;
        this.adjustH=adjustH;
        this.adjustV=adjustV;
        this.w=w;
        this.h=h;
    }
}
