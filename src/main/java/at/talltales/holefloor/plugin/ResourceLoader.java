package at.talltales.holefloor.plugin;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ResourceLoader {
    public void loadStructures() {
        try {
            Enumeration<URL> en = ResourceLoader.class.getClassLoader().getResources("structures");
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                JarURLConnection cron = (JarURLConnection) (url.openConnection());
                try (JarFile jar = cron.getJarFile();) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        String entry = entries.nextElement().getName();
                        if (entry.startsWith("structures") && entry.endsWith(".nbt")) {
                            if (!new File(HoleFloor.getInstance().getDataFolder(), entry).exists()) {
                                HoleFloor.getInstance().saveResource(entry, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadLanguage() {
        try {
            Enumeration<URL> en = ResourceLoader.class.getClassLoader().getResources("language");
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                JarURLConnection cron = (JarURLConnection) (url.openConnection());
                try (JarFile jar = cron.getJarFile();) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        String entry = entries.nextElement().getName();
                        if (entry.startsWith("language") && entry.endsWith(".yml")) {
                            if (!new File(HoleFloor.getInstance().getDataFolder(), entry).exists()) {
                                HoleFloor.getInstance().saveResource(entry, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
