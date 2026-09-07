package me.cratesmp.crateshop;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ShopCommand implements CommandExecutor {
    private final CRATEShop plugin;
    private final ShopGUI gui;

    public ShopCommand(CRATEShop plugin, ShopGUI gui) { this.plugin = plugin; this.gui = gui; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("crateshop.reload")) { sender.sendMessage(ChatColor.RED + "No permission."); return true; }
            plugin.reloadShop();
            sender.sendMessage(ChatColor.GREEN + "CRATEShop reloaded.");
            return true;
        }
        if (!(sender instanceof Player player)) { sender.sendMessage("Players only."); return true; }
        if (!player.hasPermission("crateshop.use")) { player.sendMessage(ChatColor.RED + "No permission."); return true; }
        gui.open(player);
        return true;
    }
}
