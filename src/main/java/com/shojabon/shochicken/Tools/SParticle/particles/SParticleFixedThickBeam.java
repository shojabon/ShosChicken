package com.shojabon.shochicken.Tools.SParticle.particles;

import com.google.common.collect.Lists;
import com.shojabon.shochicken.Tools.SParticle.interfaces.SParticleForm;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SParticleFixedThickBeam implements SParticleForm{

    private Vector end;
    private double dense;

    private Vector direction;
    private Vector baseMargin = new Vector();
    private Vector distanceAwayMargin = new Vector();

    private long startAfter = 0;
    private long perParticleDelay = 0;
    private boolean reverseStart = false;

    private SParticleCircle circle;


    public SParticleFixedThickBeam(SParticleCircle circle, Vector end, double dense){
        this.end = end;
        this.circle = circle;
        this.dense = dense;
    }

    public SParticleFixedThickBeam setCircle(SParticleCircle circle){
        this.circle = circle;
        return this;
    }

    public SParticleFixedThickBeam setEnd(Vector end){
        this.end = end;
        return this;
    }
    public SParticleFixedThickBeam setDense(double dense){
        this.dense = dense;
        return this;
    }

    public SParticleFixedThickBeam setBaseMargin(Vector baseMargin){
        this.baseMargin = baseMargin;
        return this;
    }
    public SParticleFixedThickBeam setDistanceAwayMargin(Vector direction, double range){
        this.distanceAwayMargin = direction.normalize().multiply(range);
        return this;
    }

    public SParticleFixedThickBeam setDirection(Vector direction){
        this.direction = direction;
        return this;
    }
    public SParticleFixedThickBeam setPerParticleDelay(long delayMills){
        this.perParticleDelay = delayMills;
        return this;
    }
    public SParticleFixedThickBeam setStartAfter(long startAfterMills){
        this.startAfter = startAfterMills;
        return this;
    }

    public SParticleFixedThickBeam setReverse(boolean reverse){
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
            pos.add(start.clone());
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
                Vector circleDirection = SParticleForm.getDirectionTowards(atLocation.toVector(), end.clone());
                circle.setDirection(circleDirection).playParticle(v.toLocation(Objects.requireNonNull(atLocation.getWorld())).add(distanceAwayMargin).add(baseMargin));
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
        return null;
    }

    @Override
    public List<Vector> getLocationInfo(Vector atVector) {
        return line(atVector, end, dense);
    }
}
