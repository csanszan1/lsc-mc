package hu.csanszan1.lscmc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class pvp implements CommandExecutor, Listener {
    private boolean isPvpEnabled = true;
    private boolean isTimerRunning = false;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!isTimerRunning) {
            hotBarPvpStatus();
            isTimerRunning = true;
        }
        isPvpEnabled = !isPvpEnabled;
        if (isPvpEnabled) {
            final TextComponent textComponent = Component.text("[LSC-MC] ")
                    .append(Component.text("A PVP ", NamedTextColor.YELLOW))
                    .append(Component.text("BE ", NamedTextColor.DARK_GREEN))
                    .append(Component.text("lett kappcsolva", NamedTextColor.YELLOW));
            sender.sendMessage(textComponent);
        }
        else {
            final TextComponent textComponent = Component.text("[LSC-MC] ")
                    .append(Component.text("A PVP ", NamedTextColor.YELLOW))
                    .append(Component.text("KI ", NamedTextColor.DARK_RED))
                    .append(Component.text("lett kappcsolva", NamedTextColor.YELLOW));
            sender.sendMessage(textComponent);
        }
        return true;
    }

    public void hotBarPvpStatus() {

        new BukkitRunnable() {
            @Override
            public void run() {
                TextComponent text;
                if(isPvpEnabled) {
                    text = Component.text("PVP engedélyezett", NamedTextColor.DARK_GREEN);
                }
                else {
                    text = Component.text("PVP kikapcsolva", NamedTextColor.DARK_RED);
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendActionBar(text);
                }
            }
        }.runTaskTimer(getPlugin(main.class), 0L, 20L); // run every 20 ticks (1 second)
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!isTimerRunning) {
            hotBarPvpStatus();
            isTimerRunning = true;
        }
        if(!isPvpEnabled) {
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                final TextComponent textComponent = Component.text("Most nem tudsz PVP-zni!", NamedTextColor.DARK_RED);
                event.getDamager().sendMessage(textComponent);
                event.setDamage(0);
                event.setCancelled(true);
            }
            if (event.getDamager() instanceof Projectile) {
                ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
                if (shooter instanceof Player && event.getEntity() instanceof Player) {
                    final TextComponent textComponent = Component.text("Most nem tudsz PVP-zni!", NamedTextColor.DARK_RED);
                    ((Player) shooter).getPlayer().sendMessage(textComponent);
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }
        }
    }
}