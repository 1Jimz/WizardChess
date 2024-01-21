import greenfoot.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
/*
 Controls:
 WASD wizard movement(costs EP)
 G dispose card(costs EP)
 Click on card to use(cannot cancel when clicked)
 Enter to end turn
 
 Not bug list:
 when card is in process of being turned into a spell and mouse is off screen the process freezes, this is intentional.
 when it is not wizard's turn and spell can continue to be active.//maybe make it so have to use spell before next round
 
 */
public class Game extends World
{
    public final static int hPush=550,vPush=90;//maybe need change into private and use getter.
    private static boolean throwingCard=false, pickCard=false,leftBorder,spellActivated=false;
    private static int wave=1,throwX, throwY, throwActive, startX, startY;
    private static Wizard wizard;
    private HPBar hpBar;
    private EnergyBar energyBar;
    private static int level;
    private static Text waveNumber;
    private static String[] levelFens;
    private static boolean canNewWave,kingDied;
    public Game() throws IOException,InterruptedException{    
        super(1200, 740, 1, false);
        System.out.println("_____________________________________________________________");
        throwingCard=false;
        pickCard=false;
        spellActivated=false;
        canNewWave=false;
        enemyMoving = false;
        keyPressChecked = true;
        kingDied=false;
        moveNumber = 0;//
        level = 0;
        EnemyTargetting.setup();
        //each time size 80
        for(int i = 0; i<8; i++)for(int j = 0; j<8; j++)addObject(new Tile(i,j),hPush+j*80,vPush+i*80);
        energyBar = new EnergyBar(100);
        addObject(energyBar, 279, 270);
        wizard = new Wizard();
        wizard.setEnergyBar(energyBar);
        addObject(wizard,hPush+4*80,vPush+7*80-25);
        addObject(new HPBar(100), 279, 210); // assuming 100 health?
        waveNumber = new Text(30,"Arial",Integer.toString(level));
        addObject(waveNumber,952,731);
        //addObject(new Overlay(), 600,370);
        //setPaintOrder(CardHitbox.class,Overlay.class);
        levelFens = new String[8];
        levelFens[0] = "2bk1b2/4pppp/8/6K1/8/8/8/8 b - - 0 1";
        levelFens[1] = "rnbkq3/ppppp3/8/8/8/8/7K/8 b - - 0 1";
        levelFens[2] = "r7/3n1k2/4b1q1/2p5/1p6/8/3K4/8 b - - 0 1";
        levelFens[3] = "2rq1rk1/5ppp/8/8/8/8/1K2p3/8 b - - 0 1";
        levelFens[4] = "6k1/1bp5/p1n4q/8/6p1/7p/4K3/r7 b - - 0 1";
        levelFens[5] = "q4r2/1p4kp/1p3bp1/5p2/6b1/3K4/8/8 b - - 0 1";
        levelFens[6] = "2b5/1p6/k1p5/1pbp4/r7/3K4/8/8 b - - 0 1";
        levelFens[7] = "q3k3/ppp1nppp/2n1p3/2bp4/6b1/3K4/8/8 b - - 0 1";
    }
    private static int moveNumber;
    public static void nextMove() {
        moveNumber++;
    }
    public static int moveCount() {
        return moveNumber;
    }
    public static boolean wizardTurn() {
        if(moveNumber % 2 == 0) {
            return true;
        }
        return false;
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
    public static Wizard getWizard(){
        return wizard;
    }
    private boolean keyPressChecked = true;
    private static boolean enemyMoving = false;
    public void act(){
        zSort((ArrayList<Actor>)(getObjects(Actor.class)),this);//if takes too much resources then comment out
        if(pickCard){
            addObject(new Hand(),-120,510);
            pickCard=false;
        }
        if(throwingCard){
            addObject(new Card(throwX,throwY,throwActive,leftBorder),startX,startY);
            throwingCard=false;
        }
        if((wizardTurn()&&Greenfoot.isKeyDown("Enter"))||(!wizardTurn()&&BoardManager.getCountdown()<=0)) {
            if(keyPressChecked) {
                nextMove();
                enemyMoving = false;
                if(!wizardTurn()) {
                    System.out.println("SNAO"+" "+BoardManager.isWarned());
                    if(BoardManager.isWarned()) {
                        BoardManager.spawnPieces();
                        BoardManager.unwarn();
                        //System.out.println("E");
                        nextMove();
                        canNewWave=true;
                    }
                    else {
                        if(!canNewWave) {
                            //System.out.println("asd");
                            //BoardManager.resetTiles();
                            //for(Piece p: getObjects(Piece.class))p.kill();
                            nextLevel();
                            //canNewWave=false;
                        }
                        else{
                            try{
                                try{
                                    if(!enemyMoving) {
                                        enemyMoving = true;
                                        //BoardManager.enemyTurn(6,1,200);
                                        BoardManager.enemyTurn(6,1000,1);
                                    }
                                }catch(IOException e1){}
                            }catch(InterruptedException e2){}
                        }
                    }
                }
                keyPressChecked = false;
            }
        } 
        else keyPressChecked = true;
        if(canNewWave&&kingDied) {
            BoardManager.resetTiles();
            BoardManager.wipe();
            for(Piece p: getObjects(Piece.class))p.kill();
            nextLevel();
            nextMove();
            canNewWave=false;
            kingDied=false;
        } 
        if(level == 8){
            //addObject(new Fader(false, 3, Color.BLACK, true), getWidth()/2, getHeight()/2);
            Greenfoot.setWorld(new EndScreen(false));
        }
    }
    public static void grabCardAnimation(){
        pickCard=true;
    }
    public void enemyFinishedMoving() {
        enemyMoving = false;
    }
    public static void activateSpell(){
        //Wizard.highlightRange(200);//200 is temp val
        spellActivated=true;
    }
    public static void deactivateSpell(){
        spellActivated=false;
        BoardManager.resetTiles();
    }
    public static boolean isSpellActivated(){
        return spellActivated;
    }
    private static Scanner readFile;
    public static void nextLevel() {
        level++;
        BoardManager.createIncoming(levelFens[level-1]);
        BoardManager.warn();
        nextMove();
        waveNumber.changeText(Integer.toString(level));
    }
    
    private static FileWriter out;
    private static PrintWriter output;
    
    public static void saveProgress() throws IOException {
        try{
            out = new FileWriter("saveFile.txt",false);
            output = new PrintWriter(out);
            output.println(Integer.toString(level));
            output.println(BoardManager.currentFEN());
            output.println(BoardManager.getPiecesHP());
        } catch(IOException e) {}
        finally {
            out.close();
            output.close();
        }
    }
    public static void kingDying(){
        kingDied=true;
    }
    private static Scanner scanFile;
    public static boolean loadProgress() {
        try{
            scanFile = new Scanner(new File("saveFile.txt"));
            level = Integer.parseInt(scanFile.nextLine());
            BoardManager.createIncoming(scanFile.nextLine());
            return false;
        } catch(IOException e) {
            return true;
        } finally {
            scanFile.close();
        }
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
                    try{
                        EnergyBar eBar=(EnergyBar)a;
                    }catch(ClassCastException e3){
                        try{
                            HPBar hpBar=(HPBar)a;
                        }catch(ClassCastException e4){ 
                            acList.add (new ActorContent (a, a.getX(), a.getY()));
                        }
                    }
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
