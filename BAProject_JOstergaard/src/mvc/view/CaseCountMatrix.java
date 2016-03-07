package mvc.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import mvc.main.Main;

@SuppressWarnings("serial")
public class CaseCountMatrix extends JPanel {
	//Offset space on each side
	private double leftStringWidth,xOuterOffsetLeft,xOuterOffsetRight,yOuterOffsetTop,yOuterOffsetBot,xInnerOffset,yInnerOffset,xDrawRange,yDrawRange,textHeightTop;
	private int dataMaxValue, dataMinValue, weekdayMinValue, weekdayMaxValue, hoverWeekDay, hoverDayTime, selectedWeekDay, selectedDayTime;
	private int transformationMode = 1; // 0=linear, 1=log, 2=sqroot
	private Point mousePos;
	private int[] weekDayCounts;
	private int[][] dataMatrix;
	private Mainframe mFrame;
	private Rectangle2D[][] dataRectangles; 
	private Rectangle2D[] weekDayRectangles;
	private Rectangle2D rectangleMouseIsOver ,selectedRectangle;
	private Color[] colorMaps = {
			new Color(255,255,204),
			new Color(255,247,160),
			new Color(254,217,118),
			new Color(254,178,76),
			new Color(253,151,60),
			new Color(252,78,42),
			new Color(227,26,28),
			new Color(189,0,38),
			new Color(128,0,38),
			};

	public CaseCountMatrix(int[][] matrix, Mainframe mFrame){
		super();
		this.mFrame = mFrame;
		this.dataMatrix = matrix;
		this.init();
		this.repaint();
	}
	
	private void init() {
		Dimension size = new Dimension(500, 200);
		this.setupMouseListener();
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.hoverWeekDay = -1;
		this.hoverDayTime = -1;
		this.selectedWeekDay = -1;
		this.selectedDayTime = -1;
	}

	private void setupMouseListener(){
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(rectangleMouseIsOver == null){
					selectedWeekDay = -1;
					selectedDayTime = -1;
					selectedRectangle = null;
				} else if(selectedRectangle == null){
					selectedWeekDay = hoverWeekDay;
					selectedDayTime = hoverDayTime;
					selectedRectangle = rectangleMouseIsOver;
				} else if(selectedRectangle.equals(rectangleMouseIsOver)){
					selectedRectangle = null;
					selectedWeekDay = -1;
					selectedDayTime = -1;
				} else {
					selectedWeekDay = hoverWeekDay;
					selectedDayTime = hoverDayTime;
					selectedRectangle = rectangleMouseIsOver;
				}
				boolean isSelected;
				if(selectedRectangle == null){
					isSelected = false;
				} else {
					isSelected = true;
				}
				mFrame.controller.filterForCaseCountMatrixSelection(isSelected, selectedWeekDay, selectedDayTime);
				repaint();
			}
			
			@Override
			public void mousePressed(MouseEvent e){} // ignore
			
			@Override
			public void mouseExited(MouseEvent e) {
				rectangleMouseIsOver = null;
				repaint();
				System.out.println("EXITED");
			}
			
			@Override
			public void mouseEntered(MouseEvent e){} // ignore
			
			@Override
			public void mouseClicked(MouseEvent e){} // ignore
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				this.anyMouseMovement(e);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				this.anyMouseMovement(e);
			}
			
			private void anyMouseMovement(MouseEvent e){
				mousePos = e.getPoint();
				Rectangle2D temp = getRectangleMouseIsHovering(mousePos);
				if(temp == null){
					rectangleMouseIsOver = temp;
				} else if(rectangleMouseIsOver == null){
					rectangleMouseIsOver = temp;
					repaint();
				} else if(rectangleMouseIsOver != null && rectangleMouseIsOver.equals(temp)){
					
				} else if(rectangleMouseIsOver != null && !rectangleMouseIsOver.equals(temp)){
					rectangleMouseIsOver = temp;
					repaint();
				}
			}
		});
	}
	
	private Rectangle2D getRectangleMouseIsHovering(Point p){
		if(this.weekDayRectangles != null && p != null){
			if(this.weekDayRectangles[0].getY()+this.weekDayRectangles[0].getHeight() > p.getY()){
				for(int weekDay = 0; weekDay<this.weekDayRectangles.length; weekDay++){
					if(this.weekDayRectangles[weekDay].getX() < p.getX() && this.weekDayRectangles[weekDay].getY() < p.getY() && this.weekDayRectangles[weekDay].getX()+this.weekDayRectangles[weekDay].getWidth() > p.getX()){
						this.hoverWeekDay = weekDay;
						this.hoverDayTime = -1;
						return this.weekDayRectangles[weekDay];
					}
				}
			} else {
				for(int weekDay = 0; weekDay<this.dataRectangles.length; weekDay++){
					for(int dayTime = 0; dayTime<this.dataRectangles[0].length; dayTime++){
						if(this.dataRectangles[weekDay][dayTime].getX() < p.getX() && this.dataRectangles[weekDay][dayTime].getY() < p.getY() && this.dataRectangles[weekDay][dayTime].getX()+this.dataRectangles[weekDay][dayTime].getWidth() > p.getX() && this.dataRectangles[weekDay][dayTime].getY()+this.dataRectangles[weekDay][dayTime].getHeight() > p.getY()){
							this.hoverWeekDay = weekDay;
							this.hoverDayTime = dayTime;
							return this.dataRectangles[weekDay][dayTime];
						}
					}
				}
			}
		}
		return null;
	}
	
	private void refreshLayoutSettings() {
		this.xOuterOffsetLeft = this.leftStringWidth*1.2;
		this.xOuterOffsetRight = this.getWidth()*0.05;
		this.yOuterOffsetTop = this.getHeight()*0.2;
		this.textHeightTop = this.getHeight() / 11;
		this.yOuterOffsetBot = this.getHeight()*0.1;
		this.xInnerOffset = 0; //this.getWidth()*0.02;
		this.yInnerOffset = 0; //this.getHeight()*0.02;
		this.xDrawRange = this.getWidth()*(1.0-(this.xOuterOffsetLeft+this.xOuterOffsetRight)/this.getWidth());
		this.yDrawRange = this.getHeight()*(1.0-(this.yOuterOffsetTop+this.yOuterOffsetBot+this.textHeightTop)/this.getHeight());
	}
	
	private void renewSelectedRectangle() {
		if(this.selectedRectangle != null){
			if(this.selectedDayTime == -1){
				this.selectedRectangle = this.weekDayRectangles[this.selectedWeekDay];
			} else {
				this.selectedRectangle = this.dataRectangles[this.selectedWeekDay][this.selectedDayTime];			}
		}
		if(this.rectangleMouseIsOver != null){
			if(this.hoverDayTime == -1){
				this.rectangleMouseIsOver = this.weekDayRectangles[this.hoverWeekDay];
			} else {
				this.rectangleMouseIsOver = this.dataRectangles[this.hoverWeekDay][this.hoverDayTime];
			}
		}
	}

	public void setData(int[][] data){
		this.dataMatrix = data;
		this.weekDayCounts = new int[this.dataMatrix.length];
		this.computeDataValues();
		this.repaint();
	}

	private void computeDataValues() {
		//compute Data Matrix Values:
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
		//compute Weekday Data Values
		this.weekdayMinValue = Integer.MAX_VALUE;
		this.weekdayMaxValue = 0;
		int checkGray;
		for(int weekDay = 0; weekDay<this.dataMatrix.length; weekDay++){
			checkGray = 0;
			this.weekDayCounts[weekDay] = 0;
			for(int dayTime = 0; dayTime<this.dataMatrix[0].length; dayTime++){
				if(this.dataMatrix[weekDay][dayTime] == -1){
//					this.weekDayCounts[weekDay] = -1;
//					break;
					checkGray -= 1;
				} else{
					this.weekDayCounts[weekDay] += this.dataMatrix[weekDay][dayTime];
				}
			}
			if(checkGray == -6){
				this.weekDayCounts[weekDay] = -1;
			}
			if(this.weekDayCounts[weekDay] == -1){
				
			} else if(this.weekdayMinValue > this.weekDayCounts[weekDay]){
				this.weekdayMinValue = this.weekDayCounts[weekDay];
			}
			if(this.weekdayMaxValue < this.weekDayCounts[weekDay]){
				this.weekdayMaxValue = this.weekDayCounts[weekDay];
			}
		}
	}

	public void paint(Graphics g){
		//INIT:
		Graphics2D g2d = (Graphics2D)g;
		g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		this.leftStringWidth = g2d.getFontMetrics().stringWidth("0:00AM-6:00AM");
		this.refreshLayoutSettings();
		this.computeDataValues();
		double dataRectWidth = this.determineRecWidth(this.dataMatrix.length);
		double dataRectHeight = this.determineRecHeight(this.dataMatrix[0].length);
		double posX,posY,relativeFieldValue;
		int colorCase;
		String cellContent = "";
		Color backgroundColor = new Color(238,238,238);
		this.weekDayRectangles = new Rectangle2D[this.weekDayCounts.length];
		this.dataRectangles = new Rectangle2D[this.dataMatrix.length][this.dataMatrix[0].length];
		//DRAW BACKGROUND:
		g2d.setColor(backgroundColor);
		g2d.fill(new Rectangle2D.Double(0,0,this.getWidth(),this.getHeight()));
		g2d.setColor(Color.BLACK);
		g2d.fill(new Rectangle2D.Double(xOuterOffsetLeft-5,yOuterOffsetTop-10,this.getWidth()-xOuterOffsetLeft-xOuterOffsetRight+10,this.getHeight()-yOuterOffsetBot-yOuterOffsetTop+15));
		//DRAW WEEKDAYS
		for(int weekDay = 0; weekDay<this.dataMatrix.length; weekDay++){
			if(this.weekDayCounts[weekDay] == -1){
				g2d.setColor(Color.GRAY);
			} else {
				relativeFieldValue = this.transformFieldValue(this.weekDayCounts[weekDay], this.transformationMode, this.weekdayMinValue, this.weekdayMaxValue);
				colorCase = (int)(relativeFieldValue * 8.9999);
				g2d.setColor(this.colorMaps[colorCase]);
			}
			posX = this.xOuterOffsetLeft+weekDay*(dataRectWidth+this.xInnerOffset);
			g2d.fill(this.weekDayRectangles[weekDay] = new Rectangle2D.Double(posX-this.xInnerOffset/3, this.yOuterOffsetTop-5, dataRectWidth+this.xInnerOffset/3*2, this.textHeightTop));
		}
		//DRAW RECTANGLES:
		for(int weekDay=0; weekDay<this.dataMatrix.length; weekDay++){
			posX = this.xOuterOffsetLeft+weekDay*(dataRectWidth+this.xInnerOffset);
			for(int dayTime=0; dayTime<this.dataMatrix[0].length; dayTime++){
				posY = this.yOuterOffsetTop+this.textHeightTop+dayTime*(dataRectHeight+yInnerOffset);
				g2d.setColor(backgroundColor);
				g2d.fill(new Rectangle2D.Double(posX, posY, dataRectWidth, dataRectHeight));
				cellContent = "";
				if(this.dataMatrix[weekDay][dayTime] == -1){
					g2d.setColor(Color.GRAY);
				} else {
					relativeFieldValue = this.transformFieldValue(this.dataMatrix[weekDay][dayTime],this.transformationMode, this.dataMinValue, this.dataMaxValue);
					colorCase = (int)(relativeFieldValue * 8.9999);
					g2d.setColor(this.colorMaps[colorCase]);
					cellContent += this.dataMatrix[weekDay][dayTime];
				}
				g2d.fill(this.dataRectangles[weekDay][dayTime] = new Rectangle2D.Double(posX, posY, dataRectWidth, dataRectHeight)); //+3 posX and posY, -6 from Width and Height removed
				g2d.setColor(Color.BLACK);
				g2d.drawString(cellContent, (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth(cellContent)/2), (float)(posY+dataRectHeight/2+5));
			}
		}
		//DRAW TEXT LEFT:
		g2d.setColor(Color.BLACK);
		posY = this.yOuterOffsetTop-5+this.textHeightTop/1.5;
		g2d.drawString("Daily Total", (float)(this.leftStringWidth*0.15), (float)posY);
		posY = this.yOuterOffsetTop-5+this.textHeightTop+this.textHeightTop+0*(dataRectHeight+yInnerOffset);
		g2d.drawString("00:00 - 06:00", (float)(this.leftStringWidth*0.15), (float)posY);
		posY = this.yOuterOffsetTop-5+this.textHeightTop+this.textHeightTop+1*(dataRectHeight+yInnerOffset);
		g2d.drawString("06:00 - 10:00", (float)(this.leftStringWidth*0.15), (float)posY);
		posY = this.yOuterOffsetTop-5+this.textHeightTop+this.textHeightTop+2*(dataRectHeight+yInnerOffset);
		g2d.drawString("10:00 - 14:00", (float)(this.leftStringWidth*0.15), (float)posY);
		posY = this.yOuterOffsetTop-5+this.textHeightTop+this.textHeightTop+3*(dataRectHeight+yInnerOffset);
		g2d.drawString("14:00 - 18:00", (float)(this.leftStringWidth*0.15), (float)posY);
		posY = this.yOuterOffsetTop-5+this.textHeightTop+this.textHeightTop+4*(dataRectHeight+yInnerOffset);
		g2d.drawString("18:00 - 22:00", (float)(this.leftStringWidth*0.15), (float)posY);
		posY = this.yOuterOffsetTop-5+this.textHeightTop+this.textHeightTop+5*(dataRectHeight+yInnerOffset);
		g2d.drawString("22:00 - 00:00", (float)(this.leftStringWidth*0.15), (float)posY);
		//DRAW TEXT TOP:
		g2d.setColor(Color.BLACK);
		posY = this.yOuterOffsetTop-5+this.textHeightTop/1.3;
		for(int weekDay=0; weekDay<this.weekDayCounts.length; weekDay++){
			cellContent = "";
			if(!(this.weekDayCounts[weekDay] == -1)){
				posX = this.xOuterOffsetLeft+weekDay*(dataRectWidth+this.xInnerOffset);
				cellContent += this.weekDayCounts[weekDay];
				g2d.drawString(cellContent, (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth(cellContent)*0.5), (float)posY);
			}
		}
//		g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		g2d.setColor(Color.BLACK);
		posY = this.yOuterOffsetTop-this.textHeightTop;
		posX = this.xOuterOffsetLeft+0*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.mondayColor);
		g2d.drawString("Mon", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Mon")*0.5), (float)posY);
		posX = this.xOuterOffsetLeft+1*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.tuesdayColor);
		g2d.drawString("Tue", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Tue")*0.5), (float)posY);
		posX = this.xOuterOffsetLeft+2*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.wednesdayColor);
		g2d.drawString("Wed", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Wed")*0.5), (float)posY);
		posX = this.xOuterOffsetLeft+3*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.thursdayColor);
		g2d.drawString("Thu", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Thu")*0.5), (float)posY);
		posX = this.xOuterOffsetLeft+4*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.fridayColor);
		g2d.drawString("Fri", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Fri")*0.5), (float)posY);
		posX = this.xOuterOffsetLeft+5*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.saturdayColor);
		g2d.drawString("Sat", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Sat")*0.5), (float)posY);
		posX = this.xOuterOffsetLeft+6*(dataRectWidth+this.xInnerOffset);
		g2d.setColor(Main.sundayColor);
		g2d.drawString("Sun", (float)(posX+dataRectWidth/2-g2d.getFontMetrics().stringWidth("Sun")*0.5), (float)posY);
		this.renewSelectedRectangle();
		if(this.selectedRectangle != null){
			g2d.setStroke(new BasicStroke(3));
			g2d.setColor(Color.YELLOW);
			g2d.draw(new Rectangle2D.Double(this.selectedRectangle.getX(), this.selectedRectangle.getY(), this.selectedRectangle.getWidth(), this.selectedRectangle.getHeight()));
		}
		if(this.rectangleMouseIsOver != null){
			g2d.setStroke(new BasicStroke(2));
			g2d.setColor(Color.WHITE);
			g2d.draw(new Rectangle2D.Double(this.rectangleMouseIsOver.getX(), this.rectangleMouseIsOver.getY(), this.rectangleMouseIsOver.getWidth(), this.rectangleMouseIsOver.getHeight()));
		}
		g2d.dispose();
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
	private double transformFieldValue(int value, int transformationMode, int minValue, int maxValue) {
		double result;
		if(transformationMode == 0){
			result = (double)(value-minValue)/(maxValue-minValue);
		} else if (transformationMode == 1){
			result = (double)(Math.log(value)-Math.log(minValue))/(Math.log(maxValue)-Math.log(minValue));
		} else if (transformationMode == 2){
			result = (double)(Math.sqrt(value)-Math.sqrt(minValue))/(Math.sqrt(maxValue)-Math.sqrt(minValue));
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
	
	public int[][] getDataMatrix(){
		return this.dataMatrix;
	}
}