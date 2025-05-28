package hu.csanszan1.lscmc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class zoneManager {
    private final List<zoneDefinition> zones = new ArrayList<>();
    private final File file;
    private FileConfiguration config;

    public zoneManager(File dataFolder) {
        this.file = new File(dataFolder, "zones.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        loadZones();
    }

    public void addZone(zoneDefinition zone) {
        zones.add(zone);
        saveZone(zone);
    }

    public boolean isProtected(Location loc) {
        return zones.stream().anyMatch(zone -> zone.contains(loc));
    }
    public List<zoneDefinition> getZones() {
        return zones;
    }
    public boolean canModify(Location loc, String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if(player.hasPermission("lsc.overrideProtect")) {
            return true;
        }
        for (zoneDefinition zone : zones) {
            if (zone.contains(loc)) {
                return zone.getOwner().equalsIgnoreCase(playerName) || zone.isTrusted(playerName);
            }
        }
        return true;
    }

    public void saveZone(zoneDefinition zone) {
        String key = "zones." + zones.size();
        config.set(key + ".owner", zone.getOwner());
        config.set(key + ".corner1", zoneDefinition.serializeLocation(zone.getCorner1()));
        config.set(key + ".corner2", zoneDefinition.serializeLocation(zone.getCorner2()));
        config.set(key + ".trusted", new ArrayList<>(zone.getTrustedPlayers()));
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public zoneDefinition getZoneAt(Location location) {
        for (zoneDefinition zone : zones) {
            if (zone.contains(location)) {
                return zone;
            }
        }
        return null;
    }

    public void saveAllZones() {
        config.set("zones", null); // Clear existing zones section

        for (int i = 0; i < zones.size(); i++) {
            zoneDefinition zone = zones.get(i);
            String key = "zones." + i;

            config.set(key + ".owner", zone.getOwner());
            config.set(key + ".corner1", zoneDefinition.serializeLocation(zone.getCorner1()));
            config.set(key + ".corner2", zoneDefinition.serializeLocation(zone.getCorner2()));
            config.set(key + ".trusted", new ArrayList<>(zone.getTrustedPlayers()));
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadZones() {
        config = YamlConfiguration.loadConfiguration(file);
        if (config.contains("zones")) {
            zones.clear();
            for (String key : config.getConfigurationSection("zones").getKeys(false)) {
                String owner = config.getString("zones." + key + ".owner");
                Location c1 = zoneDefinition.deserializeLocation(config.getString("zones." + key + ".corner1"));
                Location c2 = zoneDefinition.deserializeLocation(config.getString("zones." + key + ".corner2"));
                List<String> trusted = config.getStringList("zones." + key + ".trusted");
                zones.add(new zoneDefinition(owner, c1, c2, trusted));
            }
        }
    }
}
