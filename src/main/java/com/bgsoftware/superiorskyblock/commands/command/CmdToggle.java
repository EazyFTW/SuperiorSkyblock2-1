package com.bgsoftware.superiorskyblock.commands.command;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.wrappers.SSuperiorPlayer;
import com.bgsoftware.superiorskyblock.Locale;
import com.bgsoftware.superiorskyblock.commands.ICommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CmdToggle implements ICommand {

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("toggle");
    }

    @Override
    public String getPermission() {
        return "superior.island.toggle";
    }

    @Override
    public String getUsage() {
        return "island toggle <border/blocks>";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return false;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(sender);

        if(args[1].equalsIgnoreCase("border")) {
            if (superiorPlayer.hasWorldBorderEnabled()) {
                Locale.TOGGLED_WORLD_BORDER_OFF.send(superiorPlayer);
            } else {
                Locale.TOGGLED_WORLD_BORDER_ON.send(superiorPlayer);
            }

            superiorPlayer.toggleWorldBorder();
            plugin.getNMSAdapter().setWorldBorder(superiorPlayer, plugin.getGrid().getIslandAt(superiorPlayer.getLocation()));
        }

        else if(args[1].equalsIgnoreCase("blocks")){
            if(superiorPlayer.hasBlocksStackerEnabled()){
                Locale.TOGGLED_STACKED_BLCOKS_OFF.send(superiorPlayer);
            } else{
                Locale.TOGGLED_STACKED_BLCOKS_ON.send(superiorPlayer);
            }

            superiorPlayer.toggleBlocksStacker();
        }

        else{
            Locale.INVALID_TOGGLE_MODE.send(superiorPlayer, args[1]);
        }

    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();

        if(args.length == 2){
            list.addAll(Stream.of("border", "blocks")
                    .filter(value -> value.startsWith(args[1].toLowerCase())).collect(Collectors.toList()));
        }

        return list;
    }
}