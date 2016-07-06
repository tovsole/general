package raspis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Alex on 16.05.16.
 */
public class Translator {

    private HashMap<String, String> dictMap = new HashMap<String, String>();

    public enum Dictionary  {RASPIS,STATIONS }

    public Translator(Dictionary dictName) {
        super();
        loadDictionary(dictName);
    }

    public void loadDictionary(Dictionary dictName) {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Utils.mainProps.getProperty(dictName.toString())));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length >= 2) {
                    dictMap.put(parts[0], parts[1]);
                } else {
                    System.out.println("ignoring line: " + line);
                }
            }
            reader.close();
        }
        catch (IOException e) {
                e.printStackTrace();
            }
    }

    public String toRus (String ukr) {
        String result =ukr.toUpperCase();
        for (String key : dictMap.keySet()){
            result=result.replace(key,dictMap.get(key));
        }
        return result;
    }
}
