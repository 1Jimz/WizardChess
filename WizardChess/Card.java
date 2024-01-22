import greenfoot.*;
import java.util.List;

/**
 * This class represents a card in the game. It extends SuperSmoothMover,
 * allowing smooth movements. The original code is from
 * https://www.greenfoot.org/scenarios/925, and it has been substantially
 * modified by Jimmy Zhu on 12/19/23. The modifications are significant enough
 * that credit might not be necessary; it's more of an inspired transformation.
 */
public class Card extends SuperSmoothMover {
    private List springs;
    private double x1, y1, x2 = 0, y2 = 0, r1, r2, a, b, c, d, l, ang;
    private double dl[] = new double[6], x[] = {-100, 100, 100, -100}, y[] = {-160, -160, 160, 160};
    private double xprev[] = new double[4], yprev[] = new double[4], xv[] = {0, 0, 0, 0}, yv[] = {0, 0, 0, 0};
    private boolean drag = false, stick = false, leftBorder, disposing = false;
    private int point1, point2, mx, my, pointer = 0, p = 0, points[] = new int[4];
    private int conect1[] = {0, 1, 2, 3, 0, 1}, conect2[] = {1, 2, 3, 0, 2, 3}, active, type, whirl = 0;

    /**
     * Constructor for the Card class.
     *
     * @param mx          Initial x-coordinate of the card.
     * @param my          Initial y-coordinate of the card.
     * @param active      Initial active state of the card.
     * @param leftBorder  Indicates if the card is on the left border.
     */
    public Card(int mx, int my, int active, boolean leftBorder) {
        this.mx = mx; // Initial x-coordinate
        this.my = my; // Initial y-coordinate
        this.active = active; // Active state
        this.leftBorder = leftBorder; // Indicates if the card is on the left border
        type = Greenfoot.getRandomNumber(6); // Randomly determine the type of card

        // Compute initial distances between connected points in the card
        for (int f = 0; f < 6; f++)
            dl[f] = Math.sqrt(Math.pow((x[conect1[f]] - x[conect2[f]]), 2) + Math.pow((y[conect1[f]] - y[conect2[f]]), 2));

        // Set the image based on the card type
        switch (type) {
            case 0:
                setImage(new GreenfootImage("portalCard.png"));
                break;
            case 1:
                setImage(new GreenfootImage("explosionCard.png"));
                break;
            case 2:
                setImage(new GreenfootImage("bubbleCard.png"));
                break;
            case 3:
                setImage(new GreenfootImage("slashCard.png"));
                break;
            case 4:
                setImage(new GreenfootImage("healCard.png"));
                break;
            case 5:
                setImage(new GreenfootImage("lightningCard.png"));
                break;
        }
        // Alternative image assignment: setImage(new GreenfootImage("Testcardfront2.png"));
    }

    /**
     * Method called when the Card object is added to the world.
     *
     * @param w The world to which the Card object is added.
     */
    public void addedToWorld(World w) {
        // Move the card points to their initial positions within the world
        for (int i = 0; i < x.length; i++) {
            x[i] += getX();
            y[i] += getY();
        }

        // Retrieve other Card objects in the world
        springs = getWorld().getObjects(Card.class);
        for (int i = 0; i < springs.size(); i++)
            ((Card) springs.get(i)).getWorld().getObjects(Card.class);

        // Save initial positions of the card points
        for (int i = 0; i < 4; i++) {
            xprev[i] = x[i];
            yprev[i] = y[i];
        }
    }

    /**
     * The main act method is called by the Greenfoot framework to perform
     * actions.
     */
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();

        // Card whirl animation
        if (whirl == 0 && mouse != null && Greenfoot.mouseClicked(this))
            whirl++;
        if (whirl > 0 && whirl < 35 && mouse != null) {
            whirl++;
            // Scale down, rotate, and change transparency during whirl
            getImage().scale(getImage().getWidth() - 5, getImage().getHeight() - 5);
            getImage().rotate(whirl);
            getImage().setTransparency(getImage().getTransparency() - 5);
            // Move card randomly during whirl
            setLocation(mouse.getX() + (Greenfoot.getRandomNumber(2) == 0 ? -Greenfoot.getRandomNumber(7) : Greenfoot.getRandomNumber(7)),
                    mouse.getY() + (Greenfoot.getRandomNumber(2) == 0 ? -Greenfoot.getRandomNumber(7) : Greenfoot.getRandomNumber(7)));
        } else if (whirl == 35 && mouse != null) {
            // When whirl is complete, activate the spell and create associated objects
            whirl++;
            Game.activateSpell();
            getWorld().addObject(new Spell(type), mouse.getX(), mouse.getY());
            getWorld().addObject(new Wand(), -200, -200);
        } else if (whirl > 35)
            getWorld().removeObject(this);
        else {
            // Simulate physics and movements when not whirling
            for (int i = 0; i < 130; i++)
                simulate(1);

            // Dispose of the card if conditions are met
            if (disposing && getY() > 900) {
                Game.grabCardAnimation();
                getWorld().removeObject(this);
            } else if (Greenfoot.isKeyDown("G"))
                dispose(); // Also need to make sure there is enough energy points (ep)
        }
    }

    /**
     * Increment the whirl variable.
     */
    public void whirlUp() {
        whirl++;
    }

    /**
     * Dispose of the card. Play a sound effect, set card state to inactive, and
     * reposition.
     */
    public void dispose() {
        SoundManager.playSound("High Whoosh");
        active = 1000;
        mx = Greenfoot.getRandomNumber(201) + 200;
        my = Greenfoot.getRandomNumber(201) + 1100;
        leftBorder = false;
        disposing = true;
    }

    /**
     * Simulate physics-based movements of the card.
     *
     * @param k Number of simulation steps.
     */
    public void simulate(int k) {
        drag = true;
        if (active == 0)
            drag = false;
        else
            active--;

        if (stick)
            return;

        // Save current positions as previous
        for (int i = 0; i < 4; i++) {
            xprev[i] = x[i];
            yprev[i] = y[i];
        }

        // Apply drag forces if dragging
        if (drag) {
            yv[0] += (my - y[0]) / 1500000.0;
            xv[0] += (mx - x[0]) / 1500000.0;
        }

        // Simulate physics movements
        for (int m = 0; m < k; m++) {
            // Apply spring forces
            for (int f = 0; f < 6; f++) {
                l = Math.sqrt(Math.pow((x[conect1[f]] - x[conect2[f]]), 2) + Math.pow((y[conect1[f]] - y[conect2[f]]), 2));
                l = dl[f] - l;
                l = l / 10;
                ang = Math.atan2(y[conect1[f]] - y[conect2[f]], x[conect1[f]] - x[conect2[f]]);
                xv[conect1[f]] = xv[conect1[f]] + l * Math.cos(ang);
                xv[conect2[f]] = xv[conect2[f]] - l * Math.cos(ang);
                yv[conect1[f]] = yv[conect1[f]] + l * Math.sin(ang);
                yv[conect2[f]] = yv[conect2[f]] - l * Math.sin(ang);
            }

            // Update positions based on velocities
            for (int f = 0; f < 4; f++) {
                xv[f] = 0.9999 * xv[f];
                yv[f] = 0.9999 * yv[f];
                x[f] = x[f] + xv[f];
                y[f] = y[f] + yv[f];

                // Handle boundary conditions
                if (y[f] < 0) {
                    yv[f] = 0;
                    y[f] = 0;
                    xv[f] = 0.9 * xv[f];
                }
                if (leftBorder && x[f] < 0) {
                    yv[f] = 0.9 * yv[f];
                    x[f] = 0;
                    xv[f] = 0;
                }
                if (x[f] > 1200) {
                    yv[f] = 0.9 * yv[f];
                    x[f] = 1200;
                    xv[f] = 0;
                }
            }
        }

        // Calculate center of mass and rotation
        x1 = (x[0] + x[1] + x[2] + x[3]) / 4.0;
        y1 = (y[0] + y[1] + y[2] + y[3]) / 4.0;
        r1 = 180 * Math.atan2(y[0] - y[1], x[0] - x[1]) / Math.PI;

        // Set location and rotation
        setLocation((int) x2, (int) y2);
        setRotation((int) r2);

        // Update stored values if there is a significant change
        if (Math.abs(x1 - x2) > 1.5 || Math.abs(y1 - y2) > 1.5 || Math.abs(r1 - r2) > 1.5) {
            r2 = r1;
            x2 = x1;
            y2 = y1;
        }
    }
}