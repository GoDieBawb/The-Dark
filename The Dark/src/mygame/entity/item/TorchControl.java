/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.shadow.PointLightShadowFilter;
import mygame.GameManager;
import mygame.entity.player.Player;
import mygame.util.AudioManager;

/**
 *
 * @author Bawb
 */
public class TorchControl extends AbstractControl {

    private AppStateManager stateManager;
    private Player          player;
    private Long            firstLit;
    private Torch           torch;
    private int             step;
    
    public TorchControl(AppStateManager stateManager, Torch torch) {
        this.stateManager = stateManager;
        player            = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
        this.torch        = torch;
    }
    
    private void checkLitTime() {
        
        if(!torch.isLit()) {
            return;
        }
        
        Long litTime      = System.currentTimeMillis()/1000 - firstLit/1000;
        AudioManager am   = stateManager.getState(GameManager.class).getUtilityManager().getAudioManager();
        Node      scene   = stateManager.getState(GameManager.class).getSceneManager().getScene();
        AudioNode steps   = am.getSound("Footsteps");
        AudioNode door    = am.getSound("Door"); 
        AudioNode scream  = am.getSound("Scream"); 
        
        if(scene.getName().equals("Town")) {
            
            steps.stop();
            
            if (litTime > 60 && step ==5) {
                scream.setVolume(100f);
                scream.play();
                player.die();
                steps.stop();
            }
            
            else if (litTime > 50 && step==4) {
                player.getHud().showAlert("", "It's Behind You");
                step = 5;
            }        
        
            else if (litTime > 40 && step==3) {
                player.getHud().showAlert("", "It Seeks You");
                step = 4;
            }        
        
            else if (litTime > 30 && step==2) {
                player.getHud().showAlert("", "It Comes Outside");
                door.playInstance();
                step = 3;
            }
        
            else if (litTime > 20 && step == 1) {
                player.getHud().showAlert("", "It Crawls");
                step = 2;
            }
        
            else if(litTime > 10 && step == 0) {
                player.getHud().showAlert("", "It awakens");
                step = 1;
            }
            
            return;
            
        }

        if (litTime > 60 && step ==5) {
            player.getHud().showAlert("", "It Kills");
            scream.setVolume(100f);
            scream.playInstance();
            steps.stop();
            player.die();
        }
        
        else if (litTime > 50 && step==4) {
            player.getHud().showAlert("", "It's Behind You");
            steps.play();
            steps.setVolume(4f);
            steps.setPitch(2);
            step = 5;
        }        
        
        else if (litTime > 40 && step==3) {
            player.getHud().showAlert("", "It Seeks You");
            steps.play();
            steps.setPitch(1.5f);
            steps.setVolume(2f);
            step = 4;
        }        
        
        else if (litTime > 30 && step==2) {
            player.getHud().showAlert("", "It Comes Inside");
            door.playInstance();
            step = 3;
        }
        
        else if (litTime > 20 && step == 1) {
            player.getHud().showAlert("", "It Crawls");
            step = 2;
        }
        
        else if(litTime > 10 && step == 0) {
            player.getHud().showAlert("", "It awakens");
            step = 1;
        }
        
    }
    
    public void setFirstLit() {
        firstLit = System.currentTimeMillis();
    }
    
    public void stopLight() {
        
        if(step != 0)
            player.getHud().showAlert("", "It sleeps");
        
        stateManager.getState(GameManager.class).getUtilityManager().getAudioManager().getSound("Footsteps").stop();
        step = 0;
        
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        checkLitTime();
        ((Torch) spatial).getTorchLight().update(tpf);
        ((Torch) spatial).getTorchLight().setPosition(((Torch) spatial).getFlame().getWorldTranslation());
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
