/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.monster;

import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.scene.Spatial;
import mygame.GameManager;
import mygame.entity.Humanoid;
import mygame.entity.PhysicalEntity;
import mygame.entity.ai.Finder;
import mygame.entity.ai.FinderControl;
import mygame.entity.player.Player;

/**
 *
 * @author Bawb
 */
public class Zombie extends Humanoid implements PhysicalEntity, Finder, Monster {

    private BetterCharacterControl phys;
    private FinderControl          fc;
    private AppStateManager        stateManager;
    private int                    moveSpeed;
    
    public Zombie(AppStateManager stateManager) {
        this.stateManager = stateManager;
        setModel(null, stateManager);
        createAnimControl();
        getModel().setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Zombie.j3m"));
    }
    
    @Override
    public void createPhys() {
        phys = new BetterCharacterControl(.35f, 1.1f, 150);
        addControl(phys);
    }

    @Override
    public BetterCharacterControl getPhys() {
        return phys;
    }

    @Override
    public void createFinderControl(AppStateManager stateManager) {
        
        fc = new FinderControl(stateManager, this) {
        
            @Override
            public void findTarget(Spatial target) {
                super.findTarget(target);
                run();
            }
            
            @Override
            public void stopFinding() {
                super.stopFinding();
                idle();
            }
        
        };
        
        addControl(fc);
        
    }

    @Override
    public FinderControl getFinderControl() {
        return fc;
    }
    
    @Override
    public void attack() {
    
    }

    @Override
    public void act() {
        
        Player player = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
        float  dist   = player.getWorldTranslation().distance(getWorldTranslation());
        
        if (getFinderControl().atGoal()) {
            attack();
        }
        
        else if (dist < 5 && !getFinderControl().isFinding()) {
            getFinderControl().findTarget(player);
        }
        
        else if (dist > 10) {
            getFinderControl().stopFinding();
        }
        
    }

    public void setMoveSpeed() {
        moveSpeed = 5;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }
    
}
