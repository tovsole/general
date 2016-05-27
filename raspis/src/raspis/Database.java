package raspis;

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
    private ArrayList<String> trainsSqlScript = new ArrayList<>();
    private ArrayList<String> routeItemsSqlScript = new ArrayList<>();

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
        trainsSqlScript.clear();
        routeItemsSqlScript.clear();

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
                stmtTrain.setString(3,null);
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
                trainsSqlScript.add(train.getPreparedSqlInsert());

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
                    //System.out.println(item.getPreparedSqlInsert(train.getTrainId()));
                    i   = stmtRouteItem.executeUpdate();
                    routeItemsSqlScript.add(item.getPreparedSqlInsert(train.getTrainId()));
                }
            }

            parsePeriodB();

            dbConnection.commit();
            stmtTrain.close();
            stmtRouteItem.close();
            dbConnection.close();
        }
        catch (SQLException e) {
               // defConnection.rollback();
                e.printStackTrace();
        }
    }

    public void saveTrainSqlSriptToFile (String trainsScriptFileName, String routesScriptFileName){
        //saving sql script inserting all trains to db  to file
        try {
            Files.write(Paths.get(trainsScriptFileName), trainsSqlScript, Charset.defaultCharset());
            System.out.println("Trains sql script saved to file");

            //saving sql script inserting all routes to db  to file
            Files.write(Paths.get(routesScriptFileName), routeItemsSqlScript, Charset.defaultCharset());
            System.out.println("Routes sql script saved to file");

        } catch (Exception e1) {
            e1.printStackTrace();
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
            stmtDel.executeUpdate("delete from trains");
            stmtDel.executeUpdate("delete from routes");
            stmtDel.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }


}



