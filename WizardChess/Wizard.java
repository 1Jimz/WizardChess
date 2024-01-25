import greenfoot.*;

/**
 * Main class representing the player's wizard character.
 * Handles movement, actions, and attributes of the wizard.
 *
 * @author Jimmy Zhu, Mekaeel Malik, Dorsa Rohani
 * @version January 21st, 2023
 */
public class Wizard extends SuperSmoothMover {
    // Attributes to store wizard's position, health, direction, and animation state
    private static int r, c, HP, EP, walkDirection = 0, movesMade = 0, direction = 0, phase = 0, frame = 0, rate = 0,
            h = Game.hPush + 4 * 80, v = Game.vPush + 7 * 80 - 25, range;//,w=80,h=120;
    private static boolean walking = false, damaged = false, heal = false;
    private static double degrees = 0;

    // Energy and health bars for the wizard
    private static EnergyBar energyBar;
    private static HPBar hpBar;

    /**
     * Constructor for the Wizard class.
     * Initializes the wizard's initial position, health, and triggers the card animation.
     */
    public Wizard(int r, int c) {
        // factoring in the offset of the game board from the rest of the screen
        h = Game.hPush + c * 80;
        v = Game.vPush + r * 80 - 25;
        
        // values for where the wizard should be facing
        degrees = 0;
        direction = 0;
        

        // starting values for what column and row the wizard is in 
        this.r = r;
        this.c = c;
        
        // sets HP to 100
        HP = 100;
        EP=100;
        
        // calls a function to grab a card
        Game.grabCardAnimation();
    }

    /**
     * Act method that is called on every game cycle.
     * Handles wizard movement, spell activation, and updates attributes.
     */
    public void act() {
        // updates the h and v values with the x and y values of the wizard
        h = getX();
        v = getY();
        
        // stores the mouse info within the temporary variable mouse
        MouseInfo mouse = Greenfoot.getMouseInfo();

        // Update the wizard's facing direction based on the mouse position
        if (mouse != null) degrees = Utility.bearingDegreesAToB(h, v, mouse.getX(), mouse.getY());

        // Handle walking animation and movement
        if (walking) {
            // if the move phase is starting, lift the wizard up to simulate hopping
            if (++phase <= 3) setLocation(getX(), getY() - 10);
            // if not start moving the wizard in the direction indicated by the walkDirection
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
            // at the end of the movement, set the wizard back down to end the hopping
            } else if (phase <= 14) setLocation(getX(), getY() + 10);
            // once finished end the walking phase and reset the phase variable
            else {
                walking = false;
                phase = 0;
            }
        } else {
            // Handle spell activation and update facing direction and animation frame
            if (mouse != null && Game.isSpellActivated())
                direction = Utility.direction(Utility.bearingDegreesAToB(getX(), getY(), mouse.getX(), mouse.getY()));

            // set the image of the wizard to the direction it should be in plus
            // change the image to the corresponding idle image based on the frame variable
            setImage(new GreenfootImage("Wizard-" + direction + "-" + frame + ".png"));

            if (rate == 50) rate = 0;
            if (rate == 0) frame = 0;
            else if (rate == 20) frame = 1;
            else if (rate == 40) frame = 2;

            rate++;

            Tile[][] currentBoard = BoardManager.getBoard();

            // Handle player movement based on key inputs
            
            // checks if it is the wizards turn and if the square the player wants
            // to move to is empty, if so start walking and change the image of the
            // wizard based on the direction is a spell was not activated
            if (EP>0&&Game.wizardTurn() && r != 0 && currentBoard[r - 1][c].getOccupyingPiece() == null && Greenfoot.isKeyDown("W")) {
                walking = true;
                r--;
                walkDirection = 0;
                if (!Game.isSpellActivated()) {
                    setImage(new GreenfootImage("Wizard-0-1.png"));
                    direction = 0;
                }
                // reduce the energy of the wizard
                decreaseE(2);
                movesMade++;
            } else if (EP>0&&Game.wizardTurn() && c != 0 && currentBoard[r][c - 1].getOccupyingPiece() == null && Greenfoot.isKeyDown("A")) {
                walking = true;
                c--;
                walkDirection = 6;
                if (!Game.isSpellActivated()) {
                    setImage(new GreenfootImage("Wizard-6-1.png"));
                    direction = 6;
                }
                // reduce the energy of the wizard
                decreaseE(2);
                movesMade++;
            } else if (EP>0&&Game.wizardTurn() && r != 7 && currentBoard[r + 1][c].getOccupyingPiece() == null && Greenfoot.isKeyDown("S")) {
                walking = true;
                r++;
                walkDirection = 4;
                if (!Game.isSpellActivated()) {
                    setImage(new GreenfootImage("Wizard-4-1.png"));
                    direction = 4;
                }
                // reduce the energy of the wizard
                decreaseE(2);
                movesMade++;
            } else if (EP>0&&Game.wizardTurn() && c != 7 && currentBoard[r][c + 1].getOccupyingPiece() == null && Greenfoot.isKeyDown("D")) {
                walking = true;
                c++;
                walkDirection = 2;
                if (!Game.isSpellActivated()) {
                    setImage(new GreenfootImage("Wizard-2-1.png"));
                    direction = 2;
                }
                // reduce the energy of the wizard
                decreaseE(2);
                movesMade++;
            }
        }
        // set to end screen
        if(Wizard.getHP()<=0) Greenfoot.setWorld(new EndScreen(true));
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
     * Get the number of moves made.
     *
     * @return The column index of the wizard.
     */
    public static int getMovesMade() {
        return movesMade;
    }
    
    /**
     * Method to handle taking damage by the wizard.
     *
     * @param dmg Amount of damage to be taken.
     */
    public static void takeDmg(int dmg) {
        SoundManager.playSound("Crunch");
        HP-= (int) dmg * 0.2;
        hpBar.setHP(HP);
    }
    /**
     * Method to handle taking enegy by the wizard.
     *
     * @param e Amount of energy to be taken.
     */
    public static void decreaseE(int e) {
        //SoundManager.playSound("Crunch");
        EP-=e;
        energyBar.setE(EP);
    }
    /**
     * Method to set enegy by the wizard.
     *
     * @param e Amount of energy to be set.
     */
    public static void setE(int e) {
        energyBar.setE(e);
    }
    /**
     * Method to set health by the wizard.
     *
     * @param e Amount of health to be set.
     */
    public static void setHP(int h) {
        hpBar.setHP(h);
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
     * Get the current energy points of the wizard.
     *
     * @return The current energy points.
     */
    public static int getE() {
        return EP;
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
     * Get energy bar
     *
     */
    public static EnergyBar getEnergyBar() {
        return energyBar;
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
        // sets all of the tiles back to their default state
        BoardManager.resetTiles();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // checks each tile and if it is within the range of the wizard
                // highlighting it blue if so
                Tile t = BoardManager.getTile(i, j);
                if (Utility.distance(Game.hPush + c * 80, Game.vPush + r * 80, t.getX(), t.getY()) < range) t.turnBlue();
            }
        }
    }
}
