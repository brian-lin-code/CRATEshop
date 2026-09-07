package com.cratesmp.crateshop.shop;

import com.cratesmp.crateshop.CRATEShop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.ConfigurationSection;
import java.util.*;

public final class ShopManager {
    private final CRATEShop plugin;
    private final Map<String, ShopItem> items = new LinkedHashMap<>();
    private Economy economy;
    public ShopManager(CRATEShop plugin) { this.plugin = plugin; load(); }
    public void load() {
        items.clear();
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("items");
        if (sec != null) for (String key : sec.getKeys(false)) {
            String path = "items." + key;
            Material mat = Material.matchMaterial(plugin.getConfig().getString(path + ".material", "STONE"));
            if (mat == null) continue;
            items.put(key, new ShopItem(key, mat, plugin.getConfig().getString(path+".display", key), plugin.getConfig().getDouble(path+".buy"), plugin.getConfig().getDouble(path+".sell"), plugin.getConfig().getInt(path+".slot", 0)));
        }
        setupEconomy();
    }
    private void setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) { economy = null; return; }
        var rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        economy = rsp == null ? null : rsp.getProvider();
    }
    public Collection<ShopItem> items() { return Collections.unmodifiableCollection(items.values()); }
    public boolean buy(org.bukkit.entity.Player p, ShopItem item, int amount) {
        if (amount <= 0 || economy == null) return false;
        double cost = item.buy() * amount;
        if (!economy.has(p, cost)) return false;
        var stack = new ItemStack(item.material(), amount);
        if (p.getInventory().firstEmpty() == -1 && !p.getInventory().addItem(stack).isEmpty()) return false;
        economy.withdrawPlayer(p, cost); return true;
    }
    public boolean sell(org.bukkit.entity.Player p, ShopItem item, int amount) {
        if (amount <= 0 || economy == null) return false;
        int owned = count(p, item.material());
        if (owned < amount) return false;
        remove(p, item.material(), amount); economy.depositPlayer(p, item.sell() * amount); return true;
    }
    private int count(org.bukkit.entity.Player p, Material m) { int n=0; for (ItemStack s:p.getInventory().getContents()) if(s!=null&&s.getType()==m)n+=s.getAmount(); return n; }
    private void remove(org.bukkit.entity.Player p, Material m, int amount) { int left=amount; for(int i=0;i<p.getInventory().getSize()&&left>0;i++){ItemStack s=p.getInventory().getItem(i); if(s==null||s.getType()!=m)continue; int take=Math.min(left,s.getAmount()); if(take==s.getAmount())p.getInventory().setItem(i,null);else s.setAmount(s.getAmount()-take);left-=take;}}
    public Economy economy(){return economy;}
    public void close(){}
    public record ShopItem(String key, Material material, String display, double buy, double sell, int slot) {}
}
