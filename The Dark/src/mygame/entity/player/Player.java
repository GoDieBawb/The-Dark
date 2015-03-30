/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import mygame.control.ChaseControl;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import java.util.ArrayList;
import mygame.GameManager;
import mygame.entity.Humanoid;
import mygame.entity.Vulnerable;
import mygame.entity.PhysicalEntity;

/**
 *
 * @author Bawb
 */
public class Player extends Humanoid implements PhysicalEntity, Vulnerable {
    
    private float               speedMult;
    private float               strafeMult;
    private ChaseControl        chaseControl;
    private ControlListener     controlListener;
    private AppStateManager     stateManager;
    private boolean             isDead;
    private int                 maxHealth;
    private int                 currentHealth;
    private ArrayList<String>   inventory;
    private Hud                 hud;
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
        createControlListener();
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
        inventory = new ArrayList();
    }
    
    public ArrayList<String> getInventory() {
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
    
    private void createControlListener() {
        controlListener = new ControlListener(stateManager, this);
    }
    
    public ControlListener getControlListener() {
        return controlListener;
    }
    
    public void restartGame() {
        GameManager gm = stateManager.getState(GameManager.class);
        gm.endGame();
        isDead = false;
    }
    
    @Override
    public void die() {
        super.die();
        ((SimpleApplication) stateManager.getApplication()).getRootNode().detachAllChildren();
        stateManager.getState(GameManager.class).getSceneManager().removeScene();
        inventory = new ArrayList();
        detachAllChildren();
        isDead = true;
    }
    
}
