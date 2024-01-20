import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A popup which spawns after starting a new game.
 * 
 * @author David Guo
 * @version 1.1 01/14/2024
 */
public class Tutorial extends Widget
{
    // private variables
    private TextButton back, next, count; // buttons to switch pages and display page number
    private GreenfootImage[] pages; // create array of how to play images
    private int width, height; // width and height of image
    private int xDist, yDist; // distance of the buttons from the edges of the screen
    private int maxPageNum, pageNum; // page number
    /**
     * Constructor for objects of class HowToPlay.
     * 
     */
    public Tutorial()
    {    
        // Width, height, maxPageNum in order to make it modular
        width = 1200*5/6;
        height = 740*5/6;
        maxPageNum = 5;
        // Initialize variables to change button location
        xDist = 80; // from the edges left/right
        yDist = 20; // from the bottom
        // Initialize pageNum, which can be changed by pressing either back or next
        pageNum = 1;
        // The amount of pages in the how to play menu
        pages = new GreenfootImage[maxPageNum];
        // Assign the photos to the variables
        for(int i = 0; i < pages.length; i++){
            pages[i] = new GreenfootImage("tutorial" + (i+1) + ".png");
        }
        // Scale to appropriate size
        for(int i = 0; i < pages.length; i++){
            pages[i].scale(width, height);
        }
        // Create new buttons
        back = new TextButton("BACK", 35, 255, 255, 255, 255, 20, 147);
        next = new TextButton("NEXT", 35, 255, 255, 255, 255, 20, 147);
        count = new TextButton("PAGE " + pageNum + " / " + maxPageNum, 30, 155, 155, 155, 155, 155, 155);
    }
    
    public void act(){
        // changes page numner depending on which button is clicked
        if(Greenfoot.mouseClicked(back)){
            SoundManager.playSound("Clock Ticking");
            pageNum--;
            updateCount();
        } else if (Greenfoot.mouseClicked(next)){
            SoundManager.playSound("Clock Ticking");
            pageNum++;
            updateCount();
        }
        // if page number is out of bounds of the image array, go back to the title screen
        if(pageNum <= 0){
            getWorld().removeObject(back);
            getWorld().removeObject(next);
            getWorld().removeObject(count);
            getWorld().removeObject(this);
        } else if(pageNum > pages.length){
            getWorld().removeObject(back);
            getWorld().removeObject(next);
            getWorld().removeObject(count);
            try{
                ((TitleScreen)getWorld()).startGame();
            } catch(InterruptedException e){} catch(java.io.IOException e){};
            getWorld().removeObject(this);
        } else {
            setImage(pages[pageNum-1]);
        }
    }
    
    // Greenfoot needed this method for it to be added
    public void addedToWorld(World w){
        // Adds buttons to the world
        getWorld().addObject(back, getX()-width/2+xDist, getY()+height/2-yDist);
        getWorld().addObject(next, getX()+width/2-xDist, getY()+height/2-yDist);
        getWorld().addObject(count, getX(), getY()+height/2-yDist);
    }
    
    // Helper method to update counter
    private void updateCount(){
        getWorld().removeObject(count);
        count = new TextButton("PAGE " + pageNum + " / " + maxPageNum, 30, 155, 155, 155, 155, 155, 155);
        getWorld().addObject(count, getX(), getY()+height/2-yDist);
    }
}
