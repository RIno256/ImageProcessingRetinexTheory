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
		
		private ImageHandler loadedImage, processedImage; //image object of input image
		private Kernel kernel;
		
		public ImageCanvasSQI(MyRetinexSQI retinexSQI){
			this.retinexSQI = retinexSQI;
			
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			
			if (event.getActionCommand().equalsIgnoreCase("exit")){
	             System.exit(0);
	          }
		
	        if (event.getActionCommand().equalsIgnoreCase("load image")){
	        	loadedImage = LoadImage();
	        	processedImage = loadedImage;//Initialise the processedImage so the src can be saved as dst if needed.
	        	repaint();
	          }
	        
	        if (event.getActionCommand().equalsIgnoreCase("save image")){
	        	SaveImage(processedImage);
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
		public ImageHandler LoadImage(){
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
				
				ImageHandler src = new ImageHandler(MyImageUtilities.getBufferedImage(imageName, retinexSQI));
				return src;
			}
			else{
				System.out.println("Nothing Selected!");
			}
			return null;
		}
	
		public void SaveImage(ImageHandler processedImage){
            File savedFile=new File("test.jpg");
            try {
            ImageIO.write(processedImage.getImage(),"png",savedFile);
            System.out.println("Image Saved!");
            } catch (Exception e) {}
		}
		
		
		
		public void paint(Graphics g) {//Draws the image on the screen.
			boolean x = false;
	        g.setColor(Color.WHITE);
	        g.fillRect(0, 0, getWidth(), getHeight());
	        if (loadedImage != null) {
	        	while(!x) x = g.drawImage(loadedImage.getImage(), 0, 0, null);
	        }
	    
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
	    			
	    			//this.bufferedImageRed[i][j]=(short) refRed[i][j];
	    			//this.bufferedImageGreen[i][j]=(short) refGreen[i][j];
	    			//this.bufferedImageBlue[i][j]=(short) refBlue[i][j];
	    			
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
	    public ImageHandler MSRCR(ImageHandler loadedImage){

	    	BufferedImage imageTemp;
	    	int totalRows, totalCols;
	    	totalRows = loadedImage.getTotalRows();
	    	totalCols = loadedImage.getTotalCols();
	    	
	    	float[][] hueI, satI, valI;
	    	float[] tempHSV;
	    	float r, g, b;
	    	short[][] redI, greenI, blueI;
	    	
	    	//Convert RGB to HSV. Only the V is used here to help create a more efficient Guassian Blue.
	    	redI = loadedImage.getBufferedImageRed();
	    	greenI = loadedImage.getBufferedImageGreen();
	    	blueI = loadedImage.getBufferedImageBlue();
	    	tempHSV = new float[3];
	    	hueI = new float[totalRows][totalCols];
	    	satI = new float[totalRows][totalCols];
	    	valI = new float[totalRows][totalCols];
	    	
	    	for(int i=0;i<totalRows;i++)
	    		for(int j=0;j<totalCols;j++){

	    			r = redI[i][j];
	    			g = greenI[i][j];
	    			b = blueI[i][j];
	    			
	    			tempHSV = RBGtoHSV(r, g, b);
	    			
	    			hueI[i][j] = tempHSV[0];
	    			satI[i][j] = tempHSV[1];
	    			valI[i][j] = tempHSV[2];
	    		}
	    	
	    	
		    
		    //reflectance(bufferedImageRed, bufferedImageGreen, bufferedImageBlue, imageRows, imageCols);
		    imageTemp = gaussianBlur(loadedImage.getImage());//Not ideal.
		    /*
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
		    
		    */
	    	//finishedImage = packImage2(processedIntImageRed2, processedIntImageGreen2, processedIntImageBlue2);
		    loadedImage.setImage(imageTemp);
		    ImageHandler finishedImage = new ImageHandler(loadedImage.packImage());
		    return finishedImage;
	    }
	
	    /**
	     * Converts RGB colour arrays for a given image into HSV.
	     */
		private float[] RBGtoHSV(float r, float g, float b) {
			
			float min, max, delta;
			float h, s, v;
			
			r /= 255f; g /= 255f; b /= 255f;
			
			min = Math.min(Math.min(r, g), b);
			max = Math.max(Math.max(r, g), b);
			
			v = max;
			
			delta = max - min;
			
			if(max == 0){s = 0f;}
			else{s = delta /max;}
			
			if(delta != 0){
				if(r == max){
					h = (g-b)/delta;
					if(h < 0){h += 6f;}
				}else{
					if(g == max){
						h = 2f + (b - r)/delta;
					}else{h = 4f + ( r - g ) / delta;}
				}
				
				h *= 60f;
			}else{h = 0f;}
			return new float[] {h,s,v};
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
