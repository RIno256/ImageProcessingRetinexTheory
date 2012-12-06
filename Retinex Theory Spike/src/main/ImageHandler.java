package main;

import java.awt.image.BufferedImage;

public class ImageHandler {

	private BufferedImage image;
	private int totalRows;
	private int totalCols;
	private int[] bufferedImageData;
	private short[][] bufferedImageRed;
	private short[][] bufferedImageGreen;
	private short[][] bufferedImageBlue;	
	
	public ImageHandler(BufferedImage image){
		this.image = image;
		unpackImage();
	}
	
	public ImageHandler(){
		
	}
	
	public void setImage(BufferedImage image){
		this.image = image;
		unpackImage();
	}

	public BufferedImage getImage() {
		return image;
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
    
    public void unpackImage() {

		
		if (image.getHeight() != totalRows || image.getWidth() != totalCols) {
            totalRows = image.getHeight();
            totalCols = image.getWidth();

        bufferedImageRed = new short[totalRows][totalCols];
        bufferedImageGreen = new short[totalRows][totalCols];
        bufferedImageBlue = new short[totalRows][totalCols];
            

        }

        bufferedImageData = image.getRGB(0, 0, image.getWidth(),
                                           image.getHeight(), null, 0,
                                           image.getWidth());
        int index;
        for (int currentRow = 0; currentRow < totalRows; currentRow++) {
            for (int currentCol = 0; currentCol < totalCols; currentCol++) {
                index = (currentRow * totalCols) + currentCol;
                unpackPixel(bufferedImageData[index], bufferedImageRed, bufferedImageGreen, bufferedImageBlue, currentRow, currentCol);
            }
        }
    }
    
    
    
    public BufferedImage packImage() {
    	
		int[] newBufferedImageData = new int[totalRows * totalCols];
    	int index;
    	for (int currentRow = 0; currentRow < totalRows; currentRow++) {
    		for (int currentCol = 0; currentCol < totalCols; currentCol++) {
    			index = (currentRow * totalCols) + currentCol;
    			newBufferedImageData[index] = packPixel(bufferedImageRed[currentRow][currentCol],
    										bufferedImageGreen[currentRow][currentCol],
    										bufferedImageBlue[currentRow][currentCol]
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

	public int getTotalRows() {
		return totalRows;
	}

	public int getTotalCols() {
		return totalCols;
	}

	public int[] getBufferedImageData() {
		return bufferedImageData;
	}

	public short[][] getBufferedImageRed() {
		return bufferedImageRed;
	}

	public short[][] getBufferedImageGreen() {
		return bufferedImageGreen;
	}

	public short[][] getBufferedImageBlue() {
		return bufferedImageBlue;
	}
	
	
    
    

	
	
	
}
