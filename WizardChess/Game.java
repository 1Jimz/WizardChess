import greenfoot.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
/**
 Controls:
 WASD wizard movement(costs EP)
 G dispose card(costs EP)
 Click on card to use(cannot cancel when clicked)
 Enter to end turn
 
 Not bug list:
 when card is in process of being turned into a spell and mouse is off screen the process freezes, this is intentional.
 when it is not wizard's turn and spell can continue to be active.//maybe make it so have to use spell before next round
 * 
 * Main world for the game that is started after the user finishes the title screen
 * 
 * @author Jimmy Zhu, Mekaeel Malik, David Guo, Dorsa Roha
 * @version January 21st, 2023
 */
public class Game extends World
{
    public final static int hPush=550,vPush=90;  // Initial positions for the game grid
    private static boolean throwingCard=false, pickCard=false,leftBorder,spellActivated=false;  // Flags for various game states
    private static int wave=1,throwX, throwY, throwActive, startX, startY;  // Variables related to card throwing and waves
    private static Wizard wizard;  // Reference to the Wizard object
    private HPBar hpBar;  // Health bar for the Wizard
    private EnergyBar energyBar;  // Energy bar for the Wizard
    private static int level;  // Current level of the game
    private static Text waveNumber;  // Text displaying the current wave number
    private static String[] levelFens;  // Array storing FEN strings for each level
    private static boolean canNewWave,kingDied;  // Flags for controlling wave progression and king status
    private static ImageButton settingsButton;
    public Game() throws IOException, InterruptedException {    
        super(1200, 740, 1, false);  // Initializing the game world with specific dimensions
        System.out.println("_____________________________________________________________");  // Displaying a separator line
        // Initializing various flags and variables
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
        // Creating the game grid with Tile objects
        for(int i = 0; i<8; i++)
            for(int j = 0; j<8; j++)
                addObject(new Tile(i,j),hPush+j*80,vPush+i*80);
        
        energyBar = new EnergyBar(100);
        addObject(energyBar, 279, 270);
        hpBar=new HPBar(100);
        addObject(hpBar, 279, 210);  // Adding health and energy bars to the game world
        
        wizard = new Wizard();  // Initializing the Wizard object
        wizard.setEnergyBar(energyBar);
        wizard.setHPBar(hpBar);
        addObject(wizard,hPush+4*80,vPush+7*80-25);  // Adding the Wizard to the game world
        
        waveNumber = new Text(30,"Arial",Integer.toString(level));
        addObject(waveNumber,980,731);  // Displaying the current wave number
        
        levelFens = new String[8];  // Initializing an array to store FEN strings for each level
        // Assigning FEN strings for each level
        levelFens[0] = "2bk1b2/4pppp/8/6K1/8/8/8/8 b - - 0 1";
        levelFens[1] = "rnbkq3/ppppp3/8/8/8/8/7K/8 b - - 0 1";
        levelFens[2] = "r7/3n1k2/4b1q1/2p5/1p6/8/3K4/8 b - - 0 1";
        levelFens[3] = "2rq1rk1/5ppp/8/8/8/8/1K2p3/8 b - - 0 1";
        levelFens[4] = "6k1/1bp5/p1n4q/8/6p1/7p/4K3/r7 b - - 0 1";
        levelFens[5] = "q4r2/1p4kp/1p3bp1/5p2/6b1/3K4/8/8 b - - 0 1";
        levelFens[6] = "2b5/1p6/k1p5/1pbp4/r7/3K4/8/8 b - - 0 1";
        levelFens[7] = "q3k3/ppp1nppp/2n1p3/2bp4/6b1/3K4/8/8 b - - 0 1";
        
        // Add settings button
        
    }
    
    private static int moveNumber;

    /**
     * Increments the move number.
     */
    public static void nextMove() {
        moveNumber++;
    }
    
    /**
     * Returns the current move count.
     *
     * @return int Current move count
     */
    public static int moveCount() {
        return moveNumber;
    }
    
    /**
     * Checks if it's currently the Wizard's turn based on the move number.
     *
     * @return boolean True if it's the Wizard's turn, otherwise false
     */
    public static boolean wizardTurn() {
        if(moveNumber % 2 == 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Updates the Wizard's health on the health bar.
     *
     * @param newHP New health value for the Wizard
     */
    private void updateHP(int newHP) {
        hpBar.setHP(newHP);
    }
    
    /**
     * Handles the Wizard taking damage. The Wizard class handles its own HP reduction,
     * and this method updates the HP bar accordingly.
     *
     * @param dmg Damage inflicted on the Wizard
     */
    public void wizardTakesDamage(int dmg) {
        wizard.takeDmg(dmg);
        updateHP(wizard.getHP());
    }
    
    /**
     * Returns the current wave number.
     *
     * @return int Current wave number
     */
    public static int getWave() {
        return wave;
    }
    
    /**
     * Initiates the process of throwing a card.
     *
     * @param tX X-coordinate for the card throw
     * @param tY Y-coordinate for the card throw
     * @param tA Active state of the thrown card
     * @param lB Left border indicator for the card throw
     * @param sX Starting X-coordinate for the card throw
     * @param sY Starting Y-coordinate for the card throw
     */
    public static void throwCard(int tX, int tY, int tA, int lB, int sX, int sY) {
        throwX=tX;
        throwY=tY;
        throwActive=tA;
        leftBorder=lB!=0;
        startX=sX;
        startY=sY;
        throwingCard=true;
    }
    
    /**
     * Returns a reference to the Wizard object.
     *
     * @return Wizard Reference to the Wizard object
     */
    public static Wizard getWizard() {
        return wizard;
    }
    
    private boolean keyPressChecked = true;
    private static boolean enemyMoving = false;
    
    /**
     * Main act method containing game logic, including sorting actors for proper rendering,
     * handling card picking and throwing, checking for player input or enemy turn trigger,
     * handling the end of a wave and progression to the next level, and handling the end of the game.
     */
    public void act() {
        zSort((ArrayList<Actor>)(getObjects(Actor.class)),this);  // Sorting actors for proper rendering
        
        if(pickCard){
            addObject(new Hand(),-120,510);  // Adding the Hand object to the game world
            pickCard=false;
        }
        
        if(throwingCard){
            addObject(new Card(throwX,throwY,throwActive,leftBorder),startX,startY);  // Adding a Card object to the game world
            throwingCard=false;
        }
        
        // Checking for player input or enemy turn trigger
        if((wizardTurn()&&Greenfoot.isKeyDown("Enter"))||(!wizardTurn()&&BoardManager.getCountdown()<=0)) {
            if(keyPressChecked) {
                nextMove();
                enemyMoving = false;
                
                if(!wizardTurn()) {
                    System.out.println("SNAO"+" "+BoardManager.isWarned());
                    
                    if(BoardManager.isWarned()) {
                        BoardManager.spawnPieces();
                        BoardManager.unwarn();
                        nextMove();
                        canNewWave=true;
                    }
                    else {
                        if(!canNewWave) {
                            nextLevel();
                        }
                        else{
                            try{
                                try{
                                    if(!enemyMoving) {
                                        enemyMoving = true;
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
        
        // Handling the end of a wave and progression to the next level
        if(canNewWave&&kingDied) {
            BoardManager.resetTiles();
            BoardManager.wipe();
            for(Piece p: getObjects(Piece.class))p.kill();
            nextLevel();
            nextMove();
            canNewWave=false;
            kingDied=false;
        } 
        
        // Handling the end of the game
        if(level == 8){
            Greenfoot.setWorld(new EndScreen(false));  // Transitioning to the end screen
        }
    }
    /**
     * Initiates the card grabbing animation.
     */
    public static void grabCardAnimation() {
        pickCard = true;
    }
    
    /**
     * Signals that the enemy has finished moving.
     */
    public void enemyFinishedMoving() {
        enemyMoving = false;
    }
    
    /**
     * Activates the spell and highlights the Wizard's range.
     */
    public static void activateSpell() {
        // Wizard.highlightRange(200); // 200 is a temporary value
        spellActivated = true;
    }
    
    /**
     * Deactivates the spell and resets highlighted tiles on the board.
     */
    public static void deactivateSpell() {
        spellActivated = false;
        BoardManager.resetTiles();
    }
    
    /**
     * Checks if the spell is currently activated.
     *
     * @return boolean True if the spell is activated, otherwise false
     */
    public static boolean isSpellActivated() {
        return spellActivated;
    }
    
    private static Scanner readFile;
    
    /**
     * Advances the game to the next level, creates incoming pieces based on the FEN string,
     * warns the player, increments the move number, and updates the displayed wave number.
     */
    public static void nextLevel() {
        level++;
        BoardManager.createIncoming(levelFens[level - 1]);
        BoardManager.warn();
        nextMove();
        waveNumber.changeText(Integer.toString(level), Color.WHITE);
    }
    
    private static FileWriter out;
    private static PrintWriter output;
    
    /**
     * Saves the current game progress, including the level, current FEN string, and pieces' HP.
     *
     * @throws IOException
     */
    public static void saveProgress() throws IOException {
        try {
            out = new FileWriter("saveFile.txt", false);
            output = new PrintWriter(out);
            output.println(Integer.toString(level));
            output.println(BoardManager.currentFEN());
            output.println(BoardManager.getPiecesHP());
        } catch (IOException e) {
        } finally {
            out.close();
            output.close();
        }
    }
    
    /**
     * Signals that the king is dying, triggering the end-of-wave sequence.
     */
    public static void kingDying() {
        kingDied = true;
    }
    
    private static Scanner scanFile;
    
    /**
     * Loads the saved game progress, retrieving the level and FEN string to recreate the game state.
     *
     * @return boolean True if loading is successful, otherwise false
     */
    public static boolean loadProgress() {
        try {
            scanFile = new Scanner(new File("saveFile.txt"));
            level = Integer.parseInt(scanFile.nextLine());
            BoardManager.createIncoming(scanFile.nextLine());
            BoardManager.spawnPieces();
            BoardManager.setPiecesHP(scanFile.nextLine());
            scanFile.close();
            return false;
        } catch (IOException e) {
            return true;
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
    
    /**
     * <p><strong>void started()</strong> - Plays the background music in a loop when the game starts.</p>
     */
    // Play song when the game starts
    public void started() {
        //music.playLoop();
    }
    
    /**
     * <p><strong>void stopped()</strong> - Pauses the background music when the game is stopped.</p>
     */
    // Pause song if they stop the program
    public void stopped() {
        //music.pause();
    }
    
    /**
     * <p><strong>static GreenfootSound getMusic()</strong> - Provides access to the background music.</p>
     * <ul>
     *     <li><strong>Return:</strong> GreenfootSound - The background music for the title screen.</li>
     * </ul>
     */
    // Getter method for the music
    //public static GreenfootSound getMusic(){
        //return music;
    //}
}
