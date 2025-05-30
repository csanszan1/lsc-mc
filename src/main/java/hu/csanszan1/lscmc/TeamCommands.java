package hu.csanszan1.lscmc;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamCommands implements CommandExecutor {
    private final TeamManager teamManager;

    public TeamCommands(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();

        if (args[0].equals("create")) {
            if (args.length < 1) {
                sender.sendMessage("[LSC-MC] Használat: /team create <csapat neve>");
                return true;
            }

            String teamName = args[1];
            if (!teamManager.createTeam(teamName, sender.getName())) {
                sender.sendMessage(Component.text("[LSC-MC] Már létezik ilyen csapat!", NamedTextColor.DARK_RED));
            }
            teamManager.addPlayerToTeam(sender.getName(), teamName);
            sender.sendMessage(Component.text("[LSC-MC] ").append(Component.text("A ", NamedTextColor.YELLOW)
                    .append(Component.text(teamName, NamedTextColor.GOLD)
                    .append(Component.text(" csapat létrehozva", NamedTextColor.YELLOW)))));
            return true;
        }

        else if (args[0].equals("add")) {
            if (args.length < 3) {
                sender.sendMessage("[LSC-MC] Használat: /team add <csapat neve> <játékos neve>");
                return true;
            }

            String teamName = args[1];
            String playerName = args[2];

            Player target = Bukkit.getPlayerExact(playerName);
            if (target == null) {
                sender.sendMessage(Component.text("[LSC-MC] Játékos nem található: ", NamedTextColor.DARK_RED)
                        .append(Component.text(playerName, NamedTextColor.GOLD)));
                return true;
            }

            if (teamManager.addPlayerToTeam(playerName, teamName)) {
                sender.sendMessage(Component.text("[LSC-MC] ", NamedTextColor.YELLOW)
                        .append(Component.text(playerName, NamedTextColor.GOLD))
                        .append(Component.text(" hozzáadva a ", NamedTextColor.YELLOW))
                        .append(Component.text(teamName, NamedTextColor.GOLD))
                        .append(Component.text(" csapathoz", NamedTextColor.YELLOW)));
            } else {
                sender.sendMessage(Component.text("[LSC-MC] Nem található csapat: ", NamedTextColor.DARK_RED)
                        .append(Component.text(teamName, NamedTextColor.GOLD)));
            }
            return true;
        }

        else if (args[0].equals("list")) {
            sender.sendMessage(Component.text("[LSC-MC] === CSAPATOK ===", NamedTextColor.GOLD));

            if (teamManager.getAllTeams().isEmpty()) {
                sender.sendMessage(Component.text("[LSC-MC] Nincsenek csapatok", NamedTextColor.YELLOW));
                return true;
            }

            if(args.length == 2) {
                String names = "";
                for (String playerName : teamManager.getMembers(args[1])) {
                    names += playerName+"\n";
                }
                if(names.isEmpty()) {
                    sender.sendMessage(Component.text("[LSC-MC] - Nincsenek tagjai ennek a csapatnak", NamedTextColor.YELLOW));
                    return true;
                }
                names = names.strip();
                sender.sendMessage(Component.text("[LSC-MC] - A csapat tagjai\n", NamedTextColor.YELLOW)
                        .append(Component.text(names, NamedTextColor.GOLD)));
                return true;
            }

            for (Team team : teamManager.getAllTeams().values()) {
                sender.sendMessage(Component.text("[LSC-MC] - ", NamedTextColor.YELLOW)
                        .append(Component.text(team.getName(), NamedTextColor.GOLD))
                        .append(Component.text(" (", NamedTextColor.YELLOW))
                        .append(Component.text(team.getMembers().size(), NamedTextColor.GOLD))
                        .append(Component.text(" tag)", NamedTextColor.YELLOW)));
            }
            return true;
        }

        return true;
    }
}