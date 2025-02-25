package me.dueris.genesismc.entity;

import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.enums.OriginDataType;
import me.dueris.genesismc.events.OriginChooseEvent;
import me.dueris.genesismc.events.PowerAssignEvent;
import me.dueris.genesismc.events.PowerUnassignEvent;
import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.files.GenesisDataFiles;
import me.dueris.genesismc.utils.LayerContainer;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import me.dueris.genesismc.utils.SendCharts;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class OriginPlayer {

//    public static boolean hasChosenOrigin(Player player) {
//        return !OriginPlayer.getOrigin(player).getTag().equalsIgnoreCase("");
//    }

    public static void removeArmor(Player player, EquipmentSlot slot) {
        ItemStack armor = player.getInventory().getItem(slot);

        if (armor != null && armor.getType() != Material.AIR) {
            // Remove the armor from the player's equipped slot
            player.getInventory().setItem(slot, new ItemStack(Material.AIR));

            // Add the armor to the player's inventory
            HashMap<Integer, ItemStack> excess = player.getInventory().addItem(armor);

            // If there is excess armor that couldn't fit in the inventory, drop it
            for (ItemStack item : excess.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }
    }

    public static void moveEquipmentInventory(Player player, EquipmentSlot equipmentSlot) {
        ItemStack item = player.getInventory().getItem(equipmentSlot);

        if (item != null && item.getType() != Material.AIR) {
            // Find an empty slot in the player's inventory
            int emptySlot = player.getInventory().firstEmpty();

            if (emptySlot != -1) {
                // Set the equipment slot to empty
                player.getInventory().setItem(equipmentSlot, null);

                // Move the item to the empty slot
                player.getInventory().setItem(emptySlot, item);
            }
        }
    }

    public static void launchElytra(Player player, float speed) {
        Location location = player.getEyeLocation();
        @NotNull Vector direction = location.getDirection().normalize();
        Vector velocity = direction.multiply(speed);
        player.setVelocity(velocity);
    }

    /**
     * @param originTag The tag of the origin.
     * @return true if the player has the origin.
     */
    public static boolean hasOrigin(Player player, String originTag) {
        HashMap<LayerContainer, OriginContainer> origins = CraftApoli.toOrigin(player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "originLayer"), PersistentDataType.STRING));
        for (OriginContainer origin : origins.values()) if (origin.getTag().equals(originTag)) return true;
        return false;
    }

    /**
     * @param layer The layer the origin is in
     * @return The OriginContainer for the specified layer
     */

    public static OriginContainer getOrigin(Player player, LayerContainer layer) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (data.get(new NamespacedKey(GenesisMC.getPlugin(), "originLayer"), PersistentDataType.STRING) == null) {
            setOrigin(player, layer, CraftApoli.nullOrigin());
            return CraftApoli.nullOrigin();
        }
        return CraftApoli.toOrigin(data.get(new NamespacedKey(GenesisMC.getPlugin(), "originLayer"), PersistentDataType.STRING), layer);
    }

    /**
     * @return A HashMap of layers and OriginContainer that the player has.
     */

    public static HashMap<LayerContainer, OriginContainer> getOrigin(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if (data.get(new NamespacedKey(GenesisMC.getPlugin(), "originLayer"), PersistentDataType.STRING) == null) {
            ArrayList<LayerContainer> layers = CraftApoli.getLayers();
            for (LayerContainer layer : layers) {
                setOrigin(player, layer, CraftApoli.nullOrigin());
                return new HashMap<>(Map.of(layer, CraftApoli.nullOrigin()));
            }
        }
        return CraftApoli.toOrigin(data.get(new NamespacedKey(GenesisMC.getPlugin(), "originLayer"), PersistentDataType.STRING));
    }

    public static boolean hasCoreOrigin(Player player, LayerContainer layer) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        String originTag = OriginPlayer.getOrigin(player, layer).getTag();
        if (originTag.contains("origins:human")) {
            return true;
        } else if (originTag.contains("origins:enderian")) {
            return true;
        } else if (originTag.contains("origins:merling")) {
            return true;
        } else if (originTag.contains("origins:phantom")) {
            return true;
        } else if (originTag.contains("origins:elytrian")) {
            return true;
        } else if (originTag.contains("origins:blazeborn")) {
            return true;
        } else if (originTag.contains("origins:avian")) {
            return true;
        } else if (originTag.contains("origins:arachnid")) {
            return true;
        } else if (originTag.contains("origins:shulk")) {
            return true;
        } else if (originTag.contains("origins:feline")) {
            return true;
        } else if (originTag.contains("origins:starborne")) {
            return true;
        } else if (originTag.contains("origins:allay")) {
            return true;
        } else if (originTag.contains("origins:rabbit")) {
            return true;
        } else if (originTag.contains("origins:bee")) {
            return true;
        } else if (originTag.contains("origins:sculkling")) {
            return true;
        } else if (originTag.contains("origins:creep")) {
            return true;
        } else if (originTag.contains("origins:slimeling")) {
            return true;
        } else return originTag.contains("origins:piglin");
    }

    public static void setOrigin(Player player, LayerContainer layer, OriginContainer origin) {
        NamespacedKey key = new NamespacedKey(GenesisMC.getPlugin(), "originLayer");
        HashMap<LayerContainer, OriginContainer> origins = CraftApoli.toOrigin(player.getPersistentDataContainer().get(key, PersistentDataType.STRING));
        if (!CraftApoli.getLayers().contains(layer)) {
            return;
        }

        if (!origin.getTag().equals(CraftApoli.nullOrigin().getTag())) unassignPowers(player, layer);
        for (LayerContainer layers : origins.keySet()) {
            if (layer.getTag().equals(layers.getTag())) origins.replace(layers, origin);
        }
        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, CraftApoli.toSaveFormat(origins));

        String originTag = origin.getTag();
        if (!originTag.equals(CraftApoli.nullOrigin().getTag())) SendCharts.originPopularity(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    assignPowers(player, layer);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskLater(GenesisMC.getPlugin(), 3L);
    }

    /**
     * WARNING: will remove the layer containing the origin from the playerdata. If you need to make a player re choose an origin use setOrigin and pass in CraftApoli.nullOrigin().
     *
     * @param player player.
     * @param layer  the layer to remove from playerdata.
     */
    public static void removeOrigin(Player player, LayerContainer layer) {
        HashMap<LayerContainer, OriginContainer> origins = getOrigin(player);
        ArrayList<LayerContainer> layers = new ArrayList<>(origins.keySet());
        for (LayerContainer playerLayer : layers) {
            if (playerLayer.getTag().equals(layer.getTag())) origins.remove(playerLayer);
        }
        player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "originLayer"), PersistentDataType.STRING, CraftApoli.toSaveFormat(origins));
    }

    public static LayerContainer getLayer(Player p, OriginContainer origin) {
        HashMap<LayerContainer, OriginContainer> origins = getOrigin(p);
        for (LayerContainer layer : origins.keySet()) {
            if (origins.get(layer).getTag().equals(origin.getTag())) return layer;
        }
        return null;
    }

    public static void resetOriginData(Player player, OriginDataType type) {
        if (type.equals(OriginDataType.CAN_EXPLODE)) {
            player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "can-explode"), PersistentDataType.INTEGER, 1);
        } else if (type.equals(OriginDataType.SHULKER_BOX_DATA)) {
            player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "shulker-box"), PersistentDataType.STRING, "");
        } else if (type.equals(OriginDataType.TOGGLE)) {
            player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "toggle"), PersistentDataType.INTEGER, 1);
        } else if (type.equals(OriginDataType.IN_PHASING_FORM)) {
            player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "in-phantomform"), PersistentDataType.BOOLEAN, false);
        }

    }

    public static void setOriginData(Player player, OriginDataType type, int value) {
        if (type.equals(OriginDataType.CAN_EXPLODE)) {
            player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "can-explode"), PersistentDataType.INTEGER, value);
        } else if (type.equals(OriginDataType.TOGGLE)) {
            player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "toggle"), PersistentDataType.INTEGER, value);
        }
    }

    public static void setOriginData(Player player, OriginDataType type, boolean value) {
        if (type.equals(OriginDataType.IN_PHASING_FORM)) {
            player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "in-phantomform"), PersistentDataType.BOOLEAN, value);
        }
    }

    public static void triggerChooseEvent(Player player) {
        OriginChooseEvent chooseEvent = new OriginChooseEvent(player);
        getServer().getPluginManager().callEvent(chooseEvent);
    }

    public static boolean isInPhantomForm(Player player) {
        return player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "in-phantomform"), PersistentDataType.BOOLEAN);
    }

    public static void assignPowers(Player player) {
        if (player == null) Bukkit.getServer().getConsoleSender().sendMessage("urm the player is null?!");
        HashMap<LayerContainer, OriginContainer> origins = getOrigin(player);
        for (LayerContainer layer : origins.keySet()) {
            try {
                assignPowers(player, layer);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static HashMap<Player, ArrayList<Class<? extends CraftPower>>> powersAppliedList = new HashMap<>();
    public static ArrayList<Player> hasPowers = new ArrayList<>();

    public static List<Class<? extends CraftPower>> getPowersApplied(Player p) {
        List<Class<? extends CraftPower>> array = new ArrayList<>();
        for (Player plc : powersAppliedList.keySet()) {
            if (plc.equals(p)) {
                for (Class<? extends CraftPower> c : powersAppliedList.get(plc)) {
                    array.add(c);
                }

            }
        }
        return array;
    }

    public static void assignPowers(Player player, LayerContainer layer) throws InstantiationException, IllegalAccessException {
        OriginContainer origin = getOrigin(player, layer);
        ArrayList<String> powerAppliedTypes = new ArrayList<>();
        ArrayList<Class<? extends CraftPower>> powerAppliedClasses = new ArrayList<>();
        if (player == null) Bukkit.getServer().getConsoleSender().sendMessage("rip player null");
        if (origin.getPowerContainers().isEmpty()) {
            player.sendMessage("BRO ITS EMPTY WAHT");
        }
        for (PowerContainer power : origin.getPowerContainers()) {
            for (Class<? extends CraftPower> c : CraftPower.getRegistered()) {
                CraftPower craftPower = null;
                try {
                    craftPower = c.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (power.getType().equals(craftPower.getPowerFile())) {
                    craftPower.getPowerArray().add(player);
                    if (!powersAppliedList.containsKey(player)) {
                        ArrayList lst = new ArrayList<>();
                        lst.add(c);
                        powerAppliedTypes.add(c.newInstance().getPowerFile());
                        powerAppliedClasses.add(c);
                        powersAppliedList.put(player, lst);
                    } else {
                        powersAppliedList.get(player).add(c);
                    }
                    Bukkit.getConsoleSender().sendMessage("Assigned power[" + craftPower.getPowerFile() + "] to player " + player.getName());
                }
            }
        }

        PowerAssignEvent powerAssignEvent = new PowerAssignEvent(player, powerAppliedClasses, powerAppliedTypes, origin);
        Bukkit.getServer().getPluginManager().callEvent(powerAssignEvent);

        hasPowers.add(player);
    }

    public static void unassignPowers(Player player) {
        HashMap<LayerContainer, OriginContainer> origins = getOrigin(player);
        for (LayerContainer layer : origins.keySet()) {
            unassignPowers(player, layer);
        }
    }

    public static void unassignPowers(Player player, LayerContainer layer) {
        OriginContainer origin = getOrigin(player, layer);
        ArrayList<String> powerRemovedTypes = new ArrayList<>();
        ArrayList<Class<? extends CraftPower>> powerRemovedClasses = new ArrayList<>();
        for (PowerContainer power : origin.getPowerContainers()) {
            for (Class<? extends CraftPower> c : CraftPower.getRegistered()) {
                CraftPower craftPower = null;
                try {
                    craftPower = c.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (power.getType().equals(craftPower.getPowerFile())) {
                    craftPower.getPowerArray().remove(player);
                    try {
                        powerRemovedTypes.add(c.newInstance().getPowerFile());
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    powerRemovedClasses.add(c);
                    if (GenesisDataFiles.getMainConfig().getString("console-startup-debug").equalsIgnoreCase("true")) {
                        Bukkit.getConsoleSender().sendMessage("Removed power[" + craftPower.getPowerFile() + "] to player " + player.getName());
                    }
                }
            }
        }
        for (Class<? extends CraftPower> classes : getPowersApplied(player)) {
            powersAppliedList.get(player).remove(classes);
        }
        PowerUnassignEvent powerUnassignEvent = new PowerUnassignEvent(player, powerRemovedClasses, powerRemovedTypes, origin);
        Bukkit.getServer().getPluginManager().callEvent(powerUnassignEvent);
        hasPowers.remove(player);
    }

    /**
     * @param p Player
     * @return The layers and origins currently assigned to the player
     */
    public static HashMap<LayerContainer, OriginContainer> returnOrigins(Player p) {
        return CraftApoli.toOrigin(p.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "originLayer"), PersistentDataType.STRING));
    }

}
