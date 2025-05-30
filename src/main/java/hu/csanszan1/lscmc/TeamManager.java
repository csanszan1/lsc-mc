package hu.csanszan1.lscmc;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class TeamManager implements Listener {
    private final Map<String, Team> teams;
    private final Map<String, String> playerTeams;

    public TeamManager() {
        this.teams = new HashMap<>();
        this.playerTeams = new HashMap<>();
    }

    public boolean createTeam( String name, String creator ) {
        if ( teams.containsKey( name.toLowerCase() ) ) {
            return false;
        }

        Team team = new Team(name, creator);
        teams.put( name.toLowerCase(), team );
        return true;
    }

    public Set<String> getMembers(String teamName) {
        for(Team team : teams.values()) {
            if(team.getName().equals(teamName)) {
                return team.getMembers();
            }
        }
        return null;
    }

    public boolean addPlayerToTeam( String playerName, String teamName ) {
        Team team = teams.get(teamName.toLowerCase());
            if ( team == null ) {
                return false;
            }

            //removePlayerFromAllTeams(playerName);
            team.addMember(playerName);
            playerTeams.put( playerName.toLowerCase(), teamName.toLowerCase() );
            return true;

    }

    public Team GetPlayerTeam(String playerName) {
        String teamName = playerTeams.get(playerName.toLowerCase());
        return teamName != null ? teams.get( teamName ) : null;

    }

    public Team getPlayerTeam(Player player) {
        String name = player.getName();
        for(Team team : teams.values()) {
            if(team.isMember(name)) {
                return team;
            }
        }
        return null;
    }

    public Map<String, Team> getAllTeams() {
        return new HashMap<>( teams );
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        if(getPlayerTeam(player) == null) {
            return;
        }

        String tag = "["+getPlayerTeam(player).getName()+"] ";
        String message = event.getMessage();

        event.setFormat(ChatColor.AQUA + tag + ChatColor.RESET + player.getName()+ " > " + message);
    }

}