package me.dueris.genesismc.factory.powers.player.inventory;

import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

public class KeepInventory extends CraftPower implements Listener {

    @Override
    public void setActive(String tag, Boolean bool) {
        if (powers_active.containsKey(tag)) {
            powers_active.replace(tag, bool);
        } else {
            powers_active.put(tag, bool);
        }
    }


    @EventHandler
    public void keepinv(PlayerDeathEvent e) {
        Player player = e.getEntity();
        for (OriginContainer origin : OriginPlayer.getOrigin(player).values()) {
            if (keep_inventory.contains(player)) {
                ConditionExecutor conditionExecutor = new ConditionExecutor();
                for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                    if (conditionExecutor.check("item_condition", "item_conditions", player, power, "origins:keep_inventory", player, null, null, null, player.getInventory().getItemInHand(), null)) {
                        ArrayList<Long> slots = new ArrayList<>();
                        if (!getPowerArray().contains(player)) return;
                        setActive(power.getTag(), true);
                        if (power.getSlots() != null) {
                            for (long slot : power.getSlots()) {
                                slots.add(slot);
                            }
                        }

                        if (!slots.isEmpty()) {
                            for (int i = 0; i < player.getInventory().getSize(); i++) {
                                if (slots.contains((long) i)) {
                                    e.getItemsToKeep().add(player.getInventory().getItem(i));
                                }
                            }
                        } else {
                            e.setKeepInventory(true);
                        }
                    } else {
                        if (!getPowerArray().contains(player)) return;
                        setActive(power.getTag(), false);
                    }
                }
            }
        }
    }

    Player p;

    public KeepInventory() {
        this.p = p;
    }

    @Override
    public void run(Player p) {

    }

    @Override
    public String getPowerFile() {
        return "origins:keep_inventory";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return keep_inventory;
    }
}
