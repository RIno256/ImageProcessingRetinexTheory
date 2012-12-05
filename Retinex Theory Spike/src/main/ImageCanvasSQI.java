package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
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
    	
    	private double[][] processedImageRed;//Double to avoid rounding erros in calculations.
    	private double[][] processedImageGreen;
    	private double[][] processedImageBlue;
		
		private BufferedImage loadedImage, processedImage; //image object of input image
		private Kernel kernel;
		private int[] bufferedImageData2;
		private short[][] bufferedImageRed2;
		private short[][] bufferedImageGreen2;
		private short[][] bufferedImageBlue2;
		private int[][] processedIntImageRed2;
		private int[][] processedIntImageGreen2;
		private int[][] processedIntImageBlue2;
		private int totalRows2;
		private int totalCols2;
		
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
	        	processedImage = MSRCR(loadedImage);
	        	
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
			}
			else{
				System.out.println("Nothing Selected!");
			}
		}
	
		public void SaveImage(){
			//processedImage = packImage(bufferedImageRed, bufferedImageGreen, bufferedImageBlue);
            File savedFile=new File("test.jpg");
            try {
            ImageIO.write(processedImage,"png",savedFile);
            //Image image = retinexSQI.getToolkit().getImage(savedFile.getPath());
    		//Graphics2D g2d = processedImage.createGraphics();
    		//g2d.drawImage(image, 0, 0, retinexSQI);
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
	    
	    public void unpackImage2(BufferedImage bufferedImage) {

			
			if (bufferedImage.getHeight() != totalRows2 || bufferedImage.getWidth() != totalCols2) {
	            totalRows2 = bufferedImage.getHeight();
	            totalCols2 = bufferedImage.getWidth();

	            bufferedImageRed2   = new short[totalRows2][totalCols2];
	            bufferedImageGreen2 = new short[totalRows2][totalCols2];
	            bufferedImageBlue2  = new short[totalRows2][totalCols2];
	        }

	        bufferedImageData2 = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(),
	                                           bufferedImage.getHeight(), null, 0,
	                                           bufferedImage.getWidth());
	        int index;
	        for (int currentRow = 0; currentRow < totalRows2; currentRow++) {
	            for (int currentCol = 0; currentCol < totalCols2; currentCol++) {
	                index = (currentRow * totalCols2) + currentCol;
	                unpackPixel(bufferedImageData2[index], bufferedImageRed2, bufferedImageGreen2, bufferedImageBlue2, currentRow, currentCol);
	            }
	        }
	    }
	    
	    
	    
	    public BufferedImage packImage(int[][] processedImageRed,
                								int[][] proccesedImageGreen,
                								int[][] processedImageBlue ) {
	    	
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
	    
	    public BufferedImage packImage2(int[][] processedImageRed,
				int[][] proccesedImageGreen,
				int[][] processedImageBlue ) {

int[] newBufferedImageData = new int[totalRows2 * totalCols2];
int index;
for (int currentRow = 0; currentRow < totalRows2; currentRow++) {
for (int currentCol = 0; currentCol < totalCols2; currentCol++) {
index = (currentRow * totalCols2) + currentCol;
newBufferedImageData[index] = packPixel(processedImageRed[currentRow][currentCol],
				proccesedImageGreen[currentRow][currentCol],
				processedImageBlue[currentRow][currentCol]
				);
}
}

BufferedImage newImage = new BufferedImage(totalCols2, totalRows2, BufferedImage.TYPE_INT_RGB);
for (int row = 0; row < totalRows2; row++) {
for (int col = 0; col < totalCols2; col++) {
index = (row * totalCols2) + col;
newImage.setRGB(col, row, newBufferedImageData[index]);
}
}



return newImage;
}
	    
	    public void reflectance(short[][] bufferedImageRed,short[][] bufferedImageGreen,short[][]
	    						bufferedImageBlue,int imageRows,int imageCols){
	    
	    	float[][] refRed = new float[imageRows][imageCols],
	    			  refGreen = new float[imageRows][imageCols], 
	    			  refBlue= new float[imageRows][imageCols];
	    	float red, green, blue;
	    	float redMax = 0, greenMax = 0, blueMax = 0;
	    	
	    	for(int i=0;i<imageRows;i++)
	    		for(int j=0; j<imageCols;j++){
	    			red = bufferedImageRed[i][j];
	    			green = bufferedImageGreen[i][j];
	    			blue = bufferedImageBlue[i][j];
	    			
	    			if(redMax < red){redMax = red;}
	    			if(greenMax < green){greenMax = green;}
	    			if(blueMax < blue){blueMax = blue;}
	    		}
	    	for(int i = 0;i<imageRows;i++)
	    		for(int j = 0;j<imageCols;j++){
	    				    			
	    			red = bufferedImageRed[i][j];
	    			green = bufferedImageGreen[i][j];
	    			blue = bufferedImageBlue[i][j];
	    			
	    			refRed[i][j] =((red/redMax)*255f);
	    			refGreen[i][j] =((green/greenMax)*255f);
	    			refBlue[i][j] = ((blue/blueMax)*255f);
	    			
	    			this.bufferedImageRed[i][j]=(short) refRed[i][j];
	    			this.bufferedImageGreen[i][j]=(short) refGreen[i][j];
	    			this.bufferedImageBlue[i][j]=(short) refBlue[i][j];
	    			
	    		}
	    	
	    	
	    	
	    	
	    	
	    	
	    }
	    
	    
	    public float[][] linearise (short[][] bufferedImageRed,short[][] bufferedImageGreen,short[][] 
	    						bufferedImageBlue,int imageRows,int imageCols){
	    	float[][] linearisedImage = new float[3][imageRows * imageCols];
	    	
	    	for(int i = 0; i < imageRows ; i++)
	    		for(int j = 0; j < imageCols ; j++){
	    			linearisedImage[0][(i*imageCols)+j] = (float)bufferedImageRed[i][j];
	    			linearisedImage[1][(i*imageCols)+j] = (float)bufferedImageGreen[i][j];
	    			linearisedImage[2][(i*imageCols)+j] = (float)bufferedImageBlue[i][j];
	    		}
	    	return linearisedImage;
	    }
	    
	    public short[][] delinearise (float[] imageChannels, int imageRows, int imageCols,int channel){
	    	short[][] colourChannel = new short[imageRows][imageCols];
	    	
	    	for(int i = 0; i < imageRows; i++){//For each width.//For each red, green, blue.-----------------------------De-linearise.
	    		for(int j=0; j < imageCols; j++){//For each height.
	    			colourChannel[i][j]= (short)imageChannels[(i*imageCols)+j];//Change float to short.
	    		}		    		
	    	}
			return colourChannel;
	    }
	    
	    
	    /**
	     * Retinex Algorithm.
	     * MultiScale Retinex with Colour Restoration(MSRCR).
	     * Takes in the image's red, green, blue information for each pixel as 3 short[][].
	     * Takes in image rows for height and cols for width as ints.
	     */
	    public BufferedImage MSRCR(BufferedImage src){
	    	BufferedImage finishedImage;
	    	//int linearImageArraySize = imageRows * imageCols * 3;//3 for RGB.

		    //imageChannels = linearise(bufRed, bufGreen, bufBlue, imageRows, imageCols);//Linearised Image.
		    
		    
		    //reflectance(bufferedImageRed, bufferedImageGreen, bufferedImageBlue, imageRows, imageCols);
		    finishedImage = gaussianBlur(src);//Not ideal.
		    
		    unpackImage2(finishedImage);
		    processedImageRed= new double[totalRows2][totalCols2];
			processedImageGreen=new double[totalRows2][totalCols2];
			processedImageBlue=new double[totalRows2][totalCols2];
		    //Singlescalar retinex.
		    for(int i=0;i<totalRows2;i++){
		    	for(int j=0;j<totalCols2;j++){
		    		processedImageRed[i][j] = 10*(Math.log(bufferedImageRed2[i][j]) - Math.log((bufferedImageRed2[i][j])*bufferedImageRed2[i][j]));
		    		processedImageGreen[i][j] = 10*(Math.log(bufferedImageGreen2[i][j]) - Math.log((bufferedImageGreen2[i][j])*bufferedImageGreen2[i][j]));
		    		processedImageBlue[i][j] = 10*(Math.log(bufferedImageBlue2[i][j]) - Math.log((bufferedImageBlue2[i][j])*bufferedImageBlue2[i][j]));
		    	}
		    }
			processedIntImageRed2= new int[totalRows2][totalCols2];
			processedIntImageGreen2=new int[totalRows2][totalCols2];
			processedIntImageBlue2=new int[totalRows2][totalCols2];

			
		    for(int i=0;i<totalRows2;i++)
		    	for(int j=0;j<totalCols2;j++){
		    		processedIntImageRed2[i][j] = (int)processedImageRed[i][j];
		    		processedIntImageGreen2[i][j] = (int)processedImageGreen[i][j];
		    		processedIntImageBlue2[i][j] = (int)processedImageBlue[i][j];
		    	}
		    
	    	finishedImage = packImage2(processedIntImageRed2, processedIntImageGreen2, processedIntImageBlue2);
	    	return finishedImage;
	    }
	
	    
		public static int clamp(int c) {
			if (c < 0)
				return 0;
			if (c > 255)
				return 255;
			return c;
		}
	    /**
	     * http://www.jhlabs.com/ip/GaussianFilter.java
	     * 
	     * @param src
	     * @return
	     */
	    public BufferedImage gaussianBlur( BufferedImage src) {
	        int width = src.getWidth();
	        int height = src.getHeight();
	        kernel = make1DKernel(3);
	        
	        int[] inPixels = new int[width*height];
	        int[] outPixels = new int[width*height];
	        src.getRGB( 0, 0, width, height, inPixels, 0, width );

			convolveAndTranspose(kernel, inPixels, outPixels, width, height);
			convolveAndTranspose(kernel, outPixels, inPixels, height, width);

	        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			dst.setRGB( 0, 0, width, height, inPixels, 0, width );
	        return dst;
	    }
		/**
		 * 
		 * http://www.jhlabs.com/ip/GaussianFilter.java
		 * 
		 * @param kernel
		 * @param inPixels
		 * @param outPixels
		 * @param width
		 * @param height
		 */
		public static void convolveAndTranspose(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height) {
			float[] matrix = kernel.getKernelData( null );
			int cols = kernel.getWidth();
			int halfCols = cols/2;

			for (int y = 0; y < height; y++) {
				int index = y;
				int ioffset = y*width;
				for (int x = 0; x < width; x++) {
					float r = 0, g = 0, b = 0, a = 0;
					int moffset = halfCols;
					for (int col = -halfCols; col <= halfCols; col++) {
						float f = matrix[moffset+col];

						if (f != 0) {
							int ix = x+col;
							if ( ix < 0 ) {
									ix = 0;
							} else if ( ix >= width) {
									ix = width-1;
							}
							int rgb = inPixels[ioffset+ix];
							a += f * ((rgb >> 24) & 0xff);
							r += f * ((rgb >> 16) & 0xff);
							g += f * ((rgb >> 8) & 0xff);
							b += f * (rgb & 0xff);
						}
					}
					int ia = clamp((int)(a+0.5));// : 0xff;
					int ir = clamp((int)(r+0.5));
					int ig = clamp((int)(g+0.5));
					int ib = clamp((int)(b+0.5));
					outPixels[index] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
	                index += height;
				}
			}
		}
		/**
		 * http://www.jhlabs.com/ip/GaussianFilter.java
		 * 
		 * 
		 * @param radius
		 * @return
		 */
		public static Kernel make1DKernel(float radius) {
			int r = (int)Math.ceil(radius);
			int rows = r*2+1;
			float[] matrix = new float[rows];
			float sigma = radius/3;
			float sigma22 = 2*sigma*sigma;
			float sigmaPi2 = 2*((float)Math.PI)*sigma;
			float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2);
			float radius2 = radius*radius;
			float total = 0;
			int index = 0;
			for (int row = -r; row <= r; row++) {
				float distance = row*row;
				if (distance > radius2)
					matrix[index] = 0;
				else
					matrix[index] = (float)Math.exp(-(distance)/sigma22) / sqrtSigmaPi2;
				total += matrix[index];
				index++;
			}
			for (int i = 0; i < rows; i++)
				matrix[i] /= total;

			return new Kernel(rows, 1, matrix);
		}

}
