import greenfoot.*;
/**
 * <html>
 * <body>
 * <h1>Message Class</h1>
 * <p>The <em>Message</em> class represents a message displayed on the screen with customizable text, color, size, lifespan, levitating speed, and fading effect.</p>
 * <h2>Class Attributes:</h2>
 * <ul>
 *     <li><strong>lifespan (int):</strong> The remaining lifespan of the message in acts.</li>
 *     <li><strong>startingLifespan (int):</strong> The initial lifespan of the message in acts.</li>
 *     <li><strong>leviatingSpeed (int):</strong> The speed at which the message levitates.</li>
 *     <li><strong>isNew (boolean):</strong> A boolean indicating if the message is new (not yet added to the world).</li>
 *     <li><strong>value (String):</strong> The text content of the message.</li>
 *     
 * </ul>
 * @author Jimmy Zhu, Dorsa Rohani
 * @version January 22nd, 2023
 */

public class Message extends Actor {
    private int lifespan = 50, startingLifespan=50, leviatingSpeed=5; // increased lifespan for a slower fade-out
    private boolean isNew=false;
    private String value;
    private Actor a;
    /**
     * <h3>Message(String text, Color color)</h3>
     * <p>Creates a message with the specified text and text color.</p>
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *     <li><strong>text (String):</strong> The text content of the message.</li>
     *     <li><strong>color (Color):</strong> The color of the text.</li>
     * </ul>
     */
    public Message(String text, Color color) {
        GreenfootImage img = new GreenfootImage(text, 24, color, new Color(0, 0, 0, 0));
        setImage(img);
        value=text;
    }
    /**
     * <h3>Message(String text, Color color, int lifespan, int leviatingSpeed, int size)</h3>
     * <p>Creates a message with the specified text, text color, lifespan, levitating speed, and text size.</p>
     * <p><strong>Parameters:</strong></p>
     * <ul>
     *     <li><strong>text (String):</strong> The text content of the message.</li>
     *     <li><strong>color (Color):</strong> The color of the text.</li>
     *     <li><strong>lifespan (int):</strong> The lifespan of the message in acts before it disappears.</li>
     *     <li><strong>leviatingSpeed (int):</strong> The speed at which the message levitates.</li>
     *     <li><strong>size (int):</strong> The size of the text in the message.</li>
     * </ul>
     */
    public Message(String text, Color color, int lifespan, int leviatingSpeed, int size) {
        GreenfootImage img = new GreenfootImage(text, size, color, new Color(0, 0, 0, 0));
        setImage(img);
        value=text;
        startingLifespan=lifespan;
        this.lifespan=lifespan;
        this.leviatingSpeed=leviatingSpeed;
    }
    /**
     * <h3>void act()</h3>
     * <p>Updates the message's position and transparency each act to create a levitating and fading effect.</p>
     */
    public void act() {
        update();
    }
    private void update() {
        if (lifespan > 0) {
            // move up more slowly
            setLocation(getX(), getY() - (startingLifespan - lifespan) / (startingLifespan/leviatingSpeed));

            // fade out more gradually
            GreenfootImage img = getImage();
            int transparency = 255 * lifespan / startingLifespan; // adjusted for a more gradual fade
            img.setTransparency(transparency);
            lifespan--;
        } 
        else getWorld().removeObject(this);
    }
}