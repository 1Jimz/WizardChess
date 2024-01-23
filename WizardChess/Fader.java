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
    private boolean nextFader, nextWorld;
    private Fader fader;
    private World world;

    /**
     * Constructor for Fader without specifying color and screen fade direction.
     * 
     * @param skip           True if the fader is skippable via a click, false otherwise.
     * @param rateOfFading   The rate at which the fader fades in or out.
     */
    public Fader(boolean skip, double rateOfFading) {
        super();
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = false;
        fadeColor = Color.BLACK;
        nextFader = false;
        nextWorld = false;
    }

    /**
     * Constructor for Fader specifying color.
     * 
     * @param skip           True if the fader is skippable via a click, false otherwise.
     * @param rateOfFading   The rate at which the fader fades in or out.
     * @param colour         The color of the fader.
     */
    public Fader(boolean skip, double rateOfFading, Color colour) {
        super();
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = false;
        fadeColor = colour;
        nextFader = false;
        nextWorld = false;
    }

    /**
     * Constructor for Fader specifying screen fade direction.
     * 
     * @param skip           True if the fader is skippable via a click, false otherwise.
     * @param rateOfFading   The rate at which the fader fades in or out.
     * @param screenFadeOut  True if the fader fades out, false if it fades in.
     */
    public Fader(boolean skip, double rateOfFading, boolean screenFadeOut) {
        super();
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = screenFadeOut;
        fadeColor = Color.BLACK;
        nextFader = false;
        nextWorld = false;
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
        super();
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = screenFadeOut;
        fadeColor = colour;
        nextFader = false;
        nextWorld = false;
    }
    
    /**
     * Constructor for Fader specifying color and screen fade direction.
     * 
     * @param rateOfFading   The rate at which the fader fades in or out.
     * @param colour         The color of the fader.
     * @param screenFadeOut  True if the fader fades out, false if it fades in.
     * @param fader          The world which will spawn after the fade is finished.
     */
    public Fader(double rateOfFading, Color colour, boolean screenFadeOut, Fader fader) {
        super();
        fadeRate = rateOfFading;
        clickable = false;
        fadeOut = screenFadeOut;
        fadeColor = colour;
        this.fader = fader;
        nextFader = true;
        nextWorld = false;
    }
    
    /**
     * Constructor for Fader specifying color and screen fade direction.
     * 
     * @param rateOfFading   The rate at which the fader fades in or out.
     * @param colour         The color of the fader.
     * @param screenFadeOut  True if the fader fades out, false if it fades in.
     * @param world          The world which will spawn after the fade is finished.
     */
    public Fader(double rateOfFading, Color colour, boolean screenFadeOut, World world) {
        super();
        fadeRate = rateOfFading;
        clickable = false;
        fadeOut = screenFadeOut;
        fadeColor = colour;
        this.world = world;
        nextFader = false;
        nextWorld = true;
    }
    
    /**
     * Constructor for Fader specifying color and screen fade direction.
     * 
     * @param rateOfFading   The rate at which the fader fades in or out.
     * @param colour         The color of the fader.
     * @param screenFadeOut  True if the fader fades out, false if it fades in.
     * @param fader          The world which will spawn after the fade is finished.
     * @param world          The world which will spawn after the fade is finished.
     */
    public Fader(double rateOfFading, Color colour, boolean screenFadeOut, Fader fader, World world) {
        super();
        fadeRate = rateOfFading;
        clickable = false;
        fadeOut = screenFadeOut;
        fadeColor = colour;
        this.fader = fader;
        this.world = world;
        nextFader = true;
        nextWorld = true;
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
        super.act();
        if (!fadeOut) {
            trans = Math.max(0, trans - fadeRate);
            getImage().setTransparency((int) trans);
            if (trans == 0 || (clickable && Greenfoot.mouseClicked(this))){
                if(nextFader){
                    getWorld().addObject(fader, getWorld().getWidth()/2, getWorld().getHeight());
                }
                if(nextWorld){
                    Greenfoot.setWorld(world);
                }
                getWorld().removeObject(this);
            }
        } else {
            trans = Math.min(255, trans + fadeRate);
            getImage().setTransparency((int) trans);
            if (trans == 255 || (clickable && Greenfoot.mouseClicked(this))) {
                if(nextFader){
                    getWorld().addObject(fader, getWorld().getWidth()/2, getWorld().getHeight());
                }
                if(nextWorld){
                    Greenfoot.setWorld(world);
                }
                getWorld().removeObject(this);
            }
        }
    }
}
