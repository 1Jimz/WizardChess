import greenfoot.*;

/**
 * This class represents a hand in the game. It extends SuperSmoothMover,
 * allowing smooth movements.
 */
public class Hand extends SuperSmoothMover {
    private int phase = -1;

    /**
     * The main act method is called by the Greenfoot framework to perform actions.
     */
    public void act() {
        phase++;

        // Move hand to the right during the initial phase
        if (phase == 0)
            setImage("HandSlide.png");
        else if (phase < 30)
            setLocation(getX() + 9, getY());
        // Change image to a hand holding a card
        else if (phase == 30)
            setImage("HandPickwithCard.png");
        // Move hand to the left
        else if (phase < 62)
            setLocation(getX() - 9, getY());
        // Ready to throw card, remove the hand object
        else {
            CardManager.readyThrowCard();
            getWorld().removeObject(this);
        }
    }
}