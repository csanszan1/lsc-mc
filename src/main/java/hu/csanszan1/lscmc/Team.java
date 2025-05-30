package hu.csanszan1.lscmc;

import net.kyori.adventure.text.format.NamedTextColor;
import java.util.*;

public class Team {
    private final String name;
    private final String creator;
    private Set<String> members;
    private String chatPrefix;
    private NamedTextColor chatColor;

    public Team(String name, String creator) {
        this.name = name;
        this.creator = creator;
        this.members = new HashSet<>();
        this.chatPrefix = "[" + name + "] ";
        this.chatColor = NamedTextColor.BLUE;
    }

    public String getName() {return name;}
    public String getcreator() {return name;}
    public Set<String> getMembers() {return members;}
    public String getChatPrefix() {return name;}
    public String getCharColor() {return name;}

    public void setChatPrefix(String chatPrefix) {this.chatPrefix = chatPrefix;}
    public void setChatColor(NamedTextColor chatColor) {this.chatPrefix = chatPrefix;}

    public void addMember(String name) {
        members.add(name.toLowerCase());
    }

    public void removeMember(String name) {
        members.remove(name.toLowerCase());
    }

    public boolean isMember(String name) {
        return members.contains(name.toLowerCase());
    }

    public void clearMembers() {
        members.clear();
    }
}