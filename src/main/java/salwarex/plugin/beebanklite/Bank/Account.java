package salwarex.plugin.beebanklite.Bank;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import salwarex.plugin.beebanklite.MainClass;
import salwarex.plugin.beebanklite.Reports;
import salwarex.plugin.beebanklite.Utils.CurrencyUtils;
import salwarex.plugin.beebanklite.Utils.Database.Database;
import salwarex.plugin.beebanklite.Utils.text;

import java.util.HashMap;
import java.util.Objects;

public class Account {
    private final Database db = MainClass.getDatabase();
    private String holder = "";
    private int default_id = 0;
    private final HashMap<String, Integer> cash = new HashMap<>();
    private HashMap<String, Integer> default_cash = new HashMap<>();

    public Account(String _holder){
        holder = _holder;
        for(String curr : MainClass.getCurrencies()){
            if(db.dataExists("bbl_accounts", "holder", holder.toLowerCase())){
                cash.put(curr, (Integer) db.getData(curr, "bbl_accounts", "holder", holder.toLowerCase()));
                default_id = (Integer) db.getData("default_currency_id", "bbl_accounts", "holder", holder.toLowerCase());
            }
            else{
                cash.put(curr, 0);
            }
        }
        default_cash = (HashMap<String, Integer>) cash.clone();
    }


    //private operations
    private int add(String currency, int sum){
        if(sum > 0){
            cash.replace(currency, cash.get(currency) + sum);
            save();
            return 1;
        }
        return 0;
    }
    private int retrieve(String currency, int sum){
        if(sum > 0){
            int fin = cash.get(currency) - sum;
            if(fin >= 0){
                cash.replace(currency, fin);
            }
            else{
                cash.replace(currency, 0);
            }
            save();
            return 1;
        }
        return 0;
    }
    private int set(String currency, int amount){
        if(amount >= 0){
            cash.replace(currency, amount);
            save();
            return 1;
        }
        return 0;
    }

    //public operations
    public int deposite(){
        Player player = Bukkit.getPlayer(holder);

        if(player != null){
            for(String curr : MainClass.getCurrencies()){
                Material material = Material.OAK_LEAVES;
                try{
                    material = Material.valueOf(MainClass.getInstance().getConfig().getString("currency." + curr + ".type"));
                }
                catch (Exception e){
                    Reports.send(player, text.translate("materialNotDeclaredError").replace("{MATERIAL}", material.toString()));
                    return -1; //can't recognize material
                }
                if(player.getInventory().getItemInMainHand().getType() == material){
                    int sum = player.getInventory().getItemInMainHand().getAmount();
                    player.getInventory().getItemInMainHand().setAmount(0);
                    Account account = new Account(player.getName());
                    int result = account.add(curr, sum);
                    if(result == 1){
                        Reports.send(player, text.translate("topUpAccountSuccess").replace("{SUM}", sum + " " + CurrencyUtils.getCurrencySymbol(curr)));
                        Transaction.deposite(holder.toLowerCase(), sum, curr);
                    }
                    else{
                        Reports.send(player, text.translate("incorrectValueError"));
                    }

                    return 1; //success
                }
            }
            Reports.send(player, text.translate("notDeclaredItemInHand"));
            return 0; //can't find any other currency in right hand
        }
        return -2; //can't find any other currency in right hand
    }
    public int withdraw(int sum){
        Player player = Bukkit.getPlayer(holder);
        String curr = MainClass.getCurrencies().get(default_id);
        int remained = sum;
        if(cash.get(curr) >= sum){
            Material material = Material.BRICK;

            try{
                material = Material.valueOf(MainClass.getInstance().getConfig().getString("currency." + curr + ".type"));
            }
            catch (Exception e){
                Reports.send(player, text.translate("materialNotDeclaredError").replace("{MATERIAL}", material.toString()));
                return -1; //can't recognize material
            }

            for(int i = 0; i <= 35; i++){
                if(player.getInventory().getItem(i) == null){
                    if(remained >= 64){
                        ItemStack is = new ItemStack(material, 64);
                        player.getInventory().setItem(i, is);
                        remained -= 64;
                    }
                    else{
                        if(remained > 0){
                            ItemStack is = new ItemStack(material, remained);
                            player.getInventory().setItem(i, is);
                            remained = 0;
                            break;
                        }
                    }
                }
            }

            if(remained < 0){return -1;}

            int result = this.retrieve(curr, sum - remained);
            if(result == 1){
                if(remained > 0){
                    int x = sum - remained;
                    Reports.send(player, text.translate("withdrawDontHaveEnoughSpaceAlert").replace("{SUM}", x + " " + CurrencyUtils.getCurrencySymbol(MainClass.getCurrencies().get(CurrencyUtils.selectedCurrency(holder)))));
                    Transaction.withdraw(holder.toLowerCase(), x, curr);
                }
                else{
                    Reports.send(player, text.translate("withdrawSuccess").replace("{SUM}", + sum + " " + CurrencyUtils.getCurrencySymbol(curr)));
                    Transaction.withdraw(holder.toLowerCase(), sum, curr);
                }
            }
            else{
                Reports.send(player, text.translate("incorrectValueError"));
            }

            return 1;
        }
        Reports.send(player, text.translate("notEnoughMoneyError").replace("{CURRENCY-NAME}", CurrencyUtils.getCurrencyName(curr)));
        return 0;
    }


    public int pay(String another_holder, int sum){
        String curr = MainClass.getCurrencies().get(default_id);
        if(cash.get(curr) >= sum && sum > 0){
            if(!db.dataExists("bbl_accounts", "holder", another_holder.toLowerCase())){return -1;}//don't exists
            Account another = new Account(another_holder);

            this.retrieve(curr, sum);
            another.add(curr, sum);
            Transaction.payment(holder.toLowerCase(), another_holder.toLowerCase(), sum, curr);
            return 1; //success
        }
        return 0; //no money no funny
    }

    public int getAmount(String curr){
        if(cash.containsKey(curr)){
            return cash.get(curr);
        }
        return 0;
    }

    private void save(){
        for(String curr : cash.keySet()){
            if(default_cash.containsKey(curr)){
                if(!Objects.equals(default_cash.get(curr), cash.get(curr))){
                    if(db.dataExists("bbl_accounts", "holder", holder.toLowerCase())){
                        db.updateData("bbl_accounts", curr, "holder", holder.toLowerCase(), cash.get(curr));
                    }
                    else{
                        db.addData("bbl_accounts", "holder", holder.toLowerCase());
                    }
                }
            }
        }
        db.updateData("bbl_accounts", "default_currency_id", "holder", holder.toLowerCase(), default_id);
    }

}
