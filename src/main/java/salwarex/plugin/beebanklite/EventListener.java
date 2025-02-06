package salwarex.plugin.beebanklite;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import salwarex.plugin.beebanklite.Interfaces.TransactionsInterface;
import salwarex.plugin.beebanklite.Utils.Database.Database;
import salwarex.plugin.beebanklite.Utils.text;

public class EventListener implements Listener{

    private final Database db = MainClass.getDatabase();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!db.dataExists("bbl_accounts", "holder", e.getPlayer().getName().toLowerCase())) {
            db.addData("bbl_accounts", "holder", e.getPlayer().getName().toLowerCase());
        }
    }

    @EventHandler
    public void onClickGUI_Transactions(InventoryClickEvent e){
        String[] splitted = e.getView().getTitle().split(" ");
        String[] trnsSplitted = text.translate("transactionHistoryAmount").split(" ");

        if(splitted[0].equalsIgnoreCase(trnsSplitted[0])){
            e.setCancelled(true);
            if(e.getCurrentItem() != null){
                if(e.getCurrentItem().getType() == Material.RED_WOOL || e.getCurrentItem().getType() == Material.GREEN_WOOL){
                    int pageNumber = Integer.parseInt(ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getLore().get(e.getInventory().getItem(4).getItemMeta().getLore().size()-1)));
                    String playerNickname = ChatColor.stripColor(splitted[3]);
                    switch (e.getCurrentItem().getType()){
                        case RED_WOOL:
                            pageNumber -= 1; break;
                        case GREEN_WOOL:
                            pageNumber += 1; break;
                        default: break;
                    }
                    e.getWhoClicked().closeInventory();
                    new TransactionsInterface((Player) e.getWhoClicked(), playerNickname, pageNumber);
                }
            }
        }
    }

}
