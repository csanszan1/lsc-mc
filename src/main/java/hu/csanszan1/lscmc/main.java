package hu.csanszan1.lscmc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Listener dummyListener = new Listener() {
        };
        pvp pvpClass = new pvp();
        tpa tpaClass = new tpa();
        spectate spectateClass = new spectate();
        zoneManager zoneMgr = new zoneManager(getDataFolder());

        teamManager = new TeamManager();
        TeamCommands teamCommands = new TeamCommands(teamManager);

        this.getCommand("lschelp").setExecutor(new help());
        this.getCommand("pvp").setExecutor(pvpClass);
        this.getCommand("tpa").setExecutor(tpaClass);
        this.getCommand("tpaccept").setExecutor(tpaClass);
        this.getCommand("tpdeny").setExecutor(tpaClass);
        this.getCommand("spectate").setExecutor(spectateClass);
        this.getCommand("protect").setExecutor(new protect(zoneMgr));

        this.getCommand("plot").setExecutor(new PlotCommands());
        // Team commands
        this.getCommand("team create").setExecutor(teamCommands);
        this.getCommand("team add").setExecutor(teamCommands);
        this.getCommand("team remove").setExecutor(teamCommands);
        this.getCommand("team delete").setExecutor(teamCommands);
        this.getCommand("team list").setExecutor(teamCommands);

        getServer().getPluginManager().registerEvents(new ZoneProtectionListener(zoneMgr), this);
        getServer().getPluginManager().registerEvent(EntityDamageByEntityEvent.class, pvpClass, EventPriority.HIGH, new EventExecutor() {
            @Override
            public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
                if (event instanceof EntityDamageByEntityEvent) {
                    pvpClass.onEntityDamageByEntity((EntityDamageByEntityEvent) event);
                }
            }
        }, this);






    }
    @Override
    public void onLoad() {
        getPlugin(main.class).getComponentLogger().info("LSC-MC is loaded");
    }

    @Override
    public void onDisable() {
        getPlugin(main.class).getComponentLogger().info("LSC-MC is disabled");
    }

}
