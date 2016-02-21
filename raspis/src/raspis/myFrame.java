package raspis;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class myFrame extends JFrame{
    private int width=500;
    private int height=400;
	public HtmlCraft htmlCraftObj;
    
	public myFrame() 
	{
		super();
		System.out.println("CLASSPATH = "+System.getProperty("java.class.path"));
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		this.setTitle("получаем расписание");
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
		JButton btn3 = new JButton("Print train List");
		
		btn1.addActionListener(new HtmlAction());
		btn2.addActionListener(new ExitAction());
		btn3.addActionListener(new PrintTrainsAction());
				
		panel1.add(btn1);
		panel1.add(btn2);
		panel1.add(btn3);
				
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
					htm.getTrainList();
					//myFrame.htmlCraftObj = htm;
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}	  			
		}
	}
	public class PrintTrainsAction implements ActionListener{
		public void actionPerformed (ActionEvent e){
			//
		}


	}
}
