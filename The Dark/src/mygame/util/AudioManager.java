package mygame.util;

import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import java.util.HashMap;

/**
 *
 * @author Bawb
 */
public class AudioManager {
    
    //Create Hashmap Of Sounds Names and Audio Nodes
    private final HashMap         sounds;
    private final AppStateManager stateManager; 
    
    public AudioManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
        sounds = new HashMap();
    }
    
    //Load Sounds By Path and name it. This will be the name to retrieve from map
    public void loadSound(String soundName, String path, boolean loop) {
        AudioNode an = new AudioNode(stateManager.getApplication().getAssetManager(), path, false);
        an.setPositional(false);
        an.setLooping(loop);
        an.setVolume(.3f);
        sounds.put(soundName, an);
    }
    
    //Gets the Audio Node By the Sound Name
    public AudioNode getSound(String soundName) {
        return ((AudioNode)sounds.get(soundName));
    }
    
}

