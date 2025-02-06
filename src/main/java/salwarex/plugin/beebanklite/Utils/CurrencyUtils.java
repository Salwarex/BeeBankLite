package salwarex.plugin.beebanklite.Utils;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import salwarex.plugin.beebanklite.MainClass;

public class CurrencyUtils {
    static FileConfiguration config = MainClass.getInstance().getConfig();

    public static int selectedCurrency(String playerName){
        int selecteditem = (Integer) MainClass.getDatabase().getData("default_currency_id", "bbl_accounts", "holder", playerName.toLowerCase());
        return selecteditem;
    }

    public static boolean isExists(String name){
        if(config.getConfigurationSection("currency." + name) != null){
            return true;
        }
        return false;
    }

    public static String getCurrencyName(String name){
        if(config.getString("currency." + name + ".custom-name") != null){
            return config.getString("currency." + name + ".custom-name").replace("&", "§");
        }
        return null;
    }

    public static String getCurrencySymbol(String name){
        if(config.getString("currency." + name + ".symbol") != null){
            return config.getString("currency." + name + ".symbol").replace("&", "§");
        }
        return null;
    }

    public static Material getCurrencyMaterial(String name){
        if(isExists(name)){
            return Material.valueOf(config.getString("currency." + name + ".type"));
        }
        return Material.BARRIER;
    }

    public static void nextMainCurrency(String holder){
        int now = selectedCurrency(holder);
        int max = MainClass.getCurrencies().size() - 1;
        int upd = selectedCurrency(holder);
        if(now < max){
            upd++;
        } else if (now == max) {
            upd = 0;
        }

        if(now != upd){
            MainClass.getDatabase().updateData("bbl_accounts", "default_currency_id", "holder", holder.toLowerCase(), upd);
        }
    }

}
