package mvc.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jhlabs.image.GaussianFilter;

import mvc.main.Main;

public class ImageController {
	private BufferedImage rawImage,scaledRawImage,finalHeatmap;
	private String imgDir = "dat/img/";
	
	public ImageController(){
		
	}
	
	/**
	 * 
	 * @param matrix with values between -1.0(BLUE) and 1.0(RED)
	 * @throws IOException 
	 */
	public void drawHeatmap(double[][] matrix,int index) throws IOException{
		long startTime = System.currentTimeMillis(), currentTime;
		System.out.println("Begin Heatmap creation...");
		this.rawImage = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_ARGB);
		int rgb,white = new Color(0,0,0,0).getRGB();
		float cellValue;
//		for(int y=0; y<matrix[0].length; y++){
//			for(int x=0; x<matrix.length; x++){
//				cellValue = (float)((matrix[x][y]+1)/2);
//				if(cellValue != 0.5){
//					rgb = Color.HSBtoRGB(cellValue*0.33f, 1.0f, 1.0f);
//				} else {
//					rgb = white;
//				}
//				this.rawImage.setRGB(x, y, rgb);
//			}
//		}
		for(int y=0; y<matrix[0].length; y++){
			for(int x=0; x<matrix.length; x++){
				if(matrix[x][y] > 0){
					rgb = new Color(255,0,0,(int)(matrix[x][y]*255)).getRGB();
				} else if(matrix[x][y] == 0){
					rgb = white;
				} else {
					rgb = new Color(0,255,0,(int)((Math.abs(matrix[x][y]))*255)).getRGB();
				}
				this.rawImage.setRGB(x, y, rgb);
			}
		}
		
		//Scaleup image
		this.scaledRawImage = this.resizeImage(this.rawImage, Main.mapController.getMapWidth(),Main.mapController.getMapHeight());
		//Blurr image
		this.finalHeatmap = this.blurImage(this.rawImage, 2);
		this.finalHeatmap = this.resizeImage(this.finalHeatmap, Main.mapController.getMapWidth(),Main.mapController.getMapHeight());
		this.finalHeatmap = this.blurImage(this.finalHeatmap, 20);
		this.saveImages(index);
		currentTime = System.currentTimeMillis();
		System.out.println("Heatmap created in "+(((double)currentTime-(double)startTime)/1000)+"sec");
	}
	
	private BufferedImage resizeImage(BufferedImage img, int newWidth, int newHeight) {
		Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		BufferedImage result = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = result.createGraphics();
//		Graphics2D g2d = img.createGraphics(); //TODO affine transformation
//		g2d.
//		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return result;
	}

	private BufferedImage blurImage(BufferedImage img,int radius){
		BufferedImage result;
//		float factor = 20.0f;
//		float[] blurKernel = new float[(int)factor];
//		for(int i=0; i<factor;i++){
//			blurKernel[i] = 1.0f/factor;
//		}
//		BufferedImageOp blurredImage = new ConvolveOp(new Kernel(3, 3, blurKernel));
//		result = blurredImage.filter(img, null);
		GaussianFilter gFilter = new GaussianFilter(radius);
		result = gFilter.filter(img, null);
		return result;
	}
	
	public ImageController(File inputImage) throws IOException{
		this.loadImage(inputImage);
	}
	
	public void loadImage(File inputImage) throws IOException{
		rawImage = ImageIO.read(inputImage);
	}
	
	public void saveImages(int index) throws IOException{
		File 	rawImage = new File(this.imgDir+"rawImage_"+index+".png"),
				scaledRawImage = new File(this.imgDir+"scaledRawImage_"+index+".png"),
				finalHeatMap = new File(this.imgDir+"finalHeatmap_"+index+".png");
		rawImage.createNewFile();
		scaledRawImage.createNewFile();
		finalHeatMap.createNewFile();
		ImageIO.write(this.rawImage, "png", rawImage);
		ImageIO.write(this.scaledRawImage, "png", scaledRawImage);
		ImageIO.write(this.finalHeatmap, "png", finalHeatMap);
	}

	public BufferedImage getCurrentImage() {
		return rawImage;
	}

	public void setCurrentImage(BufferedImage currentImage) {
		this.rawImage = currentImage;
	}
	
	public void loadAllHeatMaps(){ //TODO
		File folder = new File(this.imgDir);
		FilenameFilter filter = new FilenameFilter() {
			@Override
		    public boolean accept(File dir, String name) {
		        return name.matches("finalHeatmap_[0-9]+\\.png");
		    }
		};
		File[] fileList = folder.listFiles(filter);
		ArrayList<BufferedImage> bImages = new ArrayList<BufferedImage>(fileList.length);
		for(int i=0;i<fileList.length;i++){
			try {
				bImages.add(ImageIO.read(fileList[i]));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Main.mapController.setheatMapImages(bImages);
	}
}
