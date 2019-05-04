package com.shojabon.shochicken.Tools;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleCollisionEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;

public class SMagic {
    
    private ArrayList<Vector> locations = new ArrayList<>();
    private Vector distanceAwayMargin = new Vector();
    private Vector baseMagrin = new Vector();


    private  double cos(double a){
        return Math.cos(a);
    }
    private  double sin(double a){
        return Math.sin(a);
    }
    private  double tan(double a){
        return Math.tan(a);
    }

    private Vector rotateAboutX(Vector vect, double a){
        double Y = cos(a)*vect.getY() - sin(a)*vect.getZ();
        double Z = sin(a)*vect.getY() + cos(a)*vect.getZ();
        return vect.setY(Y).setZ(Z);
    }

    private  Vector rotateAboutY(Vector vect, double a){
        double X = cos(a)*vect.getX() + sin(a)*vect.getZ();
        double Z = -sin(a)*vect.getX() + cos(a)*vect.getZ();
        return vect.setX(X).setZ(Z);
    }

    private  Vector rotateFunction(Vector v, Location loc){

        double yawRadiant = loc.getYaw()/180*Math.PI;
        double pitchRadiant = loc.getPitch()/180*Math.PI;

        v = rotateAboutX(v, pitchRadiant);
        v = rotateAboutY(v, -yawRadiant);
        return v;
    }

    public  SMagic rotateFunction(Location direction){
        for(int i =0; i < locations.size(); i++){
            locations.set(i, rotateFunction(locations.get(i), direction));
        }
        return this;
    }

    public SMagic circle(float radius, int dots){
        for(int i =0; i < dots; i++){
            double x = sin(Math.toRadians(i*360D/((double)dots)*radius));
            double y = cos(Math.toRadians(i*360D/((double)dots)*radius));
            locations.add(new Vector(x, y, 0));
        }
        return this;
    }
    

    public SMagic setBaseMargin(Vector v){
        baseMagrin = v;
        return this;
    }

    public SMagic setBaseMargin(double x, double y, double z){
        baseMagrin = new Vector(x, y, z);
        return this;
    }

    public SMagic setDistanceAway(Vector direction, float distance){
        distanceAwayMargin.add(direction.normalize().multiply(distance));
        return this;
    }

    public ArrayList<Location> getPosInformation(Location locationAt){
        ArrayList<Location> pos = new ArrayList<>();
        for(int i =0; i < locations.size(); i++){
            pos.add(locations.get(i).toLocation(Objects.requireNonNull(locationAt.getWorld())).add(locationAt));
        }
        return pos;
    }

    private void playSingleParticle(Location l, EnumParticle particle){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) l.getX(), (float) (l.getY()), (float) l.getZ(), 0, 0, 0, 1, 1);
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if(p.getLocation().getWorld() == l.getWorld()){
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public void playParticle(Location atLocation, EnumParticle particle){
        for(int i =0; i < locations.size(); i++){
            playSingleParticle(locations
                    .get(i)
                    .toLocation(Objects.requireNonNull(atLocation.getWorld()))
                    .add(atLocation).add(distanceAwayMargin)
                    .add(baseMagrin)
                    , particle);
        }
    }

//    public void playParticle(Location l, int r, int g, int b){
//        for(Player p : Bukkit.getServer().getOnlinePlayers()){
//            p.spawnParticle(Particle.REDSTONE, l.getX(), l.getY(), l.getZ(), 0, r/255D, g/255D, b/255D,1);
//        }
//    }
}
