package raspis;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;
import java.util.List;


public class mainFrame extends JFrame {
	private int width = 500;
	private int height = 400;
	private List<String> linkList = new ArrayList<>();
	private Set<Train> trainList = new HashSet<>();


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
		JButton btn4 = new JButton("Get from db");

		btn1.addActionListener(new HtmlAction());
		btn2.addActionListener(new ExitAction());
		btn3.addActionListener(new PrintTrainsAction());
		btn4.addActionListener(new GetFromDbAction());

		panel1.add(btn1);
		panel1.add(btn2);
		panel1.add(btn3);
		panel1.add(btn4);

		if (Utils.mainProps.isEmpty()) {
			Utils.loadProps();
		}

	}

	public void getTrainList() throws Exception {
		trainList.clear();
		linkList = Files.readAllLines(Paths.get(Utils.mainProps.getProperty("linkFile")));
		System.out.println("Links have been read from file");
		Parser prs = new Parser();

		//for (int ii=1 ; ii< linkList.size();ii++)  // for every link (station) from file
		for (int ii=0 ; ii< 1; ii++)  // for every link (station) from file
		{
			trainList.addAll(prs.parseStationPage(linkList.get(ii)));
		}

		for (Train train : trainList){
			prs.parseTrainRoute(train);
		}

		//saveTrainListToFile();
		Database db = new Database("jbdc:oracle:thin:"+Utils.mainProps.getProperty("db"), Utils.mainProps.getProperty("user"), Utils.mainProps.getProperty("pass"));
		db.saveTrainListToDb( trainList);


	}


	public void saveTrainListToFile() {
		ArrayList<String> tmpList = new ArrayList<>();
		ArrayList<Train>  sortedTrainList = new ArrayList(trainList);

		Collections.sort(sortedTrainList,Train.compareByTrainId);

		for (Train train : sortedTrainList) {
			tmpList.add(train.toString());
		}

		try {
			Files.write(Paths.get(Utils.mainProps.getProperty("trainFilePath")), tmpList, Charset.defaultCharset());
			System.out.println("Trains saved to file");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		tmpList.clear();

		for (Train train : trainList) {
			for (RouteItem item : train.trainRoute) {
			tmpList.add(train.getTrainId() + "|"+ item.toString());
			}
		}
		try {
			Files.write(Paths.get(Utils.mainProps.getProperty("routeFilePath")), tmpList, Charset.defaultCharset());
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
			JOptionPane.showConfirmDialog(null,"Nothing here");
		}


	}

	private class GetFromDbAction implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			//getTrainListfromDBtoFile();
			Database db = new Database("jbdc:oracle:thin:"+Utils.mainProps.getProperty("db"), Utils.mainProps.getProperty("user"), Utils.mainProps.getProperty("pass"));
			db.getTrainsFromDb();
		}


	}
}
