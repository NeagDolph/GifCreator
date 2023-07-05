package dev.lemontree.gifcreator;

import dev.lemontree.gifcreator.commands.GifCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class GifCreator extends JavaPlugin {
    FileConfiguration config = getConfig();

    private static GifCreator instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        config.addDefault("giphyApiKey", "");
        config.options().copyDefaults(true);
        saveConfig();

        PluginCommand gifCommand = getCommand("gif");

        if (gifCommand == null) {
            throw new RuntimeException("Command \"gif\" does not exist!");
        }

        gifCommand.setExecutor(new GifCommand());


        getLogger().info("GifCreator enabled!");
    }

    public static GifCreator getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
