package hu.csanszan1.lscmc;

import net.kyori.adventure.text.*;
import org.bukkit.*;
import java.util.*;

public class TeamCommands implements CommandExecutor {
    private final TeamManager teamManager;

    public TeamCommands(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase();

        if (cmd.equals("team create")) {
            if (args.length < 1) {
                sender.sendMessage("[LSC-MC] Használat: /team create <csapat neve>");
                return true;
            }

            String teamName = args[0];
            if (teamManager.createTeam(teamName, sender.getName())) {
                sender.sendMessage(Component.text("[LSC-MC] Már létezik ilyen csapat!", NamedTextColor.DARK_RED));
            }
            return true;
        }

        if (cmd.equals("team add")) {
            if (args.length < 2) {
                sender.sendMessage("[LSC-MC] Használat: /team add <csapat neve> <játékos neve>");
                return true;
            }

            String teamName = args[0];
            String playerName = args[1];

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

        if (cmd.equals("team list")) {
            sender.sendMessage(Component.text("[LSC-MC] === CSAPATOK ===", NamedTextColor.GOLD));

            if (teamManager.getAllTeams().isEmpty()) {
                sender.sendMessage(Component.text("[LSC-MC] Nincsenek csapatok", NamedTextColor.YELLOW));
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

        return false;
    }
}