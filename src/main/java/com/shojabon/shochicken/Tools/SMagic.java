package com.shojabon.shochicken.Tools;

import com.google.common.util.concurrent.ExecutionError;
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

    private class FixedLine{
        private Vector endPoint;
        private double dense;

        public FixedLine(Vector endPoint, double dense){
            this.endPoint = endPoint;
            this.dense = dense;
        }

        public Vector getEndPoint() {
            return endPoint;
        }

        public double getDense() {
            return dense;
        }
    }
    
    private ArrayList<Vector> locations = new ArrayList<>();
    private ArrayList<FixedLine> fixedLine = new ArrayList<>();
    private Vector distanceAwayMargin = new Vector();
    private Vector baseMagrin = new Vector();
    private long playAfter = 0;
    private long playTimeMargin = 0;



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

    public SMagic setPlayAfter(long mills){
        playAfter = mills;
        return this;
    }

    public SMagic circle(double radius, int dots){
        for(double i =0; i < dots; i++){
            double x = sin(Math.toRadians(i*360D/((double)dots))) * radius;
            double y = cos(Math.toRadians(i*360D/((double)dots))) * radius;
            locations.add(new Vector(x, y, 0));
        }
        return this;
    }

    private SMagic line(Vector start, Vector end, double dense){
        Vector v = new Vector(end.getX() - start.getX(),end.getY() - start.getY(),end.getZ() - start.getZ()).divide(new Vector(start.distance(end), start.distance(end), start.distance(end)));
        double x = v.getX() * dense;
        double y = v.getY() * dense;
        double z = v.getZ() * dense;
        int d = (int) ((start.distance(end))/dense);
        for(int i =0; i < d; i++){
            if(start == new Vector(0,0,0)) {
                locations.add(start.clone());
            }else{
                locations.add(start.clone());
            }
            start.add(new Vector(x,y,z));
        }
        return this;
    }

    public SMagic line(Vector end, double dense){
        line(new Vector(0,0,0), end, dense);
        return this;
    }

    public SMagic fixedLine(Vector end, double dense){
        fixedLine.add(new FixedLine(end, dense));
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

    public SMagic setPlayTimeMargin(long mills){
        playTimeMargin = mills;
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

    private void render(Location atLocation, EnumParticle particle){
            for(FixedLine line: fixedLine){
                Location start = atLocation.clone();
                Location end = line.getEndPoint().toLocation(Objects.requireNonNull(start.getWorld())).clone();
                Vector v = new Vector(end.getX() - start.getX(),end.getY() - start.getY(),end.getZ() - start.getZ()).divide(new Vector(start.distance(end), start.distance(end), start.distance(end)));
                double x = v.getX() * line.getDense();
                double y = v.getY() * line.getDense();
                double z = v.getZ() * line.getDense();
                int d = (int) ((start.distance(end))/line.getDense());
                for(int i =0; i < d; i++){
                    playSingleParticle(start, particle);
                    start.add(new Vector(x,y,z));
                }
            }
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
        render(atLocation, particle);
        for(int i =0; i < locations.size(); i++){
            if(playTimeMargin != 0){
                try{
                    Thread.sleep(playTimeMargin);
                }catch (Exception e){}
            }
            Location l = locations
                    .get(i)
                    .toLocation(Objects.requireNonNull(atLocation.getWorld()))
                    .add(atLocation)
                    .add(distanceAwayMargin)
                    .add(baseMagrin);
            playSingleParticle(l
                    , particle);
        }
    }

//    public void playParticle(Location l, int r, int g, int b){
//        for(Player p : Bukkit.getServer().getOnlinePlayers()){
//            p.spawnParticle(Particle.REDSTONE, l.getX(), l.getY(), l.getZ(), 0, r/255D, g/255D, b/255D,1);
//        }
//    }
}
