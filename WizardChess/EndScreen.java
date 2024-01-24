import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * <html>
 * <body>
 * <p><strong>EndScreen</strong> class extends <em>World</em> and represents the ending screen of a game in Greenfoot.</p>
 * <p>This class manages the display of the end screen, which varies based on the game's outcome (casino getting wealthy or bankrupt), and handles user interactions to restart the game.</p>
 *
 * <h3>Class Fields:</h3>
 * <ul>
 *     <li><strong>bg</strong> - The background image for the end screen, varying based on game outcome.</li>
 *     <li><strong>music</strong> - The background music for the end screen, different for each outcome.</li>
 *     <li><strong>restartButton</strong> - A button that restarts the game when clicked.</li>
 * </ul>
 *
 * @author David Guo
 * @version January 22nd, 2023
 */
public class EndScreen extends World
{
    // Make greenfoot images for all possible endings
    private GreenfootImage bg;
    // Sound for the possible endings
    private GreenfootSound music;
    // Text for endings
    private Text title;
    // Play again button
    private TextButton restartButton;
    // Act count and game over storage
    private boolean gameOver;
    private int actCount;
    // For stats
    private ArrayList<Text> statNames;
    private ArrayList<Text> stats;
    private int statIndex;
    /**
     * <h3>Constructor:</h3>
     * <p>Initializes the end screen with different backgrounds and music based on the game outcome, and sets up a restart button.</p>
     * <ul>
     *     <li><strong>@param gameOver true if the player lost. false if they won
     * <ul>
     */
    public EndScreen(boolean gameOver)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 740, 1); 
        // Fader transition
        setPaintOrder(Fader.class, Text.class);
        addObject(new Fader(false, 2, Color.BLACK), getWidth()/2, getHeight()/2);
        // Create new buttons for the variables
        restartButton = new TextButton("MAIN MENU", 60, 255, 255, 255, 234, 122, 67);
        // Add buttons to the world
        addObject(restartButton, 1200/2, 740/16*15);
        
        // Act count & game over
        this.gameOver = gameOver;
        actCount = 0;
        
        // set background image and music
        if(gameOver){
            bg = new GreenfootImage ("losescreen.png");
            bg.scale(1200, 740);
            music = new GreenfootSound("greatfairyfountain.mp3");
            Text title = new Text(140, 6, "impact", "", 5); // 3rd param does not matter
        } else {
            bg = new GreenfootImage ("winscreen.png");
            bg.scale(1200, 740);
            music = new GreenfootSound("uncharted.mp3");
            Text title = new Text(140, 5, "impact", ""); // 3rd param does not matter
        }
        setBackground(bg);
        music.setVolume(Settings.getMusicVolume());
        music.playLoop();
        
        // Stats
        statNames = new ArrayList<Text>();
        statNames.add(new Text(52, "Arial", "HP Remaining:", Color.WHITE));
        statNames.add(new Text(52, "Arial", "EP Remaining:", Color.WHITE));
        statNames.add(new Text(52, "Arial", "# Moves Made:", Color.WHITE));
        statNames.add(new Text(52, "Arial", "# Cards Used:", Color.WHITE));
        
        stats = new ArrayList<Text>();
        stats.add(new Text(52, "Arial", String.valueOf(Math.max(0, HPBar.getHP())), Color.WHITE));
        stats.add(new Text(52, "Arial", String.valueOf(Math.max(0, EnergyBar.getE())), Color.WHITE));
        stats.add(new Text(52, "Arial", String.valueOf(HPBar.getHP()), Color.WHITE)); // temp
        stats.add(new Text(52, "Arial", String.valueOf(EnergyBar.getE()), Color.WHITE)); //temp
        
        statIndex = 0;
    }
    
    /**
     * <h3>void act()</h3>
     * <p>Handles user interactions on the end screen, specifically checks if the restart button is clicked and restarts the game if so.</p>
     */
    public void act(){
        // Checks if the player has clicked restart and puts them into the game if they did
        if(Greenfoot.mouseClicked(restartButton)){
            music.stop(); // stops the end screen music
            TitleScreen ts = new TitleScreen(); // create the title screen
            ts.started(); // start the title screen music
            Greenfoot.setWorld(ts); // set world to title screen
        }
        showStats();
        actCount++;
    }
    
    private void showStats(){
        int iStatDelay = 80;
        int statDelay = 60;
        int xPos = 0;
        if(gameOver)xPos = 300;
        else xPos = 750;
        if(actCount > iStatDelay && actCount % statDelay == 0){
            GreenfootImage img = new GreenfootImage(600, 400);
            img.setColor(Color.WHITE);
            img.fill();
            //addObject(new ImageButton(img), xPos+100, 400);
            if(statIndex < stats.size()){
                //Text temp = statNames.get(statIndex).changeText();
                addObject(statNames.get(statIndex), xPos, statIndex*80+300);
                addObject(stats.get(statIndex), xPos+420, statIndex*80+300);
                statIndex++;
            }
        }
    }
    
    /**
     * <h3>void started()</h3>
     * <p>Plays background music in a loop when the end screen starts.</p>
     */
    // Play song when the game starts
    public void started() {
        music.playLoop();
    }
    
    /**
     * <h3>void stopped()</h3>
     * <p>Pauses the background music when the end screen is stopped.</p>
     */
    // Pause song if they stop the program
    public void stopped() {
        music.pause();
    }
}
