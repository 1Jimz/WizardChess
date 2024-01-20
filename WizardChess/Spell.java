import greenfoot.*;
public class Spell extends SuperSmoothMover{
    private int type=0,frame=0,rate=0,frameCount,adjustH,adjustV,w,h,fadeTime=0,aoe[][],lastHighlightedC = -1,lastHighlightedR = -1,bC,bR,range,dmg;
    private String picName;
    private boolean placed = false,fading = false;
    public Spell(int type){
        this.type=type;
        switch(type){
            case 0:setup(6,"MagicFire",-6,-56,70,145,new int[][]{{0,0}},200,100);break;
            case 1:setup(6,"MagicFire",-6,-56,70,145,new int[][]{{-1,-1},{-1,0},{-1,1},{0,-1},{0,0},{0,1},{1,-1},{1,0},{1,1}},200,100);break;
            case 2:setup(6,"MagicFire",-6,-56,70,145,new int[][]{{-1,0},{-2,0},{0,-2},{0,-1}, {0,0}, {0,1},{0,2},{1,0},{2,0}},200,100);break;
            case 3:setup(6,"MagicFire",-6,-56,70,145,new int[][]{{-2,-2},{-1,-1},{-1,1},{-2,2},{0,0},{2,-2},{1,-1},{1,1},{2,2}},200,100);break;
        }
    }
    public void act(){
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(!placed&&mouse!=null) {
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
            Wizard.highlightRange(range);
            Tile cur = BoardManager.getTile(bR, bC);
            if(cur!=null&&cur.isBlue()){
                for(int[] p : aoe){
                    Tile currT = BoardManager.getTile(bR +p[0], bC +p[1]);
                    if(currT!=null)currT.turnGreen();
                }
            }
            lastHighlightedC = bC;
            lastHighlightedR = bR;
            if(!placed&&Greenfoot.mouseClicked(null)&&Utility.distance(mouse.getX(),mouse.getY(),Wizard.getH(),Wizard.getV())<=range){
                placed = true;
                setLocation(Game.hPush+bC*80-10,Game.vPush+bR*80-40);
                for(int[] p : aoe){
                    try{
                        Tile t = BoardManager.getTile(bR+p[0],bC+p[1]); //target tile
                        if(t!=null){
                            t.turnGreen();
                            if(t.getOccupyingPiece()!=null){
                                t.getOccupyingPiece().takeDmg(dmg);
                            }
                        }
                    }catch (IndexOutOfBoundsException e) {}
                }
                Game.deactivateSpell();
                Game.grabCardAnimation();
            }
        }
        if(rate==5){
            rate=0;
            if(++frame==frameCount)frame=0;
            if(placed&&fadeTime++>=30)setImage(Utility.customize(w,h,new GreenfootImage(picName+"_"+frame+".png"),(int)8.5*(60-fadeTime)));
            else setImage(Utility.customize(w,h,new GreenfootImage(picName+"_"+frame+".png")));
            if(fadeTime==60)getWorld().removeObject(this);
        }
        else rate++;
    }
    private void setup(int frameCount, String picName, int adjustH, int adjustV, int w, int h, int[][] aoe, int range, int dmg){
        this.frameCount=frameCount;
        this.picName=picName;
        this.adjustH=adjustH;
        this.adjustV=adjustV;
        this.w=w;
        this.h=h;
        this.aoe=aoe;
        this.range=range;
        this.dmg=dmg;
    }
}