package com.company;

/**
 * Created by otovstiuk on 16.06.2016.
 */


import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JParser  {

    static String readFiletoString(String path)
            throws IOException
    {
        return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
    }



    public void parseFile(String fileName) throws Exception {

        String country = fileName.substring(fileName.indexOf("\\\\")+2, fileName.indexOf("-")).toUpperCase();

        JSONArray arr = new JSONObject(readFiletoString(fileName)).getJSONObject("sheets").getJSONArray("Players");

        for (int i = 0; i < arr.length(); i++) {
            Main.playerList.add(new Player(country,arr.getJSONObject(i)));
        }

    }
}
