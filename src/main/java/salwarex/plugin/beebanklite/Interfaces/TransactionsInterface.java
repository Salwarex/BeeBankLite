package salwarex.plugin.beebanklite.Interfaces;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import salwarex.plugin.beebanklite.Bank.Transaction;
import salwarex.plugin.beebanklite.MainClass;
import salwarex.plugin.beebanklite.Utils.CurrencyUtils;
import salwarex.plugin.beebanklite.Utils.Database.Database;
import salwarex.plugin.beebanklite.Utils.Interface;
import salwarex.plugin.beebanklite.Utils.ItemStackTemplate;
import salwarex.plugin.beebanklite.Utils.text;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionsInterface extends Interface {
    private final static Database db = MainClass.getDatabase();
    private String considered_name;

    public TransactionsInterface(Player _player, String considered, int page) {
        super(_player, text.translate("transactionHistoryAmount").replace("{NICKNAME}", considered), 54);
        considered_name = considered;
        ItemStackTemplate ist = new ItemStackTemplate(considered, Material.PLAYER_HEAD);
        ist.setHead(considered);

        ArrayList<ItemStack> stacks = reverseItemStackList(transAsStacks(playerTransactions()));
        ItemStack[] stackArr = new ItemStack[stacks.size()];
        stacks.toArray(stackArr);
        _player.openInventory(ItemsList(ist.get(), page, stackArr));
    }

    private ArrayList<Transaction> playerTransactions(){
        ArrayList<Transaction> result = new ArrayList<>();
        ArrayList<ArrayList<Object>> objs = db.getDataRS_OR("bbl_transactions", "sender", "receiver",
                considered_name.toLowerCase(), considered_name.toLowerCase(),
                "id", "timestamp", "type", "sender", "receiver", "sum", "currency");
        for(ArrayList<Object> prov : objs){
            Transaction transaction = null;
            Timestamp ts; int id;
            if(MainClass.getInstance().getConfig().getString("database.type").equalsIgnoreCase("sqlite")){
                String dstr = (String) prov.get(1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parsed = new Date();
                try{
                    parsed = sdf.parse(dstr);
                }
                catch (ParseException e){
                    e.printStackTrace();
                    return null;
                }

                ts = new Timestamp(parsed.getTime());
                id = (int) prov.get(0);
            }//sqlite
            else{
                ts = (Timestamp) prov.get(1);
                id = ((java.math.BigInteger) prov.get(0)).intValue();
            }//mysql
            transaction = new Transaction(id, ts, (String) prov.get(2), (String) prov.get(3), (String) prov.get(4), (int) prov.get(5), (String) prov.get(6));
            result.add(transaction);
        }
        return result;
    }

    private ArrayList<ItemStack> transAsStacks(ArrayList<Transaction> transactions){
        ArrayList<ItemStack> arr = new ArrayList<>();
        for(Transaction tr : transactions){
            String title = "";
            Material material = Material.GLASS;
            switch (tr.getType()){
                case "PAY":
                    if(tr.getSender().equalsIgnoreCase(considered_name.toLowerCase())){
                        title = text.translate("transferTo").replace("{SUM}", tr.getSum() + " " + CurrencyUtils.getCurrencySymbol(tr.getCurrency())).replace("{RECEIVER}", tr.getReceiver());
                        material = Material.RED_TERRACOTTA;
                    }
                    else{
                        title = text.translate("transferFrom").replace("{SUM}", tr.getSum() + " " + CurrencyUtils.getCurrencySymbol(tr.getCurrency())).replace("{SENDER}", tr.getSender());
                        material = Material.LIME_TERRACOTTA;
                    }
                    break;
                case "WIT":
                    title = text.translate("withdraw").replace("{SUM}", tr.getSum() + " " + CurrencyUtils.getCurrencySymbol(tr.getCurrency()));
                    material = Material.BLUE_TERRACOTTA;
                    break;
                case "DEP":
                    title = text.translate("deposit").replace("{SUM}", tr.getSum() + " " + CurrencyUtils.getCurrencySymbol(tr.getCurrency()));
                    material = Material.YELLOW_TERRACOTTA;
                    break;
            }

            ItemStackTemplate ist = new ItemStackTemplate(title, material, text.translate("transactionTime").replace("{TIME}", tr.getTimestamp()));
            arr.add(ist.get());
        }
        return arr;
    }

    public static ArrayList<ItemStack> reverseItemStackList(ArrayList<ItemStack> itemList) {
        ArrayList<ItemStack> reversedList = new ArrayList<>();

        for (int i = itemList.size() - 1; i >= 0; i--) {
            reversedList.add(itemList.get(i));
        }

        return reversedList;
    }

}
