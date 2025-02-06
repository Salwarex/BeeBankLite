package salwarex.plugin.beebanklite.Utils;

import salwarex.plugin.beebanklite.MainClass;

public class text {

    public static String translate(String key){ //Хератень для мультиязычных плагинов
        String language = MainClass.getInstance().getConfig().getString("language");
        String Final = MainClass.getText().getConfig().getString(language + "." + key).replace("&", "§");
        return Final;
    }
}
