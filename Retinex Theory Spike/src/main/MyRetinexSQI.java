package main;


import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class MyRetinexSQI extends JFrame {

		/**
	 * 
	 */
	private static final long serialVersionUID = -3043654662483158071L;
		private JMenuItem LoadImage, Exit, About, SaveImage, processImage;
//		private BufferedImage sourceImage;
		private ImageCanvasSQI canvas;
		
	
		public MyRetinexSQI(){
			super("My Retinex Example");
			
			//JPanel myPanel = new JPanel();
			
		    JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);
			
			canvas = new ImageCanvasSQI(this);
			this.setContentPane(canvas);
			
	    	JMenu fileMenu = new JMenu("File");
	        JMenu imageProcessingMenu = new JMenu("Image Processing");
	        JMenu otherMenu = new JMenu("Other");
	        
	        LoadImage = new JMenuItem("Load Image");
	    	LoadImage.addActionListener(canvas);
	        
	    	SaveImage = new JMenuItem("Save Image");
	    	SaveImage.addActionListener(canvas);
	    	
	        Exit = new JMenuItem("Exit");
	        Exit.addActionListener(canvas);
	        
	        processImage = new JMenuItem("Process Image");
	        processImage.addActionListener(canvas);
	        
		    fileMenu.add(LoadImage);
		    fileMenu.add(SaveImage);
		    fileMenu.add(new JSeparator());
	    	fileMenu.add(Exit);
	    	
	    	imageProcessingMenu.add(processImage);
	    	
		    menuBar.add(fileMenu);
            menuBar.add(imageProcessingMenu);
            menuBar.add(otherMenu);
            
	        
		}

	//	public void LoadImage(BufferedImage sourceImage){
			
		//	this.sourceImage = sourceImage;
			//int width = sourceImage.getWidth();
			//int height = sourceImage.getHeight();
			//BufferedImage drawn = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			//Graphics graphics = drawn.getGraphics();
			//Graphics graphics = canvas.getGraphics();
			//graphics.drawImage(sourceImage, 0, 0, canvas);
			
		//}
		


				
		public static void main(String[] args){
			MyRetinexSQI run = new MyRetinexSQI();
			run.setDefaultCloseOperation(EXIT_ON_CLOSE);
			run.setSize(800, 600);
			run.setVisible(true);
		}
	
}
