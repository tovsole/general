package raspis;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static java.lang.Class.*;

/**
 * Created by otovstiuk on 16.03.2016.
 */
public class Database {

    public Database() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
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

    public void saveTrainListToDb(List<Train> trainList, Connection conn)  {
        private final String sqlInsertStr;

        try {
            for (Train train : trainList) {
                Statement stmt = conn.createStatement();
                //System.out.println(train.getSqlInsert());
                //int i = stmt.executeUpdate(train.getSqlInsert());     // better way is to use prepared parameterized statement

                setSqlInsert("Insert into train_tab (id,train_Id, train_Route_Link,train_Num,train_Title, train_Raspis, train_Dep, train_Arr, train_Dur)" +
                        " values (train_seq.nextval,'" + getTrainId() + "','"+getTrainRouteLink() + "','" + getTrainNum() + "','" + getTrainTitleSql() + "','" + getTrainRaspisSql() + "','" + getTrainDep() + "','" + getTrainArr() + "','" + getTrainDur() + "')");

                stmt.close();  // ORA -1000 maximum numbers of cursors exceeded
            }
            conn.commit();
            conn.close();
        }
        catch (SQLException e) {  e.printStackTrace();
        }

    }

}



