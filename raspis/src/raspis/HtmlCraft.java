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
	    public List<String> trainLinkList = new ArrayList<>();
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
				  				  
 				  Elements tr_elements = tbody.getElementsByTag("tr");
				  for (Element tr : tr_elements ) // for every row
				  {
					 Train tmpTrain = new Train();
					 					 
					 Element href = tr.select("a").first();
					 URL routeUrl = new URL(baseUrl,href.attr("href"));
					 
					 tmpTrain.setTrainRouteLink(routeUrl);
					 //System.out.println(routeUrl);
					 
					 Elements td_elements = tr.getElementsByTag("td");
					 for (Element td : td_elements)
					 {
						 System.out.print(td.text()+"|");
						 
					 }
					 
					 trainList.add(tmpTrain);
					 
					 System.out.println("");
					 System.out.println ("---------------------------------------");
				 }
				   
				}
				catch(Exception e) {e.printStackTrace();}
			}
			
		}
	}


