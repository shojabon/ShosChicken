package com.shojabon.shochicken.SMagicSpells.Spells;

import com.shojabon.shochicken.SMagicSpells.SMagicSpell;
import com.shojabon.shochicken.Tools.SParticle.interfaces.SParticleForm;
import com.shojabon.shochicken.Tools.SParticle.interfaces.SRayTraceResult;
import com.shojabon.shochicken.Tools.SParticle.particles.SParticleFixedLine;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class BeamAttack implements SMagicSpell {

    private Location target;
    private Location fromLocation;
    private EnumParticle trail;
    private long delayPerparticle;
    private Entity ignoreEntity;

    public BeamAttack(double damage, Location fromLocation, Location towards,EnumParticle trail){
        this.target = towards;
        this.fromLocation = fromLocation;
        this.trail = trail;
    }

    public BeamAttack setPerParticleDelay(long delayMills){
        this.delayPerparticle = delayMills;
        return this;
    }

    public BeamAttack setIgnoreEntity(Entity entity){
        ignoreEntity = entity;
        return this;
    }


    @Override
    public void play() {
        new Thread(()-> {
            Vector hit = target.clone().toVector();
            SRayTraceResult srtr = SParticleForm.rayTrace(fromLocation, SParticleForm.getDirectionTowards(fromLocation.toVector(), target.toVector()), 100, ignoreEntity);
            if(srtr != null)  hit = srtr.getLocation();
            new SParticleFixedLine(trail, hit, 0.1).setPerParticleDelay(delayPerparticle).playParticle(fromLocation);
            SParticleForm.playSingleParticle(target, EnumParticle.EXPLOSION_LARGE);
            if(srtr.getHitEntity() != null) srtr.getHitEntity().setFireTicks(200);
        }).start();
    }

    @Override
    public void cancel() {

    }
}
