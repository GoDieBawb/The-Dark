/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;

/**
 *
 * @author Bawb
 */
public class InteractionManager implements ActionListener {
    
    private final InputManager      inputManager;
    private final SimpleApplication app;
    private boolean           up = false, down = false, left = false, right = false, click = false, interact = false, cursor = false;
    private boolean           up1 = false, down1 = false, left1 = false, right1 = false, rightClick = false, debugLight, inventory = false;
    private Vector2f          touchSpot;
    
    //Constructor
    public InteractionManager(SimpleApplication app) {
        this.app     = app;
        inputManager = app.getInputManager();
        inputManager.setCursorVisible(false);
        setUpKeys();
    }
    
    //Set up the Keys
    private void setUpKeys() {
        
        inputManager.addMapping("Up",         new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down",       new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left",       new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right",      new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Cursor",     new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Inventory",  new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("Interact",   new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Click",      new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("RightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("Up1",        new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down1",      new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left1",      new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right1",     new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("DebugLight", new KeyTrigger(KeyInput.KEY_L));
        
        inputManager.addListener(this, "Inventory");
        inputManager.addListener(this, "Up1");
        inputManager.addListener(this, "Down1");
        inputManager.addListener(this, "Left1");
        inputManager.addListener(this, "Right1");
        inputManager.addListener(this, "RightClick");
        inputManager.addListener(this, "DebugLight");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Cursor");
        inputManager.addListener(this, "Interact");
        inputManager.addListener(this, "Click");        
        
    }
    
    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        
        switch (binding) {
            case "Up":
                up = isPressed;
                break;
            case "Down":
                down = isPressed;
                break;
            case "Left":
                left = isPressed;
                break;
            case "Right":
                right = isPressed;
                break;
            case "Inventory":
                inventory = isPressed;
                break;
            case "Cursor":
                cursor = isPressed;
                break;
            case "Interact":
                interact = isPressed;
                break;
            case "Click":
                click = isPressed;
                break;
            case "Up1":
                up1 = isPressed;
                break;
            case "Down1":
                down1 = isPressed;
                break;
            case "Left1":
                left1 = isPressed;
                break;
            case "Right1":
                right1 = isPressed;
                break;
            case "RightClick":
                rightClick = isPressed;
                break;
            case "DebugLight":
                debugLight = isPressed;
                break;
            default:
                break;
        }
        
    }
    
    public void setTouchSpot(Vector2f newVal) {
        touchSpot = newVal;
    }
    
    public Vector2f getTouchSpot() {
        return touchSpot;
    }
    
    public void setClick(boolean newVal) {
        click = newVal;
    }
    
    public void setUp(boolean newVal) {
        up = newVal;
    }
    
    public void setDown(boolean newVal) {
        down = newVal;
    }
    
    public void setLeft(boolean newVal) {
        left = newVal;
    }
    
    public void setRight(boolean newVal) {
        right = newVal;
    }
    
    public void setUp1(boolean newVal) {
        up1 = newVal;
    }
    
    public void setDown1(boolean newVal) {
        down1 = newVal;
    }
    
    public void setLeft1(boolean newVal) {
        left1 = newVal;
    }
    
    public void setRight1(boolean newVal) {
        right1 = newVal;
    }    
    
    //Ability to Check Buttons Pressed This is For the Camera Manager
    public boolean getIsPressed(String triggerName) {
        
        switch (triggerName) {
            case "Left":
                return left;
            case "Right":
                return right;
            case "Up":
                return up;
            case "Down":
                return down;
            case "Click":
                return click;
            case "Interact":
                return interact;
            case "Inventory":
                return inventory;
            case "Cursor":
                return cursor;
            case "Left1":
                return left1;
            case "Right1":
                return right1;
            case "Up1":
                return up1;
            case "Down1":
                return down1;
            case "RightClick":
                return rightClick;
            case "DebugLight":
                return debugLight;
            default:
                return false;
        }
        
    }
    
}
