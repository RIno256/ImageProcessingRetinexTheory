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

	
		//A test commit.
	
		private MyRetinexSQI retinexSQI;
		private static BufferedImage loadedImage; //image object of input image
		//private static BufferedImage smoothColorImage; //grey image of input image
		private JFileChooser chooser;//for loading image using a file chooser.
		private String imageName;
		
		
		public ImageCanvasSQI(MyRetinexSQI retinexSQI){
			setBackground(Color.white);
			this.retinexSQI = retinexSQI;
			//BufferedImage sourceImage = null;
	        
			
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			
			if (event.getActionCommand().equalsIgnoreCase("exit")){
	             System.exit(0);
	          }

	        if (event.getActionCommand().equalsIgnoreCase("load image")){
	             LoadImage();
	          }

			
		}
		
		
		public void LoadImage(){
			File dir = new File(".");
			FileFilter filt = new PictureFilter();
			
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(dir);//Start at in set directory.
			chooser.setDialogTitle("Image Selecter");//Set title for the file chooser.
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//Only choose files.
			//chooser.setAcceptAllFileFilterUsed(false);// Disallow all files to be shown.
			
			
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				dir = chooser.getSelectedFile();
				System.out.println("File Selected! " + dir);
				imageName = dir.getPath();//Get's the directory of the file as a string.
				
				loadedImage = MyImageUtilities.getBufferedImage(imageName, retinexSQI);
				
			}
			else{
				System.out.println("Nothing Selected!");
			}
			
			
		}
	
	
}
