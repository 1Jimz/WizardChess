import greenfoot.*;
public class Overlay extends Actor{
    private GifImage gi = new GifImage("RetroOverlay.gif");
    // public Overlay(){
        // //setImage(gif);
        // //gif = new GifImage("RetroLinesOverlay.gif");
    // }
    public void act(){
        setImage(Utility.customize(1200,740,gi.getCurrentImage(),40));
        //getImage().setTransparency(80);
        //getImage().
    }
}
