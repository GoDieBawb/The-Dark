/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import mygame.GameManager;
import mygame.entity.player.Player;
import mygame.util.AudioManager;

/**
 *
 * @author Bawb
 */
public class TorchControl extends AbstractControl {

    private final AppStateManager stateManager;
    private final Player          player;
    private final Torch           torch;
    private       int             step;
    private       Long            firstLit;
    
    //Constructs the Torch Control
    public TorchControl(AppStateManager stateManager, Torch torch) {
        this.stateManager = stateManager;
        player            = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
        this.torch        =  (Torch) torch;
    }
    
    //Checks what time the torch was lit. This should probably be scripted
    private void checkLitTime() {
        
        //If the torch isnt lit dont do anything
        if(!torch.isLit()) {
            return;
        }
        
        //If the player is finished stop the monster finding 
        if (player.getFlagList().get("Finish") != null) {
            return;
        }
        
        Long         litTime = System.currentTimeMillis()/1000 - firstLit/1000;
        AudioManager am      = stateManager.getState(GameManager.class).getUtilityManager().getAudioManager();
        Node         scene   = stateManager.getState(GameManager.class).getSceneManager().getScene();
        AudioNode    steps   = am.getSound("Footsteps");
        AudioNode    door    = am.getSound("Door"); 
        AudioNode    scream  = am.getSound("Scream"); 
        
        if(scene.getName().equals("Town") || scene.getName().equals("Well")) {
            
            steps.stop();
            
            if (litTime > 60 && step ==5) {
                scream.setVolume(100f);
                scream.play();
                player.die();
                steps.stop();
            }
            
            else if (litTime > 50 && step==4) {
                player.getHud().addAlert("", "It's Behind You");
                step = 5;
            }        
        
            else if (litTime > 40 && step==3) {
                player.getHud().addAlert("", "It Seeks You");
                step = 4;
            }        
        
            else if (litTime > 30 && step==2) {
                player.getHud().addAlert("", "It Comes Outside");
                door.playInstance();
                step = 3;
            }
        
            else if (litTime > 20 && step == 1) {
                player.getHud().addAlert("", "It Crawls");
                step = 2;
            }
        
            else if(litTime > 10 && step == 0) {
                player.getHud().addAlert("", "It awakens");
                step = 1;
            }
            
            return;
            
        }

        //Kills the player if lit time is more than 60 seconds
        if (litTime > 60 && step ==5) {
            player.getHud().addAlert("", "It Kills");
            scream.setVolume(100f);
            scream.playInstance();
            steps.stop();
            player.die();
        }
        
        //50 seconds warns it's behind them
        else if (litTime > 50 && step==4) {
            player.getHud().addAlert("", "It's Behind You");
            steps.play();
            steps.setVolume(4f);
            steps.setPitch(2);
            step = 5;
        }        
        
        //Third warning after 40 seconds
        else if (litTime > 40 && step==3) {
            player.getHud().addAlert("", "It Seeks You");
            steps.play();
            steps.setPitch(1.5f);
            steps.setVolume(2f);
            step = 4;
        }        
        
        //Second warning after 30 seconds
        else if (litTime > 30 && step==2) {
            player.getHud().addAlert("", "It Comes Inside");
            door.playInstance();
            step = 3;
        }
        
        //First warning after 20 seconds
        else if (litTime > 20 && step == 1) {
            player.getHud().addAlert("", "It Crawls");
            step = 2;
        }
        
        //When the torch is first lit it wakes up
        else if(litTime > 10 && step == 0) {
            player.getHud().addAlert("", "It awakens");
            step = 1;
        }
        
    }
    
    //Sets the time the torch was lit
    public void setFirstLit() {
        firstLit = System.currentTimeMillis();
    }
    
    //Turns off the lights and stops the sound
    public void stopLight() {
        
        if(step != 0)
            player.getHud().addAlert("", "It sleeps");
        
        stateManager.getState(GameManager.class).getUtilityManager().getAudioManager().getSound("Footsteps").stop();
        step = 0;
        
    }
    
    //Update loop for the torch and torch control
    @Override
    protected void controlUpdate(float tpf) {
        
        //checkLitTime();
        Torch t = ((Torch) spatial);
        FireLight f = (FireLight) t.getLight();
        f.update(tpf);
        f.setPosition(((Torch) spatial).getFlame().getWorldTranslation());
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
