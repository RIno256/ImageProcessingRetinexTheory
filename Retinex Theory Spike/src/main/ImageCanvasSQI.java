package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
			processedImage = packImage(bufferedImageRed, bufferedImageGreen, bufferedImageBlue);
            File savedFile=new File("test.jpg");
            try {
            ImageIO.write(processedImage,"jpeg",savedFile);
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
	    
	    
	    public float[][] linearise (short[][] bufferedImageRed,short[][] bufferedImageGreen,short[][] 
	    						bufferedImageBlue,int imageRows,int imageCols){
	    	float[][] linearisedImage = new float[3][imageRows * imageCols];
	    	
	    	for(int i = 0; i < imageRows ; i++)
	    		for(int j = 0; j < imageCols ; j++){
	    			linearisedImage[0][(i*j)+j] = (float)bufferedImageRed[i][j];
	    			linearisedImage[1][(i*j)+j] = (float)bufferedImageRed[i][j];
	    			linearisedImage[2][(i*j)+j] = (float)bufferedImageRed[i][j];
	    		}
	    	return linearisedImage;
	    }
	    
	    public short[][][] delinearise (float[][] imageChannels, int imageRows, int imageCols){
	    	short[][][] colourChannels = new short[3][imageRows][imageCols];
	    	
	    	for(int i = 0; i < imageRows; i++){//For each width.//For each red, green, blue.-----------------------------De-linearise.
	    		for(int j=0; j < imageCols; j++){//For each height.
	    			colourChannels[0][i][j]= (short)imageChannels[0][(i*j)+j];//Change short to float.
	    			colourChannels[1][i][j]= (short)imageChannels[1][(i*j)+j];;
	    			colourChannels[2][i][j]= (short)imageChannels[2][(i*j)+j];;
	    		}		    		
	    	}
			return colourChannels;
	    }
	    
	    public void normalise(){
		    /*for(channel = 0;channel < 3; channel++)//For each red, green, blue.
			       for(int i=0; i<imageRows; i++)
			    	   for(int j=0; j<imageCols; j++) {
			    		   if(imageChannels[channel][(i*j)+j]<rmin) rmin=imageChannels[channel][(i*j)+j];
			    		   if(imageChannels[channel][(i*j)+j]>rmax) rmax=imageChannels[channel][(i*j)+j];
			    	   }	

			       rmax -= rmin;

			    for(channel = 0;channel < 3; channel++)//For each red, green, blue.
			       for(int i=0; i<imageRows; i++)
			        	for(int j=0; j<imageCols; j++) {
			        		temp=255f*(imageChannels[channel][(i*j)+j]-rmin)/rmax;

			        		// Grad[i][j]=(float)Math.log(temp+1); // log space
			        		//   Grad[i][j]=temVal[i][j]; // value
			        		imageChannels[channel][(i*j)+j]=temp; // normalised image
			        	}*/
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
		    float rmin = Float.MAX_VALUE;
		    float rmax = Float.MIN_VALUE;
		    float temp;
		    
		    imageChannels = linearise(bufferedImageRed, bufferedImageGreen, bufferedImageBlue, imageRows, imageCols);//Linearised Image.
		    
	    	// normalisation
		    // normalise();
		    
	    	imageChannels = GaussianBlur(imageChannels,imageRows,imageCols);//GaussianBlur.
	    	
	    	delinearise(imageChannels, imageRows, imageCols);

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
	    	float [][] smoothImage = new float[3][imageRows*imageCols];
	    	float [] RGBToHSVTemp = new float[3];
	    	float [] HSVToRGBTemp = new float[3];
	    	  	
	    	    	
	    	for(int i=0;i<size;i++)
	    		for(int j=0;j<size;j++)
	    			kernel[(i*j)+j]= Math.exp(-((i-(size/2))*(i-(size/2))+(j-(size/2))*(j-(size/2)))/(2*1f*1f));
	    	
	    	for(int rows=0;rows < imageRows;rows++)
	    		for(int cols=0;cols < imageCols;cols++){
	    			RGBToHSVTemp[0] = imageChannels[0][rows + cols];
	    			RGBToHSVTemp[1] = imageChannels[1][rows + cols];
	    			RGBToHSVTemp[2] = imageChannels[2][rows + cols];
	    				
	    			RGBToHSVTemp = RGBtoHSV(RGBToHSVTemp);//Converts the RGB to HSV;
	    				
    				smoothImage[0][rows + cols] = RGBToHSVTemp[0];
    				smoothImage[1][rows + cols] = RGBToHSVTemp[1];
    				smoothImage[2][rows + cols] = RGBToHSVTemp[2];		
	    		}   

	    				
	    	for(int row=0; row < imageRows; row++){
	    	      for(int col=0; col < imageCols; col++) {
	    	      total=tot=0f;
		    	      for(int channel=0; channel<3;channel++){     
			    	      for(int i=0; i<size; i++)
			    	    	  for(int j=0; j<size; j++)
			    	    		  if((row-size/2+i)>=0 && (row-size/2+i)<imageRows && (col-size/2+j)>=0 && (col-size/2+j)<imageCols) {   
			    	    			  total += kernel[(i*j)+j] * imageChannels[channel][(col-size/2+j)+(row-size/2+i)];
			    	    			  tot += kernel[(i*j)+j];
			    	    		  }
			    	      smoothImage[channel][(row*col)+col]=(float) (total/tot);
			    	  }
		    	}
	    	}
	    	
	    		    	
	    	for(int rows=0;rows < imageRows;rows++)
	    		for(int cols=0;cols < imageCols;cols++){
	    			HSVToRGBTemp[0] = smoothImage[0][rows + cols];
	    			HSVToRGBTemp[1] = smoothImage[1][rows + cols];
	    			HSVToRGBTemp[2] = smoothImage[2][rows + cols];
	    				
	    			HSVToRGBTemp = HSVtoRGB(HSVToRGBTemp);//Converts the RGB to HSV;
	    	
	    				
    				smoothImage[0][rows + cols] = HSVToRGBTemp[0];
    				smoothImage[1][rows + cols] = HSVToRGBTemp[1];
    				smoothImage[2][rows + cols] = HSVToRGBTemp[2];
	    			
	    			
	    		}
	    	
	    	return smoothImage;
	    }
	    /**
	     * Taken from SC24110
	     * 
	     * @param r
	     * @param g
	     * @param b
	     * @return
	     */
	    public float[] RGBtoHSV(float[] RGB)
	    {
	    	float min, max, delta;
	        float r, g, b;    
	    	float h, s, v;
	        
	        r = RGB[0];
	        g = RGB[1];
	        b = RGB[2];
	            r /= 255f; g /= 255f; b /= 255f;

	    	min = Math.min(Math.min(r, g), b );
	    	max = Math.max(Math.max(r, g), b );

	    	v = max;				// v

	    	delta = max - min;

	    	if( max == 0 )
	    		// r = g = b = 0		// s = 0, v is undefined
	    		s = 0f;

	            else s = delta / max; // s

	            if(delta != 0) {
	    	if( r == max ) {
	    		h = ( g - b ) / delta;		// between yellow & magenta
	                    if( h < 0 )
	    		h += 6f;
	                    }
	    	else { if( g == max )
	    		h = 2f + ( b - r ) / delta;	// between cyan & yellow
	    	else
	    		h = 4f + ( r - g ) / delta;	// between magenta & cyan
	            }

	    	h *= 60f;				// degrees

	    	} else { h=0f; }

	            return new float[] {h,s,v};
	    	//System.out.print("h= "+h+" "+s+" "+v);

	    }
	    
	    
	    public float[] HSVtoRGB(float[] HSV)
	    {
	    	int i;
	    	float f, p, q, t;
	        float r, g, b;
	        float h, s, v;
	        
	        h = HSV[0];
	        s = HSV[1];
	        v = HSV[2];

	            v *= 255f;

	    	if( s == 0 ) {
	    		r = g = b = v;
	    		return new float[] {r, g, b};
	    	}

	    	h /= 60f;
	    	i=0;			// sector 0 to 5
	    	for(int j=0; j<6; j++)
	            if(h>=j && h<j+1)
	    	i=j;

	    	f = h - i;			// factorial part of h
	    	p = v * ( 1 - s );
	    	q = v * ( 1 - s * f );
	    	t = v * ( 1 - s * ( 1 - f ) );

	    	switch( i ) {
	    		case 0:
	    			r = v;
	    			g = t;
	    			b = p;
	    			break;
	    		case 1:
	    			r = q;
	    			g = v;
	    			b = p;
	    			break;
	    		case 2:
	    			r = p;
	    			g = v;
	    			b = t;
	    			break;
	    		case 3:
	    			r = p;
	    			g = q;
	    			b = v;
	    			break;
	    		case 4:
	    			r = t;
	    			g = p;
	    			b = v;
	    			break;
	    		default:		// case 5:
	    			r = v;
	    			g = p;
	    			b = q;
	    			break;
	    	}

	           return new float[] {r, g, b};

	    }



}
