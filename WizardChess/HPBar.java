import greenfoot.*;

/**
 * The HPBar class represents the health bar for the Wizard in the game.
 * It displays the current health level, and its color changes based on the health level.
 * 
 * @author Dorsa Rohani
 * @version January 22nd, 2023
 */
public class HPBar extends Actor {
    private int maxHp=100, barWidth, barHeight; 
    private static int hp;
    private Color good, warning, danger;  // Color when HP is low (red)

    /**
     * Constructor for HPBar objects.
     * Initializes the health bar with the specified maximum health and updates the display.
     * 
     * @param maxHp The maximum health for the Wizard.
     */
    public HPBar(int hp) {
        //this.maxHp = n;
        this.hp = hp;
        barWidth = 250;
        barHeight = 20; 
        good = new Color(0, 255, 0);
        warning = new Color(255, 255, 0);
        danger = new Color(255, 0, 0);
        update();
    }

    /**
     * Sets the health level to the specified value and updates the display.
     * 
     * @param hp The new health level.
     */
    public void setHP(int hp) {
        this.hp = hp;
        update();
    }

    /**
     * Retrieves the current health level.
     * 
     * @return int The current health level.
     */
    public static int getHP(){
        return hp;
    }

    /**
     * Updates the health bar's display based on the current health level.
     */
    private void update() {
        GreenfootImage image = new GreenfootImage(barWidth + 2, barHeight + 2);
        
        // Choose color based on health level
        Color barColor;
        if (hp > maxHp / 2) {
            barColor = good;
        } else if (hp > maxHp / 4) {
            barColor = warning;
        } else {
            barColor = danger;
        }
        
        // Fill in the health bar with a gradient
        for (int i = 0; i < barWidth; i++) {
            image.setColor(new Color(112, 219, 166)); // Set color for health bar
            image.drawLine(i, 1, i, barHeight);
        }

        // Calculate fill width based on current health
        int fillWidth = (int) Math.round(((double) hp / maxHp) * barWidth);
        image.setColor(new Color(255, 255, 255, 100)); // Set color for semitransparent white overlay
        image.fillRect(fillWidth, 1, barWidth - fillWidth, barHeight);

        // Put text on the bar
        Font font = new Font("Verdana", true, false, 12); // Bold
        image.setFont(font);
        image.setColor(Color.BLACK);
        String hpText = hp + "/" + maxHp;
        int textWidth = new GreenfootImage(hpText, 12, Color.BLACK, new Color(0, 0, 0, 0)).getWidth();
        image.drawString(hpText, (barWidth - textWidth) / 2, barHeight - 5);

        // Show the picture
        setImage(image);
    }
}