import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.io.*;
/**
 * 
 * Welcome to WizardChess!
 * 
 * We hope you enjoy our turn-based action strategy game inspired by Chess and Shotgun King.
 * Here you play as a wizard fighting against an entire army with only a few spells in your
 * arsenal. Will you survive their onslaught and attain victory? Or will you perish as you
 * slowly get battered until you run out of HP?
 * 
 * Program features include:
 * => Screens (Different Worlds)
 *   -> Title Screen [Continue based on your last saved game, start a new game, or go to settings]
 *   -> Settings [Refresh your memory on the controls, change music volume, save your game, or restart]
 *   -> End Screen(Game Over) [If you perish, a quick summary of your last known statistics will show and you will have the option to return to the main menu]
 *   -> End Sreeen(You win) [If you sucessfully fend off the wave of enemies, you will be shown your stats and an option to return to the main menu]
 *   -> Game [Where the main game takes place]
 *   
 * => Features
 *   -> Tutorial [When you start a new game, a quick tutorial will help the player familiarize themselves with their goal and controls]
 *   -> Spells [Have a variety of different spells with different AOE effects]
 *   -> Effects [Different spells cause your enemies to suffer in a variety of ways!]
 *   -> Saving [Go to settings in the top right corner in order to save or restart your game]
 *   -> Continuing [In the title screen, clicking continue will allow you to pick up where you left off]
 *   -> Moving & Turns [WASD to move your wizard and ENTER to end your turn in order to recover EP]
 *   -> Animations [The wizard and pieces jump swiftly and elegantly across the board]
 *   -> Stockfish [By using Stockfish, the enemy plays... like stockfish]
 *   -> Music [With music playing in every world, go to the settings in the top left of Game in order to change the volume]
 *    -> Card Throw [The card has some entertaining physics once discard with G]
 * 
 * => Misc.
 *   -> TextButtons and ImageButtons [Equipped with a click sound effect and a highlight once hovered over]
 *   -> Faders [For dramatic effect]
 *   -> Sliders [To change your music volume, go to settings and adjust the king slider]
 *   
 * Known Bugs:
 * => N/A
 * 
 * Credit:
 * => Code
 *   -> Button and TextSizeFinder classes [Alex Li]
 *   
 * => Visuals
 *   -> Title Screen BG from Reddit u/gazozkapagii: [https://www.reddit.com/r/midjourney/comments/10n3tn9/two_gods_are_playing_chess/]
 *   -> End Screen Win BG
 *   -> End Screen Loss BG
 *   -> Card Font [OmegaPC777, https://www.dafont.com/pixeled.fond]
 *   -> Slider [David Guo]
 *   -> Explosion GIF from GIPHY: [https://giphy.com/gifs/26BRx71hqRexBe7Wo]
 *   -> Portal GIF from Tenor: [https://tenor.com/view/wave-hello-hi-greeting-princess-gif-16926051]
 *   -> Sword Slashing GIF from RealtimeVFX: [https://realtimevfx.com/uploads/default/original/2X/b/b8543008db2b22c1cedee82ed0bcfc37993a23bf.gif]
 *   -> Explosion Card from Pixel Art Maker: [http://pixelartmaker.com/art/695c3a296d3fc8c]
 *   -> Water bubbles from Pixel Art Maker: [http://pixelartmaker.com/art/81e6a4cd95fa0fa]
 *   -> Water bubbles Preview from Dreamstime: [https://thumbs.dreamstime.com/b/pixel-bubble-ball-vector-illustration-pixel-art-pixel-bubble-ball-vector-illustration-pixel-art-221785652.jpg]
 *   -> Explosion Preview JPG from Vecteezy: [https://static.vecteezy.com/system/resources/previews/020/577/469/original/dynamite-bomb-in-pixel-art-style-vector.jpg]
 *   
 * => Music & SFX
 *   -> Background Title Music: on Spotify: In Love With a Ghost [https://open.spotify.com/track/6Lr6YaV8KW41iD53PgjPr5?si=78b6368444664b33]
 *   -> Background Game Music 
 *   -> Win Screen Music [Uncharted - Drake's Fortune: https://open.spotify.com/track/53Lp7OESwvZmD9D4b4fMG6?si=edf971cb78834640] 
 *   -> Loss Screen Music [The Great Fairy Fountain from The Legend of Zelda: https://open.spotify.com/track/0DrrH6VEMbyjccWKAJKjIP?si=a8659cabbc99435e]
 *   -> All Sound Effects [Scratch Sound Library]
 *
 * 200+ combined hours of game development/design experience!
 * Hope you enjoy our game.
 */

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
        } catch(InterruptedException e){} catch(java.io.IOException e){};
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
            } else { // if no save file, new game
                addObject(new Tutorial(), WIDTH/2, HEIGHT/2);
            }
            
        } else if(Greenfoot.mouseClicked(playButton)){
            SoundManager.playSound("Click");
            addObject(new Tutorial(), WIDTH/2, HEIGHT/2);
        } else if(Greenfoot.mouseClicked(settingsButton)){
            SoundManager.playSound("Click");
            Greenfoot.setWorld(new Settings(this));
        }
    }
    
    public void startGame(boolean savedGame) throws InterruptedException, java.io.IOException{
        music.stop();
        Greenfoot.setWorld(new Game(savedGame));
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
