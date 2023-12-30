import greenfoot.*;
public class Wand extends SuperSmoothMover{
    private int xRadius = 58, yRadius = 38;
    public void act(){
        if(!Game.isSpellActivated())getWorld().removeObject(this);
        else{
            setLocation((int)Math.round(Wizard.getH()+xRadius*Math.cos(Math.toRadians(Wizard.getDegrees()))),(int)Math.round(Wizard.getV()+yRadius*Math.sin(Math.toRadians((180+Wizard.getDegrees())%360))));
            setRotation(-Wizard.getDegrees()+90);
        }
    }
}
