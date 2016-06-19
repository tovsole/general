package com.company;

import java.sql.*;
import java.util.List;

/**
 * Created by otovstiuk on 14.06.2016.
 */
public class Db {

        private Connection dbConnection ;

        public Db(String connectString) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }

            try {
                dbConnection = DriverManager.getConnection(connectString);
                dbConnection.setAutoCommit(false);

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

    public void clearTable(){
            try {
                Statement stmtDel = dbConnection.createStatement();
                stmtDel.executeUpdate("delete from players");
                stmtDel.close();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
     }

        public void savePlaerListToDb(List<Player> playerList)  {

            PreparedStatement stmt = null;

            try {
                clearTable();
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
                    stmt.setString(12,player.getDateOfBirth());
                    stmt.setString(13,player.getRatingMatch1());
                    stmt.setString(14,player.getRatingMatch1());
                    stmt.setString(15,player.getRatingMatch1());

                    stmt.execute();
                }

                dbConnection.commit();
                stmt.close();
                dbConnection.close();
                System.out.println("Saved results to DB");
            }
            catch (SQLException e) {
                // defConnection.rollback();
                e.printStackTrace();
            }
        }


    }
