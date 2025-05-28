package hu.csanszan1.lscmc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class zoneDefinition {
    private final String owner;
    private final Location corner1;
    private final Location corner2;
    private Set<String> trustedPlayers;

    public zoneDefinition(String owner, Location corner1, Location corner2) {
        this.owner = owner;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.trustedPlayers = new HashSet<>();
    }

    public zoneDefinition(String owner, Location corner1, Location corner2, List<String> trusted) {
        this.owner = owner;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.trustedPlayers = new HashSet<>(trusted);
    }

    public Set<String> getTrustedPlayers() {
        return trustedPlayers;
    }

    public void addTrustedPlayer(String player) {
        trustedPlayers.add(player.toLowerCase());
    }
    public boolean isTrusted(String player) {
        return trustedPlayers.contains(player.toLowerCase());
    }
    public String getOwner() {
        return owner;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(corner1.getWorld())) return false;

        double x1 = Math.min(corner1.getX(), corner2.getX());
        double x2 = Math.max(corner1.getX(), corner2.getX());
        double y1 = Math.min(corner1.getY(), corner2.getY());
        double y2 = Math.max(corner1.getY(), corner2.getY());
        double z1 = Math.min(corner1.getZ(), corner2.getZ());
        double z2 = Math.max(corner1.getZ(), corner2.getZ());

        double x = loc.getX(), y = loc.getY(), z = loc.getZ();

        return x >= x1 && x <= x2 &&
                y >= y1 && y <= y2 &&
                z >= z1 && z <= z2;
    }

    public static Location deserializeLocation(String s) {
        String[] parts = s.split(",");
        World world = Bukkit.getWorld(parts[0]);
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        return new Location(world, x, y, z);
    }

    public static String serializeLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }
}