package me.dueris.genesismc.factory.powers.actions;

import me.dueris.genesismc.CooldownStuff;
import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.actions.Actions;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class SelfActionOnHit extends CraftPower implements Listener {
    Player p;

    public SelfActionOnHit() {
        this.p = p;
    }

    @Override
    public void run(Player p) {

    }

    @EventHandler
    public void s(EntityDamageByEntityEvent e) {
        Entity actor = e.getEntity();
        Entity target = e.getDamager();

        if (!(target instanceof Player player)) return;
        if (!getPowerArray().contains(target)) return;

        for (OriginContainer origin : OriginPlayer.getOrigin(player).values()) {
            ConditionExecutor executor = new ConditionExecutor();
            if (CooldownStuff.isPlayerInCooldown((Player) target, "key.attack")) return;

            for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                if (executor.check("condition", "conditions", (Player) target, power, getPowerFile(), actor, target, null, null, player.getInventory().getItemInHand(), e)) {
                    if (!getPowerArray().contains(target)) return;
                    setActive(power.getTag(), true);
                    Actions.EntityActionType(target, power.getEntityAction());
                    if (power.get("cooldown", "1") != null) {
                        CooldownStuff.addCooldown((Player) target, origin, power.getTag(), power.getType(), Integer.parseInt(power.get("cooldown", "1")), "key.attack");
                    }
                } else {
                    setActive(power.getTag(), false);
                }
            }
        }
    }

    @Override
    public String getPowerFile() {
        return "origins:self_action_on_hit";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return self_action_on_hit;
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
