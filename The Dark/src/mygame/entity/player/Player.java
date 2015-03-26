/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import mygame.control.ChaseControl;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import mygame.entity.Humanoid;
import mygame.entity.PhysicalEntity;

/**
 *
 * @author Bawb
 */
public class Player extends Humanoid implements PhysicalEntity {
    
    private float           speedMult;
    private float           strafeMult;
    private ChaseControl    chaseControl;
    private BetterCharacterControl phys;
    private AppStateManager stateManager;
    
    public Player(AppStateManager stateManager) {
        this.stateManager = stateManager;
        setModel(null, stateManager);
        createAnimControl();
        setSpeedMult(2f);
        setStrafeMult(.5f);
        setName("Player");
    }
    
    @Override
    public void createPhys() {
        phys = new BetterCharacterControl(.3f, 1.1f, 100);
        addControl(phys);
    }
    
    public BetterCharacterControl getPhys() {
        return phys;
    }
    
    public void setSpeedMult(float mult) {
        speedMult = mult;
    }
    
    public void setStrafeMult(float mult) {
        strafeMult = mult;
    }
    
    public float getSpeedMult() {
        return speedMult;
    }
    
    public float getStrafeMult() {
        return strafeMult;
    }
    
    public void createChaseControl() {
        chaseControl = new ChaseControl(stateManager, this);
    }    
    
    public ChaseControl getChaseControl() {
        return chaseControl;
    }
    
}
