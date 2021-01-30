package me.raimuakuna.simplerules.commands;

import me.raimuakuna.simplerules.SimpleRules;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Rules implements CommandExecutor {

    private final Plugin plugin;
    private final File rulesFile;

    //Read to see if the rules folder + file exists
    public Rules(Plugin plugin) {
        this.plugin = (SimpleRules) plugin;
        rulesFile = new File(plugin.getDataFolder(), "rules.txt");
        if (!rulesFile.exists()) {
            //Try to create a rules file and write default config
            try {
                rulesFile.createNewFile();
                FileWriter writer = new FileWriter(rulesFile);
                writer.append("{\"text\":\"These rules aren't set up yet. Tell an admin to change it!\",\"color\":\"#00BA3B\"}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.reload();
    }

    @Override
    //Reload command
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String pluginprefix = plugin.getConfig().getString("pluginprefix");

        if (args.length > 0 && "reload".equalsIgnoreCase(args[0]) && sender.hasPermission("simplerules.reload")) {
            this.reload();
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  pluginprefix + "&aRules file reloaded successfully!"));
            return true;
        }

        //If they don't have a permission, return
        if (args.length > 0 && "reload".equalsIgnoreCase(args[0]) && !sender.hasPermission("simplerules.reload")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  pluginprefix + "&cYou cannot use this command! (simplerules.reload)"));
            return true;
        }

        //Alert the user if the rules file is empty
        if (rules.isEmpty()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  pluginprefix + "&6Your rules file is empty!"));
        else rules.forEach(sender.spigot()::sendMessage);
        return true;


    }

    private final List<BaseComponent[]> rules = new ArrayList<>();

    private void reload() {
        rules.clear();

        //Attempt to reload the plugin
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.rulesFile));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                rules.add(ComponentSerializer.parse(line));
            }

            //Report if there's an error
        } catch (Exception e) {
            rules.clear();
            rules.add(TextComponent.fromLegacyText(ChatColor.RED + "Unable to load rules file!"));
            e.printStackTrace();
        }
    }
}