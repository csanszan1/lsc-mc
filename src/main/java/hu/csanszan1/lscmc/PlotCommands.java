package hu.csanszan1.lscmc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlotCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0 && Objects.equals(args[0], "tp")) {
            Player player = (Player) sender;
            World plotWorld = Bukkit.getWorld("plotworld");
            Location spawn = plotWorld.getSpawnLocation();
            player.teleport(spawn);
        }
        return true;
    }
}