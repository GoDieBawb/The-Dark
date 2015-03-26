/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.monster;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import mygame.GameManager;
import mygame.util.PhysicsManager;

/**
 *
 * @author Bawb
 */
public class MonsterManager {
    
    private Node                monsterNode;
    private SimpleApplication   app;
    private PhysicsManager      physicsManager;
    private ArrayList<Monster>  monsters;
    
    public MonsterManager(SimpleApplication app) {
        this.app       = app;
        physicsManager = app.getStateManager().getState(GameManager.class).getUtilityManager().getPhysicsManager();
        monsters       = new ArrayList();
        createMonsterNode();
    }
    
    public ArrayList<Monster> getMonsters() {
        return monsters;
    }
    
    private void createMonsterNode() {
        monsterNode = new Node();
        app.getRootNode().attachChild(monsterNode);
    }
    
    public Node getMonsterNode() {
        return monsterNode;
    }
    
    public void createZombie() {
        Zombie zombie;
        zombie = new Zombie(app.getStateManager());
        zombie.createPhys();
        zombie.createFinderControl(app.getStateManager());
        monsterNode.attachChild(zombie);
        physicsManager.getPhysics().getPhysicsSpace().add(zombie.getPhys());
        zombie.getPhys().warp(new Vector3f(5,1,5));
        monsters.add(zombie);
    }    
    
    public void update(float tpf) {
    
        for(int i  = 0; i < monsters.size(); i++) {
            
            Monster currentMonster = monsters.get(i);
            currentMonster.behave();
                    
        }
        
    }
    
}
