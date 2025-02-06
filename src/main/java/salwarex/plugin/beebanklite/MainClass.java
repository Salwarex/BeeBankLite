package salwarex.plugin.beebanklite;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import salwarex.plugin.beebanklite.Interfaces.BankInterfaceEvent;
import salwarex.plugin.beebanklite.Utils.Database.Database;
import salwarex.plugin.beebanklite.Utils.PlaceholderClass;
import salwarex.plugin.beebanklite.command.MainCommand;
import salwarex.plugin.beebanklite.command.PayCommand;
import salwarex.plugin.beebanklite.command.Storage;

import java.sql.SQLException;
import java.util.ArrayList;

public final class MainClass extends JavaPlugin {

    private static MainClass instance;
    private Storage lang;
    private Database database;
    private ArrayList<String> currencies = new ArrayList<>();
    public ArrayList<Player> withdrawers = new ArrayList<>();

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        saveDefaultConfig();
        instance = this;
        lang = new Storage("languages.yml");

        if(getConfig().getString("database.type").equalsIgnoreCase("sqlite")){
            try{
                if(!getDataFolder().exists()){
                    getDataFolder().mkdirs();
                }
                database = new Database(getDataFolder().getAbsolutePath() + "/" + getConfig().getString("database.sqlite.filename") + ".db");
            }
            catch (SQLException e){
                e.printStackTrace();
                System.out.println("Failed to connect SQLite db: " + getDataFolder().getAbsolutePath() + "/database.db");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        else if (getConfig().getString("database.type").equalsIgnoreCase("mysql")){
            try{
                database = new Database(getConfig().getString("database.mysql.url") + "/" + getConfig().getString("database.mysql.db-name"), getConfig().getString("database.mysql.username"), getConfig().getString("database.mysql.password"));
            }
            catch (SQLException e){
                e.printStackTrace();
                System.out.println("Failed to connect MySQL db!" );
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        else{
            System.err.println("You have incorrectly specified the database type in the plugin configuration! Available types: mysql, sqlite" );
            Bukkit.getPluginManager().disablePlugin(this);
        }



        if(instance.getConfig().getConfigurationSection("currency") != null){
            String SQLcurrenciesPart = "";
            for(String key : instance.getConfig().getConfigurationSection("currency").getKeys(false)){
                SQLcurrenciesPart = (SQLcurrenciesPart + key + " INTEGER NOT NULL DEFAULT 0, ");
                currencies.add(key);
            }

            if(SQLcurrenciesPart.length() == 0){
                System.err.println("Plugin has been installed! Please specify your currencies and choose database type: " + getDataFolder().getAbsolutePath() + "/config.yml. After configure you need restart the server!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
            SQLcurrenciesPart = SQLcurrenciesPart.substring(0, SQLcurrenciesPart.length() - 2);

            //System.err.println("holder VARCHAR(16) PRIMARY KEY, creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, default_currency_id INTEGER NOT NULL DEFAULT 0 " + SQLcurrenciesPart + ", ");
            MainClass.getDatabase().createTable("bbl_accounts", "holder VARCHAR(16) PRIMARY KEY, creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, default_currency_id INTEGER NOT NULL DEFAULT 0, " + SQLcurrenciesPart);
            if(getConfig().getString("database.type").equalsIgnoreCase("sqlite")){
                MainClass.getDatabase().createTable("bbl_transactions", "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, type VARCHAR(16) NOT NULL, sender VARCHAR(32) NOT NULL DEFAULT 'SERVER', receiver VARCHAR(32) NOT NULL DEFAULT 'SERVER', sum INTEGER NOT NULL DEFAULT 0, currency VARCHAR(64) NOT NULL DEFAULT " + currencies.get(0));
            }
            else{
                MainClass.getDatabase().createTable("bbl_transactions", "id SERIAL PRIMARY KEY NOT NULL, timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, type VARCHAR(16) NOT NULL, sender VARCHAR(32) NOT NULL DEFAULT 'SERVER', receiver VARCHAR(32) NOT NULL DEFAULT 'SERVER', sum INTEGER NOT NULL DEFAULT 0, currency VARCHAR(64) NOT NULL DEFAULT " + currencies.get(0));
            } //Изменения

            for(String key : instance.getConfig().getConfigurationSection("currency").getKeys(false)){
                if(!database.columnExists("bbl_accounts", key)){
                    database.addColumn("bbl_accounts", key + " INTEGER NOT NULL DEFAULT 0;");
                    System.out.println("Added column \"" + key + "\" to table \"bbl_accounts\"");
                }
            }
        }
        else{
            System.err.println("Please specify at least one currency in " + getDataFolder().getAbsolutePath() + " config.yml -> currency.<> and restart the server!" );
            Bukkit.getPluginManager().disablePlugin(this);
        }






        new MainCommand();
        new PayCommand();
        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderClass().register();
        }
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        Bukkit.getPluginManager().registerEvents(new BankInterfaceEvent(), this);
    }


    public static MainClass getInstance(){
        return instance;
    }
    @Override
    public void onDisable() {
        try{
            database.closeConnection();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static Storage getText(){ return  instance.lang; }
    public static Database getDatabase(){ return instance.database; }
    public static ArrayList<String> getCurrencies(){ return instance.currencies; }



}
