package main;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TextFilter extends  FileFilter	{
	
	
	//Copied from Retinex Theory Demo.

	public boolean accept(File f){

		if (f.isDirectory())
			return true;


		String extension = getExtension(f);

		if ((extension.equalsIgnoreCase("gif"))
				|| (extension.equals("png")) || (extension.equals("PNG"))
				|| (extension.equals("jpg")) || (extension.equals("JPG"))
			){

			return true;
		}
		return false;

	}

	public String getDescription(){

		return "Image files  *.gif, *.png, *.jpg";
	}

	public String getExtension(File f){

		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 &&  i < s.length() - 1){
			return s.substring(i+1).toLowerCase();
		}
		return "";
	}
}