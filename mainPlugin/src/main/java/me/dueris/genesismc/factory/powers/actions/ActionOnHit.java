package me.dueris.genesismc.factory.powers.actions;

import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.actions.Actions;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ActionOnHit extends CraftPower {
    Player p;

    public ActionOnHit() {
        this.p = p;
    }

    @Override
    public void run(Player p) {

    }

    @EventHandler
    public void action(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p) {
            Entity actor = e.getDamager();
            Entity target = p;
            for (OriginContainer origin : OriginPlayer.getOrigin(p).values()) {
                if (getPowerArray().contains(p)) {
                    for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                        if (power == null) continue;


                        setActive(power.getTag(), true);
                        Actions.biEntityActionType(actor, target, power.getBiEntityAction());
                        //todo: bientity condition and damage condition
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (power == null) {
                                    getPowerArray().remove(p);
                                    return;
                                }
                                if (!getPowerArray().contains(p)) return;
                                setActive(power.getTag(), false);
                            }
                        }.runTaskLater(GenesisMC.getPlugin(), 2L);
                    }
                }
            }
        }
    }

    @Override
    public String getPowerFile() {
        return "origins:action_on_hit";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return action_on_hit;
    }

    @Override
    public void setActive(String tag, Boolean bool) {
        if (powers_active.containsKey(tag)) {
            powers_active.replace(tag, bool);
        } else {
            powers_active.put(tag, bool);
        }
    }
}
