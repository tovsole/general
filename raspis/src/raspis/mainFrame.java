package raspis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import javax.swing.*;
import java.awt.List;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class mainFrame extends JFrame {
	private int width = 500;
	private int height = 400;
	private java.util.List<String> linkList = new ArrayList<>();
	private java.util.List<Train> trainList = new ArrayList<>();
	private Properties mainProps = new Properties();
	private final String configFileName = "config.properties";


	public mainFrame() {
		super();
		System.out.println("CLASSPATH = " + System.getProperty("java.class.path"));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		this.setTitle("Грабалка расписания");
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
		JButton btn4 = new JButton("Save to db");

		btn1.addActionListener(new HtmlAction());
		btn2.addActionListener(new ExitAction());
		btn3.addActionListener(new PrintTrainsAction());
		btn4.addActionListener(new SavetoDbAction());

		panel1.add(btn1);
		panel1.add(btn2);
		panel1.add(btn3);
		panel1.add(btn4);

		//Loading project properties
		FileInputStream in = null;
		try {
			in = new FileInputStream(configFileName);
			mainProps.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public void getTrainList() throws Exception {
		linkList = Files.readAllLines(Paths.get(mainProps.getProperty("linkFile")));
		System.out.println("Links have been read from file");

		for (String link : linkList)  // for every link (station) from file
		{
			trainList.addAll(parsePage(link));
		}

		delDupTrains();

		Collections.sort(trainList,Train.compareByTrainId);

		//parseRoutes();
		saveTrainListToFile();

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

	public void saveTrainListToFile() {
		ArrayList<String> tmpList = new ArrayList<>();

		for (Train train : trainList) {
			tmpList.add(train.toString());
		}

		try {
			Files.write(Paths.get(mainProps.getProperty("trainFilePath")), tmpList, Charset.defaultCharset());
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
			Files.write(Paths.get(mainProps.getProperty("routeFilePath")), tmpList, Charset.defaultCharset());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private class ExitAction implements ActionListener
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
	
	private class HtmlAction implements ActionListener
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
	private class PrintTrainsAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			saveTrainListToFile();
		}

	}

	private class SavetoDbAction implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			Database db = new Database();
			Connection conn = db.getConnection("jbdc:oracle:thin:"+mainProps.getProperty("db"), mainProps.getProperty("user"), mainProps.getProperty("pass")) ;
			db.saveTrainListToDb( trainList, conn);
			System.out.println("Saving results to DB");
		}


	}
}
