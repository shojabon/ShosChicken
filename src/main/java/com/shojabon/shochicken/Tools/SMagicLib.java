package com.shojabon.shochicken.Tools;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class SMagicLib {


    public  double cos(double a){
        return Math.cos(a);
    }
    public  double sin(double a){
        return Math.sin(a);
    }
    public  double tan(double a){
        return Math.tan(a);
    }

    public  Vector rotateAboutX(Vector vect, double a){
        double Y = cos(a)*vect.getY() - sin(a)*vect.getZ();
        double Z = sin(a)*vect.getY() + cos(a)*vect.getZ();
        return vect.setY(Y).setZ(Z);
    }

    public  Vector rotateAboutY(Vector vect, double a){
        double X = cos(a)*vect.getX() + sin(a)*vect.getZ();
        double Z = -sin(a)*vect.getX() + cos(a)*vect.getZ();
        return vect.setX(X).setZ(Z);
    }

    public  Vector rotateFunction(Vector v, Location loc){

        double yawRadiant = loc.getYaw()/180*Math.PI;
        double pitchRadiant = loc.getPitch()/180*Math.PI;

        v = rotateAboutX(v, pitchRadiant);
        v = rotateAboutY(v, -yawRadiant);
        return v;
    }

    public  Vector rotateFunction(Location location, Location direction){
        double yawRadiant = direction.getYaw()/180*Math.PI;
        double pitchRadiant = direction.getPitch()/180*Math.PI;

        Vector v  = new Vector(location.getX(), location.getY(), location.getZ());

        v = rotateAboutX(v, pitchRadiant);
        v = rotateAboutY(v, -yawRadiant);
        return v;
    }

    public  ArrayList<Location> rotatePosList(ArrayList<Location> pos, Location direction){
        ArrayList<Location> posOut = new ArrayList<>();
        for(int i =0; i < pos.size(); i++){
            posOut.add(rotateFunction(pos.get(i), direction).toLocation(Objects.requireNonNull(direction.getWorld())));
        }
        return posOut;
    }

    public ArrayList<Location> line(Location start, Location end, double dense){
        ArrayList<Location> pos = new ArrayList<>();
        Vector v = new Vector(end.getX() - start.getX(),end.getY() - start.getY(),end.getZ() - start.getZ()).divide(new Vector(start.distance(end), start.distance(end), start.distance(end)));
        double x = v.getX() * dense;
        double y = v.getY() * dense;
        double z = v.getZ() * dense;
        int d = (int) ((start.distance(end))/dense);
        for(int i =0; i < d; i++){
            pos.add(start.clone());
            start.add(x,y,z);
        }
        return pos;
    }

    public ArrayList<Location> circle(float radius, int dots){
        ArrayList<Location> pos = new ArrayList<>();
        for(int i =0; i < dots; i++){
            double x = sin(Math.toRadians(i*360D/((double)dots)*radius));
            double z = cos(Math.toRadians(i*360D/((double)dots)*radius));
        }
        return pos;
    }

    public ArrayList<Location> drawHelixBeam(Location start,Location end, EnumParticle particle, double dense, double inTime, double radius){
        ArrayList<Location> pos = new ArrayList<>();
        Vector v = new Vector(end.getX() - start.getX(),end.getY() - start.getY(),end.getZ() - start.getZ()).divide(new Vector(start.distance(end), start.distance(end), start.distance(end)));
        double x = v.getX() * dense;
        double y = v.getY() * dense;
        double z = v.getZ() * dense;
        int d = (int) ((start.distance(end))/dense);
        for(int i =0; i < d; i++){
            for(double ii =0; ii < 50; ii++){
                double xx = Math.sin(i*dense)*radius;
                double xy = Math.cos(i*dense)*radius;
                double xz = 0;
                Location l = start.clone().add(rotateFunction(new Vector(xx, xy, xz), start.clone()));
                pos.add(l.clone());
            }

            start.add(x,y,z);
        }
        return pos;

    }

    public void playParticle(Location l, EnumParticle particle){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) l.getX(), (float) (l.getY()), (float) l.getZ(), 0, 0, 0, 1, 1);
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if(p.getLocation().getWorld() == l.getWorld()){
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public void playParticle(ArrayList<Location> pos, EnumParticle particle){
        for(Location a : pos){
            Bukkit.broadcastMessage(String.valueOf(a));
            playParticle(a, particle);
        }
    }

    public void playParticle(Location l, int r, int g, int b){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            p.spawnParticle(Particle.REDSTONE, l.getX(), l.getY(), l.getZ(), 0, r/255D, g/255D, b/255D,1);
        }
    }

}
