/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.menu;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.GameManager;
import mygame.entity.item.FireLight;

/**
 *
 * @author Bawb
 */
public class MenuManager {
    
    private SimpleApplication   app;
    private GameManager         gm;
    private Node                menuScene;
    private boolean             enabled;
    private FireLight          fire;
    private Node                start, help, quit, helpScreen;
    private boolean             slidingIn;
    private boolean             slidingOut;
    private boolean             helpShown;
    private MenuControlListener mcl;
    private AudioNode           music;
    private FilterPostProcessor fog;
    private int                 selection;
    private boolean             hasStarted;
    
    public MenuManager(SimpleApplication app, GameManager gm) {
        this.app  = app;
        this.gm   = gm;
        menuScene = (Node) app.getAssetManager().loadModel("Scenes/Menu/Menu.j3o");
        fire      = new FireLight(app.getStateManager());
        start     = (Node) menuScene.getChild("Start");
        help      = (Node) menuScene.getChild("Help");
        quit      = (Node) menuScene.getChild("Quit");
        selection = 1;
        mcl       = new MenuControlListener(app.getStateManager(), this);
        gm.getUtilityManager().getAudioManager().loadSound("Menu", "Sounds/Menu.ogg", true);
        music     = gm.getUtilityManager().getAudioManager().getSound("Menu");
        fog       = app.getAssetManager().loadFilter("Effects/Fog.j3f");
        fog.getFilter(FogFilter.class).setFogDensity(0);
        fog.getFilter(FogFilter.class).setFogColor(ColorRGBA.Black);
        music.setVolume(0);
        fire.setPosition(menuScene.getChild("Fire").getWorldTranslation());
        fire.setIsLit(true);
        menuScene.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        inflateSelection();
        createHelp();
        hideHelp();
    }
    
    //Enables the Menu Places Camera and Initiates Scene
    public void showMenu() {
    
        System.out.println("Showing Menu");
        enabled    = true;
        slidingIn  = true;
        slidingOut = false;
        hasStarted = false;
        app.getFlyByCamera().setEnabled(false);
        fog.getFilter(FogFilter.class).setFogDensity(20);
        start.getChild(0).setLocalTranslation(0,5,0);
        help.getChild(0).setLocalTranslation(0,5,0);
        quit.getChild(0).setLocalTranslation(0,5,0);
        app.getRootNode().attachChild(menuScene);
        app.getRootNode().addLight(fire);
        app.getCamera().setLocation(new Vector3f(2.2538838f, 4.34696f, -3.2751317f));
        app.getCamera().lookAtDirection(new Vector3f(0.5634576f, -0.21033159f, -0.79892194f), new Vector3f(0,1,0));
        music.play();
        app.getViewPort().addProcessor(fog);
        //app.getViewPort().addProcessor(fire.getShadow());
        
    }
    
    //Disables the Manager and removes the scene
    public void hideMenu() {
    
        enabled = false;
        app.getRootNode().detachChild(menuScene);
        app.getRootNode().removeLight(fire); 
        music.stop();
        app.getViewPort().removeProcessor(fog);
        app.getViewPort().removeProcessor(fire.getShadow());
        
    }
    
    public void changeSelection(int change) {
        
        if(helpShown)
            return;
        
        selection += change;
        
        if (selection == 0) {
            selection = 3;
        }
        
        if (selection == 4) {
            selection = 1;
        }
        
        inflateSelection();
        
    }
    
    //Enlarges the Selected Options
    private void inflateSelection() {
    
        switch(selection) {
            
            case 1:
                start.setLocalScale(1.25f/3.4f);
                help.setLocalScale(1f/3.4f);
                quit.setLocalScale(1f/3.4f);
                break;
                
            case 2:
                start.setLocalScale(1f/3.4f);
                help.setLocalScale(1.25f/3.4f);
                quit.setLocalScale(1f/3.4f);
                break;
                
            case 3:
                start.setLocalScale(1f/3.4f);
                help.setLocalScale(1f/3.4f);
                quit.setLocalScale(1.25f/3.4f);
                break;
        
        }
        
    }
    
    //Called From Menu Control Listener. Takes Selected Node and Makes Choice
    public void makeSelection() {
        
        if (hasStarted)
            return;
        
        //If Help Menu is Shown Hide it
        if (helpShown) {
            hideHelp();
            return;
        }
            
        //Act on Selection
        switch(selection) {
            
            case 1:
                slidingIn  = false;
                slidingOut = true;
                break;
                
            case 2:
                showHelp();
                break;
                
            case 3:
                app.stop();
                break;
                
        }
    
    }
    
    //The Help Screen is A physical child node of the scene. Sets the field.
    private void createHelp() {
        helpScreen = (Node) menuScene.getChild("Controls");
    }
    
    //Shows the Help Screen By setting it to 0,0,0
    private void showHelp() {
        helpScreen.getChild(0).setLocalTranslation(0,0f,0);
        helpShown = true;
    }
    
    //Hides the Help Screen by moving it down 15 units
    private void hideHelp() {
        helpScreen.getChild(0).setLocalTranslation(0,-15f,0);
        helpShown = false;
    }
    
    //If start is selected start sliding the menu out
    private void startGame() {
        
        if (hasStarted)
            return;
        
        System.out.println("Starting New Game...");
        hasStarted = true;
        app.getStateManager().getState(GameManager.class).startNewGame();
        
    }
    
    //Enables the Menu Manager
    public void setEnabled(boolean newVal) {
        enabled = newVal;
    }
    
    //Returns the Enabled State of the Menu Manager
    public boolean isEnabled() {
        return enabled;
    }
    
    //Slides the Nodes of the Menu into place
    private void slideIn (Spatial spatial, float tpf) {
    
        //Slowly Move Nodes Down and Lessen Fog Density
        if (spatial.getLocalTranslation().y > 0) {
            spatial.move(0,-1.5f*tpf,0);
            fog.getFilter(FogFilter.class).setFogDensity(fog.getFilter(FogFilter.class).getFogDensity()-6f*tpf);
        }
        
        //Once Far Enough turn up music turn off fog and stop sliding
        else {
            slidingIn = false;
            spatial.setLocalTranslation(0,0,0);
            music.setVolume(.3f);
            fog.getFilter(FogFilter.class).setFogDensity(0);
        }
        
        //Slowly fades in music until volume of .3f
        if (music.getVolume() < .3f) {
            music.setVolume(music.getVolume()+.05f*tpf);
        }
        
    }
    
    //Move the Menu Nodes Upwards and off the screen
    private void slideOut (Spatial spatial, float tpf) {
    
        //Slow Move Nodes and Make Fog Darker
        if (spatial.getLocalTranslation().y < 6.5f) {
            spatial.move(0,1.5f*tpf,0);
            fog.getFilter(FogFilter.class).setFogDensity(fog.getFilter(FogFilter.class).getFogDensity()+6f*tpf);
        }
        
        //Once Moved Far Enough actually start the game
        else {
            slidingOut = false;
            startGame();
            spatial.setLocalTranslation(0,5,0);
            music.setVolume(0f);
            fog.getFilter(FogFilter.class).setFogDensity(2);
        }
        
        if (music.getVolume() > 0f) {
            float newVol = music.getVolume()-.05f*tpf;
            
            if (newVol > 0)
                music.setVolume(newVol);
            
        }
        
    }    
    
    //Updates the Menu Manager if Enabled
    public void update(float tpf) {
    
        if (enabled) {
            
            if (slidingIn) {
                slideIn(start.getChild(0), tpf);
                slideIn(help.getChild(0), tpf);
                slideIn(quit.getChild(0), tpf);
            }
            
            if (slidingOut) {
                slideOut(start.getChild(0), tpf);
                slideOut(help.getChild(0), tpf);
                slideOut(quit.getChild(0), tpf);
            }
                
            fire.update(tpf);
            
            mcl.update();
            
        }
    
    }
    
}
