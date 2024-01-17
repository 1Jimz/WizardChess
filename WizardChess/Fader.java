import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A fading square which can be customized in colour and speed, along with being skippable
 * via a click
 * 
 * @author David Guo (Thanks to @Jordan Cohen for the demonstration)
 * @version 1.1 01/14/2024
 */
public class Fader extends Widget
{
    private GreenfootImage image;
    private boolean clickable;
    private double trans;
    private double fadeRate;
    private boolean fadeOut;
    private Color fadeColor;
    public Fader (boolean skip, double rateOfFading) {
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = false;
        fadeColor = Color.BLACK;
    }
    
    public Fader (boolean skip, double rateOfFading, Color colour) {
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = false;
        fadeColor = colour;
    }
    
    public Fader (boolean skip, double rateOfFading, boolean screenFadeOut) {
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = screenFadeOut;
        fadeColor = Color.BLACK;
    }
    
    public Fader (boolean skip, double rateOfFading, Color colour, boolean screenFadeOut) {
        fadeRate = rateOfFading;
        clickable = skip;
        fadeOut = screenFadeOut;
        fadeColor = colour;
    }
    
    public void addedToWorld(World w){
        image = new GreenfootImage (w.getWidth(), w.getHeight());
        image.setColor(fadeColor);
        image.fill();
        setImage(image);
        if(!fadeOut){
            trans = image.getTransparency(); // 255, fully opaque
        } else {
            trans = 0;
        }
    }
    /**
     * Act - do whatever the Fader wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if(!fadeOut){
            trans = Math.max(0, trans - fadeRate);
            getImage().setTransparency((int)trans);
            if(trans == 0 || (clickable && Greenfoot.mouseClicked(this))){
                getWorld().removeObject(this);
            }
        } else {
            trans = Math.min(255, trans + fadeRate);
            getImage().setTransparency((int)trans);
            if(trans == 255 || (clickable && Greenfoot.mouseClicked(this))){
                getWorld().removeObject(this);
            }
        }
    }
}
