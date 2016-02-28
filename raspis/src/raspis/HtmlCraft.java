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
			System.out.println("Links have been read from file");

			for (String link : linkList)  // for every link (station) from file
			{
				trainList.addAll(parsePage(link));
			}

			delDupTrains();
			//sortTrainListO();
			parseRoutes();
			printTrainList();

		}

		public List<Train> parsePage(String pageLink) throws Exception {

			List<Train> resultTrains = new ArrayList<>();

			System.out.println("Starting parsing new page - "+pageLink);

			Document doc = Jsoup.connect(pageLink).get();
			Element table = doc.getElementById("cpn-timetable");
			Element tbody = table.select("tbody").first();
			ArrayList<Element> rows = tbody.getElementsByTag("tr");

			for (Element row : rows) // for every row
			{
				resultTrains.add(new Train(row));
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
			Set<Train> trainSet = new LinkedHashSet<>(trainList);
			System.out.println("ArrayList - "+trainList.size()+" ----  LinkedHashSet - "+trainSet.size());
			trainList.clear();
			trainList.addAll(trainSet);
			System.out.println("ArrayList NEW - "+trainList.size());
		}

		public void parseRoutes(){
			for (Train train : trainList) {
				train.parseRoute();
			}

		}
	}


