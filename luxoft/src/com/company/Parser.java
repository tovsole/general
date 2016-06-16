package com.company;

/**
 * Created by otovstiuk on 15.06.2016.
 */

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parser {

    public void parseFile(String filePath) throws Exception{

        ObjectMapper mapper = new ObjectMapper();

        //JSON from file to Object
        //Player pl = mapper.readValue(new File("c:\\file.json"), Player.class);


        // Convert JSON string to Object
        String jsonInString = "{\n" +
                "            \"name\": \"Etrit Berisha\",\n" +
                "            \"bio\": \"Four years ago, while Spain were winning their second consecutive European Championship, Berisha was playing for Kalmar FF in Sweden. Gianni De Biasi gave him the chance to show his value with the national team in a friendly against Iran in May 2012, and by the end of the year he became a regular starter. He was signed by Lazio the following year, although he has largely had a backup role. If it goes down to penalties for Albania in France, expect him not only to save some but also to be one of the takers. He was a regular penalty taker for Kalmar, with four goals to his name domestically and in the Europa League.\",\n" +
                "            \"photoDone\": \"yes\",\n" +
                "            \"special_player? (eg. key player, promising talent, etc)\": \"No1 goalkeeper\",\n" +
                "            \"position\": \"Goalkeeper\",\n" +
                "            \"number\": \"\",\n" +
                "            \"caps\": \"34\",\n" +
                "            \"goals for country\": \"0\",\n" +
                "            \"club\": \"Lazio\",\n" +
                "            \"league\": \"Serie A (Italy)\",\n" +
                "            \"date of birth\": \"10/03/1989\",\n" +
                "            \"\": \"\",\n" +
                "            \"rating_match1\": \"\",\n" +
                "            \"rating_match2\": \"\",\n" +
                "            \"rating_match3\": \"\"\n" +
                "        }";
        Player p1 = mapper.readValue(jsonInString, Player.class);
        System.out.println(p1);


    }
}
