package raspis;

import java.net.*;
import java.nio.charset.*;
//import java.io.*;
import java.util.*;

public class HTMLWork {
    public ArrayList<String> trainList = new ArrayList<>();
    public ArrayList<String> trainLinkList = new ArrayList<>();
	
	public HTMLWork() {
		super();
	}

	public void getHTMLbyStation(String urlName) throws Exception
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
		in.close();
	 }	
	
	catch (Exception e)
	{
		e.printStackTrace();
	}
		
}
	
	public void getTrainList (){
		//readTrainLinkFromFile();
		
		trainLinkList.clear();
		trainLinkList.add("http://uz.gov.ua/passengers/timetable/?station=739%2C47125%2C22080%2C47140%2C47175%2C22298%2C22000%2C47190%2C3723&by_station=%D0%9F%D0%BE%D1%88%D1%83%D0%BA");
		trainLinkList.add("http://uz.gov.ua/passengers/timetable/?station=739%2C47125%2C22080%2C47140%2C47175%2C22298%2C22000%2C47190%2C3723&by_station=%D0%9F%D0%BE%D1%88%D1%83%D0%BA");
		
		for (String ii : trainLinkList)
		{
			try
			{
			  //trainList.add(getHTMLbyStation(ii));
			  System.out.println("Размер массива - "+ii.indexOf(ii)+ ii.toString());
			}
			catch(Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	}
}
