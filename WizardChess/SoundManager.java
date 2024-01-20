import greenfoot.*;
import java.util.*;
/**
 * <html>
 * <body>
 * <p><strong>SoundManager</strong> is a utility class for managing sound effects in Greenfoot applications.</p>
 * <p>It allows for playing, looping, and stopping sound effects. The class handles multiple instances of the same sound effect and controls their concurrent execution.</p>
 * 
 * <h3>Usage:</h3>
 * <p>SoundManager should be used to manage all sound effects within the application. Sounds should be added during the world's construction phase for optimal performance.</p>
 * 
 * <h3>Class Fields:</h3>
 * <ul>
 *     <li><strong>sounds</strong> - A map that stores queues of <code>GreenfootSound</code> objects for different sound names.</li>
 *     <li><strong>currentlyLooping</strong> - A map that tracks currently looping sounds.</li>
 * </ul>
 * @author Jimmy Zhu, David Guo
 * @version 1.1 01/19/2024
 */
public class SoundManager  
{
    private static Map<String,Queue<GreenfootSound>> sounds = new HashMap<String,Queue<GreenfootSound>>();
    private static Map<String,GreenfootSound> currentlyLooping = new HashMap<String,GreenfootSound>();
    /**
     * <p><strong>static void playSound(String name)</strong> - Plays a sound effect once. If the sound is looping, it stops the loop. <br>
     * <strong>@param name</strong> - The name of the sound to play.</p>
     */
    //Note: when a sound that is being looped is played with the playSound method, the sound will stop looping. This is intentional.
    public static void playSound(String name){
        sounds.get(name).peek().play();
        sounds.get(name).add(sounds.get(name).poll());
        stopLooped(name);
    }
    /**
     * <html>
     * <body>
     * <p><strong>playLooped</strong> - Plays a specified sound in a continuous loop.</p>
     * <p>This method retrieves the sound associated with the given name from the <code>sounds</code> map and starts playing it in a loop. If the sound is already playing, it will continue to loop. This method is particularly useful for background music or continuous sound effects in a Greenfoot application.</p>
    */
   //star_____________AWF_)IFW)_IWAF)_F)IAI)_FAWI)_FIAW)_FI)_AWI)_I*
    public static void playLooped(String name){
        currentlyLooping.put(name,sounds.get(name).peek());
        currentlyLooping.get(name).playLoop();
    }
    /**
     * <p><strong>static void stopLooped(String name)</strong> - Stops a currently looping sound. <br>
     * <strong>@param name</strong> - The name of the looping sound to stop.</p>
     */
    public static void stopLooped(String name){
        if(currentlyLooping.get(name)!=null)currentlyLooping.remove(name).stop();
    }
    /**
     * <p><strong>static void addSound(int maxSimultaneousActive, String name, String type)</strong> - Adds a sound effect to the sound manager. <br>
     * <strong>@param maxSimultaneousActive</strong> - The maximum number of simultaneous active instances of the sound.<br>
     * <strong>@param name</strong> - The name of the sound.<br>
     * <strong>@param type</strong> - The file type of the sound (e.g., "wav", "mp3").</p>
     */
    public static void addSound(int maxSimultaneousActive, String name, String type){
        sounds.put(name,createFilledQueue(maxSimultaneousActive,name+"."+type));
    }
    /**
     * <p><strong>static void addSound(int maxSimultaneousActive, String name, String type)</strong> - Adds a sound effect to the sound manager. <br>
     * <strong>@param maxSimultaneousActive</strong> - The maximum number of simultaneous active instances of the sound.<br>
     * <strong>@param name</strong> - The name of the sound.<br>
     * <strong>@param type</strong> - The file type of the sound (e.g., "wav", "mp3").<br>
     * <strong>@param type</strong> - The sound volume percentage (e.g., "wav", "mp3").</p>
     */
    public static void addSound(int maxSimultaneousActive, String name, String type, int vol){
        sounds.put(name,createFilledQueue(maxSimultaneousActive,name+"."+type,vol));
    }
    //adds n copies of a GreenFootSound denoted by name
    private static Queue<GreenfootSound> createFilledQueue(int n, String name){
        Queue<GreenfootSound> q = new LinkedList<GreenfootSound>();
        for(int i = 0; i<n; i++)q.add(new GreenfootSound(name));
        return q;
    }
    //adds n copies of a GreenFootSound denoted by name. This time vol is customizable
    private static Queue<GreenfootSound> createFilledQueue(int n, String name, int vol){
        Queue<GreenfootSound> q = new LinkedList<GreenfootSound>();
        for(int i = 0; i<n; i++){
            GreenfootSound gfs = new GreenfootSound(name);
            gfs.setVolume(vol);
            q.add(gfs);
        }
        return q;
    }
}