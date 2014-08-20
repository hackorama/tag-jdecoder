/*
* Platform specfic (JSE)
* Interface to image pixels writes for canvas 
*/


class Pixmap
{
	ImageViewer image = null;
	private String IMGTYPE;
	private String filename;
	private int width =100, height = 100;
	private static int MAXRGB = 255; //IMAGEMAGICK 65535, JAVA 255
	private int pen_r, pen_g, pen_b;
	private int saved_pen_r, saved_pen_g, saved_pen_b;
	private int serial;
	private boolean ok;
	private boolean debug;


	Pixmap()
	{
		image = null;
		width = 0;
		height = 0;
		ok = false;
		init();
	}

	Pixmap(int _w, int _h, String _filename)
	{
		filename = _filename;
		width    = _w;
		height   = _h;
	}

	Pixmap(String _filename)
	{
		filename = _filename;
		init();
	}



	void
	dispose()
	{
	}

	void
	init()
	{
		IMGTYPE = "png";
		serial = 0 ;
		pen_r = MAXRGB;
		pen_g = 0;
		pen_b = 0;
		savePen();

		image = new ImageViewer(width, height, 0, filename);
		if (image ==  null){
			width = 0;
			height = 0;
			ok = false;
		}else{
			ok = true;
		}
		debug = false;
	}

	void
	resizePixmap(int _w, int _h)
	{
		if(width == _w && height == _h) return;
		if(debug) System.out.println( "pixmap resized to :" + width + ", " + height );
	}

	ImageViewer
	getImage()
	{
		return image;
	}

	void 
	debugImage(String name, int i)
	{
		writeImage(name, i);
	}

	void 
	debugImage(String name)
	{
		writeImage(name);
	}

	void 
	writeImage(String name, int i)
	{
		String localserial = "" ;
		if( i < 10 )		    localserial = "00";
		else if( i < 100 && i > 9 ) localserial = "0";
		writeImage(name + "=" + localserial);
	}
	void 
	writeImage(String name)
	{
		serial++;
		image.update();
	}

	void
		setDebug(boolean flag)
	{
		debug = flag;
	}

	int
	getWidth()
	{
		if( width < 0 ) width = 0;
		return width;
	}

	int
	getHeight()
	{
		if( height < 0 ) height = 0;
		return height;
	}

	boolean
	isValid()
	{
		if( image == null ) ok = false;
		return ok;
	}

	int
	maxRGB()
	{
		return MAXRGB;
	}

	boolean
		inRange(int x, int y)
	{
		if( ! isValid() )  return false;
		if( ( x >= 0 &&  x < width ) && ( y >= 0 &&  y < height ) ) {
			return true;
		}
		return false;
	}

	void
		setPen( int r, int g, int b ) 
	{
		pen_r = convertRGB(r);
		pen_g = convertRGB(g);
		pen_b = convertRGB(b);
	}

	void
	savePen() 
	{
		saved_pen_r = pen_r;
		saved_pen_g = pen_g;
		saved_pen_b = pen_b;
	}

	void
	restorePen() 
	{
		pen_r = saved_pen_r;
		pen_g = saved_pen_g;
		pen_b = saved_pen_g;
	}

	int
	convertRGB( int x ) 
	{
		if( x <= 0 || x > 255  ) return x;
		int factor = MAXRGB / 255;
		return x*factor;
	}

	boolean
	markPixels(int x, int y, int r, int g, int b, int border)
	{

		setPixel(x-border, y-border, r, g, b);
		setPixel(x,   y-border, r, g, b);
		setPixel(x+border, y-border, r, g, b);

		setPixel(x-border, y, r, g, b);
		setPixel(x,   y, r, g, b);
		setPixel(x+border, y, r, g, b);

		setPixel(x-border, y+border, r, g, b);
		setPixel(x,   y+border, r, g, b);
		setPixel(x+border, y+border, r, g, b);

		return true;
	}

	boolean
	setPixel(int x, int y)
	{
		if( ! inRange( x, y ) ) return false;
		image.setPixel(x, y, pen_r, pen_g, pen_b);
		return true;
	}

	boolean
	setPixel(int x, int y, int width)
	{
		if( ! inRange( x, y ) ) return false;
		boolean result = true;
		for(int i = 0; i < width*2; i++){
			result &= setPixel((x-width)+i, y);
		}
		for(int i = 0; i < width*2; i++){
			result &= setPixel(x, (y-width)+i);
		}
		return result;
	}

	boolean
	setPixel(int x, int y, int width, int r, int g, int b)
	{
		if( ! inRange( x, y ) ) return false;
		boolean result = true;
		for(int i = 0; i < width*2; i++){
			result &= setPixel((x-width)+i, y, r, g, b);
		}
		for(int i = 0; i < width*2; i++){
			result &= setPixel(x, (y-width)+i, r, g, b);
		}
		return result;
	}


	boolean
	setPixel256RGB(int x, int y, int r, int g, int b)
	{
		int factor = MAXRGB / 255 ;
		return setPixel(x, y, r*factor, g*factor, b*factor);
	}

	boolean
	setPixel(int x, int y, int r, int g, int b)
	{
		if( ! inRange( x, y ) ) return false;
		image.setPixel(x, y, r, g, b);
		return true;
	}

	void
	clearPixmap(int r, int g, int b)
	{
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				setPixel( i, j, r, g, b);
			}
		}
	}

	void
		clearPixmap()
	{
		clearPixmap(maxRGB(), maxRGB(), maxRGB());
	}

	void
	clearPixmapWhite()
	{
		clearPixmap(0, 0, 0);
	}

	void
	markPoint(int x, int y, int radius)
	{
		for( int i = 0; i < (2*radius); i++)
			for( int j = 0; j < (2*radius); j++)
				setPixel( (x-radius) + i, (y-radius) + j );
	}

	void
	markPoint(int x, int y)
	{
		setPixel(x,y);

		setPixel(x++,y);
		setPixel(x--,y);
		setPixel(x,y++);
		setPixel(x,y--);
		setPixel(x++,y++);
		setPixel(x++,y--);
		setPixel(x--,y++);
		setPixel(x--,y--);
	}

	void
	markVLine(int x)
	{
		markVLine(1, height-1, x);
	}

	void
	markHLine(int y)
	{
		markHLine(1, width-1, y);
	}

	void
	markHLine(int _x1, int _x2, int y)
	{
		int x1 = 0,  x2 = 0;
		if( _x1 < _x2 ) { 
			x1 = _x1 ; x2 = _x2 ;
		}else{
			x1 = _x2 ; x2 = _x1 ;
		}
		for(int x = x1; x <= x2; x++){
			setPixel(x, y);
		}
	}

	void
	markVLine(int _y1, int _y2, int x)
	{
		int y1 = 0,  y2 = 0;
		if( _y1 < _y2 ) { 
			y1 = _y1 ; y2 = _y2 ;
		}else{
			y1 = _y2 ; y2 = _y1 ;
		}
		for(int y = y1; y <= y2; y++){
			setPixel(x, y);
		}
	}

	void 
	mapEdges(boolean map[], int w, int h){
			for(int y=0; y < h; y++){
				for(int x=0; x < w; x++){
					if(map[(y*w)+x]) setPixel(x, y, 255, 255, 255);
					else             setPixel(x, y, 0, 0, 0);
				}
			}
			image.show();
	}

}
