package raspis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import javax.swing.*;
import java.awt.List;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;


public class myFrame extends JFrame {
	private int width = 500;
	private int height = 400;
	private java.util.List<String> linkList = new ArrayList<>();
	private java.util.List<Train> trainList = new ArrayList<>();


	public myFrame() {
		super();
		System.out.println("CLASSPATH = " + System.getProperty("java.class.path"));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		this.setTitle("получаем расписание");
		this.setResizable(true);
		//--
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension ScreenSize = kit.getScreenSize();
		width = ScreenSize.width / 2;
		height = ScreenSize.height / 2;
		this.setSize(width, height);
		this.setLocation(width / 4, height / 4);
		this.setVisible(true);
		//--

		JPanel panel1 = new JPanel();
		panel1.setBorder(BorderFactory.createEtchedBorder(1));
		panel1.setLayout(new FlowLayout());
		this.add(panel1, BorderLayout.SOUTH);

		JButton btn1 = new JButton("Get rasp");
		JButton btn2 = new JButton("Exit");
		JButton btn3 = new JButton("Print train List");

		btn1.addActionListener(new HtmlAction());
		btn2.addActionListener(new ExitAction());
		btn3.addActionListener(new PrintTrainsAction());

		panel1.add(btn1);
		panel1.add(btn2);
		panel1.add(btn3);

	}

	public void saveTrainListTofile() {
		ArrayList<String> tmpList = new ArrayList<>();
		for (Train train : trainList) {
			tmpList.add(train.toString());
		}

		try {
			Files.write(Paths.get("C:/a.txt"), tmpList, Charset.defaultCharset());
			System.out.println("Trains saved to file");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		tmpList.clear();

		for (Train train : trainList) {
			for (RouteItem item : train.trainRoute) {
				tmpList.add(train.getTrainId() + "|" + item.toString());
			}
		}
		try {
			Files.write(Paths.get("C:/b.txt"), tmpList, Charset.defaultCharset());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}


	public void getTrainList() throws Exception {

		linkList = Files.readAllLines(Paths.get("links.txt"));
		System.out.println("Links have been read from file");

		for (String link : linkList)  // for every link (station) from file
		{
			trainList.addAll(parsePage(link));
		}

		delDupTrains();
		//sortTrainListO();
		parseRoutes();
		saveTrainListTofile();

	}

	public java.util.List<Train> parsePage(String pageLink) throws Exception {

		java.util.List<Train> resultTrains = new ArrayList<>();

		System.out.println("Starting parsing new page - " + pageLink);

		Document doc = Jsoup.connect(pageLink).get();
		Element table = doc.getElementById("cpn-timetable");
		Element tbody = table.select("tbody").first();
		ArrayList<Element> rows = tbody.getElementsByTag("tr");

		for (Element row : rows) // for every row of page
		{
			resultTrains.add(new Train(row));
		}
		return resultTrains;
	}


	public void delDupTrains() {
		Set<Train> trainSet = new LinkedHashSet<>(trainList);
		System.out.println("ArrayList - " + trainList.size() + " ----  LinkedHashSet - " + trainSet.size());
		trainList.clear();
		trainList.addAll(trainSet);
		System.out.println("ArrayList NEW - " + trainList.size());
	}

	public void parseRoutes() throws Exception {
		for (Train train : trainList) {
			train.parseRoute();
		}

	}

	public class ExitAction implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			int i=JOptionPane.showConfirmDialog(null, "Are you sure?","Confirm exit",1);
			if (i==0) 
			{		
			  System.out.println("Exit on users demand");
			  System.exit(0);
			}  
		}
		
		
	}
	
	public class HtmlAction implements ActionListener 
	{
		public void actionPerformed (ActionEvent e)
		{
			if      (JOptionPane.showConfirmDialog(null, "Are you sure?")==0)
			{ 
				try
				{
					getTrainList();
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}	  			
		}
	}
	public class PrintTrainsAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			saveTrainListTofile();
		}

	}
}
