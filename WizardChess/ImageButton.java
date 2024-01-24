import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A modular button which can be pressed. 
 * The image may highlight itself and/or make a sound when hovered over.
 * 
 * @author David Guo
 * @version January 22nd, 2023
 */
public class ImageButton extends Widget
{
    private int width, height;
    private GreenfootImage img, imgHover;
    private GreenfootSound hover, click;
    
    /**
     * Constructs a Button which can be set using an image from the game files
     * @param img                GreenfootImage value of the image
     */
    public ImageButton(GreenfootImage img){
        this.img = img;
        this.imgHover = img;
        width = img.getWidth();
        height = img.getHeight();
        setImage(img);
    }
    
    /**
     * Constructs a Button which can be set using an image from the game files
     * @param image              String value of the image name without the file type
     */
    public ImageButton(String image){
        img = new GreenfootImage(image+".png");
        imgHover = new GreenfootImage(image+".png");
        width = img.getWidth();
        height = img.getHeight();
        setImage(img);
    }
    
    /**
     * Constructs a Button which can be set using an image from the game files
     * @param image              String value of the image name without the file type
     * @param width              Int value which determines the width of the image
     * @param height             Int value which determines the height of the image
     */
    public ImageButton(String image, int width, int height){
        img = new GreenfootImage(image+".png");
        imgHover = new GreenfootImage(image+".png");
        img.scale(width, height);
        setImage(img);
    }
    
    /**
     * Constructs a Button which can be set using an image from the game files
     * @param image              String value of the image name without the file type
     * @param imageHover         String value of the image which is shown when the mouse is on the button
     */
    public ImageButton(String image, String imageHover){
        img = new GreenfootImage(image+".png");
        imgHover = new GreenfootImage(imageHover+".png");
        width = img.getWidth();
        height = img.getHeight();
        setImage(img);
    }
    
    /**
     * Constructs a Button which can be set using an image from the game files
     * @param image              String value of the file name without the file type
     * @param width              Int value which determines the width of the image
     * @param height             Int value which determines the height of the image
     * @param imageHover         String value of the image which is shown when the mouse is on the button
     */
    public ImageButton(String image, int width, int height, String imageHover){
        img = new GreenfootImage(image+".png");
        imgHover = new GreenfootImage(imageHover+".png");
        img.scale(width, height);
        setImage(img);
    }
    
    /**
     * Act - do whatever the ImageButton wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if(actCount % 2 == 0){
            if(Greenfoot.mouseMoved(this)){ 
                //Changes the button to its "hover form"
                this.hoverOver();
            }
            //resets the button to its normal state
            if(Greenfoot.mouseMoved(null) &&!Greenfoot.mouseMoved(this)) this.reset();
        }
        super.act();
    }
    
    /**
     * Changes the button to its "hover form" if the mouse is hoving over a button
     */
    private void hoverOver(){
        setImage(imgHover);
    }
    /**
     * Resets the button to the original if the mouse moves away from it
     */
    private void reset()
    {
        setImage(img);
    }
}
