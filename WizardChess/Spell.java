import greenfoot.*;
/*
 * api
 */

// spell strategy interfact
interface spell {
    void playSpell(Spell spell, int bR, int bC);
}
// single tile spell (card 1)
class singleTile implements spell {
    @Override
    public void playSpell(Spell spell, int bR, int bC) {
        Tile t = BoardManager.getTile(bR, bC);
        if (t != null&&t.isBlue()) { 
            spell.setLocation(Game.hPush + bC * 80 - 10, Game.vPush + bR * 80 - 40);
            spell.setPlaced(true);
            if (t.getOccupyingPiece() != null)t.getOccupyingPiece().takeDmg(10);
            Game.deactivateSpell();
            Game.grabCardAnimation();
        }
    }
}
// 3x3 aoe spell (card 2)
class areaSpell implements SpellStrategy {
    @Override
    public void playSpell(Spell spell, int bR, int bC) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Tile t = BoardManager.getTile(bR + i, bC + j);
                if (t != null && t.isGreen()) {
                    spell.setLocation(Game.hPush + bC * 80 - 10, Game.vPush + bR * 80 - 40);
                    spell.setPlaced(true);
                    if (t.getOccupyingPiece() != null)t.getOccupyingPiece().takeDmg(10);
                }
            }
        }
        Game.deactivateSpell();
        Game.grabCardAnimation();
    }
}
// cross aoe spell (card 3)
class cross implements SpellStrategy {
    @Override
    public void playSpell(Spell spell, int bR, int bC) {
        for (int i =-2; i <= 2;i++) {
            for(int j = -2; j <=2; j++) {
                if(i == 0|| j ==0) { // check if tile is part of cross
                    Tile t = BoardManager.getTile(bR + i, bC + j);
                    if(t != null && t.isGreen())if (t.getOccupyingPiece() != null)t.getOccupyingPiece().takeDmg(10);
                }
            }
        }
        spell.setLocation(Game.hPush + bC * 80 - 10, Game.vPush + bR * 80 - 40);
        spell.setPlaced(true);
        Game.deactivateSpell();
        Game.grabCardAnimation();
    }
}
// diagonal spell
class diagonal implements SpellStrategy {
    @Override
    public void playSpell(Spell spell, int bR, int bC) {
        for (int i = -2; i <= 2; i++) {
            effect(bR+i, bC+i); // diagonal right down and left up
            effect(bR+i, bC-i); // diagonal left down and right up
        }
        spell.setLocation(Game.hPush+ bC *80-10,Game.vPush +bR *80 - 40);
        spell.setPlaced(true);
        Game.deactivateSpell();
        Game.grabCardAnimation();
    }
    private void effect(int r, int c) {
        Tile t = BoardManager.getTile(r, c);
        if (t != null && t.isGreen()) {
            if (t.getOccupyingPiece() != null)t.getOccupyingPiece().takeDmg(10); 
        }
    }
}
// spell class
public class Spell extends SuperSmoothMover {
    private int type, frame, rate, frameCount, adjustH, adjustV, w, h, fadeTime;
    private String picName;
    private static int bC, bR, range, lastHighlightedC = -1, lastHighlightedR = -1;
    private boolean placed, fading;
    private SpellStrategy spellStrategy;
    private Tile[] toHighlight;

    public Spell(int type) {
        this.type = type;
        switch (type) {
            case 0: // card 1
                spellStrategy = new SingleTileSpell();
                setup(6, "MagicFire", -6, -56, 70, 145);
                range = 200;
                break;
            case 1: // card 2
                spellStrategy = new areaSpell();
                setup(6, "MagicFire", -6, -56, 70, 145);
                range = 200;
                break;
            case 2:
                spellStrategy = new cross(); // card 3
                setup(6, "MagicFire", -6, -56, 70, 145);
                range = 200;
                break;
            case 3:
                spellStrategy = new diagonal(); // card 4
                setup(6, "MagicFire", -6, -56, 70, 145);
                range = 200;
                break;
        }
    }

    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        try {
            if (!placed && mouse != null) {
                setLocation(mouse.getX() + adjustH, mouse.getY() + adjustV);
                bC = (mouse.getX() - Game.hPush + 40) / 80;
                bR = (mouse.getY() - Game.vPush + 40) / 80;
                //System.out.println(BoardManager.getTile(bY,bX).isBlue());
                // reset last green highlighted tile
                if((lastHighlightedC!=bC||lastHighlightedR!=bR) && lastHighlightedC!=-1&&lastHighlightedR!=-1){
                    Tile lastT=BoardManager.getTile(lastHighlightedR,lastHighlightedC);
                    if(lastT != null&&lastT.isBlue()) {
                        lastT.turnBlue();
                    }
                }
                // first type of spell (1x1 tile aoe)
                if(type==0) {
                    // highlight the tile the cursor is hovering on to green
                    Tile currT = BoardManager.getTile(bR,bC);
                    if (currT != null && currT.isBlue()) {
                        currT.turnGreen();
                        // update last tile coords
                        lastHighlightedC=bC;
                        lastHighlightedR= bR;
                    }
                    if (!placed&&mouse!=null&&Greenfoot.mouseClicked(null)&&currT.isGreen()) {
                        spellStrategy.playSpell(this, bR,bC);
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
                        spellStrategy.playSpell(this, bR,bC);
                    }
                }
                // third spell (cross aoe)
                if(type == 2) {
                    if (lastHighlightedC != bC || lastHighlightedR != bR) clearGreenGrid();
                    Tile centerTile = BoardManager.getTile(bR, bC);
                    if (centerTile != null && centerTile.isBlue()) {
                        for (int i = -2; i <= 2; i++) {
                            for (int j = -2; j <= 2; j++) {
                                if (i == 0 || j == 0) { // check if this tile is part of the cross section
                                    Tile currT = BoardManager.getTile(bR + i, bC + j);
                                    if (currT != null) currT.turnGreen();
                                }
                            }
                        }
                    }
                    lastHighlightedC = bC;
                    lastHighlightedR = bR;
                    if (!placed && mouse != null && Greenfoot.mouseClicked(null) && centerTile.isBlue()) {
                        spellStrategy.playSpell(this, bR, bC);
                    }
                }
                // fourth spell (diagonal aoe)
                if (type == 3) {
                    if (lastHighlightedC!= bC || lastHighlightedR != bR)clearGreenGrid();
                    Tile cT = BoardManager.getTile(bR, bC);
                    if (cT != null && cT.isBlue()) {
                        for (int i = -2; i <= 2; i++) {
                            Tile currT1 = BoardManager.getTile(bR+i,bC+i); // diagonal right down and left up
                            Tile currT2 = BoardManager.getTile(bR+i,bC-i); // diagonal left down and right up
                            if (currT1 != null)currT1.turnGreen();
                            if (currT2 != null)currT2.turnGreen();
                        }
                    }
                    lastHighlightedC = bC;
                    lastHighlightedR = bR;
                    if (!placed && mouse != null && Greenfoot.mouseClicked(null) && cT.isBlue()) {
                        spellStrategy.playSpell(this, bR, bC);
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
    private void setup(int frameCount, String picName, int adjustH, int adjustV, int w, int h) {
        this.frameCount = frameCount;
        this.picName = picName;
        this.adjustH = adjustH;
        this.adjustV = adjustV;
        this.w = w;
        this.h = h;
        getImage().scale(w, h);
    }
    public void setPlaced(boolean placed) {
        this.placed = placed;
    }
    private void disappear() {
        int maxDuration = 30;
        if (fadeTime < maxDuration) {
            int transparency = (int) ((1 - (double)fadeTime / maxDuration) * 255);
            getImage().setTransparency(transparency);
            fadeTime++;
        } else getWorld().removeObject(this);
    }
}

