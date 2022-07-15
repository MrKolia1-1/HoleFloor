package net.holefloor;

import net.holefloor.arena.ArenaManager;
import net.holefloor.command.HoleFloorCommand;
import net.holefloor.listener.LobbyListener;
import net.holefloor.scoreboard.HoleFloorBoard;
import net.holefloor.tab.HoleFloorTab;
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
    public ArenaManager manager;
    public FileConfiguration locale;
    public HoleFloorTab tab;
    public HoleFloorBoard board;
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
        this.manager = new ArenaManager();
        this.registerListeners();
        this.registerCommands();
        this.tab = new HoleFloorTab();
        this.board = new HoleFloorBoard();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.manager.arenas.forEach(arena -> this.manager.stop(arena));
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        });
    }

    public void registerListeners() {
        this.manager.lobbyListener = new LobbyListener();
    }
    public void registerCommands() {
        this.getCommand("holeinthefloor").setExecutor(new HoleFloorCommand());
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
