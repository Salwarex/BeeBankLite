package salwarex.plugin.beebanklite.Bank;

import salwarex.plugin.beebanklite.MainClass;
import salwarex.plugin.beebanklite.Utils.Database.Database;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Transaction {
    private final static Database db = MainClass.getDatabase();

    private int id;
    private String timestamp;
    private String type;
    private String sender;
    private String receiver;
    private int sum;
    private String currency;

    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getSum() {
        return sum;
    }

    public String getCurrency() {
        return currency;
    }


    public Transaction(int _id){
        if(db.dataExists("bbl_transactions", "id", _id)){
            ArrayList<Object> row = (db.getDataRS("bbl_transactions", "id",  _id,  "timestamp", "type", "sender", "receiver", "sum", "currency")).get(0);
            id = _id;
            Timestamp ts = (Timestamp) row.get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
            timestamp = sdf.format(ts);
            type = (String) row.get(1);
            sender = (String) row.get(2);
            receiver = (String) row.get(3);
            sum = (int) row.get(4);
            currency = (String) row.get(5);
        }
        else{
            id = 0;
            timestamp = "00:00:00 21.02.2020";
            type = "UNK";
            sender = "unknown";
            receiver = "unknown";
            sum = 0;
            currency = "unknown";
        }
    }

    public Transaction(int _id, Timestamp _timestamp, String _type, String _sender, String _receiver, int _sum, String _curr){
        id = _id;
        Timestamp ts = _timestamp;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        timestamp = sdf.format(ts);
        type = _type;
        sender = _sender;
        receiver = _receiver;
        sum = _sum;
        currency = _curr;
    }



    public static void payment(String sender, String receiver, int sum, String curr){
        db.addData("bbl_transactions", "type, sender, receiver, sum, currency", "PAY", sender, receiver, sum, curr);
    }

    public static void withdraw(String sender, int sum, String curr){
        db.addData("bbl_transactions", "type, sender, sum, currency", "WIT", sender, sum, curr);
    }

    public static void deposite(String receiver, int sum, String curr){
        db.addData("bbl_transactions", "type, receiver, sum, currency", "DEP", receiver, sum, curr);
    }
}
