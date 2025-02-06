package salwarex.plugin.beebanklite;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reports {
    public static void send (Player player, String message){
        message = message.replace("&", "§");
        player.sendMessage(message);
    }

    public static void send (CommandSender sender, String message){
        message = message.replace("&", "§");
        sender.sendMessage(message);
    }

    public static void send (String message){
        message = message.replace("&", "§");
        for(Player pl: Bukkit.getOnlinePlayers()){
            if(pl.hasPermission("bank.getAdminReports")){
                pl.sendMessage(message);
            }
        }
        System.out.println(message);
    }
    public static void send (String permission,String message){
        message = message.replace("&", "§");
        for(Player pl: Bukkit.getOnlinePlayers()){
            if(pl.hasPermission("bank." + permission)){
                pl.sendMessage(message);
            }
        }
        System.out.println(message);
    }
}
