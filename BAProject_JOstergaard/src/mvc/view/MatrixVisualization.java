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
	private double leftStringWidth,xOuterOffsetLeft,xOuterOffsetRight,yOuterOffsetTop,yOuterOffsetBot,xInnerOffset,yInnerOffset,xDrawRange,yDrawRange;
	private int dataMaxValue, dataMinValue;
	private int transformationMode = 0;
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
		this.xOuterOffsetLeft = this.leftStringWidth*1.2;
		this.xOuterOffsetRight = this.getWidth()*0.05;
		this.yOuterOffsetTop = this.getHeight()*0.15;
		this.yOuterOffsetBot = this.getHeight()*0.15;
		this.xInnerOffset = this.getWidth()*0.05;
		this.yInnerOffset = this.getHeight()*0.02;
		this.xDrawRange = this.getWidth()*(1.0-(this.xOuterOffsetLeft+this.xOuterOffsetRight)/this.getWidth());
		this.yDrawRange = this.getHeight()*(1.0-(this.yOuterOffsetTop+this.yOuterOffsetBot)/this.getHeight());
	}
	
	public void setData(int[][] data){
		this.dataMatrix = data;
		this.computeDataValues();
		this.repaint();
	}

	private void computeDataValues() {
		this.dataMinValue = Integer.MAX_VALUE;
		this.dataMaxValue = 0;
		for(int x = 0; x<this.dataMatrix.length; x++){
			for(int y = 0; y<this.dataMatrix[0].length; y++){
				if(this.dataMatrix[x][y] == -1){
					
				} else if(this.dataMinValue > this.dataMatrix[x][y]){
					this.dataMinValue = this.dataMatrix[x][y];
				}
				if (this.dataMaxValue < this.dataMatrix[x][y]){
					this.dataMaxValue = this.dataMatrix[x][y];
				}
			}
		}
	}

	public void paint(Graphics g){
		//INIT:
		Graphics2D g2d = (Graphics2D)g;
		this.leftStringWidth = g2d.getFontMetrics().stringWidth("0:00AM-6:00AM");
		this.refreshLayoutSettings();
		this.computeDataValues();
		double dataRectWidth = this.determineRecWidth(this.dataMatrix.length);
		double dataRectHeight = this.determineRecHeight(this.dataMatrix[0].length);
		double posX,posY;
		Color backgroundColor = Color.WHITE;
		//DRAW BACKGROUND:
		g2d.setColor(backgroundColor);
		g2d.fill(new Rectangle2D.Double(0,0,this.getWidth(),this.getHeight()));
		for(int i = 0; i<this.dataMatrix.length; i++){
			g2d.setColor(Main.weekDayColors[(i)%7]);
			posX = this.xOuterOffsetLeft+i*(dataRectWidth+this.xInnerOffset);
//			g2d.fill(new Rectangle2D.Double(posX-this.xInnerOffset/3, this.yOuterOffsetTop-this.yOuterOffsetTop/3*2, dataRectWidth+this.xInnerOffset/3*2, this.yOuterOffsetTop/3*2));
			g2d.fill(new Rectangle2D.Double(posX-this.xInnerOffset/3, this.yDrawRange+this.yOuterOffsetTop, dataRectWidth+this.xInnerOffset/3*2, this.yOuterOffsetBot/3));
		}
		//DRAW RECTANGLES:
		double relativeFieldValue;
		for(int x=0; x<this.dataMatrix.length; x++){
//			System.out.print("Day "+x+":\t | \t");
			posX = this.xOuterOffsetLeft+x*(dataRectWidth+this.xInnerOffset);
			for(int y=0; y<this.dataMatrix[0].length; y++){
				posY = this.yOuterOffsetTop+y*(dataRectHeight+yInnerOffset);
				g2d.setColor(backgroundColor);
				g2d.fill(new Rectangle2D.Double(posX, posY, dataRectWidth, dataRectHeight));
				if(this.dataMatrix[x][y] == -1){
					g2d.setColor(Color.GRAY);
//					System.out.print("n/A | \t");
				} else {
					relativeFieldValue = (this.transformFieldValue(this.dataMatrix[x][y],this.transformationMode)*0.33f);
					Color fieldColor = Color.getHSBColor((float)(0.33f-relativeFieldValue), 1, 1);
					g2d.setColor(fieldColor);
//					g2d.setColor(new Color(relativeFieldValue,255-relativeFieldValue,80));
//					System.out.print(this.dataMatrix[x][y] +":"+relativeFieldValue+" | \t");
				}
				g2d.fill(new Rectangle2D.Double(posX+3, posY+3, dataRectWidth-6, dataRectHeight-6));
				
			}
//			System.out.println("");
		}
//		System.out.println("MAX: "+this.dataMaxValue);
//		System.out.println("MIN: "+this.dataMinValue);
		//DRAW TEXT LEFT:
		g2d.setColor(Color.BLACK);
		posY = dataRectHeight*0.6 + this.yOuterOffsetTop+0*(dataRectHeight+yInnerOffset);
		g2d.drawString("00:00 - 06:00", (float)(this.leftStringWidth*0.15), (float)posY);
		posY = dataRectHeight*0.6 + this.yOuterOffsetTop+1*(dataRectHeight+yInnerOffset);
		g2d.drawString("06:00 - 12:00", (float)(this.leftStringWidth*0.15), (float)posY);
		posY = dataRectHeight*0.6 + this.yOuterOffsetTop+2*(dataRectHeight+yInnerOffset);
		g2d.drawString("12:00 - 18:00", (float)(this.leftStringWidth*0.15), (float)posY);
		posY = dataRectHeight*0.6 + this.yOuterOffsetTop+3*(dataRectHeight+yInnerOffset);
		g2d.drawString("18:00 - 00:00", (float)(this.leftStringWidth*0.15), (float)posY);
		//DRAW TEXT TOP:
		posX = this.xOuterOffsetLeft+0*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.sundayColor);
		g2d.drawString("Sunday", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Sunday")*0.5), (float)(this.yOuterOffsetTop/1.25));
		posX = this.xOuterOffsetLeft+1*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.mondayColor);
		g2d.drawString("Monday", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Monday")*0.5), (float)(this.yOuterOffsetTop/1.25));
		posX = this.xOuterOffsetLeft+2*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.tuesdayColor);
		g2d.drawString("Tuesday", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Tuesday")*0.5), (float)(this.yOuterOffsetTop/1.25));
		posX = this.xOuterOffsetLeft+3*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.wednesdayColor);
		g2d.drawString("Wednesday", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Wednesday")*0.5), (float)(this.yOuterOffsetTop/1.25));
		posX = this.xOuterOffsetLeft+4*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.thursdayColor);
		g2d.drawString("Thursday", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Thursday")*0.5), (float)(this.yOuterOffsetTop/1.25));
		posX = this.xOuterOffsetLeft+5*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.fridayColor);
		g2d.drawString("Friday", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Friday")*0.5), (float)(this.yOuterOffsetTop/1.25));
		posX = this.xOuterOffsetLeft+6*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.saturdayColor);
		g2d.drawString("Saturday", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Saturday")*0.5), (float)(this.yOuterOffsetTop/1.25));
	}
	
	/**
	 * transforms the given value into the range from 0 to 1 with the max and min value
	 * of the current given data. With the transformationMode you can choose between
	 * linear, log and square.
	 * 
	 * @param value
	 * @param transformationMode 0 for linear, 1 for logarithmic, 2 for square
	 * @return the transformed value between 0 and 1
	 */
	private double transformFieldValue(int value, int transformationMode) {
		double result;
		if(transformationMode == 0){
			result = (double)(value-this.dataMinValue)/(this.dataMaxValue-this.dataMinValue);
		} else if (transformationMode == 1){
			result = (double)(Math.log(value)-Math.log(this.dataMinValue))/(Math.log(this.dataMaxValue)-Math.log(this.dataMinValue));
		} else if (transformationMode == 2){
			result = (double)(Math.sqrt(value)-Math.sqrt(this.dataMinValue))/(Math.sqrt(this.dataMaxValue)-Math.sqrt(this.dataMinValue));
		} else {
			return -1.0;
		}
		return result;
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
	
	/**
	 * Sets the fieldValue transformationMode.
	 * 
	 * @param transformationMode 0 for linear transformation, 1 for logarithmic transformation, 2 for squared transformation
	 */
	public void setTransformationMode(int transformationMode) {
		if(transformationMode < 0 || transformationMode > 2){
			System.out.println("Wrong transformationMode set, default of 0 is set.");
			this.transformationMode = 0;
		} else {
			this.transformationMode = transformationMode;
		}
	}
}