package me.cratesmp.crateshop;

import org.bukkit.plugin.java.JavaPlugin;

public final class CRATEShop extends JavaPlugin {
    private ShopGUI shopGUI;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        shopGUI = new ShopGUI(this);
        getCommand("shop").setExecutor(new ShopCommand(this, shopGUI));
        getServer().getPluginManager().registerEvents(new ShopListener(this, shopGUI), this);
        getLogger().info("CRATEShop 1.0.0 enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CRATEShop disabled.");
    }

    public void reloadShop() {
        reloadConfig();
        shopGUI.loadItems();
    }
}
