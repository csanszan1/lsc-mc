package hu.csanszan1.lscmc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Iterator;
import java.util.List;

public class ZoneProtectionListener implements Listener {
    private final zoneManager zoneMgr;

    public ZoneProtectionListener(zoneManager zoneManager) {
        this.zoneMgr = zoneManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!zoneMgr.canModify(event.getBlock().getLocation(), event.getPlayer().getName())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Itt nem törhetsz ki blokkot.");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!zoneMgr.canModify(event.getBlock().getLocation(), event.getPlayer().getName())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Itt nem helyezhetsz el blokkot.");
        }
    }

    private boolean isProtectedBlock(Material type) {
        return type.name().contains("CHEST")
                || type.name().contains("FURNACE")
                || type.name().contains("SHULKER_BOX")
                || type.name().contains("DOOR")
                || type.name().contains("TRAPDOOR")
                || type.name().contains("FENCE_GATE")
                || type.name().contains("BUTTON")
                || type.name().contains("LEVER")
                || type.name().contains("BARREL");
    }

    @EventHandler
    public void onBlockUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block clicked = event.getClickedBlock();
        if(clicked == null) {
            return;
        }
        if(isProtectedBlock(clicked.getType())) {
            if (!zoneMgr.canModify(clicked.getLocation(), event.getPlayer().getName())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("Ez az interakció itt nem engedélyezett.");
            }
        }

    }

    private String getExplosionSourceName(Entity entity) {
        if (entity instanceof TNTPrimed tnt) {
            if (tnt.getSource() instanceof Player player) {
                if(player.hasPermission("lsc.overrideProtect")) {
                    return null;
                }
                return player.getName();
            }
        } else if (entity instanceof Creeper || entity instanceof Explosive) {
            // Could optionally allow creepers if you want, or use mob ownership logic
            return "unknown"; // Block by default if not trusted
        }
        return null;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        String sourceName = getExplosionSourceName(event.getEntity());

        if (sourceName == null) return;

        protectExplosion(event.blockList(), sourceName, event);
    }

    private void protectExplosion(List<Block> blocks, String sourceName, EntityExplodeEvent event) {
        Iterator<Block> iter = blocks.iterator();
        boolean sentMessage = false;
        while (iter.hasNext()) {
            Block block = iter.next();
            Location loc = block.getLocation();
            zoneDefinition zone = zoneMgr.getZoneAt(loc);

            if (zone != null && !zone.getOwner().equalsIgnoreCase(sourceName) && !zone.isTrusted(sourceName)) {
                event.getEntity().remove();
                event.setCancelled(true);
                Player sourcePlayer = Bukkit.getPlayerExact(sourceName);
                if(sourcePlayer != null && !sentMessage) {
                    sourcePlayer.sendMessage("Ez a robbanás valaki területére esett volna, ezért nem engedélyezett.");
                    sentMessage = true;
                }
            }
        }
    }
}
