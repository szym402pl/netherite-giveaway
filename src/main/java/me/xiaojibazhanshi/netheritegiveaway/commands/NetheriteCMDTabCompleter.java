package me.xiaojibazhanshi.netheritegiveaway.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetheriteCMDTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        if (args.length == 1) {
            return StringUtil.copyPartialMatches
                    (args[0],
                    Arrays.stream(new String[]{"show", "giveaway"}).toList(),
                    new ArrayList<>());
        }

        return new ArrayList<>();
    }
}
