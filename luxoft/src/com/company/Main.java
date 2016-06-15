package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {

        String jsonPath = "C:\\input";

        if  (args.length==1) {
            jsonPath = args[0];
        }
        else if (args.length>1) {
            throw new Exception("Incorrect number of arguments");
        }

        System.out.println(jsonPath);

        File files = new File(jsonPath);
        ArrayList<String> fileNamesList = new ArrayList<String>(Arrays.asList(files.list()));

        for (String listItem :fileNamesList) {
            System.out.println(listItem.toString());
        }
    }
}
