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

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }



    public void parseFile(String fileName) throws Exception {

        String country = fileName.substring(0, fileName.indexOf("-")).toUpperCase();

        JSONObject obj = new JSONObject(readFile(fileName,Charset.defaultCharset())).getJSONObject("sheets");

        JSONArray arr = obj.getJSONArray("Players");
        for (int i = 0; i < arr.length(); i++) {

            //playerList.add();
            System.out.println(new Player(country,arr.getJSONObject(i)));
        }


    }
}
