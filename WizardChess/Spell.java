import greenfoot.*;
public class Spell extends SuperSmoothMover{
    private int type=0,frame=0,rate=0,frameCount,adjustH,adjustV,w,h,fadeTime;
    private String picName;
    private static int bC,bR,range,lastHighlightedC = -1,lastHighlightedR = -1;
    private boolean placed = false,fading = false;
    private int[][] aoe ;
    public Spell(int type){
        this.type=type;
        System.out.println(type);
        switch(type){
            case 0:
                aoe=new int[][]{{0,0}};
                setup(6,"MagicFire",-6,-56,70,145);
                range = 200;
                //Game.getWizard().setRange(range);
                break;
            case 1:
                aoe = new int[][]{
                    {-1, -1}, {-1, 0}, {-1, 1},
                    { 0, -1}, { 0, 0}, { 0, 1},
                    { 1, -1}, { 1, 0}, { 1, 1}
                };
                setup(6,"MagicFire",-6,-56,70,145);
                range = 200;
                //Game.getWizard().setRange(range);
                break;
            case 2:
                aoe = new int[][]{
                    {-1, 0},{-2, 0},
                    { 0, -2},{ 0, -1}, { 0, 0}, { 0, 1},{ 0, 2},
                    { 1, 0},{2, 0}
                };
                setup(6,"MagicFire",-6,-56,70,145);
                range = 200;
                //Game.getWizard().setRange(range);
                break;
            case 3:
                aoe = new int[][]{
                    {-2, -2},{-1, -1} , {-1, 1},{-2, 2},
                     { 0, 0}, 
                    {2, -2},{ 1, -1}, { 1, 1},{2, 2}
                };
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
            // second spell (3x3 aoe)
                // clear previous green tiles
                if(lastHighlightedC!=bC||lastHighlightedR!=bR)clearGreenGrid();
                // highlight the 3x3 tile grid the cursor is hovering on to green
                Tile cur = BoardManager.getTile(bR, bC);
                if(cur!=null&&cur.isBlue()){
                   //System.out.println(aoe.length+"awdawda"); 
                            for(int[] offset:aoe){
                            Tile currT = BoardManager.getTile(bR +offset[0], bC +offset[1]);
                            if(currT!=null)currT.turnGreen();
                        }
                }
                lastHighlightedC = bC;
                lastHighlightedR = bR;
            if(!placed&&mouse!=null&&Greenfoot.mouseClicked(null)) {
                playSpell(bR,bC);
            }
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
    private void playSpell(int bR, int bC) {
        Tile t = BoardManager.getTile(bR, bC);
        placed = true;
        setLocation(Game.hPush+bC*80-10,Game.vPush+bR*80-40);
        for(int[] offset:aoe){
            try{
                t = BoardManager.getTile(bR +offset[0],bC + offset[1]); //target tile
                if(t != null){// && t.isBlue()){  -=-==-=-=-====-++_+_+_+_
                    t.turnGreen(); // highlight tile to gren
                    doDmg(t);
                }
            }catch (IndexOutOfBoundsException e) {
                //
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