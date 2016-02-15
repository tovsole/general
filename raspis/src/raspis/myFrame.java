package raspis;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class myFrame extends JFrame{
    private int width=500;
    private int height=400;
    
	public myFrame() 
	{
		super();
		System.out.println("CLASSPATH = "+System.getProperty("java.class.path"));
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		this.setTitle("Получаем расписание");
		this.setResizable(true);
		//--
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension ScreenSize = kit.getScreenSize();
		width= ScreenSize.width/2;
		height=ScreenSize.height/2;
		this.setSize(width, height);
		this.setLocation(width/4, height/4);
		this.setVisible(true);
		//--
		
		JPanel panel1 = new JPanel();
		panel1.setBorder(BorderFactory.createEtchedBorder(1));
		panel1.setLayout( new FlowLayout() );
		this.add(panel1,BorderLayout.SOUTH);
		
		JButton btn1 = new JButton("Get rasp");
		JButton btn2 = new JButton("Exit");
		
		btn1.addActionListener(new HtmlAction());
		btn2.addActionListener(new ExitAction());
				
		panel1.add(btn1);
		panel1.add(btn2);
				
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
		    int i=JOptionPane.showConfirmDialog(null, "Are you sure?");
			 
			if      (i==0)  
			{ 
				HtmlCraft htm = new HtmlCraft();
				try 
				{
				   //htm.getHTMLbyStations("http://uz.gov.ua/passengers/timetable/?station=739%2C47125%2C22080%2C47140%2C47175%2C22298%2C22000%2C47190%2C3723&by_station=%D0%9F%D0%BE%D1%88%D1%83%D0%BA");
					htm.getTrainList();	
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}	  			
		}
	}

}
