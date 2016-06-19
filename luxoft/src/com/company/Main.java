package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static String pathToJson = "C:\\input";
    public ArrayList<Player>  playerList = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        //String pathToJson = "C:\\input";

        if  (args.length==1) {
            pathToJson = args[0];
        }
        else if (args.length>1) {
            throw new Exception("Incorrect number of arguments");
        }

        System.out.println(pathToJson);

        File files = new File(pathToJson);
        ArrayList<String> fileNamesList = new ArrayList<String>(Arrays.asList(files.list()));

        for (String listItem :fileNamesList) {
            //System.out.println(listItem.toString());
            JParser parser = new JParser();
            parser.parseFile(listItem);
        }


    }
}
