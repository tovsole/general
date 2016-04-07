package raspis;

import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Set;

import static java.lang.Class.*;

/**
 * Created by otovstiuk on 16.03.2016.
 */
public class Database {
    private Connection defConnection ;

    public Database(String server, String user, String pass) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        defConnection = getConnection(server, user,pass) ;
    }

    public Connection getConnection (String server , String user , String pass){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(server, user, pass);
            conn.setAutoCommit(false);
            return conn;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public void saveTrainListToDb(Set<Train> trainList)  {

        PreparedStatement stmt = null;
        try {
            stmt = defConnection.prepareStatement(Train.getSqlInsert());
            for (Train train : trainList) {
                stmt.setString(1,train.getTrainId());
                stmt.setString(2,train.getTrainRouteLink().toString());
                stmt.setString(3,train.getTrainNum("SHORT"));
                stmt.setString(4,train.getTrainTitleSql());
                stmt.setString(5,train.getTrainRaspisSql());
                stmt.setString(6,train.getFirstStation ());
                stmt.setString(7,train.getLastStation() );
                stmt.setString(8,train.getTrainDur());
                stmt.setString(9,train.getFirmName());
                stmt.setString(9,String.valueOf(train.getIsWagon()));

                int i = stmt.executeUpdate();
            }
            defConnection.commit();
            stmt.close();
            defConnection.close();
        }
        catch (SQLException e) {
               // defConnection.rollback();
                e.printStackTrace();
        }

    }

}



