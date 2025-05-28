package hu.csanszan1.lscmc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class protect implements CommandExecutor {
    private final zoneManager zoneMgr;
    private final Map<String, Location[]> selection = new HashMap<>();

    public protect(zoneManager zoneManager) {
        zoneMgr = zoneManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("lsc.reloadZones")) {
                zoneMgr.loadZones();
                sender.sendMessage(Component.text("[LSC-MC] ").append(Component.text("Zónák újratöltve", NamedTextColor.DARK_GREEN)));
            }
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[LSC-MC] Ezt a parancsot csak játékosok használhatják.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("poz1")) {
            selection.computeIfAbsent(player.getName(), k -> new Location[2])[0] = player.getLocation();
            player.sendMessage(Component.text("[LSC-MC] Az első pozíció elmentve."));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("poz2")) {
            selection.computeIfAbsent(player.getName(), k -> new Location[2])[1] = player.getLocation();
            player.sendMessage(Component.text("[LSC-MC] A második pozíció elmentve."));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("létrehoz")) {
            Location[] locs = selection.get(player.getName());
            if (locs == null || locs[0] == null || locs[1] == null) {
                player.sendMessage(Component.text("[LSC-MC]Először állítsd be mindkettő pozíciót."));
            } else {
                zoneMgr.addZone(new zoneDefinition(player.getName(), locs[0], locs[1]));
                player.sendMessage("Levédés létrehozva.");
                selection.remove(player.getName());
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("megbízok")) {
            for (zoneDefinition zone : zoneMgr.getZones()) {
                if (zone.getOwner().equalsIgnoreCase(player.getName())) {
                    zone.addTrustedPlayer(args[1]);
                }
            }
            zoneMgr.saveAllZones();
            sender.sendMessage(Component.text("[LSC-MC]").append(Component.text("Mostantól ", NamedTextColor.YELLOW))
                    .append(Component.text(args[1], NamedTextColor.GOLD))
                    .append(Component.text(" is hozzáfér a levédéseidhez", NamedTextColor.GOLD)));

        }
        else {
            player.sendMessage("Használat: /protect <poz1 | poz2 | létrehoz | megbízok>");
        }

        return true;
    }
}