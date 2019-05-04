package com.shojabon.shochicken;

import com.shojabon.shochicken.Tools.SMagic;
import com.shojabon.shochicken.Tools.SMagicLib;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
            new SMagic().circle(1, 20).rotateFunction(p.getLocation()).setDistanceAway(p.getLocation().getDirection(), 3).setBaseMargin(0, 1.5, 0).playParticle(p.getLocation(), EnumParticle.DRIP_WATER);
        }
        return false;
    }
}
