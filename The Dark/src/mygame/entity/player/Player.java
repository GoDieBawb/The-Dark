/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import mygame.control.ChaseControl;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import java.util.HashMap;
import mygame.entity.Humanoid;
import mygame.entity.Vulnerable;
import mygame.entity.PhysicalEntity;
import mygame.entity.item.Item;

/**
 *
 * @author Bawb
 */
public class Player extends Humanoid implements PhysicalEntity, Vulnerable {
    
    private float               speedMult;
    private float               strafeMult;
    private ChaseControl        chaseControl;
    private AppStateManager     stateManager;
    private boolean             isDead;
    private int                 maxHealth;
    private int                 currentHealth;
    private HashMap             inventory;
    private Hud                 hud;
    private Item                selectedItem;
    private boolean             hasChecked;
    private BetterCharacterControl phys;
    
    public Player(AppStateManager stateManager) {
        this.stateManager = stateManager;
        setModel(null, stateManager);
        createAnimControl();
        createHud();
        setSpeedMult(2f);
        setStrafeMult(.5f);
        setName("Player");
        createInventory();
    }
    
    @Override
    public void createPhys() {
        phys = new BetterCharacterControl(.35f, 1.1f, 100);
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

    public void setMaxHealth(int newVal) {
        maxHealth = newVal;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int newVal) {
        currentHealth = newVal;
    }

    public int getHealth() {
        return currentHealth;
    }

    public void endLife() {
        isDead = true;
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    private void createInventory() {
        inventory = new HashMap();
    }
    
    public HashMap getInventory() {
        return inventory;
    }
    
    public void dropItem() {
    }
    
    private void createHud() {
        hud = new Hud(stateManager);
    }
    
    public Hud getHud() {
        return hud;
    }
    
    public void setHasChecked(boolean newVal) {
        hasChecked = newVal;
    }
    
    public boolean hasChecked() {
        return hasChecked;
    }
    
}
