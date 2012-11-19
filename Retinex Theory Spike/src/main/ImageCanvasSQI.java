package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class ImageCanvasSQI extends JPanel implements ActionListener {


		private static BufferedImage inputImage; //image object of input image
		//private static BufferedImage smoothColorImage; //grey image of input image
		public File selectedFile = new File("");
		  // for loading images for registration
		public FileFilter[] filtersDynamic = new FileFilter[] {new TextFilter()};
		
		
		public ImageCanvasSQI(){
			setBackground(Color.white);
			
			BufferedImage sourceImage = null;
	        JFileChooser fc = new JFileChooser();
	        JFileChooser fe = new JFileChooser();
	        
			
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			
			if (event.getActionCommand().equalsIgnoreCase("exit")){
	             System.exit(0);
	          }

	        if (event.getActionCommand().equalsIgnoreCase("load image")){
	             //display=1;
	             //LoadImage();
	          }

			
		}
	
	
}
