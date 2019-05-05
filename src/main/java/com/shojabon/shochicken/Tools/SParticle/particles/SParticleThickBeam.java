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

public class SParticleThickBeam implements SParticleForm{

    private Vector end;
    private double dense;

    private Vector direction;
    private Vector baseMargin = new Vector();
    private Vector distanceAwayMargin = new Vector();

    private long startAfter = 0;
    private long perParticleDelay = 0;
    private boolean reverseStart = false;

    private SParticleCircle circle;


    public SParticleThickBeam( SParticleCircle circle, Vector end, double dense){
        this.end = end;
        this.circle = circle;
        this.dense = dense;
    }

    public SParticleThickBeam setCircle(SParticleCircle circle){
        this.circle = circle;
        return this;
    }

    public SParticleThickBeam setEnd(Vector end){
        this.end = end;
        return this;
    }
    public SParticleThickBeam setDense(double dense){
        this.dense = dense;
        return this;
    }

    public SParticleThickBeam setBaseMargin(Vector baseMargin){
        this.baseMargin = baseMargin;
        return this;
    }
    public SParticleThickBeam setDistanceAwayMargin(Vector direction, double range){
        this.distanceAwayMargin = direction.normalize().multiply(range);
        return this;
    }

    public SParticleThickBeam setDirection(Vector direction){
        this.direction = direction;
        return this;
    }
    public SParticleThickBeam setPerParticleDelay(long delayMills){
        this.perParticleDelay = delayMills;
        return this;
    }
    public SParticleThickBeam setStartAfter(long startAfterMills){
        this.startAfter = startAfterMills;
        return this;
    }

    public SParticleThickBeam setReverse(boolean reverse){
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
            List<Vector> pos = getLocationInfo();
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
                Vector circleDirection = SParticleForm.getDirectionTowards(atLocation.toVector(), end.clone().add(atLocation.toVector()));
                circle.setDirection(circleDirection).playParticle(v.toLocation(Objects.requireNonNull(atLocation.getWorld())).add(atLocation).add(distanceAwayMargin).add(baseMargin));
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
        return line(new Vector(0,0,0), end, dense);
    }

    @Override
    public List<Vector> getLocationInfo(Vector atVector) {
        List<Vector> pos = getLocationInfo();
        for(int i =0; i < pos.size(); i++){
            pos.set(i, pos.get(i).add(atVector));
        }
        return pos;
    }
}
