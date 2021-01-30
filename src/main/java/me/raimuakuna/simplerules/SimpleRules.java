package me.raimuakuna.simplerules;

import org.bukkit.plugin.java.JavaPlugin;
import me.raimuakuna.simplerules.commands.Rules;


import java.util.Objects;

public final class SimpleRules extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getDataFolder().mkdirs();
        Objects.requireNonNull(getCommand("rules")).setExecutor(new Rules(this));
    }

    @Override
    public void onDisable() {
    }
}
