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
                stmt.setString(3,train.getTrainNum());
                stmt.setString(4,train.getTrainTitleSql());
                stmt.setString(5,train.getTrainRaspisSql());
                stmt.setString(6,train.getTrainDep());
                stmt.setString(7,train.getTrainArr() );
                stmt.setString(8,train.getTrainDur());
                //System.out.println(train.getSqlInsert());
                int i = stmt.executeUpdate();


                //stmt.close();  // ORA -1000 maximum numbers of cursors exceeded
            }
            defConnection.commit();
            //defConnection.close();
        }
        catch (SQLException e) {
               // defConnection.rollback();
                e.printStackTrace();
        }

    }

}



