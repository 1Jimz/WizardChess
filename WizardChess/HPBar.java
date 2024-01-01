import greenfoot.*; 

public class HPBar extends Actor {
    private int hp;        
    private int maxHp;     // max health
    private int barWidth;  // width of bar
    private int barHeight; // height of bar
    private Color good;    // color when hp is high (green)
    private Color warning; // color when hp is ok (yellow)
    private Color danger;  // color when hp is low (red)

    public HPBar(int maxHp) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        barWidth = 100;
        barHeight = 15;
        good = Color.GREEN;
        warning = Color.YELLOW;
        danger = Color.RED;
        update();
    }

    public void setHP(int hp) {
        this.hp = hp;
        update();
    }

    private void update() {
        GreenfootImage image = new GreenfootImage(barWidth + 2, barHeight + 2);
        // draw a border around it
        image.setColor(Color.BLACK);
        image.drawRect(0, 0, barWidth + 1, barHeight + 1);
        // choose color based on hp
        Color barColor;
        if (hp > maxHp / 2) {
            barColor = good;
        } else if (hp > maxHp / 4) {
            barColor = warning;
        } else {
            barColor = danger;
        }
        // fill in the hp bar
        image.setColor(barColor);
        int fillWidth = (int) Math.round(((double) hp / maxHp) * barWidth);
        image.fillRect(1, 1, fillWidth, barHeight);
        // put text on the bar
        image.setColor(Color.BLACK);
        image.drawString(hp + "/" + maxHp, 25, barHeight);
        // show the picture
        setImage(image);
    }
}
