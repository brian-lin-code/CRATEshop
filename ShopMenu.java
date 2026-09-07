package com.cratesmp.crateshop.gui;

import com.cratesmp.crateshop.CRATEShop;
import com.cratesmp.crateshop.shop.ShopManager;
import com.cratesmp.crateshop.shop.ShopManager.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public final class ShopMenu {
    public static final String PREFIX="§6CRATEShop §8» §r";
    private final CRATEShop plugin; private final ShopManager manager;
    public ShopMenu(CRATEShop p, ShopManager m){plugin=p;manager=m;}
    public void open(Player p){
        String title=ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("settings.title","&8Shop"));
        Inventory inv=Bukkit.createInventory(new ShopHolder(),54,title);
        for(ShopItem it:manager.items()){ItemStack s=new ItemStack(it.material());ItemMeta meta=s.getItemMeta();meta.setDisplayName(color(it.display()));meta.setLore(List.of("§7Buy: §a$"+fmt(it.buy()),"§7Sell: §c$"+fmt(it.sell()),"","§eLeft-click §7to buy 1","§eRight-click §7to sell 1","§8Shift-click: x16"));s.setItemMeta(meta);inv.setItem(it.slot(),s);} p.openInventory(inv);
    }
    private String color(String s){return ChatColor.translateAlternateColorCodes('&',s);} private String fmt(double d){return d%1==0?String.format("%,.0f",d):String.format("%,.2f",d);}
    public static class ShopHolder implements InventoryHolder {public Inventory getInventory(){return null;}}
    public ShopItem itemAt(Inventory inv,int slot){for(ShopItem i:manager.items())if(i.slot()==slot)return i;return null;}
}
