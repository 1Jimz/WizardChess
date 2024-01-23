import greenfoot.*; 

/**
 * The Effects class represents various visual effects in the game.
 * It includes different types of effects, such as portal, explosion, bubble, and slash.
 * 
 * @author Mekaeel Malik 
 * @version January 21st, 2023
 */
public class Effects extends Actor
{
    private int type;               // Type of the effect (0: portal, 1: explosion, 2: bubble, 3: slash)
    private String imageName;       // Name of the image file for the effect
    private int timeLeft,            // Remaining time for the effect
                imageNumber,        // Current image number to be displayed
                maxImageNumber;     // Maximum number of images in the effect animation

    /**
     * Constructor for objects of class Effects.
     * Initializes the effect based on the specified type.
     * 
     * @param type Type of the effect (0: portal, 1: explosion, 2: bubble, 3: slash)
     */
    public Effects(int type) {
        this.type = type;
        switch(type) {
            case 0: imageName = "portal"; maxImageNumber = 9; break;
            case 1: imageName = "explosion"; maxImageNumber = 12; break;
            case 2: imageName = "bubble"; maxImageNumber = 12; break;
            case 3: imageName = "slash"; maxImageNumber = 4; break;
        }
        timeLeft = maxImageNumber * 10;
        imageNumber = maxImageNumber - 1;
    }
    
    /**
     * Performs actions during each act cycle.
     * Updates the image for the effect based on the remaining time and removes the effect if time is up.
     */
    public void act() {
        timeLeft--;
        if (timeLeft % 10 == 0) {
            setImage(new GreenfootImage("images/"+imageName+"/"+imageName+" ("+(maxImageNumber - imageNumber)+").png"));
            imageNumber--;
        } else if(timeLeft <= 0) {
            getWorld().removeObject(this);
        } 
    }
}