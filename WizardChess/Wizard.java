import greenfoot.*;

/**
 * Main class representing the player's wizard character.
 * Handles movement, actions, and attributes of the wizard.
 *
 * @author [your name]
 * @version January 21st, 2023
 */
public class Wizard extends SuperSmoothMover {
    // Attributes to store wizard's position, health, direction, and animation state
    private static int r, c, HP, walkDirection = 0, direction = 0, phase = 0, frame = 0, rate = 0,
            h = Game.hPush + 4 * 80, v = Game.vPush + 7 * 80 - 25, range;//,w=80,h=120;
    private static boolean walking = false, damaged = false, heal = false;
    private static double degrees = 0;

    // Energy and health bars for the wizard
    private EnergyBar energyBar;
    private HPBar hpBar;

    /**
     * Constructor for the Wizard class.
     * Initializes the wizard's initial position, health, and triggers the card animation.
     */
    public Wizard() {
        h = Game.hPush + 4 * 80;
        v = Game.vPush + 7 * 80 - 25;
        degrees = 0;
        direction = 0;
        r = 7;
        c = 4;
        HP = 100;
        Game.grabCardAnimation();
    }

    /**
     * Act method that is called on every game cycle.
     * Handles wizard movement, spell activation, and updates attributes.
     */
    public void act() {
        h = getX();
        v = getY();
        MouseInfo mouse = Greenfoot.getMouseInfo();

        // Update the wizard's facing direction based on the mouse position
        if (mouse != null) degrees = Utility.bearingDegreesAToB(h, v, mouse.getX(), mouse.getY());

        // Handle walking animation and movement
        if (walking) {
            if (++phase <= 3) setLocation(getX(), getY() - 10);
            else if (phase <= 11) {
                switch (walkDirection) {
                    case 0:
                        setLocation(getX(), getY() - 10);
                        break;
                    case 6:
                        setLocation(getX() - 10, getY());
                        break;
                    case 4:
                        setLocation(getX(), getY() + 10);
                        break;
                    case 2:
                        setLocation(getX() + 10, getY());
                        break;
                }
            } else if (phase <= 14) setLocation(getX(), getY() + 10);
            else {
                walking = false;
                phase = 0;
            }
        } else {
            // Handle spell activation and update facing direction and animation frame
            if (mouse != null && Game.isSpellActivated())
                direction = Utility.direction(Utility.bearingDegreesAToB(getX(), getY(), mouse.getX(), mouse.getY()));

            setImage(new GreenfootImage("Wizard-" + direction + "-" + frame + ".png"));

            if (rate == 50) rate = 0;
            if (rate == 0) frame = 0;
            else if (rate == 20) frame = 1;
            else if (rate == 40) frame = 2;

            rate++;

            Tile[][] currentBoard = BoardManager.getBoard();

            // Handle player movement based on key inputs
            if (Game.wizardTurn() && r != 0 && currentBoard[r - 1][c].getOccupyingPiece() == null && Greenfoot.isKeyDown("W")) {
                walking = true;
                r--;
                walkDirection = 0;
                if (!Game.isSpellActivated()) {
                    setImage(new GreenfootImage("Wizard-0-1.png"));
                    direction = 0;
                }
                updateEnBar(-20);
            } else if (Game.wizardTurn() && c != 0 && currentBoard[r][c - 1].getOccupyingPiece() == null && Greenfoot.isKeyDown("A")) {
                walking = true;
                c--;
                walkDirection = 6;
                if (!Game.isSpellActivated()) {
                    setImage(new GreenfootImage("Wizard-6-1.png"));
                    direction = 6;
                }
                updateEnBar(-20);
            } else if (Game.wizardTurn() && r != 7 && currentBoard[r + 1][c].getOccupyingPiece() == null && Greenfoot.isKeyDown("S")) {
                walking = true;
                r++;
                walkDirection = 4;
                if (!Game.isSpellActivated()) {
                    setImage(new GreenfootImage("Wizard-4-1.png"));
                    direction = 4;
                }
                updateEnBar(-20);
            } else if (Game.wizardTurn() && c != 7 && currentBoard[r][c + 1].getOccupyingPiece() == null && Greenfoot.isKeyDown("D")) {
                walking = true;
                c++;
                walkDirection = 2;
                if (!Game.isSpellActivated()) {
                    setImage(new GreenfootImage("Wizard-2-1.png"));
                    direction = 2;
                }
                updateEnBar(-20);
            }
        }

        // Handle wizard taking damage and healing
        if (damaged) {
            updateHP(-50); // 50 is temp
            damaged = false;
            if (HPBar.getHP() <= 0) Greenfoot.setWorld(new EndScreen(true)); // wizard died rip
        }
        if (heal) {
            updateHP(10);
            heal = false;
        }
    }

    /**
     * Get the current row of the wizard on the game board.
     *
     * @return The row index of the wizard.
     */
    public static int getR() {
        return r;
    }

    /**
     * Get the current column of the wizard on the game board.
     *
     * @return The column index of the wizard.
     */
    public static int getC() {
        return c;
    }

    /**
     * Method to handle taking damage by the wizard.
     *
     * @param dmg Amount of damage to be taken.
     */
    public static void takeDmg(int dmg) {
        SoundManager.playSound("Crunch");
        //HP-=dmg;//need to check for death
        damaged = true;
    }

    /**
     * Get the current health points of the wizard.
     *
     * @return The current health points.
     */
    public static int getHP() {
        return HP;
    }

    /**
     * Set the healing status of the wizard.
     *
     * @param h Boolean indicating whether the wizard is healing.
     */
    public static void setHeal(boolean h) {
        heal = h;
    }

    /**
     * Get the current horizontal position of the wizard.
     *
     * @return The horizontal position.
     */
    public static int getH() {
        return h;
    }

    /**
     * Get the current vertical position of the wizard.
     *
     * @return The vertical position.
     */
    public static int getV() {
        return v;
    }

    /**
     * Get the current facing angle of the wizard.
     *
     * @return The facing angle in degrees.
     */
    public static double getDegrees() {
        return degrees;
    }

    /**
     * Set the energy bar for the wizard.
     *
     * @param energyBar The EnergyBar object.
     */
    public void setEnergyBar(EnergyBar energyBar) {
        this.energyBar = energyBar;
    }

    /**
     * Set the health bar for the wizard.
     *
     * @param hpBar The HPBar object.
     */
    public void setHPBar(HPBar hpBar) {
        this.hpBar = hpBar;
    }

    /**
     * Update the energy bar of the wizard.
     *
     * @param e Amount of energy to be added.
     */
    private void updateEnBar(int e) {
        if (energyBar != null) energyBar.setE(energyBar.getE() + e);
    }

    /**
     * Update the health points of the wizard.
     *
     * @param h Amount of health points to be added.
     */
    public void updateHP(int h) {
        HP = hpBar.getHP() + h;
        if (hpBar != null) hpBar.setHP(HP);
    }

    /**
     * Highlight the range of the wizard on the game board.
     *
     * @param range The range to be highlighted.
     */
    public static void highlightRange(int range) {
        BoardManager.resetTiles();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Tile t = BoardManager.getTile(i, j);
                if (Utility.distance(Game.hPush + c * 80, Game.vPush + r * 80, t.getX(), t.getY()) < range) t.turnBlue();
            }
        }
    }
}
