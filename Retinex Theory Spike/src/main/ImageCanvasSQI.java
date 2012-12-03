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

    	
    	private int totalRows = 0;
    	private int totalCols = 0;
    	private int[] bufferedImageData;
    	
    	private short[][] bufferedImageRed;
    	private short[][] bufferedImageGreen;
    	private short[][] bufferedImageBlue;
		
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
	        if (event.getActionCommand().equalsIgnoreCase("process image")){
	        	processedImage = MSRCR(bufferedImageRed, bufferedImageGreen, bufferedImageBlue, totalRows, totalCols);
	        	
	        }
	    
			
		}
		
		/**
		 * Creates a FileChooser.
		 * Creates a picturesFilter which extends FileFilter.
		 * File Filter added to the FileChooser
		 */
		public void LoadImage(){
			String imageName;
			
			File dir = new File(".");
			chooser = new JFileChooser(dir);
			PictureFilter filter = new PictureFilter(descriptionJFileChooser ,extensionsAllowedJFileChooser);//Create the file filter with parameters String, String[].
			
			
			chooser.setFileFilter(filter);//Set the filter to the FileChooser
			chooser.setDialogTitle("Image Selecter");//Set title for the file chooser.
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//Only choose files.
			chooser.setAcceptAllFileFilterUsed(false);// Disallow all files to be shown.
			
			
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				dir = chooser.getSelectedFile();
				System.out.println("File Selected! " + dir);
				imageName = dir.getPath();//Get's the directory of the file as a string.
				
				loadedImage = MyImageUtilities.getBufferedImage(imageName, retinexSQI);
				unpackImage(loadedImage);
			}
			else{
				System.out.println("Nothing Selected!");
			}
		}
	
		public void SaveImage(){
			//processedImage = packImage(bufferedImageRed, bufferedImageGreen, bufferedImageBlue);
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
		
		
	    /** It takes a 24-bit pixel in the form 0xRRGGBB
	    * and pulls out the three components, storing
	    * them position (row, col) or the red, green,
	    * and blue arrays.
	    * Taken from RetinexSQI.java for CS24110.
		*/
	    private static void unpackPixel(int pixel, short [][] red, short [][] green, short [][] blue, int row, int col) {
	        red[row][col]   = (short)((pixel >> 16) & 0xFF);
	        green[row][col] = (short)((pixel >>  8) & 0xFF);
	        blue[row][col]  = (short)((pixel >>  0) & 0xFF);
	    }
	    
	    
	    /**
	     * 
	     * 
	     * 
	     */
	    private static int packPixel(int red, int green, int blue) {
	        return (red << 16) | (green << 8) | blue;
	    }
	    
	    public void unpackImage(BufferedImage bufferedImage) {

			
			if (bufferedImage.getHeight() != totalRows || bufferedImage.getWidth() != totalCols) {
	            totalRows = bufferedImage.getHeight();
	            totalCols = bufferedImage.getWidth();

	            bufferedImageRed   = new short[totalRows][totalCols];
	            bufferedImageGreen = new short[totalRows][totalCols];
	            bufferedImageBlue  = new short[totalRows][totalCols];
	        }

	        bufferedImageData = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(),
	                                           bufferedImage.getHeight(), null, 0,
	                                           bufferedImage.getWidth());

	        int index;
	        for (int currentRow = 0; currentRow < totalRows; currentRow++) {
	            for (int currentCol = 0; currentCol < totalCols; currentCol++) {
	                index = (currentRow * totalCols) + currentCol;
	                unpackPixel(bufferedImageData[index], bufferedImageRed, bufferedImageGreen, bufferedImageBlue, currentRow, currentCol);
	            }
	        }
	    }
	    
	    public BufferedImage packImage(short[][] processedImageRed,
                								short[][] proccesedImageGreen,
                								short[][] processedImageBlue ) {
	    	
			int[] newBufferedImageData = new int[totalRows * totalCols];
	    	int index;
	    	for (int currentRow = 0; currentRow < totalRows; currentRow++) {
	    		for (int currentCol = 0; currentCol < totalCols; currentCol++) {
	    			index = (currentRow * totalCols) + currentCol;
	    			newBufferedImageData[index] = packPixel(processedImageRed[currentRow][currentCol],
	    										proccesedImageGreen[currentRow][currentCol],
	    										processedImageBlue[currentRow][currentCol]
	    										);
	    		}
	    	}

	    	BufferedImage newImage = new BufferedImage(totalCols, totalRows, BufferedImage.TYPE_INT_RGB);
	    	for (int row = 0; row < totalRows; row++) {
	    		for (int col = 0; col < totalCols; col++) {
	    			index = (row * totalCols) + col;
	    			newImage.setRGB(col, row, newBufferedImageData[index]);
	    		}
	    	}

	    	return newImage;
	    }
	    
	    /**
	     * Retinex Algorithm.
	     * MultiScale Retinex with Colour Restoration(MSRCR).
	     * Takes in the image's red, green, blue information for each pixel as 3 short[][].
	     * Takes in image rows for height and cols for width as ints.
	     */
	    public BufferedImage MSRCR(short[][] bufferedImageRed,short[][] bufferedImageGreen,short[][]
	    						  bufferedImageBlue,int imageRows,int imageCols){

	    	BufferedImage finishedImage;
	    	//int linearImageArraySize = imageRows * imageCols * 3;//3 for RGB.
	    	int linearChannelArraySize = imageRows * imageCols;
	    	int channel;
	    	
	    	//float[] imageRGB= new float[linearImageArraySize];
	    	//float[] out = new float[linearChannelArraySize];
	    	float[][] imageChannels = new float[3][linearChannelArraySize];//3 for RGB.
	    	short[][] currentChannel = new short[imageRows][imageCols];//Used to store current channel information.
	    		    	
		    for(channel = 0;channel < 3; channel++){//For each red, green, blue.-----------------------------Linearise.
		    	switch(channel){//Sets the current colour channel.
		    	case 0: currentChannel = bufferedImageRed;	
		    	case 1: currentChannel = bufferedImageGreen;
		    	case 2: currentChannel = bufferedImageBlue;
		    	}
		    	for(int i = 0; i < imageRows; i++){//For each width.
		    		for(int j=0; j < imageCols; j++){//For each height.
		    			imageChannels[channel][i+j] = (float)currentChannel[i][j];//Change short to float.
		    		}		    		
		    	}
		    	
		    	
		    	
		    	//ImageRGB assigned.
		    }
	    	imageChannels = GaussianBlur(imageChannels,imageRows,imageCols);//GaussianBlur.
	    	
	    	for(int i = 0; i < imageRows; i++){//For each width.//For each red, green, blue.-----------------------------De-linearise.
	    		for(int j=0; j < imageCols; j++){//For each height.
	    			bufferedImageRed[i][j]= (short)imageChannels[0][i+j];//Change short to float.
	    			bufferedImageGreen[i][j]= (short)imageChannels[1][i+j];;
	    			bufferedImageBlue[i][j]= (short)imageChannels[2][i+j];;
	    		}		    		
	    	}
	    	finishedImage = packImage(bufferedImageRed, bufferedImageGreen, bufferedImageBlue);
	    	return finishedImage;
	    }
	    
	    /**
	     * Gaussian Blur
	     * 
	     * @param imageChannels
	     * @param imageRows
	     * @param imageCols
	     * @return
	     */
	    public float[][] GaussianBlur(float[][] imageChannels, int imageRows, int imageCols){
	    	int size = 3;
	    	double [] kernel = new double[size*size];
	    	double total, tot;
	    	float [][] smoothImage = new float[3][imageRows+imageCols];
	    	
	    	for(int i=0;i<size;i++)
	    		for(int j=0;j<size;j++)
	    			kernel[i+j]=Math.exp(-((i-(size/2))*(i-(size/2))+(j-(size/2))*(j-(size/2)))/(2*1f*1f));
	    	for(int channel=0; channel<3;channel++)
		    	for(int rows=0;rows < imageRows;rows++)
		    		for(int cols=0;cols < imageCols;cols++)
		    			smoothImage[channel][rows + cols] = imageChannels[channel][rows + cols];

	    	for(int row=0; row < imageRows; row++){
	    	      for(int col=0; col < imageCols; col++) {
	    	      total=tot=0f;
		    	for(int channel=0; channel<3;channel++){     
		    	      for(int i=0; i<size; i++)
		    	    	  for(int j=0; j<size; j++)
		    	    		  if((row-size/2+i)>=0 && (row-size/2+i)<imageRows && (col-size/2+j)>=0 && (col-size/2+j)<imageCols) {   
		    	    			  total += kernel[i+j] * imageChannels[channel][(col-size/2+j)+(row-size/2+i)];
		    	    			  tot += kernel[i+j];
		    	    		  }
		    	      smoothImage[channel][row+col]=(float) (total/tot);
		    	      }
		    	}
	    	}
	    	return imageChannels;
	    }	


}
