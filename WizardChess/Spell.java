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
        try{
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
                // first type of spell (1x1 tile aoe)
                if(type==0){
                    // highlight the tile the cursor is hovering on to green
                    Tile currT = BoardManager.getTile(bR,bC);
                    if (currT != null && currT.isBlue()) {
                        currT.turnGreen();
                        // update last tile coords
                        lastHighlightedC=bC;
                        lastHighlightedR= bR;
                    }
                    if (!placed&&mouse!=null&&Greenfoot.mouseClicked(null)&&currT.isGreen()) {
                        playSpell();
                    }
                }
                // second spell (3x3 aoe)
                if(type == 1) {
                    // clear previous green tiles
                    if(lastHighlightedC!=bC||lastHighlightedR!=bR)clearGreenGrid();
                    // highlight the 3x3 tile grid the cursor is hovering on to green
                    Tile cur = BoardManager.getTile(bR, bC);
                    if(cur!=null&&cur.isBlue()){
                        for(int i =-1; i <= 1;i++) {
                            for(int j = -1; j <= 1; j++) {
                                Tile currT = BoardManager.getTile(bR +i, bC + j);
                                if(currT!=null)currT.turnGreen();
                                //if(currT !=null &&currT.isBlue())currT.turnGreen();
                            }
                        }
                    }
                    // update last tile coords
                    lastHighlightedC = bC;
                    lastHighlightedR = bR;
                    if(!placed&&mouse!=null&&Greenfoot.mouseClicked(null)&&cur.isBlue()) {
                        playSpell2(bR,bC);
                    }
                }
            }
        } catch(NullPointerException e){
            //System.out.println("Error: "  + e);
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
        if(t!=null&&t.isBlue()){ 
            //System.out.println("working"+" "+bR+" "+bC);
            placed = true;
            setLocation(Game.hPush+bC*80-10,Game.vPush+bR*80-40);

            if(t.getOccupyingPiece()!=null){
                t.getOccupyingPiece().takeDmg(10);//
                System.out.println("damage taken");
                Game.deactivateSpell();
            }
            Game.deactivateSpell();
            Game.grabCardAnimation(); // new card is spawned
        }
    }
    private void playSpell2(int bR, int bC) {
        Tile t = BoardManager.getTile(bR, bC);
        // ofssets for a 3x3 area
        int[][] offsets = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1}, { 0, 0}, { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}};
        for(int[] offset:offsets){
            try{
                t = BoardManager.getTile(bR +offset[0],bC + offset[1]); //target tile
                if(t != null&&t.isGreen()){// && t.isBlue()){  -=-==-=-=-====-++_+_+_+_
                    t.turnGreen(); // highlight tile to gren
                    setLocation(Game.hPush+bC*80-10,Game.vPush+bR*80-40);
                    placed = true;
                    doDmg(t);
                }
            }catch (IndexOutOfBoundsException e) {
                System.out.println("Error : " + e);
            }
        }
        Game.deactivateSpell();
        Game.grabCardAnimation(); // new card is spawned
    }
    // to clear the green 3x3 tiles
    private void clearGreenGrid() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Tile lastHighlightedT = BoardManager.getTile(lastHighlightedR +i, lastHighlightedC + j);
                if (lastHighlightedT != null && lastHighlightedT.isGreen()) {
                    //lastHighlightedT.turnBlue();
                    lastHighlightedT.turnNormal();
                }
            }
        }
        Wizard.highlightRange(200);//200 is temp
    }
    private void doDmg(Tile tile) {
        if(tile.getOccupyingPiece()!=null)tile.getOccupyingPiece().takeDmg(10);
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
