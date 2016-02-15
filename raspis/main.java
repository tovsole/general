package raspis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class main {
    //public myFrame frame1;
    
	public main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) 
	{
	  EventQueue.invokeLater( new Runnable()
	   {		
		  public void run()
			{	  
				myFrame frame1 = new myFrame();
					
				
			}	
	   });
	  
		
	}

}
