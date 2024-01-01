import greenfoot.*; 

public class HPBar extends Actor {
    private int hp;        
    private int maxHp;     // max health
    private int barWidth;  
    private int barHeight; 
    private Color good;    // color when hp is high (green)
    private Color warning; // color when hp is ok (yellow)
    private Color danger;  // color when hp is low (red)

    public HPBar(int maxHp) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        barWidth = 250;
        barHeight = 20; 
        good = new Color(0, 255, 0);
        warning = new Color(255, 255, 0);
        danger = new Color(255, 0, 0);
        update();
    }

    public void setHP(int hp) {
        this.hp = hp;
        update();
    }

    private void update() {
        GreenfootImage image = new GreenfootImage(barWidth + 2, barHeight + 2);
        // draw a border around it
        image.setColor(Color.WHITE);
        image.drawRect(0, 0, barWidth, barHeight);
        // choose color based on hp
        Color barColor;
        if (hp > maxHp / 2) {
            barColor = good;
        } else if (hp > maxHp / 4) {
            barColor = warning;
        } else {
            barColor = danger;
        }
        // fill in the hp bar with gradient
        for (int i = 0; i < barWidth; i++) {
            float ratio = (float) i / barWidth;
            Color startColor;
            if (barColor.equals(good)) {
                startColor = new Color(180, 255, 180); // light green for good health
            } else if (barColor.equals(warning)) {
                startColor = new Color(255, 255, 180); // light yellow for warning
            } else {
                startColor = new Color(255, 180, 180); // light red for danger
            }
            // calculates gradient color
            // credit: https://www.greenfoot.org/scenarios/4862
            // will do proper credit in api
            int red = (int) (barColor.getRed() * ratio + startColor.getRed() * (1 - ratio));
            int green = (int) (barColor.getGreen() * ratio + startColor.getGreen() * (1 - ratio));
            int blue = (int) (barColor.getBlue() * ratio + startColor.getBlue() * (1 - ratio));
            image.setColor(new Color(red, green, blue));
            image.drawLine(i, 1, i, barHeight);
        }

        // calculate fill width based on current HP
        int fillWidth = (int) Math.round(((double) hp / maxHp) * barWidth);
        image.setColor(new Color(255, 255, 255, 100)); // semitransparent white overlay
        image.fillRect(fillWidth, 1, barWidth - fillWidth, barHeight);

        // put text on the bar
        Font font = new Font("Verdana", true, false, 12); //bold
        image.setFont(font);
        image.setColor(Color.BLACK);
        String hpText = hp + "/" + maxHp;
        int textWidth = new GreenfootImage(hpText, 12, Color.BLACK, new Color(0, 0, 0, 0)).getWidth();
        image.drawString(hpText, (barWidth - textWidth) / 2, barHeight - 5);

        // show the picture
        setImage(image);
    }
}
