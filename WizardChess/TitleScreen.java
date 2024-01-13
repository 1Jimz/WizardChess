import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * <html>
 * <body>
 * <p><strong>TitleScreen</strong> class extends <em>World</em> and represents the title screen of a game in Greenfoot.</p>
 * <p>This class manages the display of the title screen, including background images and buttons, and handles user interactions to start the game.</p>
 *
 * <h3>Class Fields:</h3>
 * <ul>
 *     <li><strong>bg</strong> - The background image for the title screen.</li>
 *     <li><strong>startButton</strong> - A button that starts the game when clicked.</li>
 *     <li><strong>music</strong> - The background music for the title screen.</li>
 *     <li><strong>title</strong> - The title display button.</li>
 * </ul>
 *
 * @author David Guo, Dorsa Rohani
 * @version 1.1 01/13/2024
 */
public class TitleScreen extends World
{
    // Background image
    private GreenfootImage bg;
    
    // Buttons which highlight when hovered over
    private TextButton startButton;
    private TextButton controlsButton;
    private TextButton settingsButton;
    private TextButton quitButton;
    
    // MP3 file for the title screen music
    private static GreenfootSound music;
    
    private TextButton title;
    /**
     * <h3>Constructor:</h3>
     * <p>Initializes the title screen with a specific background, sets up buttons, and configures background music.</p>
     */
    public TitleScreen()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 740, 1);
        
        // Set bg to background image
        //bg = new GreenfootImage ("");
        //setBackground(bg);
        
        // Create new buttons for the variables
        startButton = new TextButton("PLAY", 110, 55, 255, 255, 255, 20, 147);
        controlsButton = new TextButton("CONTROLS", 50, 55, 255, 255, 255, 20, 147);
        settingsButton = new TextButton("SETTINGS", 50, 55, 255, 255, 255, 20, 147);
        quitButton = new TextButton("QUIT", 50, 55, 255, 255, 255, 20, 147);

        //title
        //title = new TextButton("Title", 150, 255, 255, 255, 255, 20, 147);
        
        // Add buttons to the world
        
        addObject(startButton, 600, 500);
        addObject(controlsButton, 600, 570);
        addObject(settingsButton, 600, 640);
        addObject(quitButton, 600, 710);
        //addObject(title, 0, 0);
        
        // Assign the variable to the sound file name in folder & adjust volume
        music = new GreenfootSound("nemusplace.mp3");
        music.setVolume(50);
        
        // Add the title of the game
        //addObject(new TitlePic("TitlePic.png"), getWidth()/2, getHeight()/4);
    }

    /**
     * <p><strong>void act()</strong> - Checks for user interactions, particularly if the start button is clicked to begin the game.</p>
     */
    public void act(){
        try{
            checkClick();
        } catch(InterruptedException e){} catch(java.io.IOException e){};
        // this try catch statement is a result of using stockfish
    }
    
    // checks whether each of the buttons was clicked and spawns the related world
    private void checkClick() throws InterruptedException, java.io.IOException {
        // checks if the player has clicked play and puts them into the game if they did
        if(Greenfoot.mouseClicked(startButton)){
            music.stop();
            Greenfoot.setWorld(new Game());
        } else if(Greenfoot.mouseClicked(controlsButton)){
            music.stop();
            //Greenfoot.setWorld(new Game());
        } else if(Greenfoot.mouseClicked(settingsButton)){
            music.stop();
            Greenfoot.setWorld(new Settings());
        } else if(Greenfoot.mouseClicked(quitButton)){
            music.stop();
            //Greenfoot.setWorld(new ());
        }
    }
    
    /**
     * <p><strong>void started()</strong> - Plays the background music in a loop when the game starts.</p>
     */
    // Play song when the game starts
    public void started() {
        music.playLoop();
    }
    
    /**
     * <p><strong>void stopped()</strong> - Pauses the background music when the game is stopped.</p>
     */
    // Pause song if they stop the program
    public void stopped() {
        music.pause();
    }
    
    /**
     * <p><strong>static GreenfootSound getMusic()</strong> - Provides access to the background music.</p>
     * <ul>
     *     <li><strong>Return:</strong> GreenfootSound - The background music for the title screen.</li>
     * </ul>
     */
    // Getter method for the music
    public static GreenfootSound getMusic(){
        return music;
    }
}
