package me.dueris.genesismc.commands.subcommands.origin;

import me.dueris.genesismc.commands.PlayerSelector;
import me.dueris.genesismc.commands.subcommands.SubCommand;
import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.utils.LayerContainer;
import me.dueris.genesismc.utils.translation.LangConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static me.dueris.genesismc.utils.BukkitColour.RED;

public class Has extends SubCommand {
    @Override
    public String getName() {
        return "has";
    }

    @Override
    public String getDescription() {
        return LangConfig.getLocalizedString(Bukkit.getConsoleSender(), "command.origin.has.description");
    }

    @Override
    public String getSyntax() {
        return "/origin has <player> <layer> <origintag>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!sender.hasPermission("genesismc.origins.cmd.has")) return;
        if (args.length == 1) {
            sender.sendMessage(Component.text(LangConfig.getLocalizedString(sender, "command.origin.has.noPlayer")).color(TextColor.fromHexString(RED)));
            return;
        }
        if (args.length == 2) {
            sender.sendMessage(Component.text(LangConfig.getLocalizedString(sender, "command.origin.has.noLayer")).color(TextColor.fromHexString(RED)));
            return;
        }
        if (args.length == 3) {
            sender.sendMessage(Component.text(LangConfig.getLocalizedString(sender, "command.origin.has.noOrigin")).color(TextColor.fromHexString(RED)));
            return;
        }
        ArrayList<Player> players = PlayerSelector.playerSelector(sender, args[1]);
        if (players.size() == 0) return;
        if (!CraftApoli.getLayers().contains(CraftApoli.getLayerFromTag(args[2]))) {
            sender.sendMessage(Component.text(LangConfig.getLocalizedString(sender, "command.origin.has.invalidLayer")).color(TextColor.fromHexString(RED)));
            return;
        }
        String originTag = args[3];
        if (!CraftApoli.getOriginTags().contains(originTag)) {
            sender.sendMessage(Component.text(LangConfig.getLocalizedString(sender, "command.origin.has.invalidOrigin")).color(TextColor.fromHexString(RED)));
            return;
        }

        for (Player p : players) {
            for (LayerContainer layer : CraftApoli.getLayers()) {
                if (!layer.getTag().equals(args[2])) continue;
                if (OriginPlayer.hasOrigin(p, args[3]))
                    sender.sendMessage(Component.text(LangConfig.getLocalizedString(p, "command.origin.has.pass").replace("%player%", p.getName())));
                else
                    sender.sendMessage(Component.text(LangConfig.getLocalizedString(p, "command.origin.has.fail").replace("%player%", p.getName())));
            }
        }
    }
}
