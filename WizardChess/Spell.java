import greenfoot.*;
public class Spell extends SuperSmoothMover{
    private int type=0,frame=0,rate=0,frameCount,adjustH,adjustV,w,h,fadeTime;
    private String picName;
    private static int bC,bR,range,lastHighlightedC = -1,lastHighlightedR = -1;
    private boolean placed = false,fading = false;
    public Spell(int type){
        this.type=type;
        switch(type){
            case 0:
                setup(6,"MagicFire",-6,-56,70,145);
                range = 200;
                //Game.getWizard().setRange(range);
                break;
            case 1:
                setup(6,"MagicFire",-6,-56,70,145);
                range = 200;
                //Game.getWizard().setRange(range);
                break;
        }
    }
    public void act(){//remember to deactivate spell after
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(!placed && mouse != null) {
            setLocation(mouse.getX() + adjustH, mouse.getY() + adjustV);
            bC=(mouse.getX()-Game.hPush+40)/80;
            bR=(mouse.getY()-Game.vPush+40)/80;
            //System.out.println(BoardManager.getTile(bY,bX).isBlue());
            // reset last green highlighted tile
            if((lastHighlightedC!=bC||lastHighlightedR!=bR) && lastHighlightedC!=-1&&lastHighlightedR!=-1){
                Tile lastT=BoardManager.getTile(lastHighlightedR,lastHighlightedC);
                if(lastT != null&&lastT.isBlue()) {
                    lastT.turnBlue();
                }
            }
            // highlight the tile the cursor is hovering on to green
            Tile currT = BoardManager.getTile(bR,bC);
            if (currT != null && currT.isBlue()) {
                currT.turnGreen();
                // update last tile coords
                lastHighlightedC=bC;
                lastHighlightedR= bR;
            }
        }
        if (!placed&&mouse!=null&&Greenfoot.mouseClicked(null)) {
            //clicked(mouse, Game.getWizard());
            playSpell();
        }

        if(rate==5){
            rate=0;
            if(++frame==frameCount)frame=0;
            setImage(Utility.customize(w,h,new GreenfootImage(picName+"_"+frame+".png")));
            if(fading)disappear();
        }
        else rate++;
        if (placed&&!fading) {
            fadeTime++;
            if(fadeTime>=30){
                fading = true;
                fadeTime =0;
            }
        }
    }
    private void playSpell(){
        Tile t = BoardManager.getTile(bR, bC);
        System.out.println("e"+" "+bR+" "+bC+" "+t+" "+t.isBlue());
        if(t!=null&&t.isBlue()){ // THIS is the one that doesnt get satisfied anymore (t.isBlue())
            System.out.println("working"+" "+bR+" "+bC);
            placed = true;
            setLocation(Game.hPush+bC*80-10,Game.vPush+bR*80-40);

            //if(BoardManager.getBoard(bX,bY)!=null){
            //    BoardManager.getBoard(bX,bY).turnGreen();
            //    System.out.println("green");
            //}

            if(t.getOccupyingPiece()!=null){
                t.getOccupyingPiece().takeDmg(10);//
                System.out.println("damage taken");
                Game.deactivateSpell();
            }
            Game.deactivateSpell();
            Game.grabCardAnimation(); // new card is spawned
        }
    }
    public static int getRange(){
        return range;
    }
    private void disappear() {
        int maxDuration = 30; // duration of disappearing effect 0.5s
        if (fadeTime< maxDuration) {
            // credit to mr cohen for this math
            int transparency = (int) ((1 - (double)fadeTime / maxDuration) * 255);
            getImage().setTransparency(transparency);
            fadeTime++;
        } else getWorld().removeObject(this);
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
