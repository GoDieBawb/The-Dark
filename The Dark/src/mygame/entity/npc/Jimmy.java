/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.npc;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.entity.Entity;
import mygame.entity.animation.Animated;

/**
 *
 * @author Bawb
 */
public class Jimmy extends Entity implements Animated {

    
    //Returns It's animation control
    @Override
    public AnimControl getAnimControl() {
        return findAnimControl(this);
    }

    @Override
    public void idle() {
        getAnimControl().createChannel().setAnim("idleA");
        getAnimControl().getChannel(0).setLoopMode(LoopMode.Loop);
    }
    
    //Die animation relative to the It model
    public void die() {
        getAnimControl().createChannel().setAnim("Die");
        getAnimControl().getChannel(0).setLoopMode(LoopMode.DontLoop);
    }
    
    public AnimControl findAnimControl(final Spatial parent) {
        
        final AnimControl animControl = parent.getControl(AnimControl.class);
        if (animControl != null) {
            return animControl;
        }

        if (parent instanceof Node) {
            for (final Spatial s : ((Node) parent).getChildren()) {
                final AnimControl animControl2 = findAnimControl(s);
                if (animControl2 != null) {
                    return animControl2;
                }
                
            }
            
        }

        return null;
    
    }    
    
}
