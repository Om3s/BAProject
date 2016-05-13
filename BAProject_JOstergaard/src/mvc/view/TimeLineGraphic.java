package mvc.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import mvc.main.Main;

public class TimeLineGraphic extends JPanel {
	//Offset space on each side
	private Point2D mousePos, mousePosBeforeDrag, weightRectPosBeforeDrag;
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
	private double xOuterOffsetLeft, xOuterOffsetRight;
	private double yOuterOffsetTop, yOuterOffsetBot;
	private double xInnerOffset, yInnerOffset;
	private double xDrawRange, yDrawRange;
	private double barWidth;
	private double maxAbsoluteDataValue, maxRelativeDataValue;
	private int intervalAmount;
	private double[] relativeIntervalData,weightsOfData;
	private int[] absoluteIntervalData;
	private Rectangle2D[] weightRectangleList;
	private Rectangle2D mouseHoveringRectangle, draggedWeight;
	private int hoverInterval, draggedWeightIndex;

	public TimeLineGraphic(){
		super();
		this.init();
	}
	
	private void init() {
		this.setupMouseListener();
		this.refreshLayoutSettings();
	}

	private void setupMouseListener(){
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				//TODO if(item got dragged)set new position for dragged item
				if(draggedWeight != null){ //release dragged state and set new weight
					double newWeight;
					newWeight = computePositionToPercentage(draggedWeight.getY());
					Main.mapController.setIntervalWeight(draggedWeightIndex-1, newWeight);
					weightsOfData[draggedWeightIndex-1] = newWeight;
					draggedWeightIndex = -1;
					draggedWeight = null;
					Main.mapController.updateTimelineWeights(weightsOfData);
					repaint();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e){
				//TODO set maybe-drag-state, save hovered object
				System.out.println("CLICK");
				if(mouseHoveringRectangle != null){
					draggedWeight = mouseHoveringRectangle;
					for(int i=1;i<weightRectangleList.length;i++){
						if(weightRectangleList[i].equals(draggedWeight)){
							draggedWeightIndex = i;
						}
					}
					mousePosBeforeDrag = e.getPoint();
					weightRectPosBeforeDrag = new Point2D.Double(draggedWeight.getX(), draggedWeight.getY());
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				//TODO maybe error handling
			}
			
			@Override
			public void mouseEntered(MouseEvent e){} //ignore
			
			@Override
			public void mouseClicked(MouseEvent e){
				
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			double relativeYDistanceToStartPos,xPos,yPos,width,height;
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mousePos = e.getPoint();
				Rectangle2D temp = getRectangleMouseIsHovering(mousePos);
				if(temp == null){
					mouseHoveringRectangle = temp;
					repaint();
				} else if(mouseHoveringRectangle == null){
					mouseHoveringRectangle = temp;
					repaint();
				} else if(mouseHoveringRectangle != null && !mouseHoveringRectangle.equals(temp)){
					mouseHoveringRectangle = temp;
					repaint();
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(draggedWeight != null){
					mousePos = e.getPoint();
					//TODO handle drag
					relativeYDistanceToStartPos = mousePos.getY() - mousePosBeforeDrag.getY();
					xPos = draggedWeight.getX();
					yPos = weightRectPosBeforeDrag.getY() + relativeYDistanceToStartPos;
					width = draggedWeight.getWidth();
					height = draggedWeight.getHeight();
					draggedWeight = new Rectangle2D.Double(xPos, yPos, width, height);
					repaint();
				}
			}
		});
	}
	
	private double computePositionToPercentage(double yPos) {
		double result = 0;
		double rectHeight = this.relativeIntervalData[draggedWeightIndex]*this.yDrawRange;
		double gapLength = this.yDrawRange - rectHeight;
		System.out.println("yPos: "+yPos);
		System.out.println("yOuterOffsetTop: "+this.yOuterOffsetTop);
		System.out.println("yDrawRange: "+this.yDrawRange);
		System.out.println("rectHeight: "+rectHeight);
		System.out.println("gapLength: "+gapLength);
		System.out.println("top Edge of Rect: "+(this.yOuterOffsetTop+gapLength));
		if(yPos < this.yOuterOffsetTop + gapLength){
			result = 1.0;
		} else if (yPos > this.yOuterOffsetTop + this.yDrawRange) {
			result = 0.0;
		} else { //compute percentage
			result = yPos - this.yOuterOffsetTop - gapLength;
			System.out.println("yPosLength "+result);
			result = 1.0 - (result / rectHeight);
		}
		System.out.println("PositionToPercentage: "+result);
		return result;
	}

	private Rectangle2D getRectangleMouseIsHovering(Point2D p){
		if(this.weightRectangleList != null && p != null){
			for(int x=1;x<this.weightRectangleList.length;x++){
				if(this.weightRectangleList[x].getX() < p.getX() && this.weightRectangleList[x].getY() < p.getY() && this.weightRectangleList[x].getX()+this.weightRectangleList[x].getWidth() > p.getX() && this.weightRectangleList[x].getY()+this.weightRectangleList[x].getHeight() > p.getY()){
					this.hoverInterval = x;
					return this.weightRectangleList[x];
				}
			}
		}
		return null;
	}
	
	private void refreshLayoutSettings() {
		if(Main.mainframeController != null){
			this.xOuterOffsetLeft = this.getWidth()*0.2;
			this.xOuterOffsetRight = this.getWidth()*0.1;
			this.yOuterOffsetTop = this.getHeight()*0.1;
			this.yOuterOffsetBot = this.getHeight()*0.2;
			this.xInnerOffset = 5;
			this.yInnerOffset = 5;
			this.xDrawRange = this.getWidth()*(1.0-(this.xOuterOffsetLeft+this.xOuterOffsetRight)/this.getWidth());
			this.yDrawRange = this.getHeight()*(1.0-(this.yOuterOffsetTop+this.yOuterOffsetBot)/this.getHeight());
			this.intervalAmount = Integer.valueOf(Main.mainframeController.mainframe.filtermenu_analysis_panel_intervallAmount_textfield.getText())+1;
			this.barWidth = (xDrawRange - (this.intervalAmount-1)*this.xInnerOffset)/this.intervalAmount;
		}
	}
	
	public void paint(Graphics g){
		//INIT:
		Graphics2D g2d = (Graphics2D)g;
		this.refreshLayoutSettings();
		//DRAW BACKGROUND:
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2));
		g2d.draw(new Line2D.Double(this.xOuterOffsetLeft-5, this.yOuterOffsetTop-5, this.xOuterOffsetLeft-5, this.yOuterOffsetTop+this.yDrawRange+3));
		g2d.draw(new Line2D.Double(this.xOuterOffsetLeft-5, this.yOuterOffsetTop+this.yDrawRange+3, this.xOuterOffsetLeft+this.xDrawRange, this.yOuterOffsetTop+this.yDrawRange+3));
//		g2d.fillRect((int)this.xOuterOffsetLeft, (int)this.yOuterOffsetTop, (int)this.xDrawRange, (int)this.yDrawRange);
		//DRAW BARS:
		g2d.setColor(Color.BLACK);
		if(this.relativeIntervalData != null){
			for(int x=0;x<this.intervalAmount;x++){
				if(x==0){
					g2d.setColor(Color.GRAY);
				} else {
					g2d.setColor(Color.BLACK);
				}
				g2d.fillRect((int)(this.xOuterOffsetLeft+(this.intervalAmount-x-1)*this.barWidth+(this.intervalAmount-x-1)*this.xInnerOffset), (int)(this.yOuterOffsetTop+((1.0-this.relativeIntervalData[x])*this.yDrawRange)), (int)this.barWidth, (int)(this.yDrawRange*this.relativeIntervalData[x]));
			}
		//DRAW WEIGHTLINES(include drag-state):
			g2d.setColor(Color.BLUE);
			this.weightRectangleList = new Rectangle2D[this.intervalAmount];
			for(int i=1;i<this.intervalAmount;i++){
				if(draggedWeightIndex == i){
					this.weightRectangleList[i] = this.draggedWeight;
				} else {
					this.weightRectangleList[i] = new Rectangle2D.Double(this.xOuterOffsetLeft+(this.intervalAmount-i-1)*this.barWidth+(this.intervalAmount-i-1)*this.xInnerOffset, this.yOuterOffsetTop+((1.0-this.relativeIntervalData[i])*this.yDrawRange+(this.relativeIntervalData[i]*this.yDrawRange-5)*(1.0-this.weightsOfData[i-1])), this.barWidth, 5);
				}
				g2d.draw(this.weightRectangleList[i]);
			}
		}
		if(this.mouseHoveringRectangle != null){ //HOVERING
			g2d.setStroke(new BasicStroke(2));
			g2d.setColor(Color.WHITE);
			g2d.draw(new Rectangle2D.Double(this.mouseHoveringRectangle.getX(), this.mouseHoveringRectangle.getY(), this.mouseHoveringRectangle.getWidth(), this.mouseHoveringRectangle.getHeight()));
		}
		g2d.dispose();
	}

	public void setAbsoluteIntervalData(int[] absoluteIntervalData) {
		this.absoluteIntervalData = absoluteIntervalData;
		this.relativeIntervalData = new double[this.absoluteIntervalData.length];
		this.maxAbsoluteDataValue = 0;
		//Calculate relativeIntervalData:
		for(int i=0;i<this.absoluteIntervalData.length;i++){ //Determine MAX
			if(this.absoluteIntervalData[i] > this.maxAbsoluteDataValue){
				this.maxAbsoluteDataValue = this.absoluteIntervalData[i];
				this.maxRelativeDataValue = i;
			}
		}
		for(int i=0;i<this.absoluteIntervalData.length;i++){ //Determine MAX
			this.relativeIntervalData[i] = this.absoluteIntervalData[i] / this.maxAbsoluteDataValue;
			if(i == this.maxRelativeDataValue){
				this.maxRelativeDataValue = this.relativeIntervalData[i];
			}
		}
	}

	public double[] getWeightsOfData() {
		return weightsOfData;
	}

	public void setWeightsOfData(double[] weightsOfData) {
		this.weightsOfData = weightsOfData;
	}
}