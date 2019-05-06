package com.shojabon.shochicken.Tools.SParticle;

import com.shojabon.shochicken.Tools.SParticle.interfaces.SParticleForm;
import net.minecraft.server.v1_12_R1.ExceptionEntityNotFound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SGodParticle {

    private Location location;
    private UUID armorStandUUID;
    private Entity armorStand;
    private Vector angle;


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

    public SGodParticle(Location l, ItemStack item, int data, boolean small){
        Location cache = l.clone();
        l = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ());
        location = l.clone();
        ArmorStand as = Objects.requireNonNull(l.getWorld()).spawn(l, ArmorStand.class);
        armorStandUUID = as.getUniqueId();
        as.setHelmet(new SItemStack(item).setDamage(data).build());
        as.setSmall(small);
        as.setGravity(false);
        as.setVisible(false);
        armorStand = Bukkit.getEntity(armorStandUUID);

        faceDirection(cache.getDirection());
        teleportParticle(l);
    }

    public void teleportParticle(Location l){
        if(l.equals(getLocation())) return;
        location = l.clone();
        armorStand.teleport(new Location(l.getWorld(),l.getX(), l.getY(), l.getZ()));
    }

    public void faceDirection(Location l){
        angle = SParticleForm.directionVector(new Vector(l.getPitch(), l.getYaw(),0 )).normalize();
        Vector pyVector = SParticleForm.pitchYawVector(angle.clone());
        ((ArmorStand) armorStand).setHeadPose(new EulerAngle(Math.toRadians(pyVector.getX()), Math.toRadians(pyVector.getY()), 0));
    }

    public void faceDirection(Vector direction){
        angle = direction.clone().normalize();
        Vector pyVector = SParticleForm.pitchYawVector(angle.clone());
        ((ArmorStand) armorStand).setHeadPose(new EulerAngle(Math.toRadians(pyVector.getX()), Math.toRadians(pyVector.getY()), 0));
    }

    public void faceLocation(Location l){
        Vector v = new Vector(l.getX() - getLocation().getX(),l.getY() - getLocation().getY(),l.getZ() - getLocation().getZ());
        faceDirection(l.clone().setDirection(v).clone());
    }

    public void faceLocation(Vector l){
        Vector v = new Vector(l.getX() - getLocation().getX(),l.getY() - getLocation().getY(),l.getZ() - getLocation().getZ());
        faceDirection(v.clone());
    }


    public void faceDirectionInTime(Vector direction, double dense, long inTimeMills){
        new Thread(() -> {
            Vector p1 = angle.clone().normalize().multiply(3);
            Vector p2 = direction.clone().normalize().multiply(3);
            List<Vector> eyePosList = line(p1, p2, dense);
            for(int i =0; i < eyePosList.size(); i++){
                try{
                    Thread.sleep(inTimeMills/eyePosList.size());
                }catch (Exception e){
                }
                faceLocation(eyePosList.get(i).add(location.toVector()));
            }
        }).start();
        try {
            Thread.sleep(inTimeMills);
        } catch (InterruptedException e) {
        }
    }


    public void moveToLocation(Location end, double dense, double inTime){
        if(inTime == 0) {
            teleportParticle(end);
            return;
        }
        List<Vector> pos = line(getLocation().toVector(), end.toVector(), dense);
        new Thread(() -> {
            for(int i =0; i < pos.size(); i++){
                try{
                    Thread.sleep((long) ((inTime*1000)/pos.size()));
                }catch (Exception e){}
                teleportParticle(pos.get(i).toLocation(end.getWorld()));
            }
        }).start();
    }
    
    public Location getLocation(){
        return location.clone();
    }

    public Location getChickenLocation(){
        return location.clone().add(0, 1.5, 0);
    }
}
