package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;

/**
 * Created by otovstiuk on 16.06.2016.
 */
public class GParser {

    public void parseFile(String fileName) throws Exception {

        Gson gson = new GsonBuilder().create();
        Player p = gson.fromJson(new FileReader(fileName), Player.class);

        System.out.println(p.getName());
    }

}
