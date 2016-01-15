package mvc.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import mvc.main.Main;
import mvc.view.ResultDetailFrame;

public class PictureWorker extends SwingWorker<ImageIcon, ImageIcon> {
	private String path;
	private JLabel jLabel;
	private JPanel mainPanel;
	private ResultDetailFrame frame;
	private Dimension maxImageBoundary = new Dimension(500,500);
	
	public PictureWorker(String path, JPanel panel, JLabel jLabel, ResultDetailFrame frame){
		this.path = path;
		this.jLabel = jLabel;
		this.mainPanel = panel;
		this.frame = frame;
	}
	
	@Override
	protected ImageIcon doInBackground() throws Exception {
		System.out.println("Get Image from " + path);
        URL url = new URL(path);
        ImageIcon imageIcon = new ImageIcon(ImageIO.read(url));
        Dimension boundary = MainframeController.getScaledDimension(new Dimension(imageIcon.getIconWidth(),imageIcon.getIconHeight()), this.maxImageBoundary );
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(boundary.width, boundary.height, Image.SCALE_DEFAULT));
        System.out.println("Load image into frame...");
		return imageIcon;
	}
	
	protected void done(){
		try {
			this.mainPanel.remove(this.jLabel);
			this.jLabel = new JLabel(this.get());
			GridBagConstraints gbc_jLabel = new GridBagConstraints();
			gbc_jLabel.anchor = GridBagConstraints.CENTER;
			gbc_jLabel.fill = GridBagConstraints.BOTH;
			gbc_jLabel.gridwidth = 4;
			gbc_jLabel.gridx = 0;
			gbc_jLabel.gridy = 9;
			this.mainPanel.add(this.jLabel, gbc_jLabel);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			this.jLabel.setForeground(Color.ORANGE);
			this.jLabel.setText("Could not load Image");
		} finally {
			this.mainPanel.repaint();
			this.frame.repaint();
			this.frame.pack();
		}
	}
}
