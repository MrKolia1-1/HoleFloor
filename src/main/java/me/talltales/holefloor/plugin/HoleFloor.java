package me.talltales.holefloor.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class HoleFloor extends JavaPlugin {
    public HoleFloor() {
        super();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    protected HoleFloor(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    protected File getFile() {
        return super.getFile();
    }

    @Override
    public FileConfiguration getConfig() {
        return super.getConfig();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
    }

    @Override
    public void saveConfig() {
        super.saveConfig();
    }

    @Override
    public void saveDefaultConfig() {
        super.saveDefaultConfig();
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        super.saveResource(resourcePath, replace);
    }


    @Override
    public InputStream getResource(String filename) {
        return super.getResource(filename);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return super.onCommand(sender, command, label, args);
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return super.onTabComplete(sender, command, alias, args);
    }


    @Override
    public PluginCommand getCommand(String name) {
        return super.getCommand(name);
    }


    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return super.getDefaultWorldGenerator(worldName, id);
    }


    @Override
    public BiomeProvider getDefaultBiomeProvider(String worldName, String id) {
        return super.getDefaultBiomeProvider(worldName, id);
    }

    @Override
    public Logger getLogger() {
        return super.getLogger();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
