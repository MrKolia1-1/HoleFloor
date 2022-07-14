package net.holefloor;

import net.holefloor.arena.Arena;
import net.holefloor.arena.ArenaManager;
import net.holefloor.arena.listener.ArenaListener;
import net.holefloor.arena.tab.HoleFloorTab;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class HoleFloor extends JavaPlugin {
    public ArenaManager manager;
    public FileConfiguration locale;
    public HoleFloorTab holeFloorTab;
    public HoleFloor() {
        super();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.registerResources();
        this.registerListeners();
        this.registerCommands();
        this.manager = new ArenaManager();
        this.manager.create("arena0", 2, 10, Objects.requireNonNull(new WorldCreator("lobby").createWorld()).getSpawnLocation());
        HoleFloor.getInstance().holeFloorTab = new HoleFloorTab();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.manager.arenas.forEach(arena -> this.manager.stop(arena));
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArenaListener(), this);
    }
    public void registerCommands() {

    }
    public void registerResources() {
        this.saveDefaultConfig();
        try {
            Enumeration<URL> en = this.getClassLoader().getResources("maps");
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                JarURLConnection cron = (JarURLConnection) (url.openConnection());
                try (JarFile jar = cron.getJarFile();) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        String entry = entries.nextElement().getName();
                        if (entry.startsWith("maps") && entry.endsWith(".nbt")) {
                            if (!new File(this.getDataFolder(), entry).exists()) {
                                this.saveResource(entry, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Enumeration<URL> en = this.getClassLoader().getResources("locales");
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                JarURLConnection cron = (JarURLConnection) (url.openConnection());
                try (JarFile jar = cron.getJarFile();) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        String entry = entries.nextElement().getName();
                        if (entry.startsWith("locales") && entry.endsWith(".yml")) {
                            this.saveResource(entry, true);//temple save
                            if (!new File(this.getDataFolder(), entry).exists()) {
                                this.saveResource(entry, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.locale = YamlConfiguration.loadConfiguration(
                new File(this.getDataFolder() + "/locales/" + this.getConfig().getString("locale")));
    }

    public static HoleFloor getInstance() {
        return getPlugin(HoleFloor.class);
    }
}
