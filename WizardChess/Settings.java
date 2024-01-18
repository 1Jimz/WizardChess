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
 *  @version 1.1 01/13/2024
 */
public class Settings extends World {
    // store title screen
    private TitleScreen ts;
    
    // settings variables
    private static int casinoTarget; // casino Target
    private static int vipGamblerSpawnRate; // VIP gambler spawn rate
    private static int vipGamblerStartingMoney; // VIP gambler starting money
    private static int ordinaryStartingMoney; // VIP gambler starting money
    private static int cheaterGamblerSpawnRate; // cheater gambler spawn rate
    private static int slotsWinRate;
    private static int numberOfHorses; // # horses for horsebetting
    
    // the settings buttons
    private TextButton backButton;
    
    // text value
    private Text[] texts;

    public Settings(TitleScreen ts) {    
        super(1200, 740, 1);
        
        //setBackground("settingsworld.png");
        this.ts = ts;
        
        // back button
        backButton = new TextButton("BACK", 60, 55, 255, 255, 255, 20, 147);
        addObject(backButton, 600, 700);
        
        // initial values
        casinoTarget = 10000; 
        vipGamblerSpawnRate = 1;
        vipGamblerStartingMoney = 7500;
        cheaterGamblerSpawnRate = 1;
        ordinaryStartingMoney = 1;
        slotsWinRate = 1;
        numberOfHorses = 7;
        
        // initialize the sliders
        Slider sliders[] = {
            new Slider(1, 698, 831, 10000, 1000000, casinoTarget),
            new Slider(2, 698, 831, 1, 25, vipGamblerSpawnRate),
            new Slider(3, 698, 831, 5000, 10000, vipGamblerStartingMoney),
            new Slider(4, 698, 831, 1, 25, cheaterGamblerSpawnRate),
            new Slider(5, 698, 831, 1, 5000, ordinaryStartingMoney),
            new Slider(6, 698, 831, 1, 99, slotsWinRate),
            new Slider(7, 698, 831, 7, 20, numberOfHorses)
        };
        // add the sliders to world
        addObject(sliders[0], calculateSliderXPosition(sliders[0], casinoTarget), 219);
        addObject(sliders[1], calculateSliderXPosition(sliders[1], vipGamblerSpawnRate), 591);
        addObject(sliders[2], calculateSliderXPosition(sliders[2], vipGamblerStartingMoney), 549);
        addObject(sliders[3], calculateSliderXPosition(sliders[3], cheaterGamblerSpawnRate), 637);
        addObject(sliders[4], calculateSliderXPosition(sliders[4], ordinaryStartingMoney), 503);
        addObject(sliders[5], calculateSliderXPosition(sliders[5], slotsWinRate), 269);
        addObject(sliders[6], calculateSliderXPosition(sliders[6], numberOfHorses), 312);
        //initialize text
        texts = new Text[]{
            new Text(12, "Arial", String.valueOf(casinoTarget)),
            new Text(12, "Arial", String.valueOf(vipGamblerSpawnRate)),
            new Text(12, "Arial", String.valueOf(vipGamblerStartingMoney)),
            new Text(12, "Arial", String.valueOf(cheaterGamblerSpawnRate)),
            new Text(12, "Arial", String.valueOf(ordinaryStartingMoney)),
            new Text(12, "Arial", String.valueOf(slotsWinRate)),
            new Text(12, "Arial", String.valueOf(numberOfHorses))
        };
        //add Text
        addObject(texts[0], 877, 223);
        addObject(texts[1], 893, 591);
        addObject(texts[2], 885, 549);
        addObject(texts[3], 893, 637);
        addObject(texts[4], 893, 503);
        addObject(texts[5], 893, 269);
        addObject(texts[6], 893, 312);
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
            Greenfoot.setWorld(ts);
        }
    }

    /**
     * <p><strong>void updateVar(int sliderID, int value)</strong> - Updates game settings based on slider input.</p>
    */
    public void updateVar(int sliderID, int value) {
        switch (sliderID) {
            case 1:texts[0].changeText(String.valueOf(casinoTarget=value));break;
            case 2:texts[1].changeText(String.valueOf(vipGamblerSpawnRate = value));break;
            case 3:texts[2].changeText(String.valueOf(vipGamblerStartingMoney=value));break;
            case 4:texts[3].changeText(String.valueOf(cheaterGamblerSpawnRate=value));break;
            case 5: texts[4].changeText(String.valueOf(ordinaryStartingMoney=value));break;
            case 6: texts[5].changeText(String.valueOf(slotsWinRate=value));break;
            case 7: texts[6].changeText(String.valueOf(numberOfHorses=value));break;
        }
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