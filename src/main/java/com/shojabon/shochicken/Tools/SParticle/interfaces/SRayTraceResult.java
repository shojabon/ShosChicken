package com.shojabon.shochicken.Tools.SParticle.interfaces;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class SRayTraceResult{
    private Entity hitEntity;
    private Vector location;
    private SRayTraceResultType type = SRayTraceResultType.NULL;

    public SRayTraceResult(Entity hitEntity, Vector location){
        if(hitEntity != null){
            type = SRayTraceResultType.ENTITY;
        }
        if(hitEntity == null && location != null){
            type = SRayTraceResultType.BLOCK;
        }
        this.hitEntity = hitEntity;
        this.location = location;
    }
    public SRayTraceResult(){
        type = SRayTraceResultType.NULL;
    }

    public Entity getHitEntity() {
        return hitEntity;
    }

    public Vector getLocation() {
        return location;
    }

    public SRayTraceResultType getType() {
        return type;
    }
}

