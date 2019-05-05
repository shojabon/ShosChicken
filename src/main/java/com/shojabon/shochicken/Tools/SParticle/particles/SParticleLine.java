package com.shojabon.shochicken.Tools.SParticle.particles;

import com.google.common.collect.Lists;
import com.shojabon.shochicken.Tools.SMagic;
import com.shojabon.shochicken.Tools.SParticle.interfaces.SParticleForm;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SParticleLine implements SParticleForm{

    private EnumParticle particle;
    private Vector end;
    private double dense;

    private Location direction;
    private Vector baseMargin = new Vector();
    private Vector distanceAwayMargin = new Vector();

    private long startAfter = 0;
    private long perParticleDelay = 0;
    private boolean reverseStart = false;


    public SParticleLine(EnumParticle particle, Vector end, double dense){
        this.particle = particle;
        this.end = end;
        this.dense = dense;
    }

    public SParticleLine setParticle(EnumParticle particle){
        this.particle = particle;
        return this;
    }
    public SParticleLine setEnd(Vector end){
        this.end = end;
        return this;
    }
    public SParticleLine setDense(double dense){
        this.dense = dense;
        return this;
    }

    public SParticleLine setBaseMargin(Vector baseMargin){
        this.baseMargin = baseMargin;
        return this;
    }
    public SParticleLine setDistanceAwayMargin(Vector direction, double range){
        this.distanceAwayMargin = direction.normalize().multiply(range);
        return this;
    }

    public SParticleLine setDirection(Location direction){
        this.direction = direction;
        return this;
    }
    public SParticleLine setPerParticleDelay(long delayMills){
        this.perParticleDelay = delayMills;
        return this;
    }
    public SParticleLine setStartAfter(long startAfterMills){
        this.startAfter = startAfterMills;
        return this;
    }

    public SParticleLine setReverse(boolean reverse){
        this.reverseStart = reverse;
        return this;
    }

    private List<Vector> line(Vector start, Vector end, double dense){
        List<Vector> pos = new ArrayList<>();
        Vector v = new Vector(end.getX() - start.getX(),end.getY() - start.getY(),end.getZ() - start.getZ()).divide(new Vector(start.distance(end), start.distance(end), start.distance(end)));
        double x = v.getX() * dense;
        double y = v.getY() * dense;
        double z = v.getZ() * dense;
        int d = (int) ((start.distance(end))/dense);
        for(int i =0; i < d; i++){
            pos.add(start);
            start.add(new Vector(x,y,z));
        }
        return pos;
    }


    @Override
    public void playParticle(Location atLocation) {
        Runnable r = () -> {
            List<Vector> pos = getLocationInfo(atLocation.toVector());
            if (reverseStart) pos = Lists.reverse(pos);
            if (startAfter != 0) {
                try {
                    Thread.sleep(startAfter);
                } catch (Exception e) {
                }
            }
            for (Vector v : pos) {
                if (perParticleDelay != 0) {
                    try {
                        Thread.sleep(perParticleDelay);
                    } catch (Exception e) {
                    }
                }
                v = SParticleForm.rotateFunction(v, direction);
                SParticleForm.playSingleParticle(v.toLocation(Objects.requireNonNull(atLocation.getWorld())).add(atLocation).add(distanceAwayMargin).add(baseMargin), particle);
            }
        };
        if(startAfter == 0 && perParticleDelay == 0){
            r.run();
        }else{
            new Thread(r).start();
        }

    }

    @Override
    public List<Vector> getLocationInfo() {
        return line(new Vector(0,0,0), end, dense);
    }

    @Override
    public List<Vector> getLocationInfo(Vector atVector) {
        List<Vector> pos = getLocationInfo();
        for(int i =0; i < pos.size(); i++){
            pos.set(i, pos.get(i).add(atVector));
        }
        return null;
    }
}
