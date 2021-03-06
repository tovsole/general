package com.company;

/**
 * Created by otovstiuk on 16.06.2016.
 */


import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JParser  implements Runnable {

    private String fileName;

    public JParser(String fileName) {
        super();
        this.fileName = fileName;
    }

    // procedure to parse JSON file
    @Override
    public void run() {
        List<Player> tmpPlayerList = new ArrayList<>();
        String country = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.indexOf("-")).toUpperCase();

        try {
            String text = new String(Files.readAllBytes(Paths.get(fileName)), Charset.defaultCharset());
            JSONArray arr = new JSONObject(text).getJSONObject("sheets").getJSONArray("Players");

            for (int i = 0; i < arr.length(); i++) {
                tmpPlayerList.add(new Player(country, arr.getJSONObject(i)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Main.playerList.addAll(tmpPlayerList);
    }
}
