package com.cratesmp.crateshop;

import com.cratesmp.crateshop.command.CrateShopCommand;
import com.cratesmp.crateshop.gui.ShopMenu;
import com.cratesmp.crateshop.listener.ShopListener;
import com.cratesmp.crateshop.shop.ShopManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CRATEShop extends JavaPlugin {
    private ShopManager shopManager;
    private ShopMenu shopMenu;

    @Override public void onEnable() {
        saveDefaultConfig();
        shopManager = new ShopManager(this);
        shopMenu = new ShopMenu(this, shopManager);
        getServer().getPluginManager().registerEvents(new ShopListener(this, shopMenu, shopManager), this);
        var command = new CrateShopCommand(this, shopMenu, shopManager);
        getCommand("crateshop").setExecutor(command);
        getCommand("crateshop").setTabCompleter(command);
        getCommand("shop").setExecutor((sender, cmd, label, args) -> { if (sender instanceof org.bukkit.entity.Player p) shopMenu.open(p); else sender.sendMessage("Players only."); return true; });
        getLogger().info("CRATEShop 1.0.0 enabled for Purpur 26.2 / Java 25.");
    }
    @Override public void onDisable() { if (shopManager != null) shopManager.close(); }
    public void reloadShop() { reloadConfig(); shopManager.load(); }
    public ShopManager getShopManager() { return shopManager; }
}
