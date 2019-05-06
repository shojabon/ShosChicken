package com.shojabon.shochicken;

import com.shojabon.shochicken.SMagicSpells.Spells.BeamAttack;
import com.shojabon.shochicken.Tools.SParticle.SGodParticle;
import com.shojabon.shochicken.Tools.SParticle.SParticle;
import com.shojabon.shochicken.Tools.SParticle.interfaces.SParticleForm;
import com.shojabon.shochicken.Tools.SParticle.particles.SParticleCircle;
import com.shojabon.shochicken.Tools.SParticle.particles.SParticleFixedThickBeam;
import com.shojabon.shochicken.Tools.SParticle.particles.SParticleThickBeam;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Collection;

public final class ShoChicken extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    SGodParticle particle;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = ((Player)sender);
        if(command.getName().equalsIgnoreCase("test")){
            new BeamAttack(5, p.getEyeLocation(), p.getLocation().getDirection().normalize().multiply(100).toLocation(p.getLocation().getWorld()).add(p.getEyeLocation()), EnumParticle.DRIP_WATER).setIgnoreEntity(p).play();
        }
        return false;
    }
}
