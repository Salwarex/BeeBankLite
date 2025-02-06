package salwarex.plugin.beebanklite.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Interface {
    protected Inventory gui;
    ItemStack nextPage = new ItemStack(Material.GREEN_WOOL, 1);
    ItemStack prevPage = new ItemStack(Material.RED_WOOL, 1);
    Player player;

    public Interface(Player _player, String name, int Slots){
        name = name.replace("&", "§");
        gui = Bukkit.createInventory(player, Slots, name);
        player = _player;
    }

    void SetNamesPaging(){
        ItemMeta nM = nextPage.getItemMeta();
        ItemMeta pM = prevPage.getItemMeta();

        nM.setDisplayName(text.translate("nextPage"));
        pM.setDisplayName(text.translate("prevPage"));

        nextPage.setItemMeta(nM); prevPage.setItemMeta(pM);
    }

    public Inventory ItemsList(ItemStack mainItemStack, int _page, ItemStack ... stacks){
        int maxPerPage = 45;
        int page = _page;
        ArrayList<String> lore = new ArrayList<>();
        if(mainItemStack.getItemMeta().getLore() != null){
           lore = new ArrayList<String>(mainItemStack.getItemMeta().getLore());
        }
        lore.add("§8" + page);
        ItemMeta itemMeta = mainItemStack.getItemMeta();
        itemMeta.setLore(lore);
        mainItemStack.setItemMeta(itemMeta);

        gui.setItem(4, mainItemStack);
        if(stacks.length <= 45){
            for(int i = 0; i < stacks.length; i++){
                gui.setItem(i + 9, stacks[i]);
            }
        }
        else{
            maxPerPage = 36;
            for(int i = (maxPerPage*page); i < Math.min((maxPerPage*(page+1)), stacks.length); i++){
                gui.setItem(i + 9 - (page*maxPerPage), stacks[i]);
            }
            SetNamesPaging();
            if(page != 0){
                gui.setItem(45, prevPage);
            }
            if(page != Math.floor(stacks.length / maxPerPage)){
                gui.setItem(53, nextPage);
            }

        }

        return gui;
    }

    public void openGUI(){
        player.openInventory(gui);
    }
}

