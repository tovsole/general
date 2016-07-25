package raspis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.Class.*;

/**
 * Created by otovstiuk on 16.03.2016.
 */
public class Database {
    private Connection dbConnection ;

    public Database(String server, String user, String pass) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            dbConnection = DriverManager.getConnection(server, user, pass);
            dbConnection.setAutoCommit(false);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void saveTrainListToDb(Set<Train> trainList)  {

        Translator raspisTrans = new Translator(Translator.Dictionary.RASPIS);
        Translator stationsTrans = new Translator(Translator.Dictionary.STATIONS);

        PreparedStatement stmtTrain = null;
        PreparedStatement stmtRouteItem = null;


        try {
            clearTables();
            stmtTrain = dbConnection.prepareStatement(Train.getSqlInsert());
            stmtRouteItem = dbConnection.prepareStatement(RouteItem.getSqlInsert());

            for (Train train : trainList) {
                stmtTrain.setString(1,train.getTrainId());
                stmtTrain.setString(2,train.getTrainNum("SHORT"));
                stmtTrain.setString(3,train.getTrainNum("SHORT"));
                stmtTrain.setString(4,train.getFirstStation ());
                stmtTrain.setString(5,train.getLastStation() );
                stmtTrain.setString(6,train.getTrainTitleSql().toUpperCase());
                stmtTrain.setString(7, stationsTrans.toRus(train.getTrainTitleSql().toUpperCase()));
                stmtTrain.setString(8,train.getFirmName().toUpperCase());
                stmtTrain.setString(9,train.getTrainRaspisSql().toUpperCase());
                stmtTrain.setString(10,raspisTrans.toRus(train.getTrainRaspisSql()));
                stmtTrain.setInt(11,Utils.timeToHalfMinutes(train.getTrainDur()));
                stmtTrain.setString(12,null);
                stmtTrain.setString(13,null);
                stmtTrain.setString(14,String.valueOf(train.getIsWagon()));

                int i = stmtTrain.executeUpdate();

                for (RouteItem item : train.trainRoute){
                    stmtRouteItem.setString(1,train.getTrainId());
                    stmtRouteItem.setInt(2,item.getNumItem());
                    stmtRouteItem.setString(3,item.getStationId());
                    stmtRouteItem.setInt(4,Utils.timeToHalfMinutes(item.getArrTime()));
                    stmtRouteItem.setInt(5,Utils.timeToHalfMinutes(item.getDepTime()));
                    stmtRouteItem.setInt(6,0);
                    stmtRouteItem.setString(7,null);
                    stmtRouteItem.setInt(8,Utils.timeToHalfMinutes(item.getArrTime())/2);
                    stmtRouteItem.setInt(9,Utils.timeToHalfMinutes(item.getDepTime())/2);
                    i   = stmtRouteItem.executeUpdate();
                }
            }

            parsePeriodB();

            dbConnection.commit();
            stmtTrain.close();
            stmtRouteItem.close();
            dbConnection.close();
            System.out.println("Saved results to DB");
        }
        catch (SQLException e) {
               // defConnection.rollback();
                e.printStackTrace();
        }
    }


    public void parsePeriodB(){
        CallableStatement stmtParsePeriod =null;

            String plsql ="declare\n" +
                    "str varchar2(1000);\n" +
                    "begin\n" +
                    "  for ii in (select * from trains order by id)\n" +
                    "  loop  \n" +
                    "     str := trainparser_sk.strtoschedule(ii.id);  \n" +
                    "     update trains \n" +
                    "       set period_b= str\n" +
                    "       where id=ii.id; \n" +
                    "  end loop; \n" +
                    "end;";
        try {
            stmtParsePeriod=dbConnection.prepareCall(plsql);
            stmtParsePeriod.execute(plsql);
            stmtParsePeriod.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

    }

    public void clearTables(){
        try {
            Statement stmtDel = dbConnection.createStatement();
            stmtDel.execute("delete from trains");
            stmtDel.execute("delete from routes");
            stmtDel.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void getTrainsFromDb(){

        try {
            Statement stmt = dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            String sql = "Select * from trains order by id";
            ResultSet rsTrains = stmt.executeQuery(sql);
            //System.out.println(rsTrains.getFetchSize());
            //BufferedWriter outFile = new BufferedWriter(new FileWriter(Utils.mainProps.getProperty("TrainsFilePath")));
            ArrayList<String> tmpList = new ArrayList<>();

            while (rsTrains.next()) {
                //outFile.write(rsTrains.getString("ID")+" | "+rsTrains.getString("NUM_TRAIN")+" | "+rsTrains.getString("NUM_EXPRESS")+" | "+rsTrains.getString("ST1")+" | "+rsTrains.getString("ST2")+" | "+rsTrains.getString("MNAME_U")+" | "+rsTrains.getString("MNAME_R")+" | "+rsTrains.getString("FNAME")+" | "+rsTrains.getString("PERIOD_U")+" | "+rsTrains.getString("PERIOD_R")+" | "+rsTrains.getString("MOVE_TIME")+" | "+rsTrains.getString("MOVE_STAND")+" | "+rsTrains.getString("COMMENT")+" | "+rsTrains.getString("PERIOD_B")+" | "+rsTrains.getString("PRECEP"));
                //outFile.newLine();
                tmpList.add(rsTrains.getString("ID")+" | "+rsTrains.getString("NUM_TRAIN")+" | "+rsTrains.getString("NUM_EXPRESS")+" | "+rsTrains.getString("ST1")+" | "+rsTrains.getString("ST2")+" | "+rsTrains.getString("MNAME_U")+" | "+rsTrains.getString("MNAME_R")+" | "+rsTrains.getString("FNAME")+" | "+rsTrains.getString("PERIOD_U")+" | "+rsTrains.getString("PERIOD_R")+" | "+rsTrains.getString("MOVE_TIME")+" | "+rsTrains.getString("MOVE_STAND")+" | "+rsTrains.getString("COMMENT")+" | "+rsTrains.getString("PERIOD_B")+" | "+rsTrains.getString("PRECEP"));
            }
            Files.write(Paths.get(Utils.mainProps.getProperty("TrainsFilePath")), tmpList, Charset.defaultCharset());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        try {
            Statement stmt = dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            String sql = "Select * from routes order by train_id";
            ResultSet rsRoutes = stmt.executeQuery(sql);

            //BufferedWriter outFile = new BufferedWriter(new FileWriter(Utils.mainProps.getProperty("RoutesFilePath")));
            ArrayList<String> tmpList = new ArrayList<>();
            while (rsRoutes.next()) {
                //outFile.write(rsRoutes.getString("TRAIN_ID")+" | "+rsRoutes.getString("NUM")+" | "+rsRoutes.getString("ST")+" | "+rsRoutes.getString("ARR_TIME")+" | "+rsRoutes.getString("DEP_TIME")+" | | ");
                //outFile.newLine();
                tmpList.add(rsRoutes.getString("TRAIN_ID")+" | "+rsRoutes.getString("NUM")+" | "+rsRoutes.getString("ST")+" | "+rsRoutes.getString("ARR_TIME")+" | "+rsRoutes.getString("DEP_TIME")+" | | ");
            }
            Files.write(Paths.get(Utils.mainProps.getProperty("RoutesFilePath")), tmpList, Charset.defaultCharset());

        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        try {
            Statement stmt = dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            String sql = "select  ST1, ST2, NAIM, NAIMR, NAIMA, \n" +
                    "case when t.st1 in (24000,22700,22000,22100,23200,23600,24200,22500,22800,22200,22580,22590,23000,22300,22400,23340,20095,23536,23400,22450,23300,23530,23500,22600,23060,23410,22750)\n" +
                    "        then \n" +
                    "          (select count(*)+150 from routes rr where rr.st=t.st1) \n" +
                    "        else \n" +
                    "          (select count(*) from routes rr where rr.st=t.st1)\n" +
                    "        end\n" +
                    "            as RAT,\n" +
                    "D1\n" +
                    "from station_all t\n" +
                    "where t.st1 in (select distinct st from routes)";

            ResultSet rsStations = stmt.executeQuery(sql);

            ArrayList<String> tmpList = new ArrayList<>();
            while (rsStations.next()) {
                tmpList.add(rsStations.getString("ST1")+" | "+rsStations.getString("ST2")+" | "+rsStations.getString("NAIM")+" | "+rsStations.getString("NAIMR")+" | "+rsStations.getString("NAIMA")+" | "+rsStations.getString("RAT")+" | "+rsStations.getString("D1"));
            }
            Files.write(Paths.get(Utils.mainProps.getProperty("StationsFilePath")), tmpList, Charset.defaultCharset());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        System.out.println("Saved results from DB");

    }
}



