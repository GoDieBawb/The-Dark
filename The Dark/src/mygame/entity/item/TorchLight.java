/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.app.state.AppStateManager;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.shadow.PointLightShadowFilter;
import java.util.Random;

/**
 *
 * @author Bawb
 */
public class TorchLight extends PointLight {
    
    private Long    lastFlicker;
    private int     flickerDelay;
    private boolean isLit;
    private FilterPostProcessor shadowFilter;

    
    public TorchLight(AppStateManager stateManager) {
        lastFlicker = 0L;
        createShadow(stateManager);
    }
    
    private void createShadow(AppStateManager stateManager) {
        PointLightShadowFilter shadow = new PointLightShadowFilter(stateManager.getApplication().getAssetManager(), 1024);
        shadow.setLight(this);
        shadow.setShadowIntensity(1f);
        shadow.setEnabled(true);
        shadowFilter = new FilterPostProcessor();
        shadowFilter.addFilter(shadow);
        //stateManager.getApplication().getViewPort().addProcessor(shadowFilter);
    }
    
    public FilterPostProcessor getShadow() {
        return shadowFilter;
    }    
    
    public void setIsLit(boolean newVal) {
        isLit = newVal;
    }
    
    public boolean isLit() {
        return isLit;
    }
    
    private void flicker() {
    
        lastFlicker = System.currentTimeMillis();
        setColor(ColorRGBA.Orange.mult(randInt(1,2)));
        setRadius(randInt(5,15));
        flickerDelay = randInt(20,100);
        
    }
    
    private int randInt(int min, int max) {
        
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
        
    }
    
    public void update (float tpf) {
        
        if(!isLit) {
            return;
        }

        if (System.currentTimeMillis()/10 - lastFlicker/10 > flickerDelay)
            flicker();
        
    }
    
}
