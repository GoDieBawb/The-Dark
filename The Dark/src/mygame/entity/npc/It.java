/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.npc;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import mygame.entity.Entity;
import mygame.entity.animation.Animated;

/**
 *
 * @author Bawb
 */
public class It extends Entity implements Animated {

    //Returns It's animation control
    public AnimControl getAnimControl() {
        return getModel().getChild("Model").getControl(AnimControl.class);
    }

    public void idle() {

    }
    
    //Die animation relative to the It model
    public void die() {
        getAnimControl().createChannel().setAnim("Die");
        getAnimControl().getChannel(0).setLoopMode(LoopMode.DontLoop);
    }
    
}
