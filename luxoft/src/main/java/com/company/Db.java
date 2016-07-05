package com.company;

import java.sql.*;
import java.util.List;

/**
 * Created by otovstiuk on 14.06.2016.
 */
public class Db {

        private Connection dbConnection ;

        public Db(String server,String usr, String pass) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }

            try {
                dbConnection = DriverManager.getConnection(server, usr,pass);
                dbConnection.setAutoCommit(false);
                System.out.println("Connected to database ...");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

    public void createTable() throws SQLException {
        //if table exists - drop it
        try {
           Statement stmt = dbConnection.createStatement();
           stmt.execute("drop table euro2016");
           stmt.close();
           System.out.println("Table euro2016 dropped ...");
        }
        catch(SQLSyntaxErrorException ex){
            //if table euro2016 does not exist - just ignore this error
            if (ex.getErrorCode()!=942){
                throw ex;
            }
        }

        try {
            Statement stmt = dbConnection.createStatement();
            stmt.execute("create table euro2016 (\n" +
                    "  country varchar2(250),\n" +
                    "  name varchar2(250),\n" +
                    "  bio varchar2(4000),\n" +
                    "  photoDone varchar2(5),\n" +
                    "  specialPlayer varchar2(250),\n" +
                    "  position  varchar2(50),\n" +
                    "  num  number,\n" +
                    "  caps number,\n" +
                    "  goalsForCountry number,\n" +
                    "  club  varchar2(250),\n" +
                    "  league  varchar2(250),\n" +
                    "  dateOfBirth date,\n" +
                    "  ratingMatch1 number,\n" +
                    "  ratingMatch2 number,\n" +
                    "  ratingMatch3 number\n" +
                    ") ");
            stmt.close();
            System.out.println("Table euro2016 created ...");
        }
        catch(SQLException ex){
            ex.printStackTrace();
            throw ex;
        }
     }

        public void savePlayerListToDb(List<Player> playerList)  {

            PreparedStatement stmt = null;

            try {
                stmt = dbConnection.prepareStatement(Player.getSQL());

                for (Player player : playerList) {
                    stmt.setString(1,player.getCountry());
                    stmt.setString(2,player.getName());
                    stmt.setString(3,player.getBio());
                    stmt.setString(4,String.valueOf(player.getPhotoDone()));
                    stmt.setString(5,player.getSpecialPlayer ());
                    stmt.setString(6,player.getPosition() );
                    stmt.setString(7,player.getNumber());
                    stmt.setInt(8,player.getCaps());
                    stmt.setInt(9,player.getGoalsForCountry());
                    stmt.setString(10,player.getClub());
                    stmt.setString(11,player.getLeague());
                    stmt.setDate(12,new java.sql.Date(player.getDateOfBirth().getTime()));
                    stmt.setString(13,player.getRatingMatch1());
                    stmt.setString(14,player.getRatingMatch1());
                    stmt.setString(15,player.getRatingMatch1());

                    stmt.execute();
                }

                dbConnection.commit();
                stmt.close();
                dbConnection.close();
                System.out.println("Data inserted into table euro2016... ");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
