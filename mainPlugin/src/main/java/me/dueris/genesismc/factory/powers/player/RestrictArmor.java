package me.dueris.genesismc.factory.powers.player;

import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import me.dueris.genesismc.utils.translation.LangConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

import static me.dueris.genesismc.utils.ArmorUtils.getArmorValue;

public class RestrictArmor extends CraftPower {

    private Long interval;
    private final int ticksE;

    public RestrictArmor() {
        this.interval = 1L;
        this.ticksE = 0;
    }

    public static boolean compareValues(double value1, String comparison, double value2) {
        switch (comparison) {
            case ">":
                return value1 > value2;
            case ">=":
                return value1 >= value2;
            case "<":
                return value1 < value2;
            case "<=":
                return value1 <= value2;
            case "==":
                return value1 == value2;
            case "=":
                return value1 == value2;
            case "!=":
                return value1 != value2;
            default:
                return false;
        }
    }

    @Override
    public void setActive(String tag, Boolean bool) {
        if (powers_active.containsKey(tag)) {
            powers_active.replace(tag, bool);
        } else {
            powers_active.put(tag, bool);
        }
    }

    /*

     */

    Player p;

    public void run(Player p, HashMap<Player, Integer> ticksEMap) {
        ticksEMap.putIfAbsent(p, 0);

        if (getPowerArray().contains(p)) {
            for (OriginContainer origin : OriginPlayer.getOrigin(p).values()) {
                for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                    if (power == null) continue;
                    if (power.getInterval() == null) {
                        Bukkit.getLogger().warning(LangConfig.getLocalizedString(p, "powers.errors.action_over_time"));
                        return;
                    }

                    interval = power.getInterval();
                    int ticksE = ticksEMap.getOrDefault(p, 0);
                    if (ticksE <= interval) {
                        ticksE++;
                        ticksEMap.put(p, ticksE);
                    } else {
                        ConditionExecutor executor = new ConditionExecutor();
                        if (executor.check("condition", "conditions", p, power, getPowerFile(), p, null, p.getLocation().getBlock(), null, p.getItemInHand(), null)) {
                            setActive(power.getTag(), true);
                            boolean headb = true;
                            boolean chestb = true;
                            boolean legsb = true;
                            boolean feetb = true;

                            if (power.getHead() == null) headb = false;
                            if (power.getChest() == null) chestb = false;
                            if (power.getLegs() == null) legsb = false;
                            if (power.getFeet() == null) feetb = false;

                            if (power.getHead().get("type").toString().equalsIgnoreCase("origins:armor_value")) {
                                String comparisonh = power.getHead().get("comparison").toString();
                                String comparisontoh = power.getHead().get("compare_to").toString();
                                if (!headb) return;
                                ItemStack item = p.getInventory().getHelmet();
                                if (item != null) {
                                    double armorValue = getArmorValue(item);
                                    double compareValue = Double.parseDouble(comparisontoh);
                                    if (compareValues(armorValue, comparisonh, compareValue)) {
                                        OriginPlayer.moveEquipmentInventory(p, EquipmentSlot.HEAD);
                                    }
                                }
                            } else if (power.getHead().get("type").toString().equalsIgnoreCase("origins:ingredient")) {
                                if (!headb) return;
                            }

                            if (power.getChest().get("type").toString().equalsIgnoreCase("origins:armor_value")) {
                                String comparisonc = power.getChest().get("comparison").toString();
                                String comparisontoc = power.getChest().get("compare_to").toString();
                                if (!chestb) return;
                                ItemStack item = p.getInventory().getChestplate();
                                if (item != null) {
                                    double armorValue = getArmorValue(item);
                                    double compareValue = Double.parseDouble(comparisontoc);
                                    if (compareValues(armorValue, comparisonc, compareValue)) {
                                        OriginPlayer.moveEquipmentInventory(p, EquipmentSlot.CHEST);
                                    }
                                }
                            } else if (power.getChest().get("type").toString().equalsIgnoreCase("origins:ingredient")) {
                                if (!chestb) return;
                            }

                            if (power.getLegs().get("type").toString().equalsIgnoreCase("origins:armor_value")) {
                                String comparisonl = power.getLegs().get("comparison").toString();
                                String comparisontol = power.getLegs().get("compare_to").toString();
                                if (!legsb) return;
                                ItemStack item = p.getInventory().getLeggings();
                                if (item != null) {
                                    double armorValue = getArmorValue(item);
                                    double compareValue = Double.parseDouble(comparisontol);
                                    if (compareValues(armorValue, comparisonl, compareValue)) {
                                        OriginPlayer.moveEquipmentInventory(p, EquipmentSlot.LEGS);
                                    }
                                }
                            } else if (power.getLegs().get("type").toString().equalsIgnoreCase("origins:ingredient")) {
                                if (!legsb) return;
                            }

                            if (power.getFeet().get("type").toString().equalsIgnoreCase("origins:armor_value")) {
                                String comparisonf = power.getFeet().get("comparison").toString();
                                String comparisontof = power.getFeet().get("compare_to").toString();
                                if (!feetb) return;
                                ItemStack item = p.getInventory().getBoots();
                                if (item != null) {
                                    double armorValue = getArmorValue(item);
                                    double compareValue = Double.parseDouble(comparisontof);
                                    if (compareValues(armorValue, comparisonf, compareValue)) {
                                        OriginPlayer.moveEquipmentInventory(p, EquipmentSlot.FEET);
                                    }
                                }
                            } else if (power.getFeet().get("type").toString().equalsIgnoreCase("origins:ingredient")) {
                                if (!feetb) return;
                            }
                        } else {
                            setActive(power.getTag(), false);
                        }
                        ticksEMap.put(p, 0);
                    }
                }
            }
        }
    }

    @Override
    public void run(Player p) {

    }

    @Override
    public String getPowerFile() {
        return "origins:restrict_armor";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return restrict_armor;
    }
}
