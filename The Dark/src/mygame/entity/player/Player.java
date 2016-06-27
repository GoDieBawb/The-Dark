/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import mygame.control.ChaseControl;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import java.util.HashMap;
import mygame.GameManager;
import mygame.entity.Entity;
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
    private ControlListener     controlListener;
    private AppStateManager     stateManager;
    private boolean             isDead;
    private int                 maxHealth;
    private int                 currentHealth;
    private Hud                 hud;
    private boolean             hasChecked;
    private Item              leftEquip;
    private Item              rightEquip;
    private BetterCharacterControl phys;
    private HashMap<Object, Object>  inventory;
    private boolean hasLeft;
    private boolean hasRight;
    
    //Construct the player
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
    
    //Create the Character Control
    @Override
    public void createPhys() {
        phys = new BetterCharacterControl(.35f, 1.1f, 100);
        attachChild(getModel());
        addControl(phys);
    } 
    
    //Returns the Character Control
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
    
    public HashMap<Object, Object> getInventory() {
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
    
    public void setLeftEquip(Item item) {
        leftEquip = item;
        hasLeft   = true;
        attachChild(item);
        item.setLocalTranslation(.25f,.6f,.1f);
    }
    
    public Item getLeftEquip() {
        return leftEquip;
    }
    
    public boolean hasLeft () {
        return hasLeft;
    }
    
    public void setRightEquip(Item item) {
        rightEquip = item;
        hasRight   = true;
        attachChild(item);
        item.setLocalTranslation(-.25f,.6f,.25f);
    }
    
    public Item getRightEquip() {
        return rightEquip;
    }
    
    public boolean hasRight() {
        return hasRight;
    }
    
    public void restartGame() {
        GameManager gm = stateManager.getState(GameManager.class);
        gm.endGame();
        isDead = false;
    }
    
    //When the player dies clear the root node remove the cross hair and reset
    @Override
    public void die() {
        
        super.die();
        ((SimpleApplication) stateManager.getApplication()).getRootNode().detachAllChildren();
        hud.detachCrossHair();
        stateManager.getState(GameManager.class).getSceneManager().removeScene();
        inventory = new HashMap();
        detachAllChildren();
        isDead = true;
        
    }
    
}
