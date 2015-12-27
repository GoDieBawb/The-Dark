/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.ai;

import com.jme3.ai.navmesh.NavMesh;
import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.ai.navmesh.Path.Waypoint;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.PhysicalEntity;

/**
 *
 * @author Bawb
 */
public class FinderControl extends AbstractControl {
    
    private final NavMesh           navMesh;
    private final NavMeshPathfinder pathfinder;
    private final PhysicalEntity    entity;
    private final Finder            finder;
    private Spatial           target;
    private boolean           finding;
    private boolean           atGoal;
    private Long              lastCalc;
    
    public FinderControl(AppStateManager stateManager, Finder finder) {
        
        Mesh mesh         = ((Geometry)stateManager.getState(GameManager.class).getSceneManager().getScene().getChild("NavMesh")).getMesh();
        navMesh           = new NavMesh(mesh);
        pathfinder        = new NavMeshPathfinder(navMesh);
        entity            = (PhysicalEntity) finder;
        this.finder       = finder;
        setSpatial((Entity) finder);
        
    }
    
    public void findTarget(Spatial target) {
        pathfinder.clearPath();
        pathfinder.setPosition(spatial.getWorldTranslation());
        pathfinder.computePath(target.getWorldTranslation());
        finding     = true;
        this.target = target;
        lastCalc    = System.currentTimeMillis();
        
    }
    
    public void stopFinding() {
        pathfinder.clearPath();
        entity.getPhys().setWalkDirection(Vector3f.ZERO);
        finding = false;
        target = null;
    }
    
    public boolean isFinding() {
        return finding;
    }
    
    public boolean atGoal() {
        return atGoal;
    }
    
    @Override
    public void update(float tpf) {
        
        if(!finding)
            return;
        
        Long coolLength = System.currentTimeMillis()/1000 - lastCalc / 1000;
        
        if (coolLength > 1) {
            findTarget(target);
        }
        
        Waypoint wp = pathfinder.getNextWaypoint();
        
        if (wp == null)
            return;
        
        float    moveSpeed = finder.getMoveSpeed();
        Vector3f moveDir   = wp.getPosition().subtract(spatial.getWorldTranslation()).normalize().multLocal(4);
        
        if(wp.getPosition().distance(spatial.getWorldTranslation()) > 1) {
            entity.getPhys().setWalkDirection(moveDir);
            entity.getPhys().setViewDirection(moveDir);
            atGoal = false;
        }
        
        else if (pathfinder.isAtGoalWaypoint()) {
            atGoal = true;
        }
        
        else {
            pathfinder.goToNextWaypoint();
        }
        
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
