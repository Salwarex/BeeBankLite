package salwarex.plugin.beebanklite.Utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import salwarex.plugin.beebanklite.Bank.Account;
import salwarex.plugin.beebanklite.MainClass;

public class PlaceholderClass extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "beebanklite";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Salwarex";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.9.9";
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if(player == null){
            return "";
        }
        if(params.equals("mainbalance")){
            Account account = new Account(player.getName());
            String mainCurrency = MainClass.getCurrencies().get(CurrencyUtils.selectedCurrency(player.getName()));
            return String.valueOf(account.getAmount(mainCurrency) + " " + CurrencyUtils.getCurrencySymbol(mainCurrency));
        }
        return null;
    }
}
