import greenfoot.*;
import java.util.*;

/**
 * This class represents a chess piece in the game. It extends SuperSmoothMover
 * for smooth movements.
 * 
 * @author Jimmy Zhu, Mekaeel
 * @version January 21st, 2023
 */
public class Piece extends SuperSmoothMover {
    private char type; // Type of chess piece: 'p', 'n', 'b', 'r', 'q', 'k'
    private int MaxHP, tH, tV, movePhase = 0, sH, sV, saveR, saveC;
    private Queue<BoardManager.Move> q;
    private int dying = -1;
    private int HP;
    private boolean fix = false, awaitingDeath = false;

    /**
     * Constructor to initialize a chess piece.
     *
     * @param type Type of the chess piece.
     * @param tH   Target horizontal position.
     * @param tV   Target vertical position.
     */
    public Piece(char type, int tH, int tV) {
        this.type = type;
        this.tH = tH;
        this.tV = tV;
        this.sH = tH;
        this.sV = tV - 30;
        q = new LinkedList<BoardManager.Move>();

        // Set the initial image and maximum HP based on the piece type
        switch (type) {
            case 'p':
                setImage(new GreenfootImage("Piece_p_3.png"));
                MaxHP = (int) (1 * Game.getWave()) + 10;  // Slightly increased base health
                break;
            case 'n':
                setImage(new GreenfootImage("Piece_n_3.png"));
                MaxHP = 1 * Game.getWave() + 15;  // Increased base health
                break;
            case 'b':
                setImage(new GreenfootImage("Piece_b_3.png"));
                MaxHP = 2 * Game.getWave() + 20;  // Increased base health
                break;
            case 'r':
                setImage(new GreenfootImage("Piece_r_3.png"));
                MaxHP = (int) (2 * Game.getWave()) + 25;  // Slightly more health per wave
                break;
            case 'q':
                setImage(new GreenfootImage("Piece_q_3.png"));
                MaxHP = (int) (3 * Game.getWave()) + 30;  // More health per wave, stronger enemy
                break;
            case 'k':
                setImage(new GreenfootImage("Piece_k_3.png"));
                MaxHP = 3 * Game.getWave() + 40;  // Highest base health, strongest enemy
                break;
        }
        HP = MaxHP;
    }

    /**
     * The main act method is called by the Greenfoot framework to perform actions.
     */
    public void act() {
        if (dying > 0) {
            // Animate the piece's death
            setLocation(getX(), getY() - 5);
            setImage(Utility.customize(getImage(), dying-- * 15));
        } else if (dying == 0) {
            // Remove the piece after the death animation is complete
            BoardManager.getBoard()[(tV - Game.vPush) / 80][(tH - Game.hPush) / 80].empty();
            if (type == 'k')
                Game.kingDying();
            getWorld().removeObject(this);
        } else if (movePhase < 8) {
            // Move up during the initial phase
            setLocation(getX(), getY() - 4);
            movePhase++;
            if (movePhase == 8){
                Game.setDelay(70);
                BoardManager.allowNextMove();
            }
        } else if (movePhase == 8 && (!Utility.inRangeInclusive(getX(), tH - (int) Math.ceil(Utility.distance(sH, sV, tH, tV) / 25 + 1), tH + (int) Math.ceil(Utility.distance(sH, sV, tH, tV) / 25 + 1))
                || !Utility.inRangeInclusive(getY(), tV - 32 - (int) Math.ceil(Utility.distance(sH, sV, tH, tV) / 25 + 1), tV - 32 + (int) Math.ceil(Utility.distance(sH, sV, tH, tV) / 25 + 1)))) {
            // Adjust the position if not in the target range
            double bearing = Utility.bearingDegreesAToB(getX(), getY(), tH, tV - 32);
            fix = false;
            setLocation(getX() + Math.cos(Utility.degreesToRadians(bearing)) * (Utility.distance(sH, sV, tH, tV) / 25 + 1),
                    getY() + Math.sin(Utility.degreesToRadians(bearing)) * -(Utility.distance(sH, sV, tH, tV) / 25 + 1));
        } else if (!fix && (Utility.inRangeInclusive(getX(), tH - (int) Math.ceil(Utility.distance(sH, sV, tH, tV) / 25 + 1), tH + (int) Math.ceil(Utility.distance(sH, sV, tH, tV) / 25 + 1))
                && Utility.inRangeInclusive(getY(), tV - 32 - (int) Math.ceil(Utility.distance(sH, sV, tH, tV) / 25 + 1), tV - 32 + (int) Math.ceil(Utility.distance(sH, sV, tH, tV) / 25 + 1)))) {
            // Fix the position if in the target range
            fix = true;
            setLocation(tH, tV - 32);
        } else if (movePhase < 16) {
            // Move down during the latter phase
            if (movePhase == 14 && (tV - Game.vPush) / 80 == 7 && type == 'p')
                promote();
            setLocation(getX(), getY() + 4);
            movePhase++;
        } else if (!q.isEmpty() && BoardManager.timeToMove(q.peek().getI())) {
            // Move according to the queued moves
            sV = tV;
            sH = tH;
            tV = Game.vPush + q.peek().getToR() * 80;
            tH = Game.hPush + q.poll().getToC() * 80;
            movePhase = 0;
        } else if ((tV - Game.vPush) / 80 == Wizard.getR() && (tH - Game.hPush) / 80 == Wizard.getC()) {
            // The piece reaches the Wizard's position, causing damage
            dying = 17;
            Wizard.takeDmg(HP);
        }
    }

    /**
     * Get the type of the chess piece.
     *
     * @return char Type of the chess piece.
     */
    public char getType() {
        return type;
    }

    /**
     * Check if the chess piece is a king.
     *
     * @return boolean True if the piece is a king, otherwise false.
     */
    public boolean isKing() {
        return type == 'k';
    }
    
    /**
     * Return piece's max hp
     *
     * @return int MaxHP
     */
    public int getMaxHP(){
        return MaxHP;
    }

    /**
     * Get the current HP of the piece.
     *
     * @return int Current HP of the piece.
     */
    public int getHP() {
        return HP;
    }

    /**
     * Set the HP of the piece.
     *
     * @param health Health value to set.
     */
    public void setHP(int health) {
        HP = health;
    }

    /**
     * Add a move to the piece's queue.
     *
     * @param m Move to be added.
     */
    public void addMove(BoardManager.Move m) {
        q.add(m);
    }

    /**
     * Get the target horizontal position.
     *
     * @return int Target horizontal position.
     */
    public int getTargetH() {
        return tH;
    }

    /**
     * Get the target vertical position.
     *
     * @return int Target vertical position.
     */
    public int getTargetV() {
        return tV;
    }

    /**
     * Take damage and update the piece's HP.
     *
     * @param dmg Amount of damage to take.
     */
    public void takeDmg(int dmg) {
        setImage(new GreenfootImage("Piece_" + type + "_" + Math.max((int) Math.ceil((HP / (double) MaxHP) * 3), 0) + ".png"));
        playDmgEffect(-dmg); // Negative damage for red color in the effect
        HP -= dmg;
        if (HP <= 0)
            dying = 17;
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
     * Initiate the death sequence for the piece.
     */
    public void kill() {
        dying = 17;
    }

    /**
     * Check if the piece is in the process of dying.
     *
     * @return boolean True if the piece is dying, otherwise false.
     */
    public boolean isDying() {
        return dying >= 0 || awaitingDeath;
    }

    /**
     * Promote a pawn to a queen.
     */
    public void promote() {
        //System.out.println(tV + " " + tH + " promote");
        if (type != 'p')
            return;
        type = 'q';
        MaxHP = (int) (1.5 * Game.getWave()) + 2;
        HP = MaxHP;
        setImage(new GreenfootImage("Piece_q_3.png"));
    }
}
