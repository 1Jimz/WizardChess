import greenfoot.*;

/**
 * A fading square which can be customized in colour and speed, along with being skippable
 * via a click
 * 
 * @author David Guo (Thanks to @Jordan Cohen for the demonstration)
 * @version 1.1 01/14/2024
 */
public class Fader extends Widget {
    private GreenfootImage image;
    private boolean clickable;
    private double trans;
    private double fadeRate;
    private boolean fadeOut;
    private Color fadeColor;

    /**
     * Constructor for Fader without specifying color and screen fade direction.
     * 
     * @param skip           True if the fader is skippable via a click, false otherwise.
     * @param rateOfFading   The rate at which the fader fades in or out.
     */
    public Fader(boolean skip, double rateOfFading) {
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = false;
        fadeColor = Color.BLACK;
    }

    /**
     * Constructor for Fader specifying color.
     * 
     * @param skip           True if the fader is skippable via a click, false otherwise.
     * @param rateOfFading   The rate at which the fader fades in or out.
     * @param colour         The color of the fader.
     */
    public Fader(boolean skip, double rateOfFading, Color colour) {
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = false;
        fadeColor = colour;
    }

    /**
     * Constructor for Fader specifying screen fade direction.
     * 
     * @param skip           True if the fader is skippable via a click, false otherwise.
     * @param rateOfFading   The rate at which the fader fades in or out.
     * @param screenFadeOut  True if the fader fades out, false if it fades in.
     */
    public Fader(boolean skip, double rateOfFading, boolean screenFadeOut) {
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = screenFadeOut;
        fadeColor = Color.BLACK;
    }

    /**
     * Constructor for Fader specifying color and screen fade direction.
     * 
     * @param skip           True if the fader is skippable via a click, false otherwise.
     * @param rateOfFading   The rate at which the fader fades in or out.
     * @param colour         The color of the fader.
     * @param screenFadeOut  True if the fader fades out, false if it fades in.
     */
    public Fader(boolean skip, double rateOfFading, Color colour, boolean screenFadeOut) {
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = screenFadeOut;
        fadeColor = colour;
    }

    /**
     * Method called when the object is added to the world.
     * Initializes the image and transparency based on fade direction.
     * 
     * @param w The world to which the fader is added.
     */
    public void addedToWorld(World w) {
        image = new GreenfootImage(w.getWidth(), w.getHeight());
        image.setColor(fadeColor);
        image.fill();
        setImage(image);
        if (!fadeOut) {
            trans = image.getTransparency(); // 255, fully opaque
        } else {
            trans = 0;
        }
    }

    /**
     * Act method called on every game cycle.
     * Handles the fading in or out of the fader based on its settings.
     */
    public void act() {
        if (!fadeOut) {
            trans = Math.max(0, trans - fadeRate);
            getImage().setTransparency((int) trans);
            if (trans == 0 || (clickable && Greenfoot.mouseClicked(this))) {
                getWorld().removeObject(this);
            }
        } else {
            trans = Math.min(255, trans + fadeRate);
            getImage().setTransparency((int) trans);
            if (trans == 255 || (clickable && Greenfoot.mouseClicked(this))) {
                getWorld().removeObject(this);
            }
        }
    }
}
