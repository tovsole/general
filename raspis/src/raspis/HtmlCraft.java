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
	    //public List<String> trainLinkList = new ArrayList<>();
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
					 //Train tmpTrain = new Train();
					 trainList.add(new Train());
					 					 
					 Element href = tr_elements.get(i).select("a").first();
					 URL routeUrl = new URL(baseUrl,href.attr("href"));
					 
					 trainList.get(i).setTrainRouteLink(routeUrl);
					 					 
					 ArrayList<Element> td_elements = tr_elements.get(i).getElementsByTag("td");
					 for (int j=0; j<td_elements.size(); j++ ) // for every column
					 {
						 if 	 (j==0) 
							 			 trainList.get(i).setTrainNum(td_elements.get(j).text());
						 else if (j==1) 
							 			 trainList.get(i).setTrainTitle(td_elements.get(j).text());
						 else if (j==2) 
							 			 trainList.get(i).setTrainRaspis(td_elements.get(j).text());
						 else if (j==3) 
							 			 trainList.get(i).setTrainArr(td_elements.get(j).text());
						 else if (j==4) 
							 			trainList.get(i).setTrainDep(td_elements.get(j).text());
						 else if (j==5) 
					 					trainList.get(i).setTrainDur(td_elements.get(j).text());
					 }	 						 
					 
					 
					 //trainList.add(tmpTrain);
					 
					System.out.println ("---------------------------------------");
					System.out.println (trainList.get(i).getTrainNum() +" | "+ trainList.get(i).getTrainTitle()  +" | "+ trainList.get(i).getTrainRaspis()   +" | "+  trainList.get(i).getTrainArr()  +" | "+trainList.get(i).getTrainDep()  +" | "+ trainList.get(i).getTrainDur()  +" | "+ trainList.get(i).getTrainRouteLink());
				 }
				   
				}
				catch(Exception e) 
					{e.printStackTrace();}
			}
			
		}
	}


