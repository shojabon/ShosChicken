package com.shojabon.shochicken.Tools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class SGodParticle {

    private Location location;
    private UUID armorStandUUID;
    private Entity armorStand;
    private Vector angle;

    SMagicLib magicLib = new SMagicLib();


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

        faceDirection(cache);
        teleportParticle(l);
    }

    public void teleportParticle(Location l){
        if(l.equals(getLocation())) return;
        location = l.clone();
        armorStand.teleport(new Location(l.getWorld(),l.getX(), l.getY(), l.getZ()));
    }

    public void faceDirection(Location l){
        angle = new Vector(l.getPitch(), l.getYaw(),0 );
        ((ArmorStand) armorStand).setHeadPose(new EulerAngle(Math.toRadians(angle.getX()), Math.toRadians(angle.getY()), 0));
    }

    public void faceLocation(Location l){
        Vector v = new Vector(l.getX() - getLocation().getX(),l.getY() - getLocation().getY(),l.getZ() - getLocation().getZ());
        faceDirection(l.clone().setDirection(v).clone());
    }

    public void moveToLocation(Location end, double dense, double inTime){
        if(inTime == 0) {
            teleportParticle(end);
            return;
        }
        ArrayList<Location> pos = magicLib.line(getLocation(), end, dense);
        new Thread(() -> {
            for(int i =0; i < pos.size(); i++){
                try{
                    Thread.sleep((long) ((inTime*1000)/pos.size()));
                }catch (Exception e){}
                teleportParticle(pos.get(i));
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
