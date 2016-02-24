package raspis;

import java.net.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.io.*;
import java.util.*;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListTransducedAccessorImpl;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;


	public class HtmlCraft {
	    public List<String> linkList = new ArrayList<>();
	    public List<Train>  trainList = new ArrayList<>();
		
		
		public void getHtmlbyStation(String urlName) throws Exception
		{
			URL url1 = new URL(urlName) ;
			//URLConnection conn = new URLConnection(url1);
			Scanner in = new Scanner(url1.openStream());
			
			String page="";
			
			while (in.hasNextLine()==true)
			{	
				page=page+in.nextLine() ;
			}
			
			String utf_page = new String(page.getBytes(),"UTF-8");
			System.out.println(utf_page);
						in.close();
	
					
	}

				
		public void getTrainList() throws Exception
		{

			linkList = Files.readAllLines(Paths.get("links.txt"));

			for (String link : linkList)  // for every link (station) from file
			{
				trainList.addAll(parseStationPage(link));
			}

			delDupTrains();
			//sortTrainListO();
			printTrainList();
		}

		public List<Train> parseStationPage(String pageLink) throws Exception {
			final URL baseUrl = new URL("http://www.uz.gov.ua/passengers/timetable/");
			List<Train> resultTrains = new ArrayList<>();

			Document doc = Jsoup.connect(pageLink).get();

			Element table = doc.getElementById("cpn-timetable");

			Element tbody = table.select("tbody").first();

			ArrayList<Element> tr_elements = tbody.getElementsByTag("tr");
			for (int i = 0; i < tr_elements.size(); i++) // for every row
			{
				Train tmpTrain = new Train();

				Element href = tr_elements.get(i).select("a").first();
				URL routeUrl = new URL(baseUrl, href.attr("href"));

				tmpTrain.setTrainRouteLink(routeUrl);
				tmpTrain.setTrainId(tmpTrain.parseTrainIdFromRouteLink());

				ArrayList<Element> td_elements = tr_elements.get(i).getElementsByTag("td");
				for (int j = 0; j < td_elements.size(); j++) // for every column
				{
					switch (j) {
						case 0: tmpTrain.setTrainNum(td_elements.get(j).text());
						case 1: tmpTrain.setTrainTitle(td_elements.get(j).text());
						case 2: tmpTrain.setTrainRaspis(td_elements.get(j).text());
						case 3:	tmpTrain.setTrainArr(td_elements.get(j).text());
						case 4:	tmpTrain.setTrainDep(td_elements.get(j).text());
						case 5:	tmpTrain.setTrainDur(td_elements.get(j).text());
					}
				}

				resultTrains.add(tmpTrain);

			}

			return resultTrains;
		}

		public void printTrainList(){


			for (Train train: trainList) {
				System.out.println("---------------------------------------------------------------------------------------------");
				System.out.println(train.toString());
			}
		}


		public void delDupTrains() {
			System.out.println("ArrayList - "+trainList.size());

			Set<Train> trainSet = new LinkedHashSet<>(trainList);
			System.out.println("LinkedHashSet - "+trainSet.size());
			trainList.clear();
			trainList.addAll(trainSet);
			System.out.println("ArrayList NEW - "+trainList.size());
		}
	}


