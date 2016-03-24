package raspis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by otovstiuk on 21.03.2016.
 */
public class Parser {



    public List<Train> parseStationPage(String pageLink) throws Exception {

        List<Train> resultTrains = new ArrayList<>();
        Train tmpTrain = null;

        System.out.println("Starting parsing new page - " + pageLink);

        Document doc = Jsoup.connect(pageLink).get();
        Element table = doc.getElementById("cpn-timetable");
        Element tbody = table.select("tbody").first();
        ArrayList<Element> rows = tbody.getElementsByTag("tr");

        for (Element row : rows) // for every row of page
        {
            tmpTrain = new Train(row);
            //parseRoute(tmpTrain);
            resultTrains.add(tmpTrain);
        }
        return resultTrains;
    }


    public void parseRoute(Train tmpTrain) throws Exception {
        System.out.println("    >> Parsing train " + tmpTrain.getTrainNum());
        Document doc = Jsoup.connect(tmpTrain.getTrainRouteLink().toString()).get();

        Element table = doc.getElementById("cpn-timetable");

        Element tbody = table.select("tbody").last();

        ArrayList<Element> rows = tbody.getElementsByTag("tr");
        for (Element row : rows) // for every row of route of train
        {
            tmpTrain.addTrainRouteItem(new RouteItem(row));
        }
    }
}