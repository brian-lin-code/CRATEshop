package me.cratesmp.crateshop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ShopGUI {
    public record ShopItem(Material material, String name, double price, double sellPrice, int slot) {}

    private final CRATEShop plugin;
    private final Map<Integer, ShopItem> items = new LinkedHashMap<>();

    public ShopGUI(CRATEShop plugin) { this.plugin = plugin; loadItems(); }

    public void loadItems() {
        items.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("shop.items");
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            String path = "shop.items." + key;
            Material material = Material.matchMaterial(plugin.getConfig().getString(path + ".material", "STONE"));
            if (material == null) material = Material.STONE;
            String name = plugin.getConfig().getString(path + ".name", key);
            double price = plugin.getConfig().getDouble(path + ".price", 0);
            double sell = plugin.getConfig().getDouble(path + ".sell-price", price / 4);
            int slot = plugin.getConfig().getInt(path + ".slot", 0);
            items.put(slot, new ShopItem(material, name, price, sell, slot));
        }
    }

    public void open(Player player) {
        String title = color(plugin.getConfig().getString("shop.title", "&8CRATE SMP Shop"));
        int size = Math.max(9, Math.min(54, plugin.getConfig().getInt("shop.size", 54)));
        Inventory inv = plugin.getServer().createInventory(null, size, title);
        for (ShopItem shopItem : items.values()) {
            if (shopItem.slot() >= size) continue;
            ItemStack stack = new ItemStack(shopItem.material());
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(color(shopItem.name()));
            meta.setLore(java.util.List.of(
                    color("&7Buy: &a$" + money(shopItem.price())),
                    color("&7Sell: &c$" + money(shopItem.sellPrice())),
                    "",
                    color("&eLeft-click &7to buy 1"),
                    color("&eRight-click &7to sell 1")
            ));
            stack.setItemMeta(meta);
            inv.setItem(shopItem.slot(), stack);
        }
        player.openInventory(inv);
    }

    public ShopItem getItem(int slot) { return items.get(slot); }
    public String title() { return color(plugin.getConfig().getString("shop.title", "&8CRATE SMP Shop")); }
    public CRATEShop plugin() { return plugin; }

    private static String color(String text) { return ChatColor.translateAlternateColorCodes('&', text); }
    private static String money(double value) { return String.format(java.util.Locale.US, "%,.0f", value); }
}
