package hu.csanszan1.lscmc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class help implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("""
                /lsc help - Kiírja ezt a listát
                /pvp - Ki/be lehet kapcsolni a pvp-t
                /protect <poz1|poz2|megbíz|reload> - Teület levédése
                /spectate
                /team - Csapat kezelő parancsok
                /tpa <Játékos> - Teleport kérelem
                """);
        return true;
    }
}
