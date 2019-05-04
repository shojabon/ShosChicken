package com.shojabon.shochicken;

import com.shojabon.shochicken.Tools.SMagic;
import com.shojabon.shochicken.Tools.SMagicLib;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class ShoChicken extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = ((Player)sender);
        SMagicLib lib = new SMagicLib();
        if(command.getName().equalsIgnoreCase("test")){
            SMagic circle1 = new SMagic().circle(0.3, 20);
            SMagic circle2 = new SMagic().circle(0.6, 30).setBaseMargin(0,0, 1);
            SMagic circle3 = new SMagic().circle(1, 40).setBaseMargin(0,0,2);
            SMagic shootOut = new SMagic().addSMagic(circle1).addSMagic(circle2).addSMagic(circle3);
            shootOut.rotateFunction(p.getLocation());
            shootOut.setDistanceAway(p.getLocation().getDirection(), 1);
            shootOut.playParticle(p.getEyeLocation(), EnumParticle.DRIP_WATER);
        }
        return false;
    }
}
