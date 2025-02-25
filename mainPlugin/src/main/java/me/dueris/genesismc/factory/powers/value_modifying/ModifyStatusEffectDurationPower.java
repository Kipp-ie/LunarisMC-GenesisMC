package me.dueris.genesismc.factory.powers.value_modifying;

import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BinaryOperator;

import static me.dueris.genesismc.factory.powers.player.attributes.AttributeHandler.getOperationMappingsFloat;
import static me.dueris.genesismc.factory.powers.value_modifying.ValueModifyingSuperClass.modify_effect_duration;

public class ModifyStatusEffectDurationPower extends CraftPower implements Listener {

    @Override
    public void setActive(String tag, Boolean bool) {
        if (powers_active.containsKey(tag)) {
            powers_active.replace(tag, bool);
        } else {
            powers_active.put(tag, bool);
        }
    }


    @EventHandler
    public void runD(EntityPotionEffectEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (!modify_effect_duration.contains(p)) return;
            for (OriginContainer origin : OriginPlayer.getOrigin(p).values()) {
                ConditionExecutor executor = new ConditionExecutor();
                for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                    if (executor.check("condition", "conditions", p, power, getPowerFile(), p, null, p.getLocation().getBlock(), null, p.getItemInHand(), null)) {
                        setActive(power.getTag(), true);
                        if (power.get("status_effect", null) != null) {
                            if (e.getNewEffect().getType().equals(PotionEffectType.getByName(power.get("status_effect", null)))) {
                                PotionEffect effect = e.getNewEffect();
                                for (HashMap<String, Object> modifier : power.getPossibleModifiers("modifier", "modifiers")) {
                                    Float value = Float.valueOf(modifier.get("value").toString());
                                    String operation = modifier.get("operation").toString();
                                    BinaryOperator mathOperator = getOperationMappingsFloat().get(operation);
                                    if (mathOperator != null) {
                                        float result = (float) mathOperator.apply(effect.getDuration(), value);
                                        effect.withDuration(Math.toIntExact(Long.valueOf(String.valueOf(result))));
                                    }
                                }

                            }
                        } else {
                            for (PotionEffect effect : p.getActivePotionEffects()) {
                                for (HashMap<String, Object> modifier : power.getPossibleModifiers("modifier", "modifiers")) {
                                    Float value = Float.valueOf(modifier.get("value").toString());
                                    String operation = modifier.get("operation").toString();
                                    BinaryOperator mathOperator = getOperationMappingsFloat().get(operation);
                                    if (mathOperator != null) {
                                        float result = (float) mathOperator.apply(effect.getDuration(), value);
                                        effect.withDuration(Math.toIntExact(Long.valueOf(String.valueOf(result))));
                                    }
                                }
                            }
                        }
                    } else {
                        setActive(power.getTag(), false);
                    }
                }

            }
        }
    }

    Player p;

    public ModifyStatusEffectDurationPower() {
        this.p = p;
    }

    @Override
    public void run(Player p) {

    }

    @Override
    public String getPowerFile() {
        return "origins:modify_status_effect_duration";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return modify_effect_duration;
    }
}
