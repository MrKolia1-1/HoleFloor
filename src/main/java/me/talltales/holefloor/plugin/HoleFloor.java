package me.talltales.holefloor.plugin;

import me.talltales.holefloor.arena.HoleFloorManager;
import me.talltales.holefloor.listeners.MainListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class HoleFloor extends JavaPlugin {
    private HoleFloorManager manager;
    private FileConfiguration locale;
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
        this.onLoadConfigs();
        this.onLoadResource();
        this.onRegisterListeners();
        this.onRegisterCommands();
        this.manager = new HoleFloorManager();
    }

    private void onLoadConfigs() {
        this.saveDefaultConfig();
        try {
            Enumeration<URL> en = getClass().getClassLoader().getResources("language");
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                JarURLConnection cron = (JarURLConnection) (url.openConnection());
                try (JarFile jar = cron.getJarFile();) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        String entry = entries.nextElement().getName();
                        if (entry.startsWith("language") && entry.endsWith(".yml")) {
                            if (!new File(getDataFolder(), entry).exists()) {
                                this.saveResource(entry, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        locale = YamlConfiguration.loadConfiguration(
                new File(getDataFolder() + "/language/" +
                        this.getConfig().getString("language")));
    }

    private void onRegisterCommands() {

    }

    private void onRegisterListeners() {
        this.getServer().getPluginManager().registerEvents(new MainListener(), this);
    }

    private void onLoadResource() {
        try {
            Enumeration<URL> en = getClass().getClassLoader().getResources("structures");
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                JarURLConnection cron = (JarURLConnection) (url.openConnection());
                try (JarFile jar = cron.getJarFile();) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        String entry = entries.nextElement().getName();
                        if (entry.startsWith("structures") && entry.endsWith(".nbt")) {
                            if (!new File(getDataFolder(), entry).exists()) {
                                this.saveResource(entry, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getLocale() {
        return locale;
    }

    public HoleFloorManager getManager() {
        return manager;
    }

    public static HoleFloor getInstance() {
        return getPlugin(HoleFloor.class);
    }
}
