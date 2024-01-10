import greenfoot.*;
public class Spell extends SuperSmoothMover{
    private static int type=0;
    private int frame=0,rate=0,frameCount,adjustH,adjustV,w,h;
    private String picName;
    private boolean placed = false;// tracks if spell was placed
    public Spell(int type){
        this.type=type;
        switch(type){
            case 0:setup(6,"MagicFire",-6,-56,70,145);break;
            case 1:setup(6,"MagicFire",-6,-56,70,145);break;
        }
    }
    public void act(){//remember to deactivate spell after
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(!placed && mouse!=null)setLocation(mouse.getX()+adjustH,mouse.getY()+adjustV);
        if(rate==5){
            rate=0;
            if(++frame==frameCount)frame=0;
            setImage(Utility.customize(w,h,new GreenfootImage(picName+"_"+frame+".png")));
        }
        else rate++;
        
        if(Greenfoot.mouseClicked(this) && Game.isSpellActivated()){
            placed = true;
            int pixelX = getX(), pixelY = getY();
        
            // Convert pixel coordinates to board indices
            BoardManager.Position boardPos = Game.convPixToTile(pixelX, pixelY);
        
            // Apply spell to the converted position
            BoardManager.applySpell(boardPos);
        
            //getWorld().addObject(new Spell(getSpellType()), pixelX, pixelY);
            Game.deactivateSpell();
        }

    }
    private void setup(int frameCount, String picName, int adjustH, int adjustV, int w, int h){
        this.frameCount=frameCount;
        this.picName=picName;
        this.adjustH=adjustH;
        this.adjustV=adjustV;
        this.w=w;
        this.h=h;
    }
    public static int getSpellType() {
        return type;
    }
}
