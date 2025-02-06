package salwarex.plugin.beebanklite.Interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import salwarex.plugin.beebanklite.Bank.Account;
import salwarex.plugin.beebanklite.MainClass;
import salwarex.plugin.beebanklite.Reports;
import salwarex.plugin.beebanklite.Utils.CurrencyUtils;
import salwarex.plugin.beebanklite.Utils.text;


public class BankInterfaceEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getView().getTitle().toString().equalsIgnoreCase(text.translate("bankOperationsTitle"))){
            e.setCancelled(true);
            if(e.getCurrentItem() != null){
                Player player = (Player) e.getWhoClicked();
                if(e.getSlot() == 4){
                    e.getWhoClicked().closeInventory();
                    CurrencyUtils.nextMainCurrency(player.getName());
                    BankInterface bi = new BankInterface(player);
                }
                if(e.getSlot() == 20){
                    MainClass.getInstance().withdrawers.add(player);
                    Reports.send(player, text.translate("writeAmountChat"));
                    e.getWhoClicked().closeInventory();
                }
                if(e.getSlot() == 24){
                    e.getWhoClicked().closeInventory();
                    Account account = new Account(player.getName());
                    int result = account.deposite();
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(MainClass.getInstance().withdrawers.contains(e.getPlayer())){
            try {
                e.setCancelled(true);
                int sum = Integer.parseInt(e.getMessage());
                Account account = new Account(e.getPlayer().getName());
                account.withdraw(sum);
                MainClass.getInstance().withdrawers.remove(e.getPlayer());
                return;
            } catch (Exception ex) {
                Reports.send(e.getPlayer(), text.translate("incorrectValueError"));
                return;
            }
        }
    }

}
