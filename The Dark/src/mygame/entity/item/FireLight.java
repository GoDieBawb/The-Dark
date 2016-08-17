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
public class FireLight extends PointLight {
    
    private Long    lastFlicker;
    private int     flickerDelay;
    private boolean isLit;
    private int     minRadius;
    private int     maxRadius;
    private FilterPostProcessor shadowFilter;

    //Constructs the time of last flicker and creates the shadow filer
    public FireLight(AppStateManager stateManager) {
        lastFlicker = 0L;
        createShadow(stateManager);
        minRadius = 5;
        maxRadius = 15;
    }
    
    public void setMaxRadius(int newMax) {
        maxRadius = newMax;
    }
    
    public void setMinRadius(int newMin) {
        minRadius = newMin;
    }
    
    //Construct the shadow filter. Currently not working
    private void createShadow(AppStateManager stateManager) {
        PointLightShadowFilter shadow = new PointLightShadowFilter(stateManager.getApplication().getAssetManager(), 1024);
        shadow.setLight(this);
        shadow.setShadowIntensity(1f);
        shadow.setEnabled(true);
        shadowFilter = new FilterPostProcessor();
        shadowFilter.addFilter(shadow);
        //stateManager.getApplication().getViewPort().addProcessor(shadowFilter);
    }
    
    //Returns the shadow filter
    public FilterPostProcessor getShadow() {
        return shadowFilter;
    }    
    
    //Sets whether the torch is lit
    public void setIsLit(boolean newVal) {
        isLit = newVal;
    }
    
    //Returns whether the torch is lit
    public boolean isLit() {
        return isLit;
    }
    
    //Flicker the torch by changing color and distance
    private void flicker() {
    
        lastFlicker = System.currentTimeMillis();
        setColor(ColorRGBA.Orange.mult(randInt(1,2)));
        setRadius(randInt(minRadius,maxRadius));
        flickerDelay = randInt(20,100);
        
    }
    
    //Generate a random integer within a range
    private int randInt(int min, int max) {
        
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
        
    }
    
    //Upate loop for the torch light
    public void update (float tpf) {
        
        //Do nothing if the torch isn't lit
        if(!isLit) {
            return;
        }

        //Flicker the torch light on timed loop
        if (System.currentTimeMillis()/10 - lastFlicker/10 > flickerDelay)
            flicker();
        
    }
    
}
