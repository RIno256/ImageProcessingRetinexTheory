package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ImageCanvasSQI extends JPanel implements ActionListener {

	
		/**
	 * 
	 */
	private static final long serialVersionUID = -752847105553481270L;
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
	        	//processedImage = loadedImage;//Initialise the processedImage so the src can be saved as dst if needed.
	        	repaint();
	          }
	        
	        if (event.getActionCommand().equalsIgnoreCase("save image")){
	        	SaveImage(processedImage);
	        	repaint();
	        }
	        if (event.getActionCommand().equalsIgnoreCase("process image")){
	        	processedImage = SSR(loadedImage);
	        	
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
		

	    
	    public ImageHandler reflectance(ImageHandler img, double c){
			
	    	float[][] valI, valG, ref;
	    	int rows, cols;
	    	rows = img.getTotalRows();
	    	cols = img.getTotalCols();
	    	
	    	valI = img.getValI();
	    	valG = img.getGausValI();
	    	ref = new float[rows][cols];
	    	
	    	c=0.1;
	    			
	    	for(int i=0;i<rows;i++)
	    		for(int j=0;j<cols;j++){
	    			ref[i][j] = (float)(Math.log(((valI[i][j])+c)/((Math.max(valI[i][j], valG[i][j]))+ c)));
	    			if(ref[i][j] > 0){
	    				//System.out.println(ref[i][j]);
	    			}
	    		}
	    	img.setReflectance(ref);
	    	return img;
	    	
	    }
	    
	    public ImageHandler normalisation(ImageHandler img){
			
	    	int rows,cols, lowNorm, highNorm;
			float[][] ref, norm;
			float max, min;
			
			rows = img.getTotalRows();
			cols = img.getTotalCols();
	    	ref = img.getReflectance();
	    	norm = new float[rows][cols];
			max = Float.NEGATIVE_INFINITY;
			min = Float.POSITIVE_INFINITY;
			lowNorm = 0;
			highNorm = 1;
	    	
	    	for(int i=0;i<rows;i++)
	    		for(int j=0;j<cols;j++){
	    			if(max < ref[i][j]){max = ref[i][j];}
	    			if(ref[i][j] < min){min = ref[i][j];}
	    		}
	    	System.out.println(min + " " + max);
	    	/** a + (x-A)*(b-a)/(B-A) where:
	    	 * A is min value
	    	 * B is max value
	    	 * a is lowest value after normalisation (0)
	    	 * b is highest value after normalisation (1)
	    	 * x is the current value
	    	 * */ 
	    	
	    	for(int i=0;i<rows;i++)
	    		for(int j=0;j<cols;j++){
	    			norm[i][j] = (lowNorm + (((ref[i][j] - min)*(highNorm - lowNorm))/(max - min)));
	    		}
	    	img.setNormalisedReflectance(norm);
	    	
	    	return img;
	    	
	    }
	    
	    
	    /**
	     * Retinex Algorithm.
	     * Singlescale Retinex(SSR).
	     * Takes ImageHandler that holds all the pictures information.
	     * Takes in image rows for height and cols for width as ints.
	     */
	    public ImageHandler SSR(ImageHandler inputImage){

	    	ImageHandler image = inputImage;
	    	int totalRows, totalCols;
	    	totalRows = image.getTotalRows();
	    	totalCols = image.getTotalCols();
	    	
		    image.RGBtoHSV();
	    	image = gaussianBlur(image, 3);
		    
		    image = reflectance(image, 1.5);
		    
		    image = normalisation(image);
		    image.HSVtoRGB();
		    
		    //Singlescalar retinex.
		    float[][] red, green, blue;
		    short[][] redI, greenI, blueI;
		    
		    red = image.getProRed();
		    green = image.getProGreen();
		    blue =  image.getProBlue();
		    
		    redI = image.getBufferedImageRed();
		    greenI = image.getBufferedImageGreen();
		    blueI = image.getBufferedImageBlue();
		    /*
		    float gain = 4f;
		    float alpha = 128f;
		    float offset = 0f;
		    float log1;
		    
		    for(int i=0;i<totalRows;i++){
		    	for(int j=0;j<totalCols;j++){
		    		log1 = (float)Math.log((float)redI[i][j] + (float)greenI[i][j] + (float)blueI[i][j] + 3f);
		    		
		    		redI[i][j] = (short) ((gain*(Math.log(alpha*(red[i][j]+1.0f)) - log1)* Math.log(redI[i][j])) + offset);
		    		greenI[i][j] = (short) ((gain*(Math.log(alpha*(green[i][j]+1.0f)) - log1)* Math.log(greenI[i][j])) + offset);
		    		blueI[i][j] = (short) ((gain*(Math.log(alpha*(blue[i][j]+1.0f)) - log1)* Math.log(blueI[i][j])) + offset);
		    	}
		    }*/
		    
		    for(int i=0;i<totalRows;i++){
		    	for(int j=0;j<totalCols;j++){
		    		redI[i][j] =(short) red[i][j];
		    		greenI[i][j] = (short) green[i][j];
		    		blueI[i][j] = (short) blue[i][j];
		    	}
		    }
		    
		    
		    image.setBufferedImageRed(redI);
		    image.setBufferedImageGreen(greenI);
		    image.setBufferedImageBlue(blueI);
		    
		    //ImageHandler finishedImage = new ImageHandler(loadedImage.packImage());
		    image.packImage();
		    return image;
	    }
	

		public static int clamp(int c) {
			if (c < 0)
				return 0;
			if (c > 255)
				return 255;
			return c;
		}
	    
		/**
		 * HSV version
		 * @param img takes image information as an image handler.
		 */
		public ImageHandler gaussianBlur(ImageHandler img, int kernSize){
	        //kernel = make1DKernel(3);
			float[][] matrix = new float[kernSize][kernSize];
			float sigma = kernSize/3.0f;
			float sigma22 = 2*sigma*sigma;
			float sigmaPi2 = 2*((float)Math.PI)*sigma;
			float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2);
			float radius2 = kernSize*kernSize;
			float total = 0;
			float[][] gausBlur2D = new float[kernSize][kernSize];
			
			float[][] valI, gausValI;
			
			int cols,rows;
			float halfKernSize = kernSize/2;
			
	        rows = img.getTotalRows();
	        cols = img.getTotalCols();	  
	        valI = img.getValI(); 
			
			/*Define the kernel in a size by size array */
			for(int i=0; i<kernSize;i++)
				for(int j=0; j<kernSize;j++){
					radius2=(i-kernSize/2.0f)*(i-kernSize/2.0f)+(j-kernSize/2.0f)*(j-kernSize/2.0f);
					matrix[i][j] = (float)Math.exp(-(radius2)/sigma22) / sqrtSigmaPi2;
					total += matrix[i][j];
				}

	        for(int i=0; i<kernSize;i++)
				for(int j=0; j<kernSize;j++){
					gausBlur2D[i][j] = matrix[i][j] / total;
				}
	        /*Perform Gaussian Blur on the Value aspect of the image in HSV colour space*/


	        
	       
	        gausValI = new float[rows][cols];
	        for(int x=0;x<rows;x++)
	        	for(int y=0;y<cols;y++){
	        		gausValI[x][y] = 0;
	        	}
	        
	        for(int x=0;x<rows;x++)
	        	for(int y=0;y<cols;y++){
	        		
			        for(int i=0; i<kernSize;i++)
			  			for(int j=0; j<kernSize;j++){
			  				if(x+i-(kernSize/2) >= 0 && x+i-(kernSize/2) < rows && y+j-(kernSize/2) >= 0 && y+j-(kernSize/2) < cols)//Checks for out of bounds errors on the image V.	
			  					{gausValI[x][y] += valI[x+i-kernSize/2][y+j-kernSize/2]*gausBlur2D[i][j];}//Might be problems here with the kernsize.				  				
			  			}
			        
	        	}
	        img.setGausValI(gausValI);
	        return img;
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
