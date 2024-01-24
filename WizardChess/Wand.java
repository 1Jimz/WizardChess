import greenfoot.*;

/**
 * This class represents the wand used by the player in the game. It extends SuperSmoothMover for smooth movements.
 * @author Jimmy Zhu
 * @version January 21st, 2023
*/
public class Wand extends SuperSmoothMover {
    private int xRadius = 58, yRadius = 38;

    /**
     * The main act method is called by the Greenfoot framework to perform actions.
     */
    public void act() {
        // Check if the spell is activated; if not, remove the wand from the world
        if (!Game.isSpellActivated()) {
            getWorld().removeObject(this);
        } else {
            // Set the location and rotation of the wand based on the Wizard's position and orientation
            setLocation((int) Math.round(Wizard.getH() + xRadius * Math.cos(Math.toRadians(Wizard.getDegrees()))),
                    (int) Math.round(Wizard.getV() + yRadius * Math.sin(Math.toRadians((180 + Wizard.getDegrees()) % 360))));
            setRotation(-Wizard.getDegrees() + 90);
        }
    }
}
