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
    private Spatial                 target;
    private boolean                 finding;
    private boolean                 atGoal;
    private Long                    lastCalc;
    
    //On Construct Determine the nav mesh and the entity attached
    public FinderControl(AppStateManager stateManager, Finder finder) {
        
        Mesh mesh         = ((Geometry)stateManager.getState(GameManager.class).getSceneManager().getScene().getChild("NavMesh")).getMesh();
        navMesh           = new NavMesh(mesh);
        pathfinder        = new NavMeshPathfinder(navMesh);
        entity            = (PhysicalEntity) finder;
        this.finder       = finder;
        setSpatial((Entity) finder);
        
    }
    
    //Instructs the object to begin finding their way to a target
    public void findTarget(Spatial target) {
        pathfinder.clearPath();
        pathfinder.setPosition(spatial.getWorldTranslation());
        pathfinder.computePath(target.getWorldTranslation());
        finding     = true;
        this.target = target;
        lastCalc    = System.currentTimeMillis();
        
    }
    
    //Clears the path and finding status when run
    public void stopFinding() {
        pathfinder.clearPath();
        entity.getPhys().setWalkDirection(Vector3f.ZERO);
        finding = false;
        target = null;
    }
    
    //Returns whether the object is currently seeking an ojbect
    public boolean isFinding() {
        return finding;
    }
    
    //Returns whether the finder is at its goal
    public boolean atGoal() {
        return atGoal;
    }
    
    //The update loop
    @Override
    public void update(float tpf) {
        
        //If not finding do nothing
        if(!finding)
            return;
        
        //Determines the current cool down time
        Long coolLength = System.currentTimeMillis()/1000 - lastCalc / 1000;
        
        //If the cool down length is more than one second recalculate
        if (coolLength > 1) {
            findTarget(target);
        }
        
        //Gets the next waypoint in the path
        Waypoint wp = pathfinder.getNextWaypoint();
        
        //If there is no waypoint do nothing
        if (wp == null)
            return;
        
        //Gets the move speed and the movement direction
        float    moveSpeed = finder.getMoveSpeed();
        Vector3f moveDir   = wp.getPosition().subtract(spatial.getWorldTranslation()).normalize().multLocal(4);
        
        //If they are more than one world distance unit they are not at the goal and keep finding
        if(wp.getPosition().distance(spatial.getWorldTranslation()) > 1) {
            entity.getPhys().setWalkDirection(moveDir);
            entity.getPhys().setViewDirection(moveDir);
            atGoal = false;
        }
        
        //If at the final waypoint set at goal to true
        else if (pathfinder.isAtGoalWaypoint()) {
            atGoal = true;
        }
        
        //If less than one from current waypoint and not the goal. Go to next waypoint
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
