package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class ImageCanvasSQI extends JPanel implements ActionListener {

	
		private MyRetinexSQI retinexSQI;
		//private static BufferedImage smoothColorImage; //grey image of input image
		private JFileChooser chooser;//for loading image using a file chooser.
		private final static String[] extensionsAllowedJFileChooser = new String[]{"gif","jpg","jpeg","png"};
		private final static String descriptionJFileChooser = "Image files *.gif, *.png, *.jpeg, *.jpg";
		
		private BufferedImage loadedImage, processedImage; //image object of input image
		
		public ImageCanvasSQI(MyRetinexSQI retinexSQI){
			//setBackground(Color.white);
			this.retinexSQI = retinexSQI;
			
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			
			if (event.getActionCommand().equalsIgnoreCase("exit")){
	             System.exit(0);
	          }
		
	        if (event.getActionCommand().equalsIgnoreCase("load image")){
	        	LoadImage();
	        	repaint();
	          }
	        
	        if (event.getActionCommand().equalsIgnoreCase("save image")){
	        	SaveImage();
	        }
	    
			
		}
		
		
		public void LoadImage(){
			String imageName;
			
			File dir = new File(".");
			chooser = new JFileChooser(dir);
			FileFilter filter = new PictureFilter(descriptionJFileChooser ,extensionsAllowedJFileChooser);//Create the file filter with parameters String, String[].
			
			
			chooser.setFileFilter(filter);//Set the filter to the FileChooser
			chooser.setDialogTitle("Image Selecter");//Set title for the file chooser.
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//Only choose files.
			chooser.setAcceptAllFileFilterUsed(false);// Disallow all files to be shown.
			
			
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				dir = chooser.getSelectedFile();
				System.out.println("File Selected! " + dir);
				imageName = dir.getPath();//Get's the directory of the file as a string.
				
				loadedImage = MyImageUtilities.getBufferedImage(imageName, this);
			}
			else{
				System.out.println("Nothing Selected!");
			}
		}
	
		public void SaveImage(){
			processedImage = loadedImage;
           File savedFile=new File("test.jpg");
           try {
           ImageIO.write(processedImage,"jpeg",savedFile);
           System.out.println("Image Saved!");
           } catch (Exception e) {}
		}
		
		public void paint(Graphics g) {//Draws the image on the screen.
			boolean x = false;
	        g.setColor(Color.WHITE);
	        g.fillRect(0, 0, getWidth(), getHeight());
	        if (loadedImage != null) {
	        	while(!x) x = g.drawImage(loadedImage, 0, 0, null);
	        }

		}
		
		
	
}
