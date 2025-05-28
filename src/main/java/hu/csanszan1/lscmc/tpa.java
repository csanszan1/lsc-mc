package hu.csanszan1.lscmc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class tpa implements CommandExecutor {
    private Map<UUID, UUID> teleportRequests = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("tpa")) {
            if(args.length != 1) {
                sender.sendMessage("Használat: /tpa <játékos, akihez menni szeretnél>");
                return true;
            }
            Player teleportTo = Bukkit.getPlayerExact(args[0]);
            if (teleportTo == null) {
                sender.sendMessage(Component.text("Nincs ilyen játékos", NamedTextColor.DARK_RED));
                return true;
            }
            teleportRequests.put(teleportTo.getUniqueId(), ((Player) sender).getUniqueId());
            TextComponent tpaText = Component.text(((Player) sender).getName(), NamedTextColor.GOLD)
                    .append(Component.text(" szeretne hozzád teleportálni. Ha elfogadod, akkor használt a ", NamedTextColor.YELLOW))
                    .append(Component.text("/tpaccept", NamedTextColor.GOLD))
                    .append(Component.text(" parancsot. Ha nem fogadod el, akkor használd a ", NamedTextColor.YELLOW))
                    .append(Component.text("/tpdeny", NamedTextColor.GOLD))
                    .append(Component.text(" parancsot.", NamedTextColor.YELLOW));
            teleportTo.sendMessage(tpaText);
        }
        if(command.getName().equals("tpaccept")) {
            UUID requesterUUID = teleportRequests.get(((Player) sender).getUniqueId());
            if(requesterUUID == null) {
                sender.sendMessage(Component.text("Nincs aktív teleport kérelmed.", NamedTextColor.YELLOW));
                return true;
            }
            Player requester = Bukkit.getPlayer(teleportRequests.get(requesterUUID));
            if(requester == null) {
                sender.sendMessage(Component.text("Ez a játékos már valószínűleg elhagyta a szervert.", NamedTextColor.DARK_RED));
                return true;
            }
            requester.teleport((Player) sender);
            teleportRequests.remove(((Player) sender).getUniqueId());
            return true;
        }
        if(command.getName().equals("tpdeny")) {
            UUID requesterUUID = teleportRequests.get(((Player) sender).getUniqueId());
            if(requesterUUID == null) {
                sender.sendMessage(Component.text("Nincs aktív teleport kérelmed.", NamedTextColor.YELLOW));
                return true;
            }
            Player requester = Bukkit.getPlayer(teleportRequests.get(requesterUUID));
            if(requester != null) {
                requester.sendMessage(Component.text("A teleport kérelmed vissza lett utasítva.", NamedTextColor.DARK_RED));
            }
            teleportRequests.remove(((Player) sender).getUniqueId());
            sender.sendMessage(Component.text("Kérelem visszautasítva.", NamedTextColor.YELLOW));
            return true;
        }

        return true;
    }
}