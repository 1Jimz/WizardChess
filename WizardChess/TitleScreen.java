import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.io.*;
/**
 * <html>
 * <body>
 * <p><strong>TitleScreen</strong> class extends <em>World</em> and represents the title screen of a game in Greenfoot.</p>
 * <p>This class manages the display of the title screen, including background images and buttons, and handles user interactions to start the game.</p>
 *
 * <h3>Class Fields:</h3>
 * <ul>
 *     <li><strong>bg</strong> - The background image for the title screen.</li>
 *     <li><strong>continueButton</strong> - A button that starts the game when clicked.</li>
 *     <li><strong>music</strong> - The background music for the title screen.</li>
 *     <li><strong>title</strong> - The title display button.</li>
 * </ul>
 *
 * @author David Guo
 * @version 1.1 01/13/2024
 */
public class TitleScreen extends World
{
    // World height and width constants
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 740;
    
    // Background image
    private GreenfootImage bg;
    
    // Buttons which highlight when hovered over
    private TextButton continueButton;
    private TextButton playButton;
    private TextButton settingsButton;
    
    // MP3 file for the title screen music
    private static GreenfootSound music;
    
    // Other stuff
    //private int actCount;
    private TextButton title;
    /**
     * Initializes the title screen with a specific background, sets up buttons, and configures background music.</p>
     */
    public TitleScreen()
    {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(WIDTH, HEIGHT, 1);
       
        // Set paint order so the layering is right
        setPaintOrder(Fader.class);
        
        // Initialize setting values & actCount
        Settings.initialize();
        //actCount = 0;
        
        // Add the fade effect
        addObject(new Fader(false, 5), WIDTH/2, HEIGHT/2);
        
        // Set bg to background image
        bg = new GreenfootImage ("finaltitlescreen.png");
        bg.scale(WIDTH, HEIGHT);
        setBackground(bg);
        
        // Create new buttons for the variables
        continueButton = new TextButton("CONTINUE", 35, 55, 255, 255, 255, 20, 147);
        playButton = new TextButton("NEW GAME", 35, 55, 255, 255, 255, 20, 147);
        settingsButton = new TextButton("SETTINGS", 35, 55, 255, 255, 255, 20, 147);

        //title
        //title = new TextButton("Title", 150, 255, 255, 255, 255, 20, 147);
        
        // Add buttons to the world
        int xOffset = 0;
        addObject(continueButton, WIDTH/2+xOffset, 625);
        addObject(playButton, WIDTH/2+xOffset, 665);
        addObject(settingsButton, WIDTH/2+xOffset, 705);
        
        // Assign the variable to the sound file name in folder & adjust volume
        music = new GreenfootSound("nemusplace.mp3");
        music.setVolume(Settings.getMusicVolume());
        
        // Add sound effects to SoundManager
        SoundManager.addSound(1, "Click", "wav");
        SoundManager.addSound(6, "Crunch", "wav");
        SoundManager.addSound(1, "High Whoosh", "wav", 80);
        SoundManager.addSound(1, "NoSpellEP", "wav");
    }

    /**
     * <p><strong>void act()</strong> - Checks for user interactions, particularly if the start button is clicked to begin the game.</p>
     */
    public void act(){
        try{
            checkClick();
        } catch(InterruptedException e){} catch(IOException e){};
        // this try catch statement is a result of using stockfish
    }
    
    // checks whether each of the buttons was clicked and spawns the related world
    private void checkClick() throws InterruptedException, java.io.IOException {
        // checks if the player has clicked play and puts them into the game if they did
        if(Greenfoot.mouseClicked(continueButton)){
            SoundManager.playSound("Click");
            // if player has a saved game
            if(saveFilePresent()) {
                startSavedGame();
            } else {
                System.out.println("Save file could not be located");
            }
        } else if(Greenfoot.mouseClicked(playButton)){
            SoundManager.playSound("Click");
            addObject(new Tutorial(), WIDTH/2, HEIGHT/2);
        } else if(Greenfoot.mouseClicked(settingsButton)){
            SoundManager.playSound("Click");
            Greenfoot.setWorld(new Settings(this));
        }
    }
    
    public void startGame() throws InterruptedException, java.io.IOException{
        music.stop();
        Greenfoot.setWorld(new Game(false));
    }
    
    public void startSavedGame() throws InterruptedException, java.io.IOException{
        music.stop();
        
        Greenfoot.setWorld(new Game(true));
    }
    
    private static boolean saveFilePresent() {
        try {
            Scanner scanFile = new Scanner(new File("saveFile.txt"));
            scanFile.nextLine();
            scanFile.nextLine();
            scanFile.close();
            return true;
        } catch(FileNotFoundException e) {
            return false;
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
