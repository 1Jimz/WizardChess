import greenfoot.*;

/**
 * The EnergyBar class represents the energy bar for the Wizard in the game.
 * It displays the current energy level, and its color changes based on the energy level.
 * 
 * @author Jimmy Zhu
 * @version January 21st, 2023
 */
public class EnergyBar extends Actor {
    private static int e, maxE, barWidth, barHeight; 
    private Color fullEnergy, mediumEnergy, lowEnergy;

    /**
     * Constructor for EnergyBar objects.
     * Initializes the energy bar with the specified maximum energy and updates the display.
     * 
     * @param maxE The maximum energy level for the Wizard.
     */
    public EnergyBar(int maxE) {
        this.maxE = maxE;
        this.e = maxE;
        barWidth = 250;
        barHeight = 20; 
        fullEnergy = new Color(0, 0, 255, 255);
        mediumEnergy = new Color(135, 206, 255);
        lowEnergy = new Color(176, 226, 255);
        update();
    }
    
    /**
     * Empty act method to comply with Greenfoot Actor requirements.
     */
    public void act(){
        // No specific actions required during each act cycle.
    }

    /**
     * Retrieves the current energy level.
     * 
     * @return int The current energy level.
     */
    public static int getE(){
        return e;
    }

    /**
     * Sets the energy level to the specified value and updates the display.
     * 
     * @param e The new energy level.
     */
    public void setE(int e) {
        this.e = e;
        update();
    }

    /**
     * Updates the energy bar's display based on the current energy level.
     */
    private void update() {
        if(e >= 0) {
            GreenfootImage image = new GreenfootImage(barWidth + 2, barHeight + 2);
            
            // Choose color based on energy level
            Color barColor;
            if (e > maxE / 2) {
                barColor = fullEnergy;
            } else if (e > maxE / 4) {
                barColor = mediumEnergy;
            } else {
                barColor = lowEnergy;
            }
            
            // Fill in the energy bar with a gradient
            for (int i = 0; i < barWidth; i++) {
                image.setColor(new Color(112, 219, 202)); // Set color for energy bar
                image.drawLine(i, 1, i, barHeight);
            }
            
            // Calculate fill width based on current energy
            int fillWidth = (int) Math.round(((double) e / maxE) * barWidth);
            image.setColor(new Color(255, 255, 255, 100)); // Set color for semitransparent white overlay
            image.fillRect(fillWidth, 1, barWidth - fillWidth, barHeight);
    
            // Put text on the bar
            Font font = new Font("Verdana", true, false, 12); // Bold
            image.setFont(font);
            image.setColor(Color.BLACK);
            String eText = e + "/" + maxE;
            int textWidth = new GreenfootImage(eText, 12, Color.BLACK, new Color(0, 0, 0, 0)).getWidth();
            image.drawString(eText, (barWidth - textWidth) / 2, barHeight - 5);
    
            // Show the picture
            setImage(image);
        }
    }
}