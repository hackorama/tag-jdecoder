import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;

class ImageViewer {

	private BufferedImage image = null;
	private Graphics2D graphics = null;
	JFrame frame = null;

	ImageViewer(int w, int h, int border, String name){
		frame = new JFrame(name);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		frame.setBounds(0, 0, w+(border*2), h+(border*2));
		frame.setSize(w+(border*2), h+(border*2));
		JImagePanel panel = new JImagePanel(createImage(w, h), border, border);
		frame.add(panel);
		//frame.setVisible(true);
	}

	/* KEEP for debug  
        public static void main(String[] args) {
		ImageViewer iv = new ImageViewer(100, 100, 10, "test");
		iv.setPixel(10, 10, 5);
		iv.setPixel(50, 50, 10);
	}
	*/

	public BufferedImage 
	createImage(int w, int h) 
	{
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		graphics = (Graphics2D)image.getGraphics();
		graphics.setColor(Color.RED);
		graphics.fillRect(0, 0, w, h);
		return image;
	}

	public void 
	setPixel(int x, int y, int size)
	{
		setPixel(x, y, size, 100, 100, 100);
	}

	public void 
	setPixel(int x, int y, int r, int g, int b)
	{
		setPixel(x, y, 1, r, g, b);
	}

	public void 
	setPixel(int x, int y, int size, int r, int g, int b)
	{
		int radius = 0;
		if( size > 2) radius = size/2;
		graphics.setColor(new Color(r, g, b));
		graphics.fillRect(x-radius, y-radius, size, size);
	}

	public void 
	show()
	{
		frame.update(frame.getGraphics());
		frame.setVisible(true);
	}

	public void 
	update()
	{
		frame.update(frame.getGraphics());
	}

	public class JImagePanel extends JPanel
	{
		private BufferedImage image;
		int x, y;
		
		public JImagePanel(BufferedImage image, int x, int y) {
			super();
			this.image = image;
			this.x = x;
			this.y = y;
		}
		
		protected void 
		paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, x, y, null);
		}
	}

}
