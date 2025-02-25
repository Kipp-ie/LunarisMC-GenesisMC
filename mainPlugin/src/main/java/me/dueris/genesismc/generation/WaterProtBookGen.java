package me.dueris.genesismc.generation;

import me.dueris.genesismc.events.WaterProtectionGenerateEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

import static me.dueris.genesismc.GenesisMC.waterProtectionEnchant;
import static org.bukkit.Bukkit.getServer;

public class WaterProtBookGen implements Listener {

    @EventHandler
    public void OnGen(LootGenerateEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (e.getInventoryHolder() != null) {
            if (e.getWorld().canGenerateStructures()) {
                WaterProtectionGenerateEvent event = new WaterProtectionGenerateEvent();
                getServer().getPluginManager().callEvent(event);
                Random random = new Random();
                int r = random.nextInt(1000);
                if (r <= 20) {

                    int l = random.nextInt(4);
                    if (l == 1) {
                        ItemStack enchbook = new ItemStack(Material.ENCHANTED_BOOK);
                        enchbook.addEnchantment(waterProtectionEnchant, 1);
                        enchbook.setLore(List.of(ChatColor.GRAY + "Water Protection I"));
                        e.getLoot().add(enchbook);
                    } else if (l == 2) {
                        ItemStack enchbook = new ItemStack(Material.ENCHANTED_BOOK);
                        enchbook.addEnchantment(waterProtectionEnchant, 1);
                        enchbook.setLore(List.of(ChatColor.GRAY + "Water Protection II"));
                        e.getLoot().add(enchbook);
                    } else if (l == 3) {
                        ItemStack enchbook = new ItemStack(Material.ENCHANTED_BOOK);
                        enchbook.addEnchantment(waterProtectionEnchant, 1);
                        enchbook.setLore(List.of(ChatColor.GRAY + "Water Protection III"));
                        e.getLoot().add(enchbook);
                    } else if (l == 4) {
                        ItemStack enchbook = new ItemStack(Material.ENCHANTED_BOOK);
                        enchbook.addEnchantment(waterProtectionEnchant, 1);
                        enchbook.setLore(List.of(ChatColor.GRAY + "Water Protection IV"));
                        e.getLoot().add(enchbook);
                    }

                }


            }
        }
    }

}
