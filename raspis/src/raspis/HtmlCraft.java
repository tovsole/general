package raspis;

import java.net.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.io.*;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;


	public class HtmlCraft {
	    public List<String> raspByStationLinkList = new ArrayList<>();
	    public List<String> trainLinkList = new ArrayList<>();
		
		
		public void getHtmlbyStation(String urlName) throws Exception
		{
		try
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
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
	}
		
				
		public void getTrainList() throws Exception
		{
			try  
			{
			 raspByStationLinkList = Files.readAllLines(Paths.get("links.txt"));
			
			}
			catch (Exception e) {e.printStackTrace(); }
			
			for (String ii : raspByStationLinkList)
			{
				try
				{
				  //System.out.println(ii.toString());
				  Document doc = Jsoup.connect(ii.toString()).get();
				  //System.out.println(doc.title());
				  
				  Elements table = doc.getElementsByTag("td");
				  				  
				  //Elements links = doc.select("a[href]");
				  for (Element tab : table) 
				    //{
					 System.out.println (tab.text());
				     System.out.println ("---------------------"); 
				    //}
				}
				catch(Exception e) {e.printStackTrace();}
			}
			
		}
	}


