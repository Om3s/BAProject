package mvc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import mvc.main.Main;

@SuppressWarnings("serial")
public class MatrixVisualization extends JPanel {
	//Offset space on each side
	private double xOuterOffset,yOuterOffset,xInnerOffset,yInnerOffset,xDrawRange,yDrawRange;
	private int[][] dataMatrix;
		
	public MatrixVisualization(int[][] matrix){
		this.dataMatrix = matrix;
		Dimension size = new Dimension(500, 200);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.repaint();
	}
	
	private void refreshLayoutSettings() {
		this.xOuterOffset = this.getWidth()*0.07;
		this.yOuterOffset = this.getHeight()*0.1;
		this.xInnerOffset = this.getWidth()*0.05;
		this.yInnerOffset = this.getHeight()*0.02;
		this.xDrawRange = this.getWidth()*(1.0-this.xOuterOffset/this.getWidth()*2);
		this.yDrawRange = this.getHeight()*(1.0-this.yOuterOffset/this.getHeight()*2);
	}
	
	public void newData(int[][] data){
		this.dataMatrix = data;
		this.repaint();
	}

	public void paint(Graphics g){
		//INIT:
		this.refreshLayoutSettings();
		Graphics2D g2d = (Graphics2D)g;
		double dataRectWidth = this.determineRecWidth(this.dataMatrix.length);
		double dataRectHeight = this.determineRecHeight(this.dataMatrix[0].length);
		double posX,posY;
		Color backgroundColor = Color.WHITE;
		//DRAW BACKGROUND:
		g2d.setColor(backgroundColor);
		g2d.fill(new Rectangle2D.Double(0,0,this.getWidth(),this.getHeight()));
		for(int i = 0; i<this.dataMatrix.length; i++){
			g2d.setColor(Main.weekDayColors[(i+1)%7]);
			posX = this.xOuterOffset+i*(dataRectWidth+this.xInnerOffset);
			g2d.fill(new Rectangle2D.Double(posX-this.xInnerOffset/3, this.yOuterOffset-this.yOuterOffset/3*2, dataRectWidth+this.xInnerOffset/3*2, this.yOuterOffset/3*2));
			g2d.fill(new Rectangle2D.Double(posX-this.xInnerOffset/3, this.yDrawRange+this.yOuterOffset, dataRectWidth+this.xInnerOffset/3*2, this.yOuterOffset/3*2));
		}
		//DRAW RECTANGLES:
		int randomInt; //TESTING NEED DATA FIRST
		for(int x = 0; x<this.dataMatrix.length; x++){
			posX = this.xOuterOffset+x*(dataRectWidth+this.xInnerOffset);
			for(int y = 0; y<this.dataMatrix[0].length; y++){
				posY = this.yOuterOffset+y*(dataRectHeight+yInnerOffset);
				randomInt = (int)(Math.random()*255+0.5); //TESTING
				g2d.setColor(backgroundColor);
				g2d.fill(new Rectangle2D.Double(posX, posY, dataRectWidth, dataRectHeight));
				g2d.setColor(new Color(randomInt,255-randomInt,80));
				g2d.fill(new Rectangle2D.Double(posX+3, posY+3, dataRectWidth-6, dataRectHeight-6));
			}
		}
	}

	private double determineRecWidth(int amount) {
		double recWidth = ((this.xDrawRange-((amount-1)*this.xInnerOffset))/amount);
		return recWidth;
	}

	private double determineRecHeight(int amount) {
		double recHeight = ((this.yDrawRange-((amount-1)*this.yInnerOffset))/amount);
		return recHeight;
	}
	
	public int[][] getDataMatrix(){
		return this.dataMatrix;
	}
}