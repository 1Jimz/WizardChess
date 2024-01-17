import greenfoot.*; 
import java.util.*;
/**
 * <html>
 * <body>
 * <p><strong>Slider</strong> is an <em>Actor</em> subclass in Greenfoot used for creating a slider control on the main screen.</p>
 * <p>This class handles slider functionality, including value calculation based on slider position and interaction with the user via mouse controls.</p>
 * <p>Modified by David Guo. Originally created for the Casino Simulation Project.</p>
 * 
 * <h3>Usage:</h3>
 * <p>Instantiate a Slider object with an ID, minimum and maximum X positions, and value range. The slider updates its value based on user interaction and communicates this to a controlling world or class.</p>
 * 
 * <h3>Constructor</h3>
 * <p><strong>Slider(int id, int minX, int maxX, int minRange, int maxRange)</strong></p>
 * <ul>
 *     <li><strong>@param id</strong> - The unique identifier for the slider.</li>
 *     <li><strong>@param minX</strong> - The minimum X position of the slider.</li>
 *     <li><strong>@param maxX</strong> - The maximum X position of the slider.</li>
 *     <li><strong>@param minRange</strong> - The minimum value of the slider's range.</li>
 *     <li><strong>@param maxRange</strong> - The maximum value of the slider's range.</li>
 * </ul>
 * 
 * @author Dorsa Rohani, David Guo
 * @version 1.3 01/13/2024
 */
public class Slider extends Widget {
    private int sliderID;
    private int value;
    private int sliderMinX;  // min x position of the slider
    private int sliderMaxX;  // max x position of the slider
    private int rangeMin;    // min value of the slider
    private int rangeMax;    // max value of the slider

    public Slider(int id, int minX, int maxX, int minRange, int maxRange, int initialValue) {
        setImage("kingslider.png");
        this.sliderID = id;
        this.sliderMinX = minX;
        this.sliderMaxX = maxX;
        this.rangeMin = minRange;
        this.rangeMax = maxRange;
        setValue(initialValue);
        // setImage(...); // Set the image if needed
    }

    public void act() {
        if (Greenfoot.mouseDragged(this)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            int clampedX = Math.min(Math.max(mouse.getX(), sliderMinX), sliderMaxX);
            setLocation(clampedX, getY());
            value = calculateValue(clampedX);
            ((Settings)getWorld()).updateVar(sliderID, value);
        }
    }

    public void setValue(int newValue) {
        value = Math.min(Math.max(newValue, rangeMin), rangeMax);
        // No setLocation call here
    }

    private int calculateValue(int xPosition) {
        double fraction = (double)(xPosition - sliderMinX) / (sliderMaxX - sliderMinX);
        fraction = Math.min(Math.max(fraction, 0), 1); // ensures fraction is between 0 and 1
        value = (int)(rangeMin + (fraction * (rangeMax - rangeMin)));
        return value;
    }

    // Getter methods
    public int getRangeMin() {
        return rangeMin;
    }

    public int getRangeMax() {
        return rangeMax;
    }

    public int getSliderMinX() {
        return sliderMinX;
    }

    public int getSliderMaxX() {
        return sliderMaxX;
    }
}

