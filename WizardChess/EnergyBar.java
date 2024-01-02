import greenfoot.*; 

public class EnergyBar extends Actor {
    private static int e, maxE, barWidth, barHeight; 
    private Color fullEnergy, mediumEnergy, lowEnergy;

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
    
    public void act(){
        
    }

    public static int getE(){
        return e;
    }
    public void setE(int e) {
        this.e = e;
        update();
    }

    private void update() {
        if(e >= 0){
            GreenfootImage image = new GreenfootImage(barWidth + 2, barHeight + 2);
            // draw a border around it
            image.setColor(Color.WHITE);
            image.drawRect(0, 0, barWidth, barHeight);
            // choose color based on energy
            Color barColor;
            if (e > maxE / 2) {
                barColor = fullEnergy;
            } else if (e > maxE / 4) {
                barColor = mediumEnergy;
            } else {
                barColor = lowEnergy;
            }
            // fill in the energy bar with gradient
            // calculates gradient color
            // credit: https://www.greenfoot.org/scenarios/4862
            // will do proper credit in api
            for (int i = 0; i < barWidth; i++) {
                float ratio = (float) i / barWidth;
                Color startColor = new Color(0, 255, 255); // light blue for starting color
                int red = (int) (barColor.getRed() * ratio + startColor.getRed() * (1 - ratio));
                int green = (int) (barColor.getGreen() * ratio + startColor.getGreen() * (1 - ratio));
                int blue = (int) (barColor.getBlue() * ratio + startColor.getBlue() * (1 - ratio));
                image.setColor(new Color(red, green, blue));
                image.drawLine(i, 1, i, barHeight);
            }
            // calculate fill width based on current energy
            int fillWidth = (int) Math.round(((double) e / maxE) * barWidth);
            image.setColor(new Color(255, 255, 255, 100)); // semitransparent white overlay
            image.fillRect(fillWidth, 1, barWidth - fillWidth, barHeight);
    
            // put text on the bar
            Font font = new Font("Verdana", true, false, 12); //bold
            image.setFont(font);
            image.setColor(Color.BLACK);
            String eText = e + "/" + maxE;
            int textWidth = new GreenfootImage(eText, 12, Color.BLACK, new Color(0, 0, 0, 0)).getWidth();
            image.drawString(eText, (barWidth - textWidth) / 2, barHeight - 5);
    
            // show the picture
            setImage(image);
        }
    }
}
