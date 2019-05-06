package com.shojabon.shochicken.Tools.SParticle.interfaces;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class SRayTraceResult{
    private Entity hitEntity;
    private Vector location;

    public SRayTraceResult(Entity hitEntity, Vector location){
        this.hitEntity = hitEntity;
        this.location = location;
    }

    public Entity getHitEntity() {
        return hitEntity;
    }

    public Vector getLocation() {
        return location;
    }

}
