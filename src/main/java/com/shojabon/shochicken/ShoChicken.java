package com.shojabon.shochicken;

import com.shojabon.shochicken.Tools.SMagicLib;
import com.shojabon.shochicken.Tools.SParticle.SParticle;
import com.shojabon.shochicken.Tools.SParticle.interfaces.SParticleForm;
import com.shojabon.shochicken.Tools.SParticle.particles.SParticleCircle;
import com.shojabon.shochicken.Tools.SParticle.particles.SParticleFixedThickBeam;
import com.shojabon.shochicken.Tools.SParticle.particles.SParticleThickBeam;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Bukkit;
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
//            new SParticleCircle(EnumParticle.VILLAGER_HAPPY, 0.5, 20)
//                    .setDirection(SParticleForm.getDirectionTowards(p.getEyeLocation().toVector(), new Vector(500,120,-1200)))
//                    .setDistanceAwayMargin(p.getLocation().getDirection(), 1)
//                    .playParticle(p.getEyeLocation());
            new SParticleFixedThickBeam(new SParticleCircle(EnumParticle.VILLAGER_HAPPY, 0.5, 20).setPerParticleDelay(100), new Vector(500,120,-1200), 1).playParticle(p.getLocation());
        }
        return false;
    }
}
