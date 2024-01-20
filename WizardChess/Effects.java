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
    private int timeLeft, imageNumber, maxImageNumber;
    //private ArrayList<Integer> targets;

    /**
     * Constructor for objects of class Effects
     */
    public Effects(int type) {
        this.type = type;
        switch(type){
            case 0: imageName = "portal"; maxImageNumber = 9;break;
            case 1: imageName = "slash"; maxImageNumber = 4;break;
            case 2: imageName = "bubble"; maxImageNumber = 12;break;
            case 3: imageName = "explosion"; maxImageNumber = 12;break;
        }
        timeLeft = maxImageNumber * 10;
        imageNumber = maxImageNumber - 1;
        //targets = new ArrayList<Integer>();
    }
    
    public void act() {
        timeLeft--;
        if (timeLeft % 10 == 0) {
            setImage(new GreenfootImage("images/"+imageName+"/"+imageName+" ("+(maxImageNumber - imageNumber)+").png"));
            imageNumber--;
        } else if(timeLeft <= 0) {
            getWorld().removeObject(this);
        } 
    }

    /*
    public void addTarget(int x, int y) {
        targets.add(x);
        targets.add(y);
    }
    */
}
