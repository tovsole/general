package raspis;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Alex on 21.02.16.
 */
public class RouteItem {


    private int numItem;
    private String stationID;
    private String stationName;
    private String arrTime;
    private String depTime;

    private static final String sqlInsert = "Insert into routes (ID, TRAIN_ID, NUM,ST,ARR_TIME, DEP_TIME,OZN_TECH,DIST,ARR_TIME_ACT,DEP_TIME_ACT)" +
            " values (seq_routes.nextval, ?, ?,?,?,?,?,?,?,?)";

    public static String getSqlInsert(){
        return sqlInsert;
    }

    public String getPreparedSqlInsert(String trainId ) {
        return "Insert into routes (ID, TRAIN_ID, NUM,ST,ARR_TIME, DEP_TIME,OZN_TECH,DIST,ARR_TIME_ACT,DEP_TIME_ACT)" +
                " values (seq_routes.nextval,"
                + trainId+","
                +getNumItem()+","
                +getStationId()+","
                +Train.timeToHalfMinutes(getArrTime())+","
                +Train.timeToHalfMinutes(getDepTime())+","
                +"0"+","
                +"null"+","
                +Train.timeToHalfMinutes(getArrTime())/2+","
                +Train.timeToHalfMinutes(getDepTime())/2+");";

    }

    public int getNumItem() {
        return numItem;
    }

    public void setNumItem(int numItem) {
        this.numItem = numItem;
    }

    public void setStationId (String stationID){
        this.stationID=stationID;
    }

    public void setStationName (String StationName){
        this.stationName=StationName;
    }

    public void setArrTime (String arrTime){
        this.arrTime=arrTime;
    }

    public void setDepTime (String depTime){
        this.depTime=depTime;
    }

    public String getStationId(){
        return this.stationID;
    }

    public String getStationName(){
        return this.stationName;
    }

    public String getArrTime(){
        return this.arrTime;
    }

    public String getDepTime(){
        return this.depTime;
    }

    private String parseStationId(String url) {
        return url.substring(url.indexOf("=")+1,url.lastIndexOf("&"));
    }

    public RouteItem(){
        super();
    }

    public RouteItem(Element row, int numRow) {
        super();
        setNumItem(numRow);
        Element href = row.select("a").first();
        setStationId(parseStationId(href.attr("href")));

        ArrayList<Element> columns = row.getElementsByTag("td");
        for (int j = 0; j < columns.size(); j++) // for every column of row of route
        {
            switch (j) {
                case 0: setStationName(columns.get(j).text()); break;
                case 1:	setArrTime(columns.get(j).text()); break;
                case 2: setDepTime(columns.get(j).text()); break;
            }
        }
    }

    public String toString(){
        return (getNumItem()+"|"+getStationId()+"|"+getStationName()+"|"+getArrTime()+"|"+getDepTime());
    }

    public static Comparator<RouteItem> compareByArrTime = new Comparator<RouteItem>(){
        public int compare (RouteItem item1, RouteItem item2) {
            return item1.getArrTime().compareTo(item2.getArrTime());
        }
    };

}
