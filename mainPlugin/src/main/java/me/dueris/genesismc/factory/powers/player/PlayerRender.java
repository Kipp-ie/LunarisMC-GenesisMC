package me.dueris.genesismc.factory.powers.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.events.OriginChangeEvent;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import me.dueris.genesismc.utils.translation.LangConfig;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import net.skinsrestorer.api.property.IProperty;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineskin.MineskinClient;
import org.mineskin.data.Skin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class PlayerRender extends CraftPower {

    @Override
    public void setActive(String tag, Boolean bool) {
        if (powers_active.containsKey(tag)) {
            powers_active.replace(tag, bool);
        } else {
            powers_active.put(tag, bool);
        }
    }

    Player p;

    public PlayerRender() {
        this.p = p;
    }

    @Override
    public void run(Player p) {
//        ScoreboardManager manager = Bukkit.getScoreboardManager();
//        Scoreboard scoreboard = manager.getMainScoreboard();
//        Team team = scoreboard.getTeam("origin-players");
//        if (team == null) {
//            team = scoreboard.registerNewTeam("origin-players");
//        }
        boolean isInvisible = p.hasPotionEffect(PotionEffectType.INVISIBILITY);
        boolean isInTranslucentList = translucent.contains(p);
        boolean isInPhantomForm = OriginPlayer.isInPhantomForm(p);

        if (isInPhantomForm) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(p)) {
                    other.hidePlayer(GenesisMC.getPlugin(), p);
                }
            }
//                if (!team.getEntries().contains(p)) {
//                    team.addEntry(p.getName());
//                }
        } else if (isInvisible && !isInTranslucentList) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(p)) {
                    other.hidePlayer(GenesisMC.getPlugin(), p);
                }
            }
            Location location = p.getLocation();
            location.getWorld().spawnParticle(Particle.SPELL_MOB_AMBIENT, location, 2, 0.0, 0.0, 0.0, 1.0, null);
//                if (!team.getEntries().contains(p)) {
//                    team.addEntry(p.getName());
//                }
        } else {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(p)) {
                    other.showPlayer(GenesisMC.getPlugin(), p);
                }
            }
//                if (!team.getEntries().contains(p)) {
//                    team.addEntry(p.getName());
//                }
        }

        if (isInTranslucentList) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(p)) {
                    other.showPlayer(GenesisMC.getPlugin(), p);
                }
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15, 255, false, false, false));
//                if (!team.getEntries().contains(p)) {
//                    team.addEntry(p.getName());
//                }
        }

        // Hide player from pumpkin_hate players if wearing a pumpkin
        ItemStack helmet = p.getInventory().getHelmet();
        boolean wearingPumpkin = helmet != null && helmet.getType() == Material.CARVED_PUMPKIN;

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (pumpkin_hate.contains(target)) {
                if (wearingPumpkin) {
                    target.hidePlayer(GenesisMC.getPlugin(), p);
                } else {
                    target.showPlayer(GenesisMC.getPlugin(), p);
                }
            }
        }
    }

    @Override
    public String getPowerFile() {
        return "origins:invisibility";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return invisibility;
    }

    public static class ModelColor extends BukkitRunnable implements Listener {
        private SkinsRestorerAPI skinsRestorerAPI = null;

        @SuppressWarnings("null")
        @EventHandler
        public void onPlayerChoose(OriginChangeEvent event) {
            Player player = event.getPlayer();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!Bukkit.getServer().getPluginManager().isPluginEnabled("SkinsRestorer")) return;
                    if (model_color.contains(player)) {
                        for (OriginContainer origin : OriginPlayer.getOrigin(player).values()) {
                            for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                                Double red;
                                Double blue;
                                Double green;

                                if (power == null) {
                                    model_color.remove(player);
                                }
                                if (power.getColor("red") == null) {
                                    red = 0.0;
                                } else {
                                    red = power.getColor("red");
                                }
                                if (power.getColor("blue") == null) {
                                    blue = 0.0;
                                } else {
                                    blue = power.getColor("blue");
                                }
                                if (power.getColor("green") == null) {
                                    green = 0.0;
                                } else {
                                    green = power.getColor("green");
                                }

                                String savePath = Bukkit.getServer().getPluginManager().getPlugin("genesismc").getDataFolder().getPath() + File.separator + "skins";
                                skinsRestorerAPI = SkinsRestorerAPI.getApi();
                                ModelColor.modifyPlayerSkin(player, red, green, blue, savePath, 1L, skinsRestorerAPI, false, power);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "modified-skin-url"), PersistentDataType.STRING));
                                player.sendMessage("skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "modified-skin-url"), PersistentDataType.STRING));
                                player.saveData();
                            }
                        }
                    } else {
                        for (OriginContainer origin : OriginPlayer.getOrigin(player).values()) {
                            for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                                Double red = Double.valueOf(0);
                                Double blue = Double.valueOf(0);
                                Double green = Double.valueOf(0);
                                Long alphaTint = 0L;
                                String savePath = Bukkit.getServer().getPluginManager().getPlugin("genesismc").getDataFolder().getPath() + File.separator + "skins";
                                skinsRestorerAPI = SkinsRestorerAPI.getApi();
                                ModelColor.modifyPlayerSkin(player, red, green, blue, savePath, alphaTint, skinsRestorerAPI, true, power);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin clear " + player.getName());
                                player.saveData();
                            }
                        }
                    }
                    this.cancel();
                }
            }.runTaskTimer(GenesisMC.getPlugin(), 20L, 1L);
        }

        @EventHandler
        public void JoinApplyTest(PlayerJoinEvent e) {
            Player player = e.getPlayer();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (model_color.contains(player)) {
                        if (player.getPlayerProfile().getTextures().getSkinModel() == PlayerTextures.SkinModel.CLASSIC) {
                            Bukkit.getConsoleSender().sendMessage(LangConfig.getLocalizedString(Bukkit.getConsoleSender(), "powers.modelColour"));
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "modified-skin-url"), PersistentDataType.STRING) + " CLASSIC");
                        } else {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "modified-skin-url"), PersistentDataType.STRING) + " SLIM");
                        }
                    } else {
                        if (player.getPlayerProfile().getTextures().getSkinModel() == PlayerTextures.SkinModel.CLASSIC) {
                            Bukkit.getConsoleSender().sendMessage(LangConfig.getLocalizedString(Bukkit.getConsoleSender(), "powers.modelColour"));
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "original-skin-url"), PersistentDataType.STRING) + " CLASSIC");
                        } else {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "original-skin-url"), PersistentDataType.STRING) + " SLIM");
                        }
                    }
                    this.cancel();
                }
            }.runTaskTimer(GenesisMC.getPlugin(), 4L, 1L);
        }

        public static void modifyPlayerSkin(Player player, Double redTint, Double greenTint, Double blueTint, String savePath, Long alphaTint, SkinsRestorerAPI skinsRestorerAPI, boolean applyOriginal, PowerContainer power) {
            PlayerProfile gameProfile = player.getPlayerProfile();
            if(!Bukkit.getPluginManager().isPluginEnabled("SkinsRestorer")) return;
            try {
                String textureProperty = skinsRestorerAPI.getSkinTextureUrl(SkinsRestorerAPI.getApi().getSkinData(player.getName()));
                String imageUrl = textureProperty;
                String uuid = player.getUniqueId().toString();
                String originalFileName = uuid + ".png";
                String modifiedFileName = uuid + "_modified.png";

                try {
                    BufferedImage originalImage = downloadImage(imageUrl, savePath, originalFileName);
                    BufferedImage modifiedImage = modifyImage(originalImage, redTint, greenTint, blueTint, alphaTint, player, power);
                    saveImage(modifiedImage, savePath, modifiedFileName);
                    MineskinClient mineskinClient = new MineskinClient();

                    File modifiedFile = new File(savePath, modifiedFileName);

                    CompletableFuture<Skin> future = mineskinClient.generateUpload(modifiedFile);
                    future.thenAccept(skinData -> {
                        if (skinData == null) {
                            // Failed to generate the skin data
                            future.cancel(true);
                            return;
                        }

                        Skin skin = skinData;
                        String url = skin.data.texture.url;
                        player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "modified-skin-url"), PersistentDataType.STRING, url);
                        IProperty platformprop = skinsRestorerAPI.createPlatformProperty(player.getUniqueId() + "_modified", skin.data.texture.value, skin.data.texture.signature);
                        PlayerWrapper playerWrapper = new PlayerWrapper(player);
                        skinsRestorerAPI.applySkin(playerWrapper, platformprop);

                        ModelColor modelColor = new ModelColor();
                        modelColor.runTaskTimer(GenesisMC.getPlugin(), 2, 1);

                        try {
                            // /api custom
                            skinsRestorerAPI.setSkinData(player.getUniqueId() + "_modified", skinsRestorerAPI.createPlatformProperty(player.getUniqueId() + "_modified", skin.data.texture.value, skin.data.texture.signature), 0);
                            // #setSkin() for player skin
                            skinsRestorerAPI.setSkin(player.getName(), player.getUniqueId() + "_modified");

                            // Force skin refresh for player
                            skinsRestorerAPI.applySkin(new PlayerWrapper(player));

                        } catch (SkinRequestException e) {
                            e.printStackTrace();
                        }

                    });

                    CompletableFuture<Skin> futureorg = mineskinClient.generateUpload(originalImage);
                    futureorg.thenAccept(skinData -> {
                        if (skinData == null) {
                            // Failed to generate the skin data
                            return;
                        }

                        Skin skin = skinData;

                        String url = skin.data.texture.url;
                        player.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "original-skin-url"), PersistentDataType.STRING, url);
                        String playername = player.getName();

                        ModelColor modelColor = new ModelColor();
                        modelColor.runTaskTimer(GenesisMC.getPlugin(), 2, 1);

                        if (applyOriginal) {
                            try {
                                // /api custom
                                skinsRestorerAPI.setSkinData(player.getUniqueId().toString(), skinsRestorerAPI.createPlatformProperty(player.getUniqueId().toString(), skin.data.texture.value, skin.data.texture.signature), 0);
                                // Force skin refresh for player
                                skinsRestorerAPI.removeSkin(playername);
                                skinsRestorerAPI.applySkin(new PlayerWrapper(player));

                            } catch (SkinRequestException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                } catch (IOException e) {
                    //rip
                }
            } catch (Exception e){
            }
        }

        @SuppressWarnings("javax.imageio.IIOException")
        private static BufferedImage downloadImage(String imageUrl, String savePath, String fileName) throws IOException {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);
            File outputDir = new File(savePath);
            outputDir.mkdirs();

            File outputFile = new File(savePath, fileName);
            ImageIO.write(image, "png", outputFile);

            return image;
        }

        private static BufferedImage modifyImage(BufferedImage originalImage, double redTint, double greenTint, double blueTint, double alphaTint, Player player, PowerContainer power) {
            if (redTint > 1 || greenTint > 1 || blueTint > 1 || alphaTint > 1) {
                if (power.getModelRenderType() == "original") {
                    throw new IllegalArgumentException(LangConfig.getLocalizedString(Bukkit.getConsoleSender(), "powers.errors.modelColourValue"));
                }
            }

            BufferedImage modifiedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            if (power == null) return modifiedImage;
            if (power.getModelRenderType().equalsIgnoreCase("add")) {
                for (int x = 0; x < modifiedImage.getWidth(); x++) {
                    for (int y = 0; y < modifiedImage.getHeight(); y++) {
                        // Get the original RGB values of the pixel
                        int rgb = originalImage.getRGB(x, y);

                        // Extract the color components (alpha, red, green, blue)
                        int alpha = (rgb >> 24) & 0xFF;
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        // Apply additive blending to the color components
                        red = blendColorComponentAdditive(red, (int) (redTint));
                        green = blendColorComponentAdditive(green, (int) (greenTint));
                        blue = blendColorComponentAdditive(blue, (int) (blueTint));

                        // Create the modified RGB value with the updated color components
                        int modifiedRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;

                        // Set the modified pixel value in the modified image
                        modifiedImage.setRGB(x, y, modifiedRGB);
                    }
                }
            } else if (power.getModelRenderType().equalsIgnoreCase("subtract")) {
                for (int x = 0; x < modifiedImage.getWidth(); x++) {
                    for (int y = 0; y < modifiedImage.getHeight(); y++) {
                        int rgb = originalImage.getRGB(x, y);
                        int alpha = (rgb >> 24) & 0xFF;
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        // Apply tint as an overlay
                        red = blendColorComponentSubtractive(red, (int) redTint);
                        green = blendColorComponentSubtractive(green, (int) greenTint);
                        blue = blendColorComponentSubtractive(blue, (int) blueTint);

                        int modifiedRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        modifiedImage.setRGB(x, y, modifiedRGB);
                    }
                }
            } else if (power.getModelRenderType().equalsIgnoreCase("multiply")) {
                for (int x = 0; x < modifiedImage.getWidth(); x++) {
                    for (int y = 0; y < modifiedImage.getHeight(); y++) {
                        int rgb = originalImage.getRGB(x, y);
                        int alpha = (rgb >> 24) & 0xFF;
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        // Apply tint as an overlay
                        red = blendColorComponentMultiply(red, (int) redTint);
                        green = blendColorComponentMultiply(green, (int) greenTint);
                        blue = blendColorComponentMultiply(blue, (int) blueTint);

                        int modifiedRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        modifiedImage.setRGB(x, y, modifiedRGB);
                    }
                }
            } else if (power.getModelRenderType().equalsIgnoreCase("divide")) {
                for (int x = 0; x < modifiedImage.getWidth(); x++) {
                    for (int y = 0; y < modifiedImage.getHeight(); y++) {
                        int rgb = originalImage.getRGB(x, y);
                        int alpha = (rgb >> 24) & 0xFF;
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        // Apply tint as an overlay
                        red = blendColorComponentDivide(red, (int) redTint);
                        green = blendColorComponentDivide(green, (int) greenTint);
                        blue = blendColorComponentDivide(blue, (int) blueTint);

                        int modifiedRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        modifiedImage.setRGB(x, y, modifiedRGB);
                    }
                }
            } else if (power.getModelRenderType().equalsIgnoreCase("original")) {
                for (int x = 0; x < modifiedImage.getWidth(); x++) {
                    for (int y = 0; y < modifiedImage.getHeight(); y++) {
                        int rgb = originalImage.getRGB(x, y);
                        int alpha = (rgb >> 24) & 0xFF;
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        // Apply tint as an overlay
                        red = blendColorComponent(red, redTint);
                        green = blendColorComponent(green, greenTint);
                        blue = blendColorComponent(blue, blueTint);

                        int modifiedRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        modifiedImage.setRGB(x, y, modifiedRGB);
                    }
                }
            }
            return modifiedImage;
        }

        private static int blendColorComponent(int original, double tint) {
            double blended = original * tint;
            int clamped = (int) Math.min(Math.max(blended, 0), 255);
            return clamped;
        }

        private static int blendColorComponentAdditive(int baseColor, int tint) {
            int blendedColor = baseColor + tint;
            return Math.min(blendedColor, 255);
        }

        private static int blendColorComponentSubtractive(int baseColor, int tint) {
            int blendedColor = baseColor - tint;
            return Math.max(blendedColor, 0);
        }

        private static int blendColorComponentMultiply(int baseColor, int tint) {
            int blendedColor = (int) ((baseColor / 255.0) * tint);
            return Math.min(blendedColor, 255);
        }

        private static int blendColorComponentDivide(int baseColor, int tint) {
            if (tint == 0) {
                return 0;
            }

            int blendedColor = (int) ((baseColor / 255.0) / (tint / 255.0) * 255);
            return Math.min(blendedColor, 255);
        }

        private static void saveImage(BufferedImage image, String savePath, String fileName) throws IOException {
            File outputDir = new File(savePath);
            outputDir.mkdirs();

            File outputFile = new File(savePath, fileName);
            ImageIO.write(image, "png", outputFile);
        }

        public void runD() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getConsoleSender().sendMessage(player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "modified-skin-url"), PersistentDataType.STRING));
                Bukkit.getConsoleSender().sendMessage(player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "original-skin-url"), PersistentDataType.STRING));
                if (model_color.contains(player)) {
                    for (OriginContainer origin : OriginPlayer.getOrigin(player).values()) {
                        if (model_color.contains(player)) {
                            if (player.getPlayerProfile().getTextures().getSkinModel() == PlayerTextures.SkinModel.CLASSIC) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "modified-skin-url"), PersistentDataType.STRING) + " CLASSIC");
                            } else {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "modified-skin-url"), PersistentDataType.STRING) + " SLIM");
                            }
                        }
                    }
                } else {
                    for (OriginContainer origin : OriginPlayer.getOrigin(player).values()) {
                        if (!model_color.contains(player)) {
                            if (player.getPlayerProfile().getTextures().getSkinModel() == PlayerTextures.SkinModel.CLASSIC) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "original-skin-url"), PersistentDataType.STRING) + " CLASSIC");
                            } else {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getPersistentDataContainer().get(new NamespacedKey(GenesisMC.getPlugin(), "original-skin-url"), PersistentDataType.STRING) + " SLIM");
                            }
                        }
                    }
                }
                this.cancel();
            }
        }

        public String getPowerFile() {
            return "origins:model_color";
        }

        public ArrayList<Player> getPowerArray() {
            return model_color;
        }

        public void setActive(String tag, Boolean bool) {
            if (powers_active.containsKey(tag)) {
                powers_active.replace(tag, bool);
            } else {
                powers_active.put(tag, bool);
            }
        }

        @Override
        public void run() {

        }
    }
}

