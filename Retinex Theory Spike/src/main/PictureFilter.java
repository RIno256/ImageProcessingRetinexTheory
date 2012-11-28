package main;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PictureFilter extends FileFilter {

	private String description = "";//describes the file types allowed with filechooser.
	private String extensions[];//Stores the list of extensions allowed with filechooser.
	
	
	public PictureFilter(String description, String extension){
			this(description, new String[] { extension });//Set extensions that theJFileChooser can use, single case.
			toLower(this.extensions);//Make the extensions to lower case.
	}
	
	public PictureFilter(String description, String[] extensions) {
		if (description == null) {//
			this.description = "Image files *." + extensions;
		} else {
			this.description = description;
		}
		this.extensions = (String[]) extensions.clone();
		toLower(this.extensions);
	}

	private void toLower(String extentionCase[]) {
		for (int loop = 0; loop < extentionCase.length; loop++){
			
		}
		
	}

	@Override
	public boolean accept(File picture) {
		
		if(picture.isDirectory()){
			return true;
		}else{
			
			String path = picture.getAbsolutePath().toLowerCase();
			
			for (int loop = 0; loop < (extensions.length); loop++){
				String extension = extensions[loop];
				if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')){
					return true;
				}
			}		
		}
		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}

	
	
}
