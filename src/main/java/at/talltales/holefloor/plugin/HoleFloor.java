package at.talltales.holefloor.plugin;

import at.talltales.holefloor.arena.Arena;
import at.talltales.holefloor.arena.ArenaManager;
import at.talltales.holefloor.listener.MainListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HoleFloor extends JavaPlugin {
    private ResourceLoader resource = new ResourceLoader();
    private ArenaManager manager = new ArenaManager();
    private FileConfiguration locale;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.registerListeners();
        this.registerCommands();
        this.registerResources();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new MainListener(), this);
    }

    private void registerCommands() {

    }

    private void registerResources() {
        this.saveDefaultConfig();
        this.resource.loadLanguage();
        this.locale = YamlConfiguration.loadConfiguration(
                new File(this.getDataFolder() + "/language/" + this.getConfig().getString("language")));
        this.resource.loadStructures();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static HoleFloor getInstance() {
        return getPlugin(HoleFloor.class);
    }

    public ArenaManager getManager() {
        return manager;
    }

    public FileConfiguration getLocale() {
        return locale;
    }
}
