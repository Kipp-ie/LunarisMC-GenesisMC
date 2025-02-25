package me.dueris.genesismc.factory.powers.player;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import static me.dueris.genesismc.factory.powers.Power.pumpkin_hate;

public class PumpkinHate implements Listener {

    @EventHandler
    public void OnArmorChange(PlayerArmorChangeEvent e) {
        Player p = e.getPlayer();
        if (pumpkin_hate.contains(e.getPlayer())) {
            if (e.getNewItem() == null) return;
            if (e.getNewItem().getType() == Material.CARVED_PUMPKIN) {
                p.getInventory().setHelmet(new ItemStack(Material.AIR));
                p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.CARVED_PUMPKIN));
            }
        }
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (pumpkin_hate.contains(e.getPlayer())) {
            if (e.getItem().getType().equals(Material.PUMPKIN_PIE)) {
                p.getWorld().createExplosion(p.getLocation(), 0);
                p.setHealth(1);
                p.setFoodLevel(p.getFoodLevel() - 8);
            }
            if (e.getItem().getType().equals(Material.POTION)) {
                p.damage(2);
            }
        }

    }

}
