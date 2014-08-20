import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;

/* 
* Platform specfic (JSE) 
* Interface to image pixel reads 
*/

public class Tagimage {

	private int width = 0, height = 0;
	private boolean debug = false;
	private BufferedImage image  = null;
	private int blockindex = -1;
	private Config config = null;

	public Tagimage(Config _config) { 
		config = _config;
		debug = config.DEBUG;
		try {
			File imgfile = new File(config.TAG_IMAGE_FILE);
			image = ImageIO.read(imgfile);
		} catch (IOException io) { 
			System.out.println("Unable to load image file :" + config.TAG_IMAGE_FILE ); 
			if(debug) System.exit(1);
		}		
		if( image != null ){
			width  = image.getWidth();
			height = image.getHeight();
			if(config.DEBUG) System.out.println( config.TAG_IMAGE_FILE
				+ " (" + width + "x" + height + ")" );
		}
		config.GRID_WIDTH  = width;
		config.GRID_HEIGHT = height;
		if(config.PIXMAP_NATIVE_SCALE) resizeImage();
	}

	public void 
	initPixels()
	{
	}

	public void 
	display()
	{
		Pixmap pix = new Pixmap(width, height, "original");
		int pixel = 0;
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				pixel  = image.getRGB(x,y);
				pix.setPixel(x, y, (pixel & 0x00ff0000)>>16, 
					(pixel & 0x0000ff00)>>8,
					(pixel & 0x000000ff)  ) ;	
			}
		}
	}

	public void 
	dispose()
	{
		image = null;
	}

	int 
	getWidth()
	{
		return width;
	}

	int 
	getHeight()
	{
		return height;
	}

	int 
	getPixel(int x, int y)
	{
		if( x >= width || y >= height ) return 0;
		int pixel  = image.getRGB(x,y);
		int r = (pixel&0x00ff0000)>>16 ;
		int g = (pixel&0x0000ff00)>>8 ;
		int b = (pixel&0x000000ff);
		return (r+g+b);
	}

	int 
	getPixelR(int x, int y)
	{
		int pixel  = image.getRGB(x,y);
		return (pixel & 0x00ff0000)>>16 ;
	}

	int 
	getPixelG(int x, int y)
	{
		int pixel  = image.getRGB(x,y);
		return (pixel & 0x0000ff00)>>8 ;
	}

	int 
	getPixelB(int x, int y)
	{
		int pixel  = image.getRGB(x,y);
		return (pixel & 0x000000ff);
	}


	boolean 
	isValid()
	{
		if(image == null) return false;
		return true;	
	}

	int 
	maxRGB()
	{
		return 255;
	}

	void 
	resizeImage()
	{
		int boxsize = config.PIXMAP_SCALE_SIZE; //JFLOAT
		if( boxsize <= config.THRESHOLD_WINDOW_SIZE ) return;
		if( width <= boxsize && height <= boxsize ) return ;
		int scale = width > height ? (width*100)/boxsize  :  (height*100)/boxsize ;
		resizeImage((width*100)/scale, (height*100)/scale);
	}

	void 
	resizeImage(int _width, int _height)
	{
		/*JPORT
		config.GRID_WIDTH  = _width;
		config.GRID_HEIGHT = _height;
		if(config.DEBUG) System.out.println( "tag resized to :" + width + ", " + height);
		*/
	}

	BufferedImage 
	d_jse_getImage()
	{
		return image;
	}

}
