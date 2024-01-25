import greenfoot.*;

/**
 * This class represents a spell in the game. It extends SuperSmoothMover for smooth movements.
 * @author Jimmy Zhu, Mekaeel Malik, Dorsa Rohani
 * @version January 21st, 2023
*/
public class Spell extends SuperSmoothMover {
    private int type = 0, frame = 0, rate = 0, frameCount, adjustH, adjustV, w, h, fadeTime = 0;
    private int[][] aoe; // Area of effect positions relative to the center
    private String picName;
    private boolean placed = false, fading = false;
    private int lastHighlightedC = -1, lastHighlightedR = -1, bC, bR, range, dmg;

    /**
     * Constructor to initialize a spell.
     *
     * @param type Type of the spell.
     */
    public Spell(int type) {
        this.type = type;
        switch (type) {
            case 0:
                setup(0, "portalPreview", -6, -56, 70, 145, new int[][]{{0, 0}}, 200, 30);
                break;
            case 1:
                setup(0, "explosionPreview", -6, -56, 70, 145,
                        new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}}, 200, 8);
                break;
            case 2:
                setup(0, "bubblePreview", -6, -56, 70, 145,
                        new int[][]{{-1, 0}, {-2, 0}, {0, -2}, {0, -1}, {0, 0}, {0, 1}, {0, 2}, {1, 0}, {2, 0}}, 200, 4);
                break;
            case 3:
                setup(0, "slashPreview", -6, -56, 70, 145,
                        new int[][]{{-2, -2}, {-1, -1}, {-1, 1}, {-2, 2}, {0, 0}, {2, -2}, {1, -1}, {1, 1}, {2, 2}}, 200, 5);
                break;
            case 4:
                setup(0, "tornado", -6, -56, 100, 100, new int[][]{{-1, -1},{-1, 1},{0, 1},{0, -1},{-1, 0},{1, 0},{1, -1},{1, 1}}, 200, 3);
                break;
            case 5:
                setup(0, "BlackBall", -6, -40, 100, 100, new int[][]{{-1, -1},{-1, 1},{1, -1},{1, 1}}, 200, 6);
                break;
            case 6:
                setup(8, "Heal", -2, -2, 100, 100, new int[][]{{0, 0}}, 50, 1);
                break;
        }
        
    }

    /**
     * The main act method is called by the Greenfoot framework to perform actions.
     */
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (!placed && mouse != null) {
            // Set the location of the spell based on the mouse position
            setLocation(mouse.getX() + adjustH, mouse.getY() + adjustV);
            bC = (mouse.getX() - Game.hPush + 40) / 80;
            bR = (mouse.getY() - Game.vPush + 40) / 80;

            // Update highlight effects on the board
            if ((lastHighlightedC != bC || lastHighlightedR != bR) && lastHighlightedC != -1 && lastHighlightedR != -1) {
                Tile lastT = BoardManager.getTile(lastHighlightedR, lastHighlightedC);
                if (lastT != null && lastT.isBlue())
                    lastT.turnBlue();
            }
            if (lastHighlightedC != bC || lastHighlightedR != bR)
                for (int[] p : aoe) {
                    Tile lastHighlightedT = BoardManager.getTile(lastHighlightedR + p[0], lastHighlightedC + p[1]);
                    if (lastHighlightedT != null && lastHighlightedT.isGreen())
                        lastHighlightedT.turnNormal();
                }

            // Highlight tiles in the area of effect
            Wizard.highlightRange(range);
            Tile cur = BoardManager.getTile(bR, bC);
            if (cur != null && cur.isBlue()) {
                for (int[] p : aoe) {
                    Tile currT = BoardManager.getTile(bR + p[0], bC + p[1]);
                    if (currT != null)
                        currT.turnGreen();
                }
            }

            lastHighlightedC = bC;
            lastHighlightedR = bR;

            // Place the spell on click if within the valid range
            if (!placed && Greenfoot.mouseClicked(null) && Utility.distance(mouse.getX(), mouse.getY(), Wizard.getH(), Wizard.getV()) <= range) {
                placed = true;
                setLocation(Game.hPush + bC * 80 - 10, Game.vPush + bR * 80 - 40);
                //Wizard.decreaseE(Card.getSpellEP());
                // Apply the spell's effect to the targeted area
                for (int[] p : aoe) {
                    try {
                        Tile t = BoardManager.getTile(bR + p[0], bC + p[1]); // Target tile
                        if (t != null) {
                            t.turnGreen();
                            if (t.getOccupyingPiece() != null) {
                                t.getOccupyingPiece().takeDmg(dmg);
                                getWorld().addObject(new Effects(type), t.getOccupyingPiece().getX(),
                                        t.getOccupyingPiece().getY());
                            }
                            if (type == 6 && t.getR() == Wizard.getR() && t.getC() == Wizard.getC()) {
                                playDmgEffect(20);
                                Wizard.setHP(Math.min(100,Wizard.getHP()+15));
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                }

                // Deactivate the spell and perform animations
                Game.deactivateSpell();
                Game.grabCardAnimation();
                if(frameCount == 0) {
                    getWorld().removeObject(this);
                }
            }
        }

        // Animation frame handling
        if (rate == 5) {
            rate = 0;
            if (++frame == frameCount)
                frame = 0;

            // Apply fading effect
            if(frameCount == 0) {
                
            } else {
                if (placed && fadeTime++ >= 15)
                    setImage(Utility.customize(w, h, new GreenfootImage(picName + "_" + frame + ".png"),
                            (int) 8.5 * (30 - fadeTime)));
                else
                    setImage(Utility.customize(w, h, new GreenfootImage(picName + "_" + frame + ".png")));
            }
            // Remove the spell after fading
            if (fadeTime == 30)
                getWorld().removeObject(this);
        } else
            rate++;
    }

    /**
     * Display a damage effect.
     *
     * @param dmg Amount of damage.
     */
    public void playDmgEffect(int dmg) {
        getWorld().addObject(new Message((Integer.signum(dmg) == -1 ? "-" : "+") + Math.abs(dmg),
                (Integer.signum(dmg) == -1 ? Color.RED : Color.GREEN)), getX(), getY() - 30);
    }

    /**
     * Set up the spell with the given parameters.
     *
     * @param frameCount Number of frames in the animation.
     * @param picName    Base name of the image.
     * @param adjustH    Horizontal adjustment.
     * @param adjustV    Vertical adjustment.
     * @param w          Width of the spell.
     * @param h          Height of the spell.
     * @param aoe        Area of effect positions relative to the center.
     * @param range      Range of the spell.
     * @param dmg        Damage caused by the spell.
     */
    private void setup(int frameCount, String picName, int adjustH, int adjustV, int w, int h, int[][] aoe, int range, int dmg) {
        this.frameCount = frameCount;
        if(frameCount == 0) {
            setImage(Utility.customizeAndCreate(w,h,picName+".png"));
            
        }
        this.picName = picName;
        this.adjustH = adjustH;
        this.adjustV = adjustV;
        this.w = w;
        this.h = h;
        this.aoe = aoe;
        this.range = range;
        this.dmg = dmg;

    }
}