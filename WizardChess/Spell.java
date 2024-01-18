import greenfoot.*;
public class Spell extends SuperSmoothMover{
    private int type=0,frame=0,rate=0,frameCount,adjustH,adjustV,w,h,fadeTime,aoe[][] ;
    private String picName;
    private static int bC,bR,range,lastHighlightedC = -1,lastHighlightedR = -1;
    private boolean placed = false,fading = false;
    public Spell(int type){
        this.type=type;
        switch(type){
            case 0:setup(6,"MagicFire",-6,-56,70,145,new int[][]{{0,0}},200);break;
            case 1:setup(6,"MagicFire",-6,-56,70,145,new int[][]{{-1,-1},{-1,0},{-1,1},{0,-1},{0,0},{0,1},{1,-1},{1,0},{1,1}},200);break;
            case 2:setup(6,"MagicFire",-6,-56,70,145,new int[][]{{-1,0},{-2,0},{0,-2},{0,-1}, {0,0}, {0,1},{0,2},{1,0},{2,0}},200);break;
            case 3:setup(6,"MagicFire",-6,-56,70,145,new int[][]{{-2,-2},{-1,-1},{-1,1},{-2,2},{0,0},{2,-2},{1,-1},{1,1},{2,2}},200);break;
        }
    }
    public void act(){
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(!placed && mouse != null) {
            setLocation(mouse.getX() + adjustH, mouse.getY() + adjustV);
            bC=(mouse.getX()-Game.hPush+40)/80;
            bR=(mouse.getY()-Game.vPush+40)/80;
            if((lastHighlightedC!=bC||lastHighlightedR!=bR) && lastHighlightedC!=-1&&lastHighlightedR!=-1){
                Tile lastT=BoardManager.getTile(lastHighlightedR,lastHighlightedC);
                if(lastT != null&&lastT.isBlue())lastT.turnBlue();
            }
            if(lastHighlightedC!=bC||lastHighlightedR!=bR)
                for(int[] p: aoe){
                    Tile lastHighlightedT = BoardManager.getTile(lastHighlightedR+p[0], lastHighlightedC+p[1]);
                    if (lastHighlightedT != null && lastHighlightedT.isGreen())lastHighlightedT.turnNormal();
                }
            Wizard.highlightRange(200);
            Tile cur = BoardManager.getTile(bR, bC);
            if(cur!=null&&cur.isBlue()){
                for(int[] p : aoe){
                    Tile currT = BoardManager.getTile(bR +p[0], bC +p[1]);
                    if(currT!=null)currT.turnGreen();
                }
            }
            lastHighlightedC = bC;
            lastHighlightedR = bR;
            if(!placed&&mouse!=null&&Greenfoot.mouseClicked(null))playSpell(bR,bC);
        }
        if(rate==5){
            rate=0;
            if(++frame==frameCount)frame=0;
            setImage(Utility.customize(w,h,new GreenfootImage(picName+"_"+frame+".png")));
            if(fading)disappear();
        }
        else rate++;
        if(placed&&!fading&&fadeTime++>=30)fading = true;
    }
    private void playSpell(int bR, int bC) {
        Tile t = BoardManager.getTile(bR, bC);
        placed = true;
        setLocation(Game.hPush+bC*80-10,Game.vPush+bR*80-40);
        for(int[] offset:aoe){
            try{
                t = BoardManager.getTile(bR +offset[0],bC + offset[1]); //target tile
                if(t != null){
                    t.turnGreen();// highlight tile to gren
                    doDmg(t);
                }
            }catch (IndexOutOfBoundsException e) {}
        }
        Game.deactivateSpell();
        Game.grabCardAnimation(); // new card is spawned
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
        } 
        else getWorld().removeObject(this);
    }
    private void setup(int frameCount, String picName, int adjustH, int adjustV, int w, int h, int[][] aoe, int range){
        this.frameCount=frameCount;
        this.picName=picName;
        this.adjustH=adjustH;
        this.adjustV=adjustV;
        this.w=w;
        this.h=h;
        this.aoe=aoe;
        this.range=range;
    }
}