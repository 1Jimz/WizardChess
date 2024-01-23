import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
    // Act count
    private int actCount;
    // For stats
    private Text[] statNames;
    private Text[] stats;
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
        // Create new buttons for the variables
        restartButton = new TextButton("MAIN MENU", 60, 255, 255, 255, 234, 122, 67);
        // Add buttons to the world
        addObject(restartButton, 1200/2, 740/16*15);
        
        // Act count
        actCount = 0;
        
        // set background image and music
        if(gameOver){
            bg = new GreenfootImage ("gameoverimg.png");
            music = new GreenfootSound("greatfairyfountain.mp3");
            Text title = new Text(140, 6, "impact", "", 5); // 3rd param does not matter
            title.changeText("GAME OVER", Color.RED);
            addObject(title, getWidth()/2, getHeight()/4);
            showStats(false);
        } else {
            bg = new GreenfootImage ("endwizardblur.png");
            music = new GreenfootSound("greatfairyfountain.mp3");
            Text title = new Text(140, 5, "impact", ""); // 3rd param does not matter
            title.changeText("YOU WIN!", Color.GREEN);
            addObject(title, getWidth()/2, getHeight()/4);
            showStats(true);
        }
        setBackground(bg);
        music.setVolume(Settings.getMusicVolume());
        music.playLoop();
        
        // Stats
        statNames = new Text[]{
            //new Text(12, "Arial", String.valueOf(musicVolume)),
            //new Text(12, "Arial", String.valueOf(sfxVolume))
        };
        
        stats = new Text[]{
            //new Text(12, "Arial", String.valueOf(musicVolume)),
            //new Text(12, "Arial", String.valueOf(sfxVolume))
        };
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
        actCount++;
    }
    
    private void showStats(boolean win){
        
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
