package main;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;

/** A class that simplifies a few common image operations, in
 *  particular creating a BufferedImage from an image file, and
 *  using MediaTracker to wait until an image or several images are
 *  done loading. 1998 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 *  This code was taken from ImageUtilities class provided in CS24110.
 *  Modified by James timms jat29.
 */

public class MyImageUtilities {
	
	
	  /** Create Image from a file, then turn that into a BufferedImage.
	   */	
	
	public static BufferedImage getBufferedImage(String imageFile,
        Component c) {
	
		Image image = c.getToolkit().getImage(imageFile);
		waitForImage(image, c);
		BufferedImage bufferedImage =
				new BufferedImage(image.getWidth(c), image.getHeight(c),
						BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, c);
		return(bufferedImage);
	}
	  
	  /** Take an Image associated with a file, and wait until it is
	   *  done loading. Just a simple application of MediaTracker.
	   */
	
	public static boolean waitForImage(Image image, Component c) {
		MediaTracker tracker = new MediaTracker(c);
		tracker.addImage(image, 0);
		try {
			tracker.waitForAll();
		} catch(InterruptedException ie) {}
		return(!tracker.isErrorAny());
	  }
	
	
}
