/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity;

import mygame.entity.animation.Living;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author Bawb
 */
public abstract class Humanoid extends Entity implements Living {
    
    private AnimControl     animControl;
    private AnimChannel     armChannel, legChannel;
    
    public void createAnimControl() {
        animControl = getModel().getChild("Person").getControl(AnimControl.class);
        armChannel  = animControl.createChannel();
        legChannel  = animControl.createChannel();
        armChannel.addFromRootBone("TopSpine");
        legChannel.addFromRootBone("BottomSpine");
        armChannel.setAnim("ArmIdle");
        legChannel.setAnim("LegsIdle");
    }
    
    @Override
    public AnimControl getAnimControl() {
        return animControl;
    }
    
    @Override
    public void setModel(String path, AppStateManager stateManager) {
        path = "Models/Person/Person.j3o";
        super.setModel(path, stateManager);
        getModel().setLocalScale(.2f);
        getModel().setLocalTranslation(0,.4f,0);
    }
    
    public void run() {
  
        if (!armChannel.getAnimationName().equals("ArmRun")) {
            armChannel.setAnim("ArmRun");
        }
      
        if (!legChannel.getAnimationName().equals("LegRun")) {
            legChannel.setAnim("LegRun");
        }    
      
    }
  
    public void idle() {
  
        if (!armChannel.getAnimationName().equals("ArmIdle")) {
            armChannel.setAnim("ArmIdle");
        }
      
        if (!legChannel.getAnimationName().equals("LegsIdle")) {
            legChannel.setAnim("LegsIdle");
        }
        
    }
    
    public void die() {
    
    }
    
}
