import greenfoot.*;
import java.io.*;
import java.util.*;
import java.lang.*;
/*
 Controls:
 WASD wizard movement(costs EP)
 G dispose card(costs EP)
 Click on card to use(cannot cancel when clicked)
 E end turn// need to implement
 */
public class Game extends World
{
    public final static int hPush=550,vPush=90;//maybe need change into private and use getter.
    private static boolean throwingCard=false, pickCard=false,leftBorder,spellActivated=false;
    private static int wave=1,throwX, throwY, throwActive, startX, startY;
    private Wizard wizard;
    private HPBar hpBar;
    //Thing that happens if two pieces step on the same tile at once during their movement. This is completely normal. Not a bug.
    public Game() throws IOException,InterruptedException{    
        super(1200, 740, 1, false);
        System.out.println("_____________________________________________________________");
        throwingCard=false;
        pickCard=false;
        spellActivated=false;
        EnemyTargetting.setup();
        //each time size 80
        for(int i = 0; i<8; i++)for(int j = 0; j<8; j++)addObject(new Tile(i,j),hPush+j*80,vPush+i*80);
        wizard = new Wizard();
        addObject(wizard,hPush+4*80,vPush+7*80-25);
        addObject(new HPBar(100), 100, 30); // assuming 100 health
    }
    private void updateHP(int newHP) {
        hpBar.setHP(newHP);
    }
    public void wizardTakesDamage(int dmg) {
        wizard.takeDmg(dmg); // Wizard class handles its own HP reduction
        updateHP(wizard.getHP()); // Update the HP bar
    }
    public static int getWave(){
        return wave;
    }
    public static void throwCard(int tX, int tY, int tA, int lB, int sX, int sY){
        throwX=tX;
        throwY=tY;
        throwActive=tA;
        leftBorder=lB!=0;
        startX=sX;
        startY=sY;
        throwingCard=true;
    }
    public void act(){
        zSort((ArrayList<Actor>)(getObjects(Actor.class)),this);//if takes too much resources then comment out
        //setPaintOrder(Wizard.class, Wand.class);//
        if(pickCard){
            addObject(new Hand(),-120,510);
            pickCard=false;
        }
        if(throwingCard){
            addObject(new Card(throwX,throwY,throwActive,leftBorder),startX,startY);
            throwingCard=false;
        }
        //List<Card> cards = getObjects(Card.class);
        //for(int i=0;i<130;i++)for(Card c : cards)c.simulate(1);
    }
    public static void grabCardAnimation(){
        pickCard=true;
    }
    public static void activateSpell(){
        spellActivated=true;
    }
    public static void deactivateSpell(){
        spellActivated=false;
    }
    public static boolean isSpellActivated(){
        return spellActivated;
    }
    //mr cohen's Zsort. Credit if needed
    public static void zSort (ArrayList<Actor> actorsToSort, World world){
        ArrayList<ActorContent> acList = new ArrayList<ActorContent>();
        // Create a list of ActorContent objects and populate it with all Actors sent to be sorted
        for (Actor a : actorsToSort){
            try{
                Tile t=(Tile)a;
            }catch(ClassCastException e1){
                try{
                    Card c=(Card)a;
                }catch(ClassCastException e2){
                acList.add (new ActorContent (a, a.getX(), a.getY()));
                }
            }
        }    
        // Sort the Actor, using the ActorContent comparitor (compares by y coordinate)
        Collections.sort(acList);
        // Replace the Actors from the ActorContent list into the World, inserting them one at a time
        // in the desired paint order (in this case lowest y value first, so objects further down the 
        // screen will appear in "front" of the ones above them).
        for (ActorContent a : acList){
            Actor actor  = a.getActor();
            world.removeObject(actor);
            world.addObject(actor, a.getX(), a.getY());
        }
    }
    static class ActorContent implements Comparable <ActorContent> {
    private Actor actor;
    private int xx, yy;
    public ActorContent(Actor actor, int xx, int yy){
        this.actor = actor;
        this.xx = xx;
        this.yy = yy;
    }

    public void setLocation (int x, int y){
        xx = x;
        yy = y;
    }

    public int getX() {
        return xx;
    }

    public int getY() {
        return yy;
    }

    public Actor getActor(){
        return actor;
    }

    public String toString () {
        return "Actor: " + actor + " at " + xx + ", " + yy;
    }

    public int compareTo (ActorContent a){
        return this.getY() - a.getY();
    }

}
}
