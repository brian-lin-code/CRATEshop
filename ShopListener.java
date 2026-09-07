package me.cratesmp.crateshop;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public final class ShopListener implements Listener {
    private final CRATEShop plugin;
    private final ShopGUI gui;

    public ShopListener(CRATEShop plugin, ShopGUI gui) { this.plugin = plugin; this.gui = gui; }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(gui.title())) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;
        ShopGUI.ShopItem item = gui.getItem(event.getRawSlot());
        if (item == null) return;

        int amount = event.isShiftClick() ? 64 : 1;
        if (event.isRightClick()) {
            sell(player, item, amount);
        } else if (event.isLeftClick()) {
            buy(player, item, amount);
        }
    }

    private void buy(Player player, ShopGUI.ShopItem item, int amount) {
        double total = item.price() * amount;
        if (!withdraw(player, total)) {
            player.sendMessage(ChatColor.RED + "You need $" + money(total) + " to buy this.");
            return;
        }
        ItemStack stack = new ItemStack(item.material(), amount);
        java.util.Map<Integer, ItemStack> leftover = player.getInventory().addItem(stack);
        if (!leftover.isEmpty()) {
            deposit(player, total);
            player.sendMessage(ChatColor.RED + "Not enough inventory space.");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Bought " + amount + "x " + item.name() + ChatColor.GREEN + " for $" + money(total) + ".");
    }

    private void sell(Player player, ShopGUI.ShopItem item, int amount) {
        int available = count(player, item.material());
        int sold = Math.min(amount, available);
        if (sold <= 0) { player.sendMessage(ChatColor.RED + "You don't have that item."); return; }
        remove(player, item.material(), sold);
        double total = item.sellPrice() * sold;
        deposit(player, total);
        player.sendMessage(ChatColor.GREEN + "Sold " + sold + "x " + item.name() + ChatColor.GREEN + " for $" + money(total) + ".");
    }

    // Vault is accessed reflectively so the project does not bundle or depend on Vault at compile time.
    private boolean withdraw(Player player, double amount) { return economyCall("withdrawPlayer", player, amount); }
    private void deposit(Player player, double amount) { economyCall("depositPlayer", player, amount); }

    private boolean economyCall(String method, Player player, double amount) {
        try {
            Class<?> rsp = Class.forName("net.milkbowl.vault.economy.EconomyResponse");
            Object economy = getEconomy();
            if (economy == null) { player.sendMessage(ChatColor.RED + "Economy/Vault is not available."); return false; }
            Object response = economy.getClass().getMethod(method, Player.class, double.class).invoke(economy, player, amount);
            return method.startsWith("deposit") || ((Boolean) rsp.getMethod("transactionSuccess").invoke(response));
        } catch (Exception ex) { player.sendMessage(ChatColor.RED + "Economy error. Check Vault setup."); return false; }
    }

    private Object getEconomy() throws Exception {
        Object registration = plugin.getServer().getServicesManager().getRegistration(Class.forName("net.milkbowl.vault.economy.Economy"));
        return registration == null ? null : registration.getClass().getMethod("getProvider").invoke(registration);
    }

    private static int count(Player p, org.bukkit.Material m) { int n=0; for(ItemStack s:p.getInventory().getContents()) if(s!=null&&s.getType()==m)n+=s.getAmount(); return n; }
    private static void remove(Player p, org.bukkit.Material m, int amount) { for(int i=0;i<p.getInventory().getSize()&&amount>0;i++){ItemStack s=p.getInventory().getItem(i); if(s==null||s.getType()!=m)continue; int take=Math.min(amount,s.getAmount()); s.setAmount(s.getAmount()-take); amount-=take; if(s.getAmount()<=0)p.getInventory().setItem(i,null);}}
    private static String money(double v){return String.format(java.util.Locale.US,"%,.0f",v);}
}
