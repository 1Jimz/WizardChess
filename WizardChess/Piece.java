import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Piece here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Piece extends Actor
{
    private char type;//p,n,b,r,q,k,K(wiz)
    public Piece(char type){
        this.type=type;
    }
    public void act()
    {
        // Add your action code here.
    }
    public char getType(){
        return type;
    }
}
