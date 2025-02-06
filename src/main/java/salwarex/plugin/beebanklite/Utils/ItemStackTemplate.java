package salwarex.plugin.beebanklite.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class ItemStackTemplate {
    ItemStack is = new ItemStack(Material.BARRIER, 1, (short) 3);

    public ItemStackTemplate(){
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName("§cUnknown item!");
        is.setItemMeta(meta);
    }

    public ItemStackTemplate(String title, Material material, String ... lores){
        title = title.replace("&", "§");
        is = new ItemStack(material, 1, (short) 3);
        ItemMeta meta = is.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        meta.setDisplayName(title);
        for(String loreC: lores){
            loreC = loreC.replace("&", "§");
            lore.add(loreC);
        }
        meta.setLore(lore);
        is.setItemMeta(meta);
    }

    public void setHead(String name){
        if(is.getType() == Material.PLAYER_HEAD){
            SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
            skullMeta.setOwner(name);
            is.setItemMeta(skullMeta);
        }
    }

    public ItemStack get(){
        return is;
    }
}
