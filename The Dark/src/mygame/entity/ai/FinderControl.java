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
import mygame.entity.player.Player;

/**
 *
 * @author Bawb
 */
public class FinderControl extends AbstractControl {
    
    private NavMesh           navMesh;
    private NavMeshPathfinder pathfinder;
    private AppStateManager   stateManager;
    private Player            player;
    private PhysicalEntity    entity;
    private boolean           finding;
    
    public FinderControl(AppStateManager stateManager, Finder finder) {
        
        this.stateManager = stateManager;
        Mesh mesh         = ((Geometry)stateManager.getState(GameManager.class).getScene().getChild("NavMesh")).getMesh();
        navMesh           = new NavMesh(mesh);
        pathfinder        = new NavMeshPathfinder(navMesh);
        player            = stateManager.getState(GameManager.class).getPlayer();
        entity            = (PhysicalEntity) finder;
        setSpatial((Entity) finder);
        
    }
    
    public void findTarget(Spatial target) {
        pathfinder.clearPath();
        pathfinder.setPosition(spatial.getWorldTranslation());
        pathfinder.computePath(target.getWorldTranslation());
        finding = true;
    }
    
    public void stopFinding() {
        pathfinder.clearPath();
        entity.getPhys().setWalkDirection(Vector3f.ZERO);
        finding = false;
    }
    
    @Override
    public void update(float tpf) {
    
        if(!finding)
            return;
        
        //finder.getPhys().setWalkDirection(new Vector3f(player.getWorldTranslation().subtract(finder.getWorldTranslation())));
        Waypoint wp = pathfinder.getNextWaypoint();
        
        if (wp == null)
            return;
        
        Vector3f moveDir = wp.getPosition().subtract(spatial.getWorldTranslation()).normalize().mult(5);
        
        if(wp.getPosition().distance(spatial.getWorldTranslation()) > 1) {
            entity.getPhys().setWalkDirection(moveDir);
            entity.getPhys().setViewDirection(spatial.getWorldTranslation().subtract(player.getWorldTranslation()).negate());
        }
        
        else if (pathfinder.isAtGoalWaypoint()) {
            stopFinding();
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
