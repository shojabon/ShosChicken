package com.shojabon.shochicken.Tools.SParticle.interfaces;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.StrictMath.asin;
import static java.lang.StrictMath.atan2;

public interface SParticleForm {
    void playParticle(Location atLocation);

    List<Vector> getLocationInfo();

    List<Vector> getLocationInfo(Vector atVector);

    static double cos(double a){
        return Math.cos(a);
    }
    static double sin(double a){
        return Math.sin(a);
    }
    static  double tan(double a){
        return Math.tan(a);
    }

    static Vector rotateAboutX(Vector vect, double a){
        double Y = cos(a)*vect.getY() - sin(a)*vect.getZ();
        double Z = sin(a)*vect.getY() + cos(a)*vect.getZ();
        return vect.setY(Y).setZ(Z);
    }

    static  Vector rotateAboutY(Vector vect, double a){
        double X = cos(a)*vect.getX() + sin(a)*vect.getZ();
        double Z = -sin(a)*vect.getX() + cos(a)*vect.getZ();
        return vect.setX(X).setZ(Z);
    }

    static  Vector rotateFunction(Vector v, Vector direction){
        if(direction == null) return v;
        Vector loc = pitchYawVector(direction.normalize());

        double yawRadiant = Math.toRadians(loc.getY());
        double pitchRadiant = Math.toRadians(loc.getX());

        v = rotateAboutX(v, pitchRadiant);
        v = rotateAboutY(v, -yawRadiant);
        return v;
    }

    static void playSingleParticle(Location l, EnumParticle particle){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) l.getX(), (float) (l.getY()), (float) l.getZ(), 0, 0, 0, 1, 1);
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if(p.getLocation().getWorld() == l.getWorld()){
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    static Vector getDirectionTowards(Vector start, Vector end){
        return new Vector(end.getX() - start.getX(),end.getY() - start.getY(),end.getZ() - start.getZ()).divide(new Vector(start.distance(end), start.distance(end), start.distance(end))).normalize();
    }

    static Vector pitchYawVector(Vector direction){
        Location l = new Location(null, 0,0,0).setDirection(direction.normalize());
        return new Vector(l.getPitch(), l.getYaw(), 0);
    }

    static Vector directionVector(Vector pitchYaw){
        Location l = new Location(null, 0,0,0,  (float) pitchYaw.getY(), (float) pitchYaw.getX());
        return l.getDirection().normalize();
    }

    static RayTraceResult rayTrace(Location location, Vector direction, double distance){
        Vector direc = direction.clone().normalize().multiply(0.5);
        Location loc = location.add(direc).clone();
        for(int i =0; i < distance*2; i++){
            if(loc.getBlock() == null) return new RayTraceResult(null, loc.toVector());
            Collection<Entity> enti = loc.getWorld().getNearbyEntities(loc,0.1,0.1,0.1);
            if(enti.size() != 0) return new RayTraceResult(((Entity)enti.toArray()[0]),((Entity)enti.toArray()[0]).getLocation().toVector());
            loc.add(direc);
        }
        return null;
    }

}

class RayTraceResult{
    private Entity hitEntity;
    private Vector location;

    public RayTraceResult(Entity hitEntity, Vector location){
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
