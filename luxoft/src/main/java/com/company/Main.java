package com.company;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public  static final SimpleDateFormat dtformatter =new SimpleDateFormat("dd/MM/yyyy");
    private static final String dbServer = "jbdc:oracle:thin:@localhost:1521:uat";
    private static final String dbUser = "uz";
    private static final String dbPass = "uz";
    public static List<Player> playerList = Collections.synchronizedList(new ArrayList<>());


    public static void main(String[] args) throws Exception {

        Long startTime = System.currentTimeMillis();

        //Getting path to json files
        String pathToJson = "C:\\input";
        if  (args.length==1) {
            pathToJson = args[0];
        }
        else if (args.length>1) {
            throw new Exception("Incorrect number of arguments");
        }

        System.out.println("Path to JSON files is "+pathToJson);

        //Creating list of json files
        ArrayList<String> fileList = new ArrayList<>(Arrays.asList(new File(pathToJson).list()));
        System.out.println("Got files list.");

        //Parsing all json files to list<Player>
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (String listItem :fileList) {
            executor.execute (new JParser(pathToJson+"\\"+listItem));
        }
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);


        System.out.println("Files  are parsed - "+playerList.size()+" players total");

        //Filtering list<Player> - removing players with DOB earlier then 01/01/1992
        for (ListIterator<Player> i = playerList.listIterator(); i.hasNext();) {
            Player p = i.next();
            if (p.getDateOfBirth().before(dtformatter.parse("01/01/1992"))) {
                i.remove();
            }
        }
        System.out.println("Collection filtered ...");


        //Sorting list from yongest to oldest player
        Collections.sort(playerList, Player.compareByDateOfBirth.reversed());
        System.out.println("Collection sorted ...");
        Long readTime = System.currentTimeMillis();


        //Saving resultlist to DB Oracle. Assuming that table EURO2016 does not exist in database
        Db db = new Db(dbServer,dbUser,dbPass);
        db.createTable();
        db.savePlayerListToDb(playerList);


        //Summary  about program execution
        System.out.println("TOTAL EXECUTION TIME = "+(System.currentTimeMillis()-startTime)+" ms");
        System.out.println("READING TIME = "+(readTime-startTime)+" ms");
        System.out.println("PERSISTING TIME = "+(System.currentTimeMillis()-readTime)+" ms");
    }
}
