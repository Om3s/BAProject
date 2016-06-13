package mvc.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class HeatMapLegendPanel extends JPanel {
	private BufferedImage image;
	
	public HeatMapLegendPanel(){
		File imageFile = new File("dat/img/heatmap_legend_horizontal.png");
		try {
			this.image = ImageIO.read(imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
    public Dimension getPreferredSize(){
        return (new Dimension(image.getWidth(), image.getHeight()));
    }
	
	@Override
	public Dimension getMinimumSize(){
		return (new Dimension(image.getWidth(), image.getHeight()));
	}
	
	@Override
	public void paint(Graphics g){
		super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
	}
}
