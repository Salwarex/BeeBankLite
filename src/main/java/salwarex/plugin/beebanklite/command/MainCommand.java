package salwarex.plugin.beebanklite.command;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import salwarex.plugin.beebanklite.Interfaces.BankInterface;
import salwarex.plugin.beebanklite.Interfaces.TransactionsInterface;
import salwarex.plugin.beebanklite.Reports;
import salwarex.plugin.beebanklite.MainClass;
import salwarex.plugin.beebanklite.Utils.text;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends AbstractCommand{
    public  MainCommand(){
        super("bank");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args){
        if(args.length == 0){
            if(!sender.getName().equalsIgnoreCase("CONSOLE")){
                new BankInterface(Bukkit.getPlayer(sender.getName()));
                return;
            }
        }
        if(args[0].equalsIgnoreCase("reload")){
            if(!sender.hasPermission("bbl.reload")){
                sender.sendMessage(text.translate("dontHavePermissions"));
                return;
            }
            MainClass.getInstance().reloadConfig();
            sender.sendMessage(text.translate("reloadedPlugin"));
            return;
        }
        if(args[0].equalsIgnoreCase("about")){
            sender.sendMessage("§7Author: §eSalwarex");
            return;
        }
        if(args[0].equalsIgnoreCase("history")){
            if(!sender.hasPermission("bbl.history")){
                Reports.send(sender, text.translate("dontHavePermissions"));
                return;
            }
            String cons = "";
            if(args.length == 1){
                cons = sender.getName();
            }
            else{
                if(!sender.hasPermission("bbl.history.others")){
                    Reports.send(sender, text.translate("dontHavePermissions"));
                    return;
                }
                cons = args[1];
            }

            new TransactionsInterface(Bukkit.getPlayer(sender.getName()), cons, 0);
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
        if(args.length == 1) return Lists.newArrayList("history");
        if(args.length == 2 && args[0].equalsIgnoreCase("history")) return playerList();
        return Lists.newArrayList();
    }



}
