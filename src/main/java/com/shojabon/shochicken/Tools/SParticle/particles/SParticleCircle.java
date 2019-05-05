package com.shojabon.shochicken.Tools.SParticle.particles;

import com.google.common.collect.Lists;
import com.shojabon.shochicken.Tools.SParticle.SParticle;
import com.shojabon.shochicken.Tools.SParticle.interfaces.SParticleForm;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SParticleCircle implements SParticleForm {

    private EnumParticle particle;
    private double radius;
    private double dots;

    private Vector direction;
    private Vector baseMargin = new Vector();
    private Vector distanceAwayMargin = new Vector();

    private long startAfter = 0;
    private long perParticleDelay = 0;
    private boolean reverseStart = false;


    public SParticleCircle(EnumParticle particle,double radius, double dots){
        this.radius = radius;
        this.particle = particle;
        this.dots = dots;
    }

    public SParticleCircle setParticle(EnumParticle particle){
        this.particle = particle;
        return this;
    }
    public SParticleCircle setRadius(double radius){
        this.radius = radius;
        return this;
    }
    public SParticleCircle setDots(double dots){
        this.dots = dots;
        return this;
    }

    public SParticleCircle setBaseMargin(Vector baseMargin){
        this.baseMargin = baseMargin;
        return this;
    }
    public SParticleCircle setDistanceAwayMargin(Vector direction, double range){
        this.distanceAwayMargin = direction.normalize().multiply(range);
        return this;
    }

    public SParticleCircle setDirection(Vector direction){
        this.direction = direction;
        return this;
    }
    public SParticleCircle setPerParticleDelay(long delayMills){
        this.perParticleDelay = delayMills;
        return this;
    }
    public SParticleCircle setStartAfter(long startAfterMills){
        this.startAfter = startAfterMills;
        return this;
    }

    public SParticleCircle setReverse(boolean reverse){
        this.reverseStart = reverse;
        return this;
    }


    @Override
    public void playParticle(Location atLocation) {
        Runnable r = () -> {
            List<Vector> pos = getLocationInfo();
            if(reverseStart) pos = Lists.reverse(pos);
            if(startAfter != 0){
                try{
                    Thread.sleep(startAfter);
                }catch (Exception e){
                }
            }
            for(Vector v: pos){
                if(perParticleDelay != 0){
                    try{
                        Thread.sleep(perParticleDelay);
                    }catch (Exception e){
                    }
                }
                SParticleForm.playSingleParticle(v.toLocation(Objects.requireNonNull(atLocation.getWorld())).add(atLocation).add(distanceAwayMargin).add(baseMargin), particle);
            }
        };
        if(startAfter == 0 && perParticleDelay == 0){
            new Thread(r).run();
        }else{
            new Thread(r).start();
        }
    }

    @Override
    public List<Vector> getLocationInfo() {
        List<Vector> pos = new ArrayList<>();
        for(double i =0; i < dots; i++){
            double x = SParticleForm.sin(Math.toRadians(i*360D/ dots)) * radius;
            double y = SParticleForm.cos(Math.toRadians(i*360D/ dots)) * radius;
            Vector l = SParticleForm.rotateFunction(new Vector(x, y, 0), direction);
            pos.add(l);
        }
        return pos;
    }

    @Override
    public List<Vector> getLocationInfo(Vector atVector) {
        List<Vector> v = getLocationInfo();
        for(int i =0; i < v.size(); i++){
            v.set(i, v.get(i).add(atVector));
        }
        return v;
    }
}
