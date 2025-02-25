package me.dueris.genesismc.factory.powers.block;

import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class WaterBreathe extends CraftPower {
    public static ArrayList<Player> outofAIR = new ArrayList<>();

    public static boolean isInBreathableWater(Player player) {
        Block block = player.getEyeLocation().getBlock();
        Material material = block.getType();
        if (block.getType().equals(Material.WATER)) {
            return true;
        } else return player.isInWater() && !material.equals(Material.AIR);
    }

    @Override
    public void setActive(String tag, Boolean bool) {
        if (powers_active.containsKey(tag)) {
            powers_active.replace(tag, bool);
        } else {
            powers_active.put(tag, bool);
        }
    }

    Player p;

    public WaterBreathe() {
        this.p = p;
    }

    @Override
    public void run(Player p) {
        if (!getPowerArray().contains(p)) return;
        for (OriginContainer origin : OriginPlayer.getOrigin(p).values()) {
            ConditionExecutor conditionExecutor = new ConditionExecutor();
            for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                if (conditionExecutor.check("condition", "conditions", p, power, getPowerFile(), p, null, null, null, p.getItemInHand(), null)) {
                    setActive(power.getTag(), true);
                    if (water_breathing.contains(p)) {
                        if (isInBreathableWater(p)) {
                            if (p.getRemainingAir() < 290) {
                                p.setRemainingAir(p.getRemainingAir() + 7);
                            } else {
                                p.setRemainingAir(300);
                            }
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 3, 1, false, false, false));
                            outofAIR.remove(p);
                        } else {
                            if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR))
                                return;
                            int remainingAir = p.getRemainingAir();
                            if (remainingAir <= 5) {
                                p.setRemainingAir(-10);
                                outofAIR.add(p);
                            } else {
                                p.setRemainingAir(remainingAir - 5);
                                outofAIR.remove(p);
                            }
                        }
                        if (outofAIR.contains(p)) {
                            if (p.getRemainingAir() > 20) {
                                outofAIR.remove(p);
                            }
                        }
                    }
                } else {
                    setActive(power.getTag(), false);
                }
            }

        }
    }

    @Override
    public String getPowerFile() {
        return "origins:water_breathing";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return water_breathing;
    }

    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (outofAIR.contains(p)) {
                int remainingAir = p.getRemainingAir();
                if (remainingAir <= 5) {
                    p.damage(2);
                }
            }
        }
    }
}
