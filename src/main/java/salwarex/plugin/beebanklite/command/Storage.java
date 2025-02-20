package salwarex.plugin.beebanklite.command;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import salwarex.plugin.beebanklite.MainClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Storage {
    private File file;
    private FileConfiguration config;

    public Storage(String name){
        file = new File(MainClass.getInstance().getDataFolder(), name);
        try{
            if(!file.exists()) {
                file.createNewFile();
                if(name == "languages.yml"){
                    MainClass.getInstance().saveResource("languages.yml", true);
                }
            }
        }
        catch (IOException e){
            throw new RuntimeException("Failed to create file", e);
        }
        config = YamlConfiguration.loadConfiguration(file);
        checkUpdates(name);
    }

    public FileConfiguration getConfig(){
        return config;
    }
    public void save(){
        try {
            config.save(file);
        } catch (IOException e){throw new RuntimeException("Failed to save!", e);
        }
    }

    public static void checkUpdates(String file){
        File configFile = new File(MainClass.getInstance().getDataFolder(), file);
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        InputStream resourceStream = MainClass.getInstance().getResource(file);
        if (resourceStream == null) {
            MainClass.getInstance().getLogger().severe("Resource" + configFile.getName() + "not found!");
            return;
        }

        FileConfiguration resourceConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(resourceStream));

        int currentVersion = config.getInt("version");
        int resourceVersion = resourceConfig.getInt("version");


        if(currentVersion < resourceVersion){
            for (String key : resourceConfig.getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, resourceConfig.get(key));
                }
            }
            config.set("version", resourceConfig.getInt("version"));
            try {
                config.save(configFile);
            } catch (Exception e) {
                MainClass.getInstance().getLogger().severe("Failed to save updated config: " + e.getMessage());
            }
        }
    }
}
