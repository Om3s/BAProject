package mvc.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageController {
	private BufferedImage currentImage;
	
	public ImageController(){
		
	}
	
	/**
	 * 
	 * @param matrix with values between -1.0(BLUE) and 1.0(RED)
	 */
	public void drawHeatmap(double[][] matrix){
		long startTime = System.currentTimeMillis(), currentTime;
		System.out.println("Begin Heatmap creation...");
		this.currentImage = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_ARGB);
		int rgb,white = new Color(0,0,0,0).getRGB();
		for(int y=0; y<matrix.length; y++){
			for(int x=0; x<matrix[0].length; x++){
				if(matrix[x][y] > 0){
					rgb = new Color(255,0,0,(int)(matrix[x][y]*255)).getRGB();
				} else if(matrix[x][y] == 0){
					rgb = white;
				} else {
					rgb = new Color(0,0,255,(int)(Math.abs(matrix[x][y])*255)).getRGB();
				}
				this.currentImage.setRGB(x, y, rgb);
			}
		}
		
		currentTime = System.currentTimeMillis();
		System.out.println("Heatmap created in "+(((double)currentTime-(double)startTime)/1000)+"sec");
	}
	
	public ImageController(File inputImage) throws IOException{
		this.loadImage(inputImage);
	}
	
	public void loadImage(File inputImage) throws IOException{
		currentImage = ImageIO.read(inputImage);
	}
	
	public void saveImage(File outputImage) throws IOException{
		ImageIO.write(this.currentImage, "png", outputImage);
	}

	public BufferedImage getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(BufferedImage currentImage) {
		this.currentImage = currentImage;
	}
}
