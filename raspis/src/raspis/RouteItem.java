package raspis;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Alex on 21.02.16.
 */
public class RouteItem {

    private String stationID;
    private String stationName;
    private String arrTime;
    private String depTime;

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

    public RouteItem(Element row) {
        super();

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
        return (getStationId()+"|"+getStationName()+"|"+getArrTime()+"|"+getDepTime());
    }

    public static Comparator<RouteItem> compareByArrTime = new Comparator<RouteItem>(){
        public int compare (RouteItem item1, RouteItem item2) {
            return item1.getArrTime().compareTo(item2.getArrTime());
        }
    };

}
