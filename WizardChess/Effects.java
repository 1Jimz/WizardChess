import greenfoot.*; 

/**
 * Write a description of class Effects here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Effects extends Actor
{
    // instance variables - replace the example below with your own
    private int type;
    private String imageName;
    private int timeLeft, imageNumber;

    /**
     * Constructor for objects of class Effects
     */
    public Effects(int type) {
        this.type = type;
        switch(type){
            case 0: imageName = "explosion"; timeLeft = 60;break;
            case 1: imageName = "slash"; timeLeft = 60;break;
            case 2: imageName = "bubble"; timeLeft = 60;break;
            case 3: imageName = "portal"; timeLeft = 60;break;
        }
        imageNumber = 1;
    }
    
    public void act() {
        timeLeft--;
        if (timeLeft % 10 == 0) {
            setImage(new GreenfootImage("images/"+imageName+"/"+imageName+" ("+imageNumber+").png"));
            imageNumber++;
        } else if(timeLeft <= 0) {
            getWorld().removeObject(this);
        } 
    }
}
