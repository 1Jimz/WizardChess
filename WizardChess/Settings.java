import greenfoot.*; 
import java.util.*;

/**
 * <html>
 * <body>
 * <p><strong>SettingsWorld</strong> class extends <em>World</em> and provides a settings interface for a casino game within Greenfoot.</p>
 * <p>This class allows players to adjust various game settings such as the number of horses in horse betting, the spawn rate of different gambler types, and the roulette style.</p>
 *
 * <h3>Class Fields:</h3>
 * <ul>
 *     <li><strong>Static fields</strong> - Variables storing game settings like spawn rates, starting money, and more.</li>
 *     <li><strong>Instance fields</strong> - UI elements like sliders, buttons, and text fields.</li>
 * </ul>
 *
 *  @author David Guo, Dorsa Rohani
 *  @version January 22nd, 2023
 */
public class Settings extends World {
    // store title screen
    private TitleScreen ts;
    
    // settings variables
    private static int casinoTarget; // casino Target
    private static int musicVolume; // music volume
    private static int sfxVolume; // VIP gambler starting money
    
    // the settings buttons
    private TextButton backButton;
    private TextButton saveButton;
    
    // text value
    private Text[] texts;
    
    // act count for lag
    private int actCount;

    public Settings(TitleScreen ts) {    
        super(1200, 740, 1);
        
        //setBackground("GameBg.png");
        this.ts = ts;
        
        // back button
        backButton = new TextButton("BACK", 60, 55, 255, 255, 255, 20, 147);
        saveButton = new TextButton("SAVE", 60, 55, 255, 255, 255, 20, 147);
        addObject(backButton, 600, 700);
        
        // initialize the sliders
        Slider sliders[] = {
            new Slider(1, 698, 831, 10000, 1000000, casinoTarget),
            new Slider(2, 698, 831, 0, 100, musicVolume),
            new Slider(3, 698, 831, 0, 100, sfxVolume)
        };
        // add the sliders / buttons to world
        addObject(sliders[0], calculateSliderXPosition(sliders[0], casinoTarget), 219);
        addObject(sliders[1], calculateSliderXPosition(sliders[1], musicVolume), 269);
        addObject(sliders[2], calculateSliderXPosition(sliders[2], sfxVolume), 312);
        addObject(saveButton, 900, 362);
        
        //initialize text
        texts = new Text[]{
            new Text(12, "Arial", String.valueOf(casinoTarget)),
            new Text(12, "Arial", String.valueOf(musicVolume)),
            new Text(12, "Arial", String.valueOf(sfxVolume))
        };
        //add Text
        addObject(texts[0], 877, 223);
        addObject(texts[1], 893, 269);
        addObject(texts[2], 893, 312);
        
        addObject(new Text(30, 8, "calibri", "MUSIC VOLUME"), 400, 275);
        addObject(new Text(30, "calibri", "SFX VOLUME"), 400, 325);
        
        // Initialize act count
        actCount = 0;
    }

    /**
     * Must be called in the first world in order to inialize starting settings.
     */
    public static void initialize(){
        // initial values
        casinoTarget = 10000; 
        musicVolume = 50;
        sfxVolume = 50;
    }
    
    // calculate the slider value using pixels/slider length
    private int calculateSliderXPosition(Slider slider, int value) {
        return slider.getSliderMinX() + (int)((value - slider.getRangeMin()) * (double)(slider.getSliderMaxX() - slider.getSliderMinX()) / (slider.getRangeMax() - slider.getRangeMin()));
    }
    /**
     * <p><strong>void act()</strong> - Handles interactions with the start TextButton and roulette style TextButtons. It transitions to the main game world and adjusts roulette styles based on player input.</p>
     */
    public void act(){
        if(Greenfoot.mouseClicked(backButton)){
            SoundManager.playSound("Clock Ticking");
            Greenfoot.setWorld(ts);
        } else if(Greenfoot.mouseClicked(saveButton)){
            SoundManager.playSound("Clock Ticking");
            try{
                    Game.saveProgress();
            }
            catch (java.io.IOException ioe){}
        }
        
        // To update settings
        if(actCount % 5 == 0){
            ts.getMusic().setVolume(Settings.getMusicVolume());
        }
        actCount++;
    }

    /**
     * <p><strong>void updateVar(int sliderID, int value)</strong> - Updates game settings based on slider input.</p>
    */
    public void updateVar(int sliderID, int value) {
        switch (sliderID) {
            case 1:texts[0].changeText(String.valueOf(casinoTarget=value));break;
            case 2:texts[1].changeText(String.valueOf(musicVolume = value));break;
            case 3:texts[2].changeText(String.valueOf(sfxVolume=value));break;
        }
    }
    
    /**
    **
     * <p><strong>public static int getCasinoTarget()</strong> - Retrieves the current casino target setting.</p>
     * <p>Returns the value of the static field <em>casinoTarget</em>.</p>
     * <p><strong>Return:</strong> int - The current casino target value.</p>
     */
    public static int getCasinoTarget(){
        return casinoTarget;
    }
    
    /**
     * <p><strong>public static int getMusicVolume()</strong> - Retrieves the current volume of music.</p>
     * <p>Returns the value of the static field <em>musicVolume</em>.</p>
     * <p><strong>Return:</strong> int - The current volume of music.</p>
     */
    public static int getMusicVolume(){
        return musicVolume;
    }

    /**
     * <p><strong>public static int getSFXVolume()</strong> - Retrieves the current sound effect volume.</p>
     * <p>Returns the value of the static field <em>sfxVolume</em>.</p>
     * <p><strong>Return:</strong> int - The current sound effect volume.</p>
     */
    public static int getSFXVolume(){
        return sfxVolume;
    }
    
    /**
     * <p><strong>void started()</strong> - Plays background music when the game starts.</p>
     */
    // play song when the game starts
    public void started() {
        ts.getMusic().playLoop();
    }
    
    /**
     * <p><strong>void stopped()</strong> - Pauses the background music when the game is stopped.</p>
     */
    // pause song if they stop the program
    public void stopped() {
        ts.getMusic().pause();
    } 
}
