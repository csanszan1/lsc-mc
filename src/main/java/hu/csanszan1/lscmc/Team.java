package hu.csanszan1.lscmc;

import net.kyori.adventure.text.format.NamedTextColor;
import java.util.*;

public class Team {
    private final String name;
    private final String creater;
    private Set<String> members;
    private String chatPrefix;
    private NamedTextColor chatColor;

    public Team(String name, String creater) {
        this.name = name;
        this.creater = creater;
        this.members = new HashSet<>();
        this.chatPrefix = "[" + name + "] ";
        this.chatColor = NamedTextColor.BLUE;
    }

    public String getName() {return name;}
    public String getCreater() {return name;}
    public String getMembers() {return name;}
    public String getChatPrefix() {return name;}
    public String getCharColor() {return name;}

    public void setChatPrefix(String chatPrefix) {this.chatPrefix = chatPrefix;}
    public void setChatColor(NamedTextColor chatColor) {this.chatPrefix = chatPrefix}

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