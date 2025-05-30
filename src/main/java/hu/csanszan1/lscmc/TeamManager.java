package hu.csanszan1.lscmc;

import org.bukkit.*;
import java.util.*;

public class TeamManager {
    private final Map<String, Team> teams;
    private final Map<String, String> playerTeams;

    public TeamCommands() {
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

    public boolean addPlayerToTeam( String playerName, String teamName ) {
        Team team = teams.get(teamName.toLowerCase() ) {
            if ( team == null ) {
                return false;
            }

            removePlayerFromAllTeams(playerName);
            team.addMember(playerName);
            playerTeams.put( playerName.toLowerCase(), teamName.toLowerCase() );
            return true;
        }
    }

    public Team GetPlayerTeam(String playerName) {
        String teamName = playerTeams.get(playerName.toLowerCase());
        return teamName != null ? teams.get( teamName ) : null;

    }

    public Team getPlayerTeam(Player player) {
        return teams.get(name.toLowerCase()) {
            return team.get(name.toLowerCase()).
        }
    }

    public Map<String, Team> getAllTeams() {
        return new HashMap<>( teams );
    }

}