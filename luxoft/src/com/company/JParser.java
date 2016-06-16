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

        String country = "Albania";
        Player tmpPlayer = new Player();
        JSONObject obj = new JSONObject(readFile(fileName,Charset.defaultCharset())).getJSONObject("sheets");

        JSONArray arr = obj.getJSONArray("Players");
        for (int i = 0; i < arr.length(); i++) {
            tmpPlayer.setCountry(country);
            tmpPlayer.setName(arr.getJSONObject(i).getString("name"));
            tmpPlayer.setBio(arr.getJSONObject(i).getString("bio"));
            tmpPlayer.setPhotoDone(arr.getJSONObject(i).getString("photo done?"));
            tmpPlayer.setSpecialPlayer(arr.getJSONObject(i).getString("special player? (eg. key player, promising talent, etc)"));
            tmpPlayer.setPosition(arr.getJSONObject(i).getString("position"));
            tmpPlayer.setNumber(arr.getJSONObject(i).getString("number"));
            tmpPlayer.setCaps(arr.getJSONObject(i).getInt("caps"));
            tmpPlayer.setGoalsForCountry(arr.getJSONObject(i).getInt("goals for country"));
            tmpPlayer.setClub(arr.getJSONObject(i).getString("club"));
            tmpPlayer.setLeague(arr.getJSONObject(i).getString("league"));
            tmpPlayer.setDateOfBirth(arr.getJSONObject(i).getString("date of birth"));
            tmpPlayer.setRatingMatch1(arr.getJSONObject(i).getString("rating_match1"));
            tmpPlayer.setRatingMatch2(arr.getJSONObject(i).getString("rating_match2"));
            tmpPlayer.setRatingMatch3(arr.getJSONObject(i).getString("rating_match3"));
            //playerList.add();
            System.out.println(tmpPlayer);
        }


    }
}
