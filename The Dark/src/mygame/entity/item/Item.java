/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import mygame.entity.Entity;

/**
 *
 * @author Bawb
 */
public class Item extends Entity {
    
    private int quantity;
    
    public void setItemQuantity(int newVal) {
        quantity = newVal;
    }
    
    public int getItemQuantity() {
        return quantity;
    }
    
}
