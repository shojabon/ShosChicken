package com.shojabon.shochicken;

import com.shojabon.shochicken.Tools.SMagic;
import com.shojabon.shochicken.Tools.SMagicLib;
import com.shojabon.shochicken.Tools.SParticle.particles.SParticleCircle;
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
            new SParticleCircle(EnumParticle.VILLAGER_HAPPY, 1, 30).setDistanceAwayMargin(p.getLocation().getDirection(), 1).setReverse(true).setPerParticleDelay(50).setDirection(p.getLocation()).playParticle(p.getEyeLocation());
        }
        return false;
    }
}
