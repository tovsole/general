package raspis;

import java.net.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.io.*;
import java.util.*;

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
			//Document lll = Jsoup.parse(utf_page);
			in.close();
	
					
	}

				
		public void getTrainList() throws Exception
		{
			final URL baseUrl = new URL("http://www.uz.gov.ua/passengers/timetable/");
			
			linkList = Files.readAllLines(Paths.get("links.txt"));

			for (String ii : linkList)  // for every link (station) from file
			{
				try
				{
				  Document doc = Jsoup.connect(ii.toString()).get();
				  
				  Element table= doc.getElementById("cpn-timetable");

				  Element tbody = table.select("tbody").first();
				  				  
 				  ArrayList<Element> tr_elements = tbody.getElementsByTag("tr");
				  for (int i=0; i<tr_elements.size(); i++ ) // for every row
				  {
					 Train tmpTrain = new Train();

					 Element href = tr_elements.get(i).select("a").first();
					 URL routeUrl = new URL(baseUrl,href.attr("href"));

					 tmpTrain.setTrainRouteLink(routeUrl);
					 					 
					 ArrayList<Element> td_elements = tr_elements.get(i).getElementsByTag("td");
					 for (int j=0; j<td_elements.size(); j++ ) // for every column
					 {
						 switch(j) {
							 case 0:
								 tmpTrain.setTrainNum(td_elements.get(j).text());
							 case 1:
								 tmpTrain.setTrainTitle(td_elements.get(j).text());
							 case 2:
								 tmpTrain.setTrainRaspis(td_elements.get(j).text());
							 case 3:
								 tmpTrain.setTrainArr(td_elements.get(j).text());
							 case 4:
								 tmpTrain.setTrainDep(td_elements.get(j).text());
							 case 5:
								 tmpTrain.setTrainDur(td_elements.get(j).text());
						 }
					 }

					 trainList.add(tmpTrain);
					 
				  }
				   
				}
				catch(Exception e) 
					{e.printStackTrace();}
			}
			//printTrainList();
			delDupTrains();
		}

		public void printTrainList(){


			for (Train ii: trainList) {
				System.out.println("---------------------------------------------------");
				System.out.println(ii.getTrainNum() + " | " + ii.getTrainTitle() + " | " + ii.getTrainRaspis() + " | " + ii.getTrainArr() + " | " + ii.getTrainDep() + " | " + ii.getTrainDur() + " | " + ii.getTrainRouteLink());
			}
		}


		public void delDupTrains() {
			System.out.println("ArrayList - "+trainList.size());

			Set<Train> trainSet = new HashSet<>(trainList);
			System.out.println("Set - "+trainSet.size());
		}
	}


