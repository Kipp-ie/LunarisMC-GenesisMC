package me.dueris.genesismc.factory.powers.actions;

import me.dueris.genesismc.factory.powers.CraftPower;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class ActionOnItemUse extends CraftPower implements Listener {

    public ActionOnItemUse() {

    }

    @Override
    public void run(Player p) {

    }

    @EventHandler
    public void entityRightClick(PlayerInteractEvent e) {
        Player actor = e.getPlayer();
//
//        for (OriginContainer origin : OriginPlayer.getOrigin(actor).values()) {
//            PowerContainer power = power;
//            if (power == null) continue;
//            if (e.getItem() == null) return;
//            if (e.getClickedBlock() == null) return;
//            if (!getPowerArray().contains(e.getPlayer())) return;
//            setActive(power.getTag(), true);
//            ConditionExecutor conditionExecutor = new ConditionExecutor();
//            if(conditionExecutor.check("item_condition", "item_conditions", actor, origin, getPowerFile(), actor, null, actor.getLocation().getBlock(), null, actor.getInventory().getItemInHand(), null)){
//                if(conditionExecutor.check("condition", "conditions", actor, origin, getPowerFile(), actor, null, actor.getLocation().getBlock(), null, actor.getInventory().getItemInHand(), null)){
//                    if(conditionExecutor.check("entity_condition", "entity_conditions", actor, origin, getPowerFile(), actor, null, actor.getLocation().getBlock(), null, actor.getInventory().getItemInHand(), null)){
//                        Actions.EntityActionType(actor, power.getEntityAction());
//                        Actions.ItemActionType(actor.getInventory().getItemInMainHand(), power.getItemAction());
//                        new BukkitRunnable() {
//                            @Override
//                            public void run() {
//                                if (!getPowerArray().contains(e.getPlayer())) return;
//                                setActive(power.getTag(), false);
//                            }
//                        }.runTaskLater(GenesisMC.getPlugin(), 2L);
//                    }
//                }
//            }
//
//        }
        //TODO: PATCH THIS FOR POWER origins:damage_from_potions

//        if (e.getHand() == EquipmentSlot.HAND) System.out.println("main");
//        if (e.getHand() == EquipmentSlot.OFF_HAND) System.out.println("off");
    }

    @Override
    public String getPowerFile() {
        return "origins:action_on_item_use";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return action_on_item_use;
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
