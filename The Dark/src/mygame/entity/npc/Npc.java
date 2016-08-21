/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.npc;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.entity.Entity;
import mygame.entity.animation.Animated;

/**
 *
 * @author root
 */
public class Npc extends Entity  implements Animated {
    
    private AnimChannel animChannel;
    
    @Override
    public void idle() {
        
        if (animChannel == null)
            animChannel = getAnimControl().createChannel();        
        
        animChannel.setAnim((String) getScript().getSymTab().get("idleName"));
        
    }
    
    @Override
    public void setAnimation(String animName) {
        
        if (animChannel == null)
            animChannel = getAnimControl().createChannel();
        
        animChannel.setAnim(animName);
        animChannel.setLoopMode(LoopMode.Loop);
        
    }    
    
    @Override
    public void setAnimation(String animName, boolean loop) {
        
        if (animChannel == null)
            animChannel = getAnimControl().createChannel();
        
        animChannel.setAnim(animName);
        
        if (loop)
            animChannel.setLoopMode(LoopMode.Loop);
        else
            animChannel.setLoopMode(LoopMode.DontLoop);
        
    }
    
    @Override
    public AnimControl getAnimControl() {
        return findAnimControl(this);
    }
    
    private AnimControl findAnimControl(final Spatial parent) {
        
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
