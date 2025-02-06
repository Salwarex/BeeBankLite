package salwarex.plugin.beebanklite.command;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import salwarex.plugin.beebanklite.MainClass;

import java.io.File;
import java.io.IOException;

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
}
