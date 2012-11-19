package main;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class MyRetinexSQI extends JFrame {

		private JMenuItem LoadImage, Exit, About, SaveImage;
	
		public MyRetinexSQI(){
			super("My Retinex Example");
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setSize(800, 600);
			this.setVisible(true);
			
		    JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);
			
			ImageCanvasSQI canvas = new ImageCanvasSQI();
			this.setContentPane(canvas);
			
	    	JMenu fileMenu = new JMenu("File");
	        JMenu imageProcessingMenu = new JMenu("Image Processing");
	        JMenu otherMenu = new JMenu("Other");
	        
	        LoadImage = new JMenuItem("Load Image");
	    	LoadImage.addActionListener(canvas);
	        
	        Exit = new JMenuItem("Exit");
	        Exit.addActionListener(canvas);
	         
		    fileMenu.add(LoadImage);
		    fileMenu.add(new JSeparator());
	    	fileMenu.add(Exit);
	    	
		    menuBar.add(fileMenu);
            menuBar.add(imageProcessingMenu);
            menuBar.add(otherMenu);
			
	        
		}
	
	
	
		public static void main(String[] args){
			@SuppressWarnings("unused")
			MyRetinexSQI run = new MyRetinexSQI();
			
		}
	
}
