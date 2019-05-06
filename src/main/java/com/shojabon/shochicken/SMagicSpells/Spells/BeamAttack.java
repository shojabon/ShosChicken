package com.shojabon.shochicken.SMagicSpells.Spells;

import com.shojabon.shochicken.SMagicSpells.SMagicSpell;
import com.shojabon.shochicken.Tools.SParticle.interfaces.SParticleForm;
import com.shojabon.shochicken.Tools.SParticle.particles.SParticleFixedLine;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class BeamAttack implements SMagicSpell {

    private Location target;
    private Location fromLocation;
    private EnumParticle trail;
    private long delayPerparticle;

    public BeamAttack( double damage, Location fromLocation, Location towards,EnumParticle trail){
        this.target = towards;
        this.fromLocation = fromLocation;
        this.trail = trail;
    }

    public BeamAttack setPerParticleDelay(long delayMills){
        this.delayPerparticle = delayMills;
        return this;
    }


    @Override
    public void play() {
        new Thread(()-> {

            new SParticleFixedLine(trail, target.toVector(), 0.1).setPerParticleDelay(delayPerparticle).playParticle(fromLocation);
            SParticleForm.playSingleParticle(target, EnumParticle.EXPLOSION_LARGE);
        }).start();
    }

    @Override
    public void cancel() {

    }
}
