package salwarex.plugin.beebanklite.Interfaces;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import salwarex.plugin.beebanklite.Bank.Account;
import salwarex.plugin.beebanklite.MainClass;
import salwarex.plugin.beebanklite.Utils.CurrencyUtils;
import salwarex.plugin.beebanklite.Utils.Interface;
import salwarex.plugin.beebanklite.Utils.ItemStackTemplate;
import salwarex.plugin.beebanklite.Utils.text;

import java.util.ArrayList;

public class BankInterface extends Interface {
    public BankInterface(Player player) {
        super(player, text.translate("bankOperationsTitle"), 45);
        ItemStack filler = new ItemStackTemplate("&f", Material.ORANGE_STAINED_GLASS_PANE).get();

        for(int i : new int[]{0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44}){
            gui.setItem(i, filler);
        }

        String curr = MainClass.getCurrencies().get(CurrencyUtils.selectedCurrency(player.getName()));

        ArrayList<String> mainlore = new ArrayList<>();
        mainlore.add(text.translate("bankMenuMain.lore1"));
        mainlore.add(text.translate("bankMenuMain.lore2"));
        int i = 0;
        for(String item : MainClass.getCurrencies()){
            if(i == CurrencyUtils.selectedCurrency(player.getName())){
                mainlore.add("&a| &f" + CurrencyUtils.getCurrencyName(item) + " " + text.translate("bankMenuMain.selected"));
            }
            else{
                mainlore.add("&7| &f" + CurrencyUtils.getCurrencyName(item));
            }
            i++;
        }

        String[] arrayLore = new String[mainlore.size()];
        mainlore.toArray(arrayLore);

        int selectedBalance = (new Account(player.getName())).getAmount(curr);
        ItemStack selected = new ItemStackTemplate(text.translate("bankMenuMain.yourBalance").replace("{BALANCE}", selectedBalance + " " + CurrencyUtils.getCurrencySymbol(curr)) , CurrencyUtils.getCurrencyMaterial(curr), arrayLore).get();

        gui.setItem(4, selected);

        ItemStack withdraw = new ItemStackTemplate(text.translate("bankMenuWithdraw.title"), Material.RED_TERRACOTTA, text.translate("bankMenuWithdraw.lore1"), text.translate("bankMenuWithdraw.lore2")).get();
        ItemStack deposite = new ItemStackTemplate(text.translate("bankMenuDeposit.title"), Material.GREEN_TERRACOTTA, text.translate("bankMenuDeposit.lore1"), text.translate("bankMenuDeposit.lore2")).get();

        gui.setItem(20, withdraw); gui.setItem(24, deposite);
        player.openInventory(gui);
    }
}

