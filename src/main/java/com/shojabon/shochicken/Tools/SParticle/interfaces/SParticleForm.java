package com.shojabon.shochicken.Tools.SParticle.interfaces;

import com.shojabon.shochicken.Tools.SMagic;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

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

    static  Vector rotateFunction(Vector v, Location loc){
        if(loc == null) return v;

        double yawRadiant = loc.getYaw()/180*Math.PI;
        double pitchRadiant = loc.getPitch()/180*Math.PI;

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

}
