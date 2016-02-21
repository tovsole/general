package raspis;

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

}
