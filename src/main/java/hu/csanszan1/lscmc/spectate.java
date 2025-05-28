package hu.csanszan1.lscmc;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class spectate implements CommandExecutor {
    private Map<UUID, Location> playerLocations = new HashMap<>();
    private boolean isSpectatorEnabled = false;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!isSpectatorEnabled)
        {
            isSpectatorEnabled = true;
            Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(!player.getName().equals(sender.getName()))
                {
                    playerLocations.put(player.getUniqueId(), player.getLocation());
                    player.setGameMode(GameMode.SPECTATOR);
                    player.setSpectatorTarget((Player) sender);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!player.isOnline() || player.getGameMode() != GameMode.SPECTATOR || !isSpectatorEnabled) {
                                cancel();
                                return;
                            }

                            if (player.getSpectatorTarget() != (Player) sender) {
                                player.setGameMode(GameMode.SPECTATOR);
                                player.setSpectatorTarget((Player) sender);

                            }
                        }
                    }.runTaskTimer(getPlugin(main.class), 0L, 1L); // runs every 5 ticks (~0.25 seconds)

                }

            }
        }
        else {
            isSpectatorEnabled = false;
            for (Map.Entry<UUID, Location> entry : playerLocations.entrySet()) {
                UUID uuid = entry.getKey();
                Location location = entry.getValue();
                // Do something with the UUID and Location
                Player player = Bukkit.getPlayer(uuid);
                if(player != null) {
                    GameMode prevGameMode = player.getPreviousGameMode();
                    if (prevGameMode == null) {
                        prevGameMode = GameMode.SURVIVAL;
                    }
                    player.setGameMode(prevGameMode);
                    player.teleport(location);

                }
            }
        }

        return true;
    }
}