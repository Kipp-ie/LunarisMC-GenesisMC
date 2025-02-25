package me.dueris.genesismc.utils;

import me.dueris.genesismc.factory.powers.CraftPower;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PowerContainer implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    String powerTag;
    FileContainer powerFile;
    String powerSource;

    /**
     * @param powerTag    The power tag.
     * @param powerFile   The data within a power file.
     * @param powerSource What applied the power to the origin.
     */
    public PowerContainer(String powerTag, FileContainer powerFile, String powerSource) {
        this.powerTag = powerTag;
        this.powerFile = powerFile;
        this.powerSource = powerSource;
    }

    public FileContainer getPowerFile() {
        return powerFile;
    }

    /**
     * @return The power tag.
     */
    public String getTag() {
        return this.powerTag;
    }

//    /**
//     * @return The power file.
//     */
//    public FileContainer getFile() {
//        return this.powerFile;
//    }

    /**
     * @return The source of the power.
     */
    public String getSource() {
        return this.powerSource;
    }


    /**
     * @return The powerContainer formatted for debugging, not to be used in other circumstances.
     */
    @Override
    public String toString() {
        return "powerTag: " + this.powerTag + ", PowerFile: " + this.powerFile.toString() + ", PowerSource: " + this.powerSource;
    }

    public ArrayList<Long> getSlots() {
        ArrayList<Long> slots = (ArrayList<Long>) this.powerFile.get("slots");
        if (slots == null) return new ArrayList<>();
        return slots;
    }

    /**
     * @return The name of the power. Will return "No Name" if there is no power name present.
     */
    public String getName() {
        Object name = this.powerFile.get("name");
        if (name == null) return "No Name";
        return (String) name;
    }

    /**
     * Changes the name of the power.
     */
    public void setName(String newName) {
        this.powerFile.replace("name", newName);
    }

    /**
     * @return The description of the power. Will return "No Description" if there is no description present.
     */
    public String getDescription() {
        Object description = this.powerFile.get("description");
        if (description == null) return "No Description.";
        return (String) description;
    }

    /**
     * Changes the description of the power.
     */
    public void setDescription(String newDescription) {
        this.powerFile.replace("description", newDescription);
    }

    /**
     * @return Whether the power should be displayed. Will return false if "hidden" is not present.
     */
    public Boolean getHidden() {
        Object hidden = powerFile.get("hidden");
        if (hidden == null) return true;
        return (Boolean) hidden;
    }

    /**
     * @return The type from the power file. Will return "" if there is no type present.
     */
    public String getType() {
        Object type = powerFile.get("type");
        if (type == null) return "";
        return (String) type;
    }

    public Class<? extends CraftPower> getCraftPowerClass() {
        for (Class<? extends CraftPower> c : CraftPower.getRegistered()) {
            try {
                if (c.newInstance().getPowerFile().equalsIgnoreCase(getType())) return c;
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * @return Whether the elytra should be displayed. Will return false if "render_elytra" is not present.
     */
    public Boolean getShouldRender() {
        Object render = powerFile.get("render_elytra");
        if (render == null) return true;
        return (Boolean) render;
    }

    public Boolean getOverlay() {
        Object render = powerFile.get("overlay");
        if (render == null) return false;
        return (Boolean) render;
    }

    /**
     * @return The value "strength" from the power file
     */
    public Double getStrength() {
        Object render = powerFile.get("strength");
        if (render == null) return 1.3;
        return (double) render;
    }

    public String getModelRenderType() {
        Object type = powerFile.get("render_type");
        if (type == null) return "original";
        return (String) type;
    }

    /**
     * @return The value "interval" from the power file
     */
    public Long getInterval() {
        Object render = powerFile.get("interval");
        if (render == null) return 5L;
        return (long) render;
    }

    public boolean getDropOnDeath() {
        Object render = powerFile.get("drop_on_death");
        if (render == null) return false;
        return (Boolean) render;
    }

    public Double getColor(String thing) {
        Object color = powerFile.get(thing);
        if (color == null) return 0.0;
        return (Double) color;
    }

    /**
     * @return The value "burn_duration" from the power file
     */
    public Long getBurnDuration() {
        Object render = powerFile.get("burn_duration");
        if (render == null) return 100L;
        return (long) render;
    }

    /**
     * @return String value of render_type for power origins:phasing
     */
    public String getRenderType() {
        Object type = powerFile.get("render_type");
        if (type == null) return "blindness";
        return type.toString();
    }

    public String getEffect() {
        Object type = powerFile.get("effect");
        if (type == null) return "blindness";
        return type.toString();
    }

    /**
     * @return LONG view_distance value for power origins:phasing
     */
    public Long getViewDistance() {
        Object distance = powerFile.get("view_distance");
        if (distance == null) return 10L;
        return (Long) distance;
    }

    /**
     * @return The value "tick_rate" from the power file
     */
    public Long getTickRate() {
        Object render = powerFile.get("tick_rate");
        if (render == null) return 10L;
        return (long) render;
    }

    /**
     * @return Should the climbing power be canceled in the rain or not
     */
    public boolean getRainCancel() {
        Object render = powerFile.get("rain_cancel");
        if (render == null) return false;
        return (boolean) render;
    }

    public boolean isInverted() {
        Object render = powerFile.get("inverted");
        if (render == null) return false;
        return (boolean) render;
    }

    public String get(String thing, String defaultValue) {
        Object type = powerFile.get(thing);
        if (type == null) {
            return defaultValue;
        }
        return type.toString();
    }

    public String get(String thing) {
        Object type = powerFile.get(thing);
        if (type == null) {
            return null;
        }
        return type.toString();
    }

    public Object getObject(String thing) {
        Object type = powerFile.get(thing);
        return type;
    }

    public HashMap<String, Object> getJsonHashMap(String thing) {
        Object obj = powerFile.get(thing);
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public List<String> getPatternLine() {
        List<String> patternLines = new ArrayList<>();

        Object obj = powerFile.get("recipe");
        if (obj instanceof JSONObject recipeObject) {
            Object patternObj = recipeObject.get("pattern");

            if (patternObj instanceof JSONArray patternArray) {
                for (Object lineObj : patternArray) {
                    if (lineObj instanceof String line) {
                        patternLines.add(line);
                    }
                }
            }
        }

        return patternLines;
    }

    /**
     * @return Modifiers in the power file or null if not found
     */
    public List<HashMap<String, Object>> getPossibleModifiers(String singular, String plural) {
        Object obj = powerFile.get(singular);
        if (obj == null) {
            obj = powerFile.get(plural);
        }

        List<HashMap<String, Object>> result = new ArrayList<>();

        if (obj instanceof JSONArray jsonArray) {
            for (Object item : jsonArray) {
                if (item instanceof JSONObject jsonObject) {
                    HashMap<String, Object> itemMap = new HashMap<>();
                    for (Object innerKey : jsonObject.keySet()) {
                        String string_key = (String) innerKey;
                        Object value = jsonObject.get(string_key);
                        itemMap.put(string_key, value);
                    }
                    result.add(itemMap);
                }
            }
        } else if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            HashMap<String, Object> itemMap = new HashMap<>();
            for (Object innerKey : jsonObject.keySet()) {
                String string_key = (String) innerKey;
                Object value = jsonObject.get(string_key);
                itemMap.put(string_key, value);
            }
            result.add(itemMap);
        }
        return result;
    }

    public HashMap<String, Object> getSpread() {
        Object obj = powerFile.get("spread");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public HashMap<String, Object> getRecipe() {
        Object obj = powerFile.get("recipe");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public List<String> getJsonArray(String thing) {
        Object obj = powerFile.get(thing);
        if (obj == null) return new ArrayList<>();

        List<String> effectStrings = new ArrayList<>();

        if (obj instanceof JSONArray array) {
            for (Object value : array) {
                if (value instanceof String) {
                    effectStrings.add((String) value);
                }
            }
        } else if (obj instanceof String) {
            effectStrings.add((String) obj);
        }

        return effectStrings;
    }

    public HashMap<String, Object> getRecipeResult() {
        Object obj = powerFile.get("recipe");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject damageCondition) {
            Object entityConditionObj = damageCondition.get("result");
            if (entityConditionObj instanceof JSONObject entityCondition) {
                HashMap<String, Object> result = new HashMap<>();
                for (Object key : entityCondition.keySet()) {
                    String stringKey = (String) key;
                    Object value = entityCondition.get(stringKey);
                    result.put(stringKey, value);
                }
                return result;
            }
        }

        return new HashMap<>();
    }

    public List<String> getRecipeIngredients() {
        Object obj = powerFile.get("recipe");
        List<String> ingredientsList = new ArrayList<>();

        if (obj instanceof JSONObject recipeObject) {
            Object ingredientsObj = recipeObject.get("ingredients");

            if (ingredientsObj instanceof JSONArray ingredientsArray) {
                for (Object ingredient : ingredientsArray) {
                    if (ingredient instanceof JSONObject singleIngredient) {
                        if (singleIngredient.containsKey("item")) {
                            String item = singleIngredient.get("item").toString();
                            if (!item.isEmpty()) {
                                ingredientsList.add(item);
                            }
                        }
                    } else if (ingredient instanceof JSONArray nestedIngredientsArray) {
                        for (Object nestedIngredient : nestedIngredientsArray) {
                            if (nestedIngredient instanceof JSONObject nestedSingleIngredient) {
                                if (nestedSingleIngredient.containsKey("item")) {
                                    String nestedItem = nestedSingleIngredient.get("item").toString();
                                    if (!nestedItem.isEmpty()) {
                                        ingredientsList.add(nestedItem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return ingredientsList;
    }

    public List<String> getEffects() {
        Object obj = powerFile.get("effects");
        if (obj == null) return new ArrayList<>();

        List<String> effectStrings = new ArrayList<>();

        if (obj instanceof JSONArray array) {
            for (Object value : array) {
                if (value instanceof String) {
                    effectStrings.add((String) value);
                }
            }
        } else if (obj instanceof String) {
            effectStrings.add((String) obj);
        }

        return effectStrings;
    }

    public List<HashMap<String, Object>> getEffectData() {
        List<HashMap<String, Object>> result = new ArrayList<>();

        Object obj = powerFile.get("effect");
        if (obj == null) {
            obj = powerFile.get("effects");
        }

        if (obj == null) return result;

        if (obj instanceof JSONArray effects) {
            for (Object effect : effects) {
                if (effect instanceof JSONObject effectObj) {
                    HashMap<String, Object> effectData = new HashMap<>();
                    for (Object key : effectObj.keySet()) {
                        String stringKey = (String) key;
                        Object value = effectObj.get(stringKey);
                        effectData.put(stringKey, value);
                    }
                    result.add(effectData);
                }
            }
        } else if (obj instanceof JSONObject effectObj) {
            HashMap<String, Object> effectData = new HashMap<>();
            for (Object key : effectObj.keySet()) {
                String stringKey = (String) key;
                Object value = effectObj.get(stringKey);
                effectData.put(stringKey, value);
            }
            result.add(effectData);
        }

        return result;
    }

    /**
     * @return Head value in the power file or null if not found
     */
    public HashMap<String, Object> getHead() {
        Object obj = powerFile.get("head");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    /**
     * @return Chest value in the power file or null if not found
     */
    public HashMap<String, Object> getChest() {
        Object obj = powerFile.get("chest");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    /**
     * @return Legs value in the power file or null if not found
     */
    public HashMap<String, Object> getLegs() {
        Object obj = powerFile.get("legs");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    /**
     * @return Feet value in the power file or null if not found
     */
    public HashMap<String, Object> getFeet() {
        Object obj = powerFile.get("feet");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public HashMap<String, Object> getKey() {
        Object obj = powerFile.get("key");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    /**
     * Checks the powerfile for the "condition" tag
     *
     * @return Conditions in the power file or null if not found
     */
    public List<HashMap<String, Object>> getConditionFromString(String singular, String plural) {
        Object obj = powerFile.get(singular);
        if (obj == null) {
            obj = powerFile.get(plural);
        }

        List<HashMap<String, Object>> result = new ArrayList<>();

        if (obj instanceof JSONArray jsonArray) {
            for (Object item : jsonArray) {
                if (item instanceof JSONObject jsonObject) {
                    HashMap<String, Object> itemMap = new HashMap<>();
                    for (Object innerKey : jsonObject.keySet()) {
                        String string_key = (String) innerKey;
                        Object value = jsonObject.get(string_key);
                        itemMap.put(string_key, value);
                    }
                    result.add(itemMap);
                }
            }
        } else if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            HashMap<String, Object> itemMap = new HashMap<>();
            for (Object innerKey : jsonObject.keySet()) {
                String string_key = (String) innerKey;
                Object value = jsonObject.get(string_key);
                itemMap.put(string_key, value);
            }
            result.add(itemMap);
        }
        return result;
    }

    public List<HashMap<String, Object>> getSingularAndPlural(String singular, String plural) {
        Object obj = powerFile.get(singular);
        if (obj == null) {
            obj = powerFile.get(plural);
        }

        List<HashMap<String, Object>> result = new ArrayList<>();

        if (obj instanceof JSONArray jsonArray) {
            for (Object item : jsonArray) {
                if (item instanceof JSONObject jsonObject) {
                    HashMap<String, Object> itemMap = new HashMap<>();
                    for (Object innerKey : jsonObject.keySet()) {
                        String stringKey = (String) innerKey;
                        Object value = jsonObject.get(stringKey);

                        if (value instanceof JSONObject && jsonObject.containsKey(stringKey)) {
                            // Handle text components here
                            String textValue = (String) jsonObject.get(stringKey);
                            Object textComponent = textValue;
                            itemMap.put(stringKey, textComponent);
                        } else {
                            itemMap.put(stringKey, value);
                        }
                    }
                    result.add(itemMap);
                }
            }
        } else if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            HashMap<String, Object> itemMap = new HashMap<>();
            for (Object innerKey : jsonObject.keySet()) {
                String stringKey = (String) innerKey;
                Object value = jsonObject.get(stringKey);

                if (value instanceof JSONObject && jsonObject.containsKey(stringKey)) {
                    String textValue = (String) jsonObject.get(stringKey);
                    Object textComponent = textValue;
                    itemMap.put(stringKey, textComponent);
                } else {
                    itemMap.put(stringKey, value);
                }
            }
            result.add(itemMap);
        }
        return result;
    }

    public HashMap<String, Object> getCondition() {
        Object obj = powerFile.get("condition");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    /**
     * Checks the powerfile for the "condition" tag
     *
     * @return Conditions in the power file or null if not found
     */
    public HashMap<String, Object> getDamageCondition() {
        Object obj = powerFile.get("damage_condition");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public HashMap<String, Object> getFluidCondition() {
        Object obj = powerFile.get("fluid_condition");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public HashMap<String, Object> getBiomeCondition() {
        Object obj = powerFile.get("fluid_condition");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public HashMap<String, Object> getItemCondition() {
        Object obj = powerFile.get("item_condition");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public HashMap<String, Object> getBlockCondition() {
        Object obj = powerFile.get("block_condition");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public HashMap<String, Object> getThunderModifier() {
        Object obj = powerFile.get("modifier");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifierThing) {
            Object entityConditionObj = modifierThing.get("thunder_modifier");
            if (entityConditionObj instanceof JSONObject thunderThing) {
                HashMap<String, Object> result = new HashMap<>();
                for (Object key : thunderThing.keySet()) {
                    String stringKey = (String) key;
                    Object value = thunderThing.get(stringKey);
                    result.put(stringKey, value);
                }
                return result;
            }
        }

        return new HashMap<>();
    }

    public HashMap<String, Object> getEntityConditionFromDamageCondition() {
        Object obj = powerFile.get("damage_condition");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject damageCondition) {
            Object entityConditionObj = damageCondition.get("entity_condition");
            if (entityConditionObj instanceof JSONObject entityCondition) {
                HashMap<String, Object> result = new HashMap<>();
                for (Object key : entityCondition.keySet()) {
                    String stringKey = (String) key;
                    Object value = entityCondition.get(stringKey);
                    result.put(stringKey, value);
                }
                return result;
            }
        }

        return new HashMap<>();
    }

    public HashMap<String, Object> getEntityCondition() {
        Object obj = powerFile.get("entity_condition");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    public HashMap<String, Object> getPhaseDownCondition() {
        Object obj = powerFile.get("phase_down_condition");
        if (obj == null) return new HashMap<>();

        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }

    /**
     * Checks the PowerFile for the specified condition
     *
     * @return A HashMap of the keys and values in the condition or null if the condition type isn't found
     */
    public HashMap<String, Object> getCondition(String conditionType) {
        Object obj = powerFile.get(conditionType);
        if (obj == null) return new HashMap<>();
        if (obj instanceof JSONObject modifier) {
            HashMap<String, Object> result = new HashMap<>();
            for (Object key : modifier.keySet()) {
                String string_key = (String) key;
                Object value = modifier.get(string_key);
                result.put(string_key, value);
            }
            return result;
        }

        return null;
    }


    public JSONObject getBiEntityAction() {
        Object obj = powerFile.get("bientity_action");
        if (obj instanceof JSONObject modifier) {
            return modifier;
        }
        return new JSONObject();
    }

    public JSONObject getItemAction() {
        Object obj = powerFile.get("item_action");
        if (obj instanceof JSONObject modifier) {
            return modifier;
        }
        return new JSONObject();
    }

    public JSONObject getAction(String string) {
        Object obj = powerFile.get(string);
        if (obj instanceof JSONObject modifier) {
            return modifier;
        }
        return new JSONObject();
    }

    public JSONObject getEntityAction() {
        Object obj = powerFile.get("entity_action");
        if (obj instanceof JSONObject modifier) {
            return modifier;
        }
        return new JSONObject();
    }

    public JSONObject getBlockAction() {
        Object obj = powerFile.get("block_action");
        if (obj instanceof JSONObject modifier) {
            return modifier;
        }
        return new JSONObject();
    }
//            HashMap<String, Object> result = new HashMap<>();
//            for (Object key : modifier.keySet()) {
//                String string_key = (String) key;
//                Object value = modifier.get(string_key);
//                result.put(string_key, value);
//            }
//            return result;
//        }
//        return null;

}
