package main;

import java.awt.image.BufferedImage;

public class ImageHandler {

	private BufferedImage image;	
	
	public ImageHandler(String name, BufferedImage image){
		if(image != null)
			setImage(image);
	}
	
	public void setImage(BufferedImage image){
		this.image = image;
	}
	
	
}
