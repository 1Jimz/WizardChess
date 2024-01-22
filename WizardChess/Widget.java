import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Widget here.
 * 
 * @author David Guo
 * @version January 22nd, 2023
 */
public abstract class Widget extends Actor
{
    protected int actCount;
    
    public Widget(){
        actCount = 0;
    }
    /**
     * Act - do whatever the Widget wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        actCount++;
    }
}
