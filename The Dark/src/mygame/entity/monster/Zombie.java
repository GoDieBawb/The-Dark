/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.monster;

import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.scene.Spatial;
import mygame.entity.Humanoid;
import mygame.entity.PhysicalEntity;
import mygame.entity.ai.Finder;
import mygame.entity.ai.FinderControl;

/**
 *
 * @author Bawb
 */
public class Zombie extends Humanoid implements PhysicalEntity, Finder {

    private BetterCharacterControl phys;
    private FinderControl          fc;
    
    public Zombie(AppStateManager stateManager) {
        setModel(null, stateManager);
        createAnimControl();
        getModel().setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Priest.j3m"));
    }
    
    @Override
    public void createPhys() {
        phys = new BetterCharacterControl(.3f, .7f, 150);
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
        
    }

    @Override
    public FinderControl getFinderControl() {
        return fc;
    }
    
}
