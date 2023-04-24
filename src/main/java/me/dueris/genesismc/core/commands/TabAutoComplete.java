package me.dueris.genesismc.core.commands;

import me.dueris.genesismc.core.files.GenesisDataFiles;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabAutoComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("origin")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                arguments.add("info");
                if (sender.hasPermission("genesismc.origins.cmd.texture")) {
                    arguments.add("texture");
                }
                if (sender.hasPermission("genesismc.origins.cmd.commandlist")) {
                    arguments.add("commands");
                }
                if (sender.hasPermission("genesismc.origins.cmd.choose")) {
                    arguments.add("choose");
                }
                if (sender.hasPermission("genesismc.origins.cmd.config.dump")) {
                    arguments.add("dump");
                }
                if (sender.hasPermission("genesismc.origins.cmd.get")) {
                    arguments.add("get");
                }
                if (sender.hasPermission("genesismc.origins.cmd.purge")) {
                    arguments.add("purge");
                }
                if (sender.hasPermission("genesismc.origins.cmd.enchant")) {
                    arguments.add("enchant");
                }

                return arguments;
            } else if (args.length >= 3) {
                if (args[1].equalsIgnoreCase("genesis:water_protection")) {
                    List<String> enchantid = new ArrayList<>();
                    enchantid.add("1");
                    enchantid.add("2");
                    enchantid.add("3");
                    enchantid.add("4");
                    return enchantid;

                } if(args[1].equalsIgnoreCase("get")) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add("origin");
                    arguments.add("originid");
                    arguments.add("help");
                    arguments.add("keybind");
                    return arguments;
                } else {
                    List<String> nothing = new ArrayList<>();
                    return nothing;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("purge")) {
                    Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    List<String> playernames = new ArrayList<>();
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    for (int i = 0; i < players.length; i++) {
                        playernames.add(players[i].getName());
                    }
                    return playernames;
                } else if (args[0].equalsIgnoreCase("get")) {
                    Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    List<String> playernames = new ArrayList<>();
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    for (int i = 0; i < players.length; i++) {
                        playernames.add(players[i].getName());
                    }
                    return playernames;

                } else if (args[0].equalsIgnoreCase("enchant")) {
                    List<String> enchantid = new ArrayList<>();
                    enchantid.add("genesis:water_protection");
                    return enchantid;

                }else if(args[0].equalsIgnoreCase("info")) {
                    List<String> arguments = new ArrayList<>();
                    arguments.add("get");
                    return arguments;
                } else {
                    List<String> nothing = new ArrayList<>();
                    return nothing;
                }

            }

        } else if (command.getName().equalsIgnoreCase("shulker")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                arguments.add("open");
                return arguments;
            } else if (args.length >= 2) {
                List<String> nothing = new ArrayList<>();
                return nothing;
            }

        } else if (command.getName().equalsIgnoreCase("beta")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                if (GenesisDataFiles.getPlugCon().getString("beta-enabled").equalsIgnoreCase("true")) {
                    arguments.add("orboforigin");
                    arguments.add("waterprot");
                }
                return arguments;
            } else if (args.length >= 2) {
                List<String> nothing = new ArrayList<>();
                return nothing;
            }
        }


            return null;
        }

    }

