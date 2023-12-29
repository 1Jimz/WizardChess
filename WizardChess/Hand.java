import greenfoot.*;
public class Hand extends SuperSmoothMover
{
    private int phase=-1;
    public void act(){
        phase++;
        if(phase==0)setImage("HandSlide.png");
        else if(phase<30)setLocation(getX()+9,getY());
        else if(phase==30)setImage("HandPickwithCard.png");
        else if(phase<62)setLocation(getX()-9,getY());
        else {
            CardManager.readyThrowCard();
            getWorld().removeObject(this);
        }
    }
}
