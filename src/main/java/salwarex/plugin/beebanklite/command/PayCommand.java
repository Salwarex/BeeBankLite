package salwarex.plugin.beebanklite.command;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import salwarex.plugin.beebanklite.Bank.Account;
import salwarex.plugin.beebanklite.MainClass;
import salwarex.plugin.beebanklite.Reports;
import salwarex.plugin.beebanklite.Utils.CurrencyUtils;
import salwarex.plugin.beebanklite.Utils.text;

import java.util.ArrayList;
import java.util.List;

public class PayCommand extends AbstractCommand{
    public PayCommand(){
        super("pay");
    }


    @Override
    public void execute(CommandSender sender, String label, String[] args){
        if(args.length == 0){
            Reports.send(sender, text.translate("payCommandArgumentsError"));
            return;
        }
        int sum = 0;
        try{
            sum = Integer.parseInt(args[0]);
        }
        catch (Exception ex){
            Reports.send(sender, text.translate("incorrectValueError"));
            return;
        }

        if(args.length >= 2){
            String receiver = args[1];
            if(receiver.equalsIgnoreCase(sender.getName())){
                Reports.send(sender, text.translate("selfPaymentError"));
                return;
            }
            Account account = new Account(sender.getName());
            int result = account.pay(receiver, sum);
            if(result == 1){
                Reports.send(sender, text.translate("successTransfer").replace("{SUM}", sum + " " + CurrencyUtils.getCurrencySymbol(MainClass.getCurrencies().get(CurrencyUtils.selectedCurrency(sender.getName())))).replace("{RECEIVER}", receiver));
                if(Bukkit.getPlayer(receiver) != null){
                    Reports.send(Bukkit.getPlayer(receiver), text.translate("receivedTransfer").replace("{SUM}", sum + " " + CurrencyUtils.getCurrencySymbol(MainClass.getCurrencies().get(CurrencyUtils.selectedCurrency(sender.getName())))).replace("{SENDER}", sender.getName()));
                }
            } else if (result == -1) {
                Reports.send(sender, text.translate("noSuchPlayerError"));
            } else if (result == 0) {
                Reports.send(sender, text.translate("notEnoughMoneyError").replace("{CURRENCY-NAME}", CurrencyUtils.getCurrencyName(MainClass.getCurrencies().get(CurrencyUtils.selectedCurrency(sender.getName())))));
            }
            return;
        }
        sender.sendMessage(text.translate("unknownCommand"));
    }

    ArrayList<String> playerList(){
        ArrayList<String> arr = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            arr.add(player.getName());
        }
        return arr;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args){
        if(args.length == 1) return Lists.newArrayList("8", "16", "32", "64");
        if(args.length == 2) return playerList();
        return Lists.newArrayList();
    }



}
