/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import java.util.HashMap;
import mygame.GameManager;
import mygame.control.CameraManager;
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
    private CameraManager       cameraManager;
    private ControlListener     controlListener;
    private AppStateManager     stateManager;
    private boolean             isDead;
    private int                 maxHealth;
    private int                 currentHealth;
    private Hud                 hud;
    private boolean             hasChecked;
    private Item                leftEquip;
    private Item                rightEquip;
    private BetterCharacterControl   phys;
    private HashMap<Object, Object>  inventory;
    private boolean hasLeft;
    private boolean hasRight;
    
    //Construct the player
    public Player(AppStateManager stateManager) {
        this.stateManager = stateManager;
        setModel(null, stateManager);
        createAnimControl();
        createHud();
        speedMult  = 2f;
        strafeMult = .5f;
        name       = "Player";
        createInventory();
        createControlListener();
    }
    
    //Create the Character Control
    @Override
    public void createPhys() {
        phys = new BetterCharacterControl(.35f, 1.1f, 100);
        detachChild(getModel());
        addControl(phys);
    } 
    
    //Returns the Character Control
    @Override
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
    
    @Override
    public void setMaxHealth(int newVal) {
        maxHealth = newVal;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void setHealth(int newVal) {
        currentHealth = newVal;
    }

    @Override
    public int getHealth() {
        return currentHealth;
    }

    @Override
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
    
    public void createCameraManager() {
        cameraManager = new CameraManager( (SimpleApplication) stateManager.getApplication(), this);
    }
    
    public CameraManager getCameraManager() {
        return cameraManager;
    }
    
    public void setLeftEquip(Item item) {
        leftEquip = item;
        hasLeft   = true;
        cameraManager.getCameraNode().attachChild(item);
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
        cameraManager.getCameraNode().attachChild(item);
    }
    
    public Item getRightEquip() {
        return rightEquip;
    }
    
    public boolean hasRight() {
        return hasRight;
    }
    
    public void restartGame() {
        System.out.println("Restarting Game");
        cameraManager.getCameraNode().detachAllChildren();
        hud.getInfoText().hide();
        GameManager gm = stateManager.getState(GameManager.class);
        gm.endGame();
        isDead = false;
    }
    
    //When the player dies clear the root node remove the cross hair and reset
    @Override
    public void die() {
        
        if (isDead)
            return;
        
        super.die();
        System.out.println("Died");
        ((SimpleApplication) stateManager.getApplication()).getRootNode().detachAllChildren();
        stateManager.getState(GameManager.class).getSceneManager().removeScene();
        cameraManager.getCameraNode().detachAllChildren();
        hud.detachCrossHair();
        detachAllChildren();
        inventory  = new HashMap();
        rightEquip = null;
        leftEquip  = null;
        hasLeft    = false;
        hasRight   = false;
        isDead     = true;
        
    }
    
}
